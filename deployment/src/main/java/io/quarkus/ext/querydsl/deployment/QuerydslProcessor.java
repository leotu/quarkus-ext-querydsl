package io.quarkus.ext.querydsl.deployment;

import java.util.Map.Entry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.DotName;
import org.jboss.logging.Logger;
import org.objectweb.asm.Opcodes;

import com.querydsl.sql.dml.AbstractSQLDeleteClause;
import com.querydsl.sql.dml.AbstractSQLInsertClause;
import com.querydsl.sql.dml.AbstractSQLUpdateClause;
import com.querydsl.sql.dml.SQLMergeClause;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.deployment.DataSourceInitializedBuildItem;
import io.quarkus.agroal.runtime.AgroalBuildTimeConfig;
import io.quarkus.agroal.runtime.AgroalTemplate;
import io.quarkus.arc.deployment.BeanContainerListenerBuildItem;
import io.quarkus.arc.deployment.GeneratedBeanBuildItem;
import io.quarkus.arc.deployment.UnremovableBeanBuildItem;
import io.quarkus.arc.deployment.UnremovableBeanBuildItem.BeanClassNameExclusion;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.substrate.ReflectiveClassBuildItem;
import io.quarkus.deployment.recording.RecorderContext;
import io.quarkus.deployment.util.HashUtil;
import io.quarkus.ext.querydsl.runtime.AbstractQueryFactoryProducer;
import io.quarkus.ext.querydsl.runtime.AbstractQueryFactoryProducer.QueryFactoryQualifier;
import io.quarkus.ext.querydsl.runtime.QueryFactory;
import io.quarkus.ext.querydsl.runtime.QueryFactoryItemConfig;
import io.quarkus.ext.querydsl.runtime.QueryFactoryWrapper;
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
 * </pre>
 * 
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
public class QuerydslProcessor {
    private static final Logger log = Logger.getLogger(QuerydslProcessor.class);

    private static final DotName QUERY_FACTORY_QUALIFIER = DotName.createSimple(QueryFactoryQualifier.class.getName());

    private final String queryFactoryProducerClassName = AbstractQueryFactoryProducer.class.getPackage().getName() + "."
            + "QueryFactoryProducer";

    /**
     * Register a extension capability and feature
     *
     * @return QueryDSL feature build item
     */
    @Record(ExecutionTime.STATIC_INIT)
    @BuildStep(providesCapabilities = "io.quarkus.ext.querydsl")
    FeatureBuildItem featureBuildItem() {
        return new FeatureBuildItem("querydsl");
    }

    @SuppressWarnings("unchecked")
    @Record(ExecutionTime.STATIC_INIT)
    @BuildStep
    BeanContainerListenerBuildItem build(RecorderContext recorder, QuerydslTemplate template,
            BuildProducer<ReflectiveClassBuildItem> reflectiveClass,
            BuildProducer<UnremovableBeanBuildItem> unremovableBeans, QuerydslConfig querydslConfig,
            BuildProducer<GeneratedBeanBuildItem> generatedBean, AgroalBuildTimeConfig agroalBuildTimeConfig) {

        reflectiveClass.produce(new ReflectiveClassBuildItem(true, false, AbstractQueryFactoryProducer.class));
        reflectiveClass.produce(new ReflectiveClassBuildItem(true, true, AbstractSQLDeleteClause.class,
                AbstractSQLUpdateClause.class, AbstractSQLInsertClause.class, SQLMergeClause.class));

        if (!isPresentTemplate(querydslConfig.defaultConfig)) {
            log.warn("No default sql-template been defined");
        }

        createQueryFactoryProducerBean(generatedBean, unremovableBeans, querydslConfig, agroalBuildTimeConfig);
        return new BeanContainerListenerBuildItem(template.addContainerCreatedListener(
                (Class<? extends AbstractQueryFactoryProducer>) recorder.classProxy(queryFactoryProducerClassName)));
    }

    @Record(ExecutionTime.RUNTIME_INIT)
    @BuildStep
    void configureDataSource(QuerydslTemplate template, DataSourceInitializedBuildItem dataSourceInitialized,
            BuildProducer<QuerydslInitializedBuildItem> querydslInitialized, QuerydslConfig querydslConfig) {

        if (!isPresentTemplate(querydslConfig.defaultConfig) && querydslConfig.namedConfig.isEmpty()) {
            // No QueryDSL has been configured so bail out
            log.info("No QueryDSL has been configured");
            return;
        }
        querydslInitialized.produce(new QuerydslInitializedBuildItem());
    }

    private void createQueryFactoryProducerBean(BuildProducer<GeneratedBeanBuildItem> generatedBean,
            BuildProducer<UnremovableBeanBuildItem> unremovableBeans, QuerydslConfig querydslConfig,
            AgroalBuildTimeConfig dataSourceConfig) {
        ClassOutput classOutput = new ClassOutput() {
            @Override
            public void write(String name, byte[] data) {
                generatedBean.produce(new GeneratedBeanBuildItem(name, data));
            }
        };
        unremovableBeans
                .produce(new UnremovableBeanBuildItem(new BeanClassNameExclusion(queryFactoryProducerClassName)));

        ClassCreator classCreator = ClassCreator.builder().classOutput(classOutput)
                .className(queryFactoryProducerClassName).superClass(AbstractQueryFactoryProducer.class).build();
        classCreator.addAnnotation(ApplicationScoped.class);

        QueryFactoryItemConfig defaultConfig = querydslConfig.defaultConfig;
        if (isPresentTemplate(defaultConfig)) {
            if (!dataSourceConfig.defaultDataSource.driver.isPresent()) {
                log.warn("Default dataSource not found");
                System.err.println(">>> Default dataSource not found");
            }
            if (defaultConfig.datasource.isPresent()
                    && !AgroalTemplate.DEFAULT_DATASOURCE_NAME.equals(defaultConfig.datasource.get())) {
                log.warn("Skip default dataSource name: " + defaultConfig.datasource.get());
            }
            String dsVarName = "defaultDataSource";

            // FIXME: Lazy Initialize DataSource ?
            // Type[] args = new Type[] {
            // Type.create(DotName.createSimple(AgroalDataSource.class.getName()),
            // Kind.CLASS) };
            // ParameterizedType type =
            // ParameterizedType.create(DotName.createSimple(Instance.class.getName()),
            // args, null);
            // log.debug("type: " + type); //
            // javax.enterprise.inject.Instance<io.agroal.api.AgroalDataSource>
            // FieldCreator defaultDataSourceCreator = classCreator.getFieldCreator(varName,
            // DescriptorUtils.typeToString(type))
            // .setModifiers(Opcodes.ACC_MODULE);

            FieldCreator defaultDataSourceCreator = classCreator.getFieldCreator(dsVarName, AgroalDataSource.class)
                    .setModifiers(Opcodes.ACC_MODULE);

            defaultDataSourceCreator.addAnnotation(Default.class);
            defaultDataSourceCreator.addAnnotation(Inject.class);

            //
            String template = defaultConfig.template;
            MethodCreator defaultQueryFactoryMethodCreator = classCreator.getMethodCreator("createDefaultQueryFactory",
                    QueryFactory.getQueryFactoryType(template));

            defaultQueryFactoryMethodCreator.addAnnotation(Singleton.class);
            defaultQueryFactoryMethodCreator.addAnnotation(Produces.class);
            defaultQueryFactoryMethodCreator.addAnnotation(Default.class);

            ResultHandle templateRH = defaultQueryFactoryMethodCreator.load(template);

            ResultHandle dataSourceRH = defaultQueryFactoryMethodCreator.readInstanceField(
                    FieldDescriptor.of(classCreator.getClassName(), dsVarName, AgroalDataSource.class.getName()),
                    defaultQueryFactoryMethodCreator.getThis());

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

                defaultQueryFactoryMethodCreator.returnValue( //
                        defaultQueryFactoryMethodCreator.invokeVirtualMethod(
                                MethodDescriptor.ofMethod(AbstractQueryFactoryProducer.class, "createQueryFactory",
                                        QueryFactoryWrapper.class, String.class, AgroalDataSource.class,
                                        QuerydslCustomTypeRegister.class),
                                defaultQueryFactoryMethodCreator.getThis(), templateRH, dataSourceRH,
                                registerCustomTypeRH));
            } else {
                ResultHandle registerCustomTypeRH = defaultConfig.registerCustomType.isPresent()
                        ? defaultQueryFactoryMethodCreator.load(defaultConfig.registerCustomType.get())
                        : defaultQueryFactoryMethodCreator.loadNull();

                if (defaultConfig.registerCustomType.isPresent()) {
                    unremovableBeans.produce(new UnremovableBeanBuildItem(
                            new BeanClassNameExclusion(defaultConfig.registerCustomType.get())));
                }

                defaultQueryFactoryMethodCreator.returnValue( //
                        defaultQueryFactoryMethodCreator.invokeVirtualMethod(
                                MethodDescriptor.ofMethod(AbstractQueryFactoryProducer.class, "createQueryFactory",
                                        QueryFactoryWrapper.class, String.class, AgroalDataSource.class, String.class),
                                defaultQueryFactoryMethodCreator.getThis(), templateRH, dataSourceRH,
                                registerCustomTypeRH));
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
                log.warnv("(!config.datasource.isPresent()), named: {0}, namedConfig: {1}", named, namedConfig);
                continue;
            }

            String dataSourceName = namedConfig.datasource.get();
            if (!dataSourceConfig.namedDataSources.containsKey(dataSourceName)) {
                log.warnv("Named: '{0}' dataSource not found", dataSourceName);
                System.err.println(">>> Named: '" + dataSourceName + "' dataSource not found");
            }

            String suffix = HashUtil.sha1(named);
            String dsVarName = "dataSource_" + suffix;

            FieldCreator dataSourceCreator = classCreator.getFieldCreator(dsVarName, AgroalDataSource.class)
                    .setModifiers(Opcodes.ACC_MODULE);
            dataSourceCreator.addAnnotation(Inject.class);
            dataSourceCreator.addAnnotation(AnnotationInstance.create(DotNames.NAMED, null,
                    new AnnotationValue[] { AnnotationValue.createStringValue("value", dataSourceName) }));

            MethodCreator namedQueryFactoryMethodCreator = classCreator.getMethodCreator(
                    "createNamedQueryFactory_" + suffix,
                    QueryFactory.getQueryFactoryType(namedConfig.template).getName());

            namedQueryFactoryMethodCreator.addAnnotation(ApplicationScoped.class);
            namedQueryFactoryMethodCreator.addAnnotation(Produces.class);
            namedQueryFactoryMethodCreator.addAnnotation(AnnotationInstance.create(DotNames.NAMED, null,
                    new AnnotationValue[] { AnnotationValue.createStringValue("value", named) }));
            namedQueryFactoryMethodCreator.addAnnotation(AnnotationInstance.create(QUERY_FACTORY_QUALIFIER, null,
                    new AnnotationValue[] { AnnotationValue.createStringValue("value", named) }));

            ResultHandle templateRH = namedQueryFactoryMethodCreator.load(namedConfig.template);

            ResultHandle dataSourceRH = namedQueryFactoryMethodCreator.readInstanceField(
                    FieldDescriptor.of(classCreator.getClassName(), dsVarName, AgroalDataSource.class.getName()),
                    namedQueryFactoryMethodCreator.getThis());

            if (namedConfig.registerCustomTypeInject.isPresent()) {
                String registerCustomTypeInjectName = namedConfig.registerCustomTypeInject.get();
                String injectVarName = "registerCustomType_" + HashUtil.sha1(registerCustomTypeInjectName);

                FieldCreator registerCustomTypeCreator = classCreator
                        .getFieldCreator(injectVarName, QuerydslCustomTypeRegister.class)
                        .setModifiers(Opcodes.ACC_MODULE);

                registerCustomTypeCreator.addAnnotation(Inject.class);
                registerCustomTypeCreator
                        .addAnnotation(AnnotationInstance.create(DotNames.NAMED, null, new AnnotationValue[] {
                                AnnotationValue.createStringValue("value", registerCustomTypeInjectName) }));

                ResultHandle registerCustomTypeRH = namedQueryFactoryMethodCreator
                        .readInstanceField(
                                FieldDescriptor.of(classCreator.getClassName(), injectVarName,
                                        QuerydslCustomTypeRegister.class.getName()),
                                namedQueryFactoryMethodCreator.getThis());

                namedQueryFactoryMethodCreator.returnValue(namedQueryFactoryMethodCreator.invokeVirtualMethod(
                        MethodDescriptor.ofMethod(AbstractQueryFactoryProducer.class, "createQueryFactory",
                                QueryFactoryWrapper.class, String.class, AgroalDataSource.class,
                                QuerydslCustomTypeRegister.class),
                        namedQueryFactoryMethodCreator.getThis(), templateRH, dataSourceRH, registerCustomTypeRH));

            } else {
                ResultHandle registerCustomTypeRH = namedConfig.registerCustomType.isPresent()
                        ? namedQueryFactoryMethodCreator.load(namedConfig.registerCustomType.get())
                        : namedQueryFactoryMethodCreator.loadNull();

                if (namedConfig.registerCustomType.isPresent()) {
                    unremovableBeans.produce(new UnremovableBeanBuildItem(
                            new BeanClassNameExclusion(namedConfig.registerCustomType.get())));
                }

                namedQueryFactoryMethodCreator.returnValue(namedQueryFactoryMethodCreator.invokeVirtualMethod(
                        MethodDescriptor.ofMethod(AbstractQueryFactoryProducer.class, "createQueryFactory",
                                QueryFactoryWrapper.class, String.class, AgroalDataSource.class, String.class),
                        namedQueryFactoryMethodCreator.getThis(), templateRH, dataSourceRH, registerCustomTypeRH));
            }
        }

        classCreator.close();
    }

    private boolean isPresentTemplate(QueryFactoryItemConfig itemConfig) {
        return itemConfig.template != null && !itemConfig.template.isEmpty();
    }
}
