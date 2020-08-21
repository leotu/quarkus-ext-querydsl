package io.quarkus.ext.querydsl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.ext.querydsl.demo.QDepartments;
import io.quarkus.ext.querydsl.demo.QDeptEmp;
import io.quarkus.ext.querydsl.demo.QDeptManager;
import io.quarkus.ext.querydsl.demo.QEmployees;
import io.quarkus.ext.querydsl.demo.QSalaries;
import io.quarkus.ext.querydsl.demo.QTitles;
import io.quarkus.ext.querydsl.demo.pojos.Departments;
import io.quarkus.ext.querydsl.demo.pojos.DeptEmp;
import io.quarkus.ext.querydsl.demo.pojos.DeptManager;
import io.quarkus.ext.querydsl.demo.pojos.Employees;
import io.quarkus.ext.querydsl.demo.pojos.Salaries;
import io.quarkus.ext.querydsl.demo.pojos.Titles;
import io.quarkus.ext.querydsl.runtime.QueryFactoryCreator.MySQLFactory;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.test.QuarkusUnitTest;

/**
 * VM arguments add "-Djava.util.logging.manager=org.jboss.logmanager.LogManager"
 * 
 * @author Leo Tu
 */
//@Disabled
public class QuerydslTest {
    private static final Logger LOGGER = Logger.getLogger(QuerydslTest.class);

    @Order(10)
    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addAsResource("application.properties", "application.properties")
                    .addClasses(TestBean.class,
                            MyCustomTypeRegisterProducer.class,
                            MyCustomTypeRegister1.class,
                            DSLFactory.class, DSLFactory2.class,
                            Departments.class, DeptEmp.class, DeptManager.class,
                            Employees.class, Salaries.class, Titles.class,
                            QDepartments.class, QDeptEmp.class, QDeptManager.class,
                            QEmployees.class, QSalaries.class, QTitles.class));

    @Inject
    TestBean testBean;

    @Test
    public void testEntry() {
        LOGGER.info("BEGIN...");
        if (testBean == null) {
            LOGGER.error("Inject testBean is null !");
            return;
        }
        try {
            testBean.testConnection();
            //
            testBean.testAllService();
        } catch (Exception e) {
            if ("test-rollback".equals(e.getMessage())) {
                LOGGER.info(e.getMessage());
            } else {
                LOGGER.error("", e);
            }
        } finally {
            LOGGER.info("END.");
        }
    }

    @ApplicationScoped
    // @Singleton
    static class TestBean {

        @Inject
        DSLFactory queryFactory; // default + factory alias

        @Inject
        @Named("qf1")
        MySQLFactory queryFactory1;

        @Inject
        @Named("qf2")
        DSLFactory2 queryFactory2; // factory alias

        private Service service;

        @PostConstruct
        void onPostConstruct() {
            LOGGER.debug("onPostConstruct");
        }

        /**
         * Called when the runtime has started
         *
         * @param event
         */
        void onStart(@Observes StartupEvent event) {
            LOGGER.debug("onStart, event=" + event);
            service = new Service(queryFactory);
        }

        void onStop(@Observes ShutdownEvent event) {
            LOGGER.debug("onStop, event=" + event);
        }

        // public void onTxProgressEvent(@Observes(during = TransactionPhase.IN_PROGRESS) String msg) {
        //     LOGGER.debug("IN_PROGRESS, msg=" + msg);
        // }
        // public void onTxBeforeCompletionEvent(@Observes(during = TransactionPhase.BEFORE_COMPLETION) String msg) {
        //     LOGGER.debug("BEFORE_COMPLETION, msg=" + msg);
        // }
        // public void onTxSuccessEvent(@Observes(during = TransactionPhase.AFTER_SUCCESS) String msg) {
        //     LOGGER.debug("AFTER_SUCCESS, msg=" + msg);
        // }
        // public void onTxFailureEvent(@Observes(during = TransactionPhase.AFTER_FAILURE) String msg) {
        //     LOGGER.debug("AFTER_FAILURE, msg=" + msg);
        // }
        // public void onTxCompletionEvent(@Observes(during = TransactionPhase.AFTER_COMPLETION) String msg) {
        //     LOGGER.debug("AFTER_COMPLETION, msg=" + msg);
        // }

        void testConnection() throws Exception {
            // (default)
            try (Connection conn = queryFactory.getConnection()) {
                DatabaseMetaData dmd = conn.getMetaData();
                LOGGER.debugv("queryFactory, databaseProductName: {0}, databaseProductVersion: {1}",
                        dmd.getDatabaseProductName(), dmd.getDatabaseProductVersion());
            } catch (Exception e) {
                LOGGER.error("queryFactory.getConnection", e);
                throw e;
            }

            // (1)
            try (Connection conn = queryFactory1.getConnection()) {
                DatabaseMetaData dmd = conn.getMetaData();
                LOGGER.debugv("queryFactory1, databaseProductName: {0}, databaseProductVersion: {1}",
                        dmd.getDatabaseProductName(), dmd.getDatabaseProductVersion());
                listTableTypes(conn);
            } catch (Exception e) {
                LOGGER.error("queryFactory1.getConnection", e);
                throw e;
            }

            // (2)
            try (Connection conn = queryFactory2.getConnection()) {
                DatabaseMetaData dmd = conn.getMetaData();
                LOGGER.debugv("queryFactory2, databaseProductName: {0}, databaseProductVersion: {1}",
                        dmd.getDatabaseProductName(), dmd.getDatabaseProductVersion());
            } catch (Exception e) {
                LOGGER.error("queryFactory2.getConnection", e);
                throw e;
            }
        }

        private void listTableTypes(Connection conn) throws Exception {
            DatabaseMetaData dmd = conn.getMetaData();
            try (ResultSet ttRs = dmd.getTableTypes()) {
                int columnCount = ttRs.getMetaData().getColumnCount();
                while (ttRs.next()) {
                    for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                        String tableType = ttRs.getString(columnIndex);
                        LOGGER.debugv("tableType: {0}", tableType);
                    }
                }
            }
        }

        // === (default)
        @Transactional(TxType.REQUIRED)
        void testAllService() throws Exception {
            List<Departments> queryAll = service.queryAll();
            Assertions.assertTrue(queryAll.size() > 0);

            Departments testOne = service.queryOne(queryAll.get(0).getDeptNo());
            Assertions.assertNotNull(testOne);

            Departments newOne = new Departments();
            newOne.setDeptNo("H911");
            newOne.setDeptName("Help Dept");
            long insertOne = service.insertOne(newOne);
            Assertions.assertEquals(1, insertOne);

            long countAll = service.countAll();
            Assertions.assertTrue(countAll == queryAll.size() + 1);

            Departments updatedOne = newOne.clone();
            updatedOne.setDeptName("Help Dept 911");
            long updateOne = service.updateOne(updatedOne);
            Assertions.assertEquals(1, updateOne);

            Departments queryOne = service.queryOne(updatedOne.getDeptNo());
            Assertions.assertTrue(queryOne != null);
            Assertions.assertEquals(queryOne.getDeptName(), updatedOne.getDeptName());

            long deleteOne = service.deleteOne(queryOne.getDeptNo());
            Assertions.assertEquals(1, deleteOne);

            long countAll2 = service.countAll();
            Assertions.assertTrue(countAll2 == queryAll.size());

            long deleteAll = service.deleteAll();
            Assertions.assertTrue(countAll2 == deleteAll);

            long countAll3 = service.countAll();
            Assertions.assertTrue(countAll3 == 0);

            // rollback
            throw new RuntimeException("test-rollback");
        }
    }

    static class Service {
        final private DSLFactory dsl;

        public Service(DSLFactory dsl) {
            this.dsl = dsl;
        }

        long insertOne(Departments data) {
            long row = dsl.insert(QDepartments.$)
                    .set(QDepartments.$.deptNo, data.getDeptNo())
                    .set(QDepartments.$.deptName, data.getDeptName())
                    .execute();
            LOGGER.debugv("row: {0}", row);
            return row;
        }

        long updateOne(Departments data) {
            long row = dsl.update(QDepartments.$)
                    .set(QDepartments.$.deptName, data.getDeptName())
                    .where(QDepartments.$.deptNo.eq(data.getDeptNo()))
                    .execute();
            LOGGER.debugv("row: {0}", row);
            return row;
        }

        List<Departments> queryAll() {
            List<Departments> dataList = dsl.select(QDepartments.$)
                    .from(QDepartments.$)
                    .orderBy(QDepartments.$.deptNo.asc())
                    .fetch();
            LOGGER.debugv("dataList.size: {0}", dataList.size());
            return dataList;
        }

        Departments queryOne(String deptNo) {
            Departments data = dsl.select(QDepartments.$)
                    .from(QDepartments.$)
                    .where(QDepartments.$.deptNo.eq(deptNo))
                    .fetchOne();
            LOGGER.debugv("data: {0}", data);
            return data;
        }

        long deleteOne(String deptNo) {
            long row = dsl.delete(QDepartments.$)
                    .where(QDepartments.$.deptNo.eq(deptNo))
                    .execute();
            LOGGER.debugv("row: {0}", row);
            return row;
        }

        long deleteAll() {
            long row = dsl.delete(QDepartments.$)
                    .execute();
            LOGGER.debugv("row: {0}", row);
            return row;
        }

        long countAll() {
            long total = dsl.selectOne().from(QDepartments.$).fetchCount();
            LOGGER.debugv("total: {0}", total);
            return total;
        }
    }
}
