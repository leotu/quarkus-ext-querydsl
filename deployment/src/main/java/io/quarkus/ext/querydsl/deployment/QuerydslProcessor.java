package io.quarkus.ext.querydsl.deployment;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.DotName;
import org.jboss.logging.Logger;
import org.objectweb.asm.Opcodes;

import com.querydsl.sql.dml.AbstractSQLDeleteClause;
import com.querydsl.sql.dml.AbstractSQLInsertClause;
import com.querydsl.sql.dml.AbstractSQLUpdateClause;
import com.querydsl.sql.dml.SQLMergeClause;

import io.quarkus.agroal.deployment.JdbcDataSourceBuildItem;
import io.quarkus.arc.deployment.BeanContainerListenerBuildItem;
import io.quarkus.arc.deployment.BeanDefiningAnnotationBuildItem;
import io.quarkus.arc.deployment.GeneratedBeanBuildItem;
import io.quarkus.arc.deployment.UnremovableBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.datasource.common.runtime.DataSourceUtil;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.deployment.recording.RecorderContext;
import io.quarkus.deployment.util.HashUtil;
import io.quarkus.ext.querydsl.runtime.AbstractQueryFactoryProducer;
import io.quarkus.ext.querydsl.runtime.AbstractQueryFactoryProducer.QueryFactoryQualifier;
import io.quarkus.ext.querydsl.runtime.QueryFactoryCreator;
import io.quarkus.ext.querydsl.runtime.QueryFactoryItemConfig;
import io.quarkus.ext.querydsl.runtime.QueryFactory;
import io.quarkus.ext.querydsl.runtime.QuerydslConfig;
import io.quarkus.ext.querydsl.runtime.QuerydslCustomTypeRegister;
import io.quarkus.ext.querydsl.runtime.QuerydslTemplate;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.FieldCreator;
import io.quarkus.gizmo.FieldDescriptor;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;

/**
 * Deployment Processor
 * 
 * <pre>
 * https://quarkus.io/guides/cdi-reference#supported_features
 * https://github.com/quarkusio/gizmo
 * https://quarkus.io/guides/datasource
 * </pre>
 * 
 * @author Leo Tu
 */
public class QuerydslProcessor {
    private static final Logger log = Logger.getLogger(QuerydslProcessor.class);

    private static final DotName QUERY_FACTORY_QUALIFIER = DotName.createSimple(QueryFactoryQualifier.class.getName());

    private final String queryFactoryProducerClassName = AbstractQueryFactoryProducer.class.getPackage().getName()
            + ".QueryFactoryProducer";

    /**
     * Register a extension capability and feature
     *
     * @return QueryDSL feature build item
     */
    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem("querydsl");
    }

    @BuildStep
    BeanDefiningAnnotationBuildItem registerAnnotation() {
        return new BeanDefiningAnnotationBuildItem(QUERY_FACTORY_QUALIFIER);
    }

    @SuppressWarnings("unchecked")
    @Record(ExecutionTime.STATIC_INIT)
    @BuildStep
    BeanContainerListenerBuildItem build(RecorderContext recorder, QuerydslTemplate template,
            BuildProducer<ReflectiveClassBuildItem> reflectiveClass,
            BuildProducer<UnremovableBeanBuildItem> unremovableBeans, QuerydslConfig querydslConfig,
            BuildProducer<GeneratedBeanBuildItem> generatedBean,
            List<JdbcDataSourceBuildItem> jdbcDataSourceBuildItems) {
        if (isUnconfigured(querydslConfig)) {
            return null;
        }
        reflectiveClass.produce(new ReflectiveClassBuildItem(true, false, AbstractQueryFactoryProducer.class));
        reflectiveClass.produce(new ReflectiveClassBuildItem(true, true, AbstractSQLDeleteClause.class,
                AbstractSQLUpdateClause.class, AbstractSQLInsertClause.class, SQLMergeClause.class));

        if (!isPresentTemplate(querydslConfig.defaultConfig)) {
            log.warn("No default sql-template been defined");
        }

        createQueryFactoryProducerBean(generatedBean, unremovableBeans, querydslConfig, jdbcDataSourceBuildItems);
        return new BeanContainerListenerBuildItem(template.addContainerCreatedListener(
                (Class<? extends AbstractQueryFactoryProducer>) recorder.classProxy(queryFactoryProducerClassName)));
    }

    @Record(ExecutionTime.RUNTIME_INIT)
    @BuildStep
    void configureDataSource(QuerydslTemplate template,
            BuildProducer<QuerydslInitializedBuildItem> querydslInitialized, QuerydslConfig querydslConfig) {
        if (isUnconfigured(querydslConfig)) {
            return;
        }
        querydslInitialized.produce(new QuerydslInitializedBuildItem());
    }

    private boolean isUnconfigured(QuerydslConfig querydslConfig) {
        if (!isPresentTemplate(querydslConfig.defaultConfig) && querydslConfig.namedConfig.isEmpty()) {
            // No QueryDSL has been configured so bail out
            log.info("No QueryDSL has been configured");
            return true;
        } else {
            return false;
        }
    }

    private void createQueryFactoryProducerBean(BuildProducer<GeneratedBeanBuildItem> generatedBean,
            BuildProducer<UnremovableBeanBuildItem> unremovableBeans, QuerydslConfig querydslConfig,
            List<JdbcDataSourceBuildItem> jdbcDataSourceBuildItems) {
        ClassOutput classOutput = new ClassOutput() {
            @Override
            public void write(String name, byte[] data) {
                generatedBean.produce(new GeneratedBeanBuildItem(name, data));
            }
        };
        unremovableBeans.produce(UnremovableBeanBuildItem.beanClassNames(queryFactoryProducerClassName));

        ClassCreator classCreator = ClassCreator.builder().classOutput(classOutput)
                .className(queryFactoryProducerClassName).superClass(AbstractQueryFactoryProducer.class).build();
        classCreator.addAnnotation(ApplicationScoped.class);

        Set<String> dataSourceNames = jdbcDataSourceBuildItems.stream().map(JdbcDataSourceBuildItem::getName)
                .collect(Collectors.toSet());

        QueryFactoryItemConfig defaultConfig = querydslConfig.defaultConfig;
        if (isPresentTemplate(defaultConfig)) {
            if (!DataSourceUtil.hasDefault(dataSourceNames)) {
                log.warn("Default data source not found");
            }
            if (defaultConfig.datasource.isPresent()
                    && !DataSourceUtil.isDefault(defaultConfig.datasource.get())) {
                log.warnv("Skip default data source name: {}", defaultConfig.datasource.get());
            }
            String dsVarName = "defaultDataSource";

            FieldCreator defaultDataSourceCreator = classCreator.getFieldCreator(dsVarName, DataSource.class)
                    .setModifiers(Opcodes.ACC_MODULE);

            defaultDataSourceCreator.addAnnotation(Default.class);
            defaultDataSourceCreator.addAnnotation(Inject.class);

            String factoryAlias = defaultConfig.factoryAlias.orElse(null);
            if (factoryAlias != null) {
                log.debugv("BeanClassNameExclusion: factoryAlias: {0}", factoryAlias);
                unremovableBeans.produce(UnremovableBeanBuildItem.beanClassNames(factoryAlias));
            }
            String template = defaultConfig.template;
            MethodCreator defaultQueryFactoryMethodCreator = classCreator.getMethodCreator("createDefaultQueryFactory",
                    QueryFactoryCreator.getQueryFactoryType(template, factoryAlias));

            defaultQueryFactoryMethodCreator.addAnnotation(Singleton.class);
            defaultQueryFactoryMethodCreator.addAnnotation(Produces.class);
            defaultQueryFactoryMethodCreator.addAnnotation(Default.class);

            ResultHandle templateRH = defaultQueryFactoryMethodCreator.load(template);

            ResultHandle dataSourceRH = defaultQueryFactoryMethodCreator.readInstanceField(
                    FieldDescriptor.of(classCreator.getClassName(), dsVarName, DataSource.class.getName()),
                    defaultQueryFactoryMethodCreator.getThis());

            ResultHandle factoryAliasRH = factoryAlias != null ? defaultQueryFactoryMethodCreator.load(factoryAlias)
                    : defaultQueryFactoryMethodCreator.loadNull();

            if (defaultConfig.registerCustomTypeInject.isPresent()) {
                String registerCustomTypeInjectName = defaultConfig.registerCustomTypeInject.get();
                String injectVarName = "registerCustomType_" + HashUtil.sha1(registerCustomTypeInjectName);

                FieldCreator registerCustomTypeCreator = classCreator
                        .getFieldCreator(injectVarName, QuerydslCustomTypeRegister.class)
                        .setModifiers(Opcodes.ACC_MODULE);

                registerCustomTypeCreator.addAnnotation(Inject.class);
                registerCustomTypeCreator
                        .addAnnotation(AnnotationInstance.create(DotNames.NAMED, null, new AnnotationValue[] {
                                AnnotationValue.createStringValue("value", registerCustomTypeInjectName) }));

                ResultHandle registerCustomTypeRH = defaultQueryFactoryMethodCreator.readInstanceField(
                        FieldDescriptor.of(classCreator.getClassName(), injectVarName,
                                QuerydslCustomTypeRegister.class.getName()),
                        defaultQueryFactoryMethodCreator.getThis());

                defaultQueryFactoryMethodCreator.returnValue(
                        defaultQueryFactoryMethodCreator.invokeVirtualMethod(
                                MethodDescriptor.ofMethod(AbstractQueryFactoryProducer.class, "createQueryFactory",
                                        QueryFactory.class, String.class, DataSource.class,
                                        QuerydslCustomTypeRegister.class, String.class),
                                defaultQueryFactoryMethodCreator.getThis(), templateRH, dataSourceRH,
                                registerCustomTypeRH, factoryAliasRH));
            } else {
                ResultHandle registerCustomTypeRH = defaultConfig.registerCustomType.isPresent()
                        ? defaultQueryFactoryMethodCreator.load(defaultConfig.registerCustomType.get())
                        : defaultQueryFactoryMethodCreator.loadNull();

                if (defaultConfig.registerCustomType.isPresent()) {
                    unremovableBeans.produce(UnremovableBeanBuildItem.beanClassNames(defaultConfig.registerCustomType.get()));
                }

                defaultQueryFactoryMethodCreator.returnValue(
                        defaultQueryFactoryMethodCreator.invokeVirtualMethod(
                                MethodDescriptor.ofMethod(AbstractQueryFactoryProducer.class, "createQueryFactory",
                                        QueryFactory.class, String.class, DataSource.class, String.class,
                                        String.class),
                                defaultQueryFactoryMethodCreator.getThis(), templateRH, dataSourceRH,
                                registerCustomTypeRH, factoryAliasRH));
            }
        }

        for (Entry<String, QueryFactoryItemConfig> configEntry : querydslConfig.namedConfig.entrySet()) {
            String named = configEntry.getKey();
            QueryFactoryItemConfig namedConfig = configEntry.getValue();
            if (!isPresentTemplate(namedConfig)) {
                log.warnv("!isPresentTemplate(namedConfig), named: {0}, namedConfig: {1}", named, namedConfig);
                continue;
            }
            if (!namedConfig.datasource.isPresent()) {
                log.warnv("!namedConfig.datasource.isPresent(), named: {0}, namedConfig: {1}", named, namedConfig);
                continue;
            }

            String dataSourceName = namedConfig.datasource.get();
            if (!dataSourceNames.contains(dataSourceName)) {
                log.warnv("Named: {0} data source not found", dataSourceName);
            }

            String suffix = HashUtil.sha1(named);
            String dsVarName = "dataSource_" + suffix;

            FieldCreator dataSourceCreator = classCreator.getFieldCreator(dsVarName, DataSource.class)
                    .setModifiers(Opcodes.ACC_MODULE);
            dataSourceCreator.addAnnotation(Inject.class);
            dataSourceCreator.addAnnotation(AnnotationInstance.create(DotNames.NAMED, null,
                    new AnnotationValue[] { AnnotationValue.createStringValue("value", dataSourceName) }));

            String factoryAlias = namedConfig.factoryAlias.orElse(null);
            if (factoryAlias != null) {
                log.debugv("BeanClassNameExclusion: factoryAlias: {0}", factoryAlias);
                unremovableBeans.produce(UnremovableBeanBuildItem.beanClassNames(factoryAlias));
            }

            MethodCreator namedQueryFactoryMethodCreator = classCreator.getMethodCreator(
                    "createNamedQueryFactory_" + suffix,
                    QueryFactoryCreator.getQueryFactoryType(namedConfig.template, factoryAlias).getName());

            namedQueryFactoryMethodCreator.addAnnotation(ApplicationScoped.class);
            namedQueryFactoryMethodCreator.addAnnotation(Produces.class);
            namedQueryFactoryMethodCreator.addAnnotation(AnnotationInstance.create(DotNames.NAMED, null,
                    new AnnotationValue[] { AnnotationValue.createStringValue("value", named) }));
            namedQueryFactoryMethodCreator.addAnnotation(AnnotationInstance.create(QUERY_FACTORY_QUALIFIER, null,
                    new AnnotationValue[] { AnnotationValue.createStringValue("value", named) }));

            ResultHandle templateRH = namedQueryFactoryMethodCreator.load(namedConfig.template);

            ResultHandle dataSourceRH = namedQueryFactoryMethodCreator.readInstanceField(
                    FieldDescriptor.of(classCreator.getClassName(), dsVarName, DataSource.class.getName()),
                    namedQueryFactoryMethodCreator.getThis());

            ResultHandle factoryAliasRH = factoryAlias != null ? namedQueryFactoryMethodCreator.load(factoryAlias)
                    : namedQueryFactoryMethodCreator.loadNull();

            if (namedConfig.registerCustomTypeInject.isPresent()) {
                String registerCustomTypeInjectName = namedConfig.registerCustomTypeInject.get();
                String injectVarName = "registerCustomType_" + HashUtil.sha1(registerCustomTypeInjectName);

                FieldCreator registerCustomTypeCreator = classCreator
                        .getFieldCreator(injectVarName, QuerydslCustomTypeRegister.class)
                        .setModifiers(Opcodes.ACC_MODULE);

                registerCustomTypeCreator.addAnnotation(Inject.class);
                registerCustomTypeCreator.addAnnotation(AnnotationInstance.create(DotNames.NAMED, null, new AnnotationValue[] {
                        AnnotationValue.createStringValue("value", registerCustomTypeInjectName) }));

                ResultHandle registerCustomTypeRH = namedQueryFactoryMethodCreator
                        .readInstanceField(
                                FieldDescriptor.of(classCreator.getClassName(), injectVarName,
                                        QuerydslCustomTypeRegister.class.getName()),
                                namedQueryFactoryMethodCreator.getThis());

                namedQueryFactoryMethodCreator.returnValue(namedQueryFactoryMethodCreator.invokeVirtualMethod(
                        MethodDescriptor.ofMethod(AbstractQueryFactoryProducer.class, "createQueryFactory",
                                QueryFactory.class, String.class, DataSource.class,
                                QuerydslCustomTypeRegister.class, String.class),
                        namedQueryFactoryMethodCreator.getThis(), templateRH, dataSourceRH, registerCustomTypeRH,
                        factoryAliasRH));

            } else {
                ResultHandle registerCustomTypeRH = namedConfig.registerCustomType.isPresent()
                        ? namedQueryFactoryMethodCreator.load(namedConfig.registerCustomType.get())
                        : namedQueryFactoryMethodCreator.loadNull();

                if (namedConfig.registerCustomType.isPresent()) {
                    unremovableBeans.produce(UnremovableBeanBuildItem.beanClassNames(namedConfig.registerCustomType.get()));
                }

                namedQueryFactoryMethodCreator.returnValue(namedQueryFactoryMethodCreator.invokeVirtualMethod(
                        MethodDescriptor.ofMethod(AbstractQueryFactoryProducer.class, "createQueryFactory",
                                QueryFactory.class, String.class, DataSource.class, String.class,
                                String.class),
                        namedQueryFactoryMethodCreator.getThis(), templateRH, dataSourceRH, registerCustomTypeRH,
                        factoryAliasRH));
            }
        }

        classCreator.close();
    }

    private boolean isPresentTemplate(QueryFactoryItemConfig itemConfig) {
        return itemConfig.template != null && !itemConfig.template.isEmpty();
    }
}
