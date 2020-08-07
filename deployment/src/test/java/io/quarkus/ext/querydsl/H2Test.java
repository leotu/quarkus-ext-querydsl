package io.quarkus.ext.querydsl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.h2.tools.Server;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author Leo Tu
 */
@Disabled
public class H2Test {
    private static final Logger LOGGER = Logger.getLogger(H2Test.class);

    private static String url = "jdbc:h2:tcp://localhost:19092/mem:default";
    private static String username = "username-default";
    private static String password = "username-default";

    /**
     * http://www.h2database.com/html/main.html
     */
    static private Server server;

    @BeforeAll
    static void startDatabase() {
        LOGGER.info("Start H2 server...");
        try {
            server = Server
                    .createTcpServer(new String[] { "-trace", "-tcp", "-tcpAllowOthers", "-tcpPort", "19092", "-tcpDaemon" })
                    .start();
            Thread.sleep(1000 * 1); // waiting for database to be ready
        } catch (Exception e) {
            LOGGER.error("Start H2 server failed", e);
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void stopDatabase() {
        LOGGER.info("Stop...");
        while (true) { // only for GUI tool to view data
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.info(e.toString());
                break;
            }
        }
        LOGGER.debug("Stop H2 server..." + server);
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    @Order(1)
    @Test
    void testConnection() {
        LOGGER.info("testConnection...");
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DatabaseMetaData dmd = conn.getMetaData();
            LOGGER.infov("databaseProductName: {0}, databaseProductVersion: {1}",
                    dmd.getDatabaseProductName(), dmd.getDatabaseProductVersion());
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    @Order(2)
    @Test
    void testDDL() {
        LOGGER.info("testDDL...");
        String table = "demo";
        String ddl = "CREATE TABLE " + table + " (" +
                " id char(32) NOT NULL," +
                " name varchar(128) NOT NULL," +
                " amount decimal(12,3) ," +
                " created_at timestamp NOT NULL," +
                " PRIMARY KEY (id)" +
                ")";

        LOGGER.infov("ddl: {0}", ddl);

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            Statement stmt = conn.createStatement();
            stmt.execute(ddl);

            ResultSet rs = conn.getMetaData().getTables(null, null, table.toUpperCase(), new String[] { "TABLE" });
            boolean tableExists = false;
            while (rs.next()) {
                String tableType = rs.getString("TABLE_TYPE");
                String tableCatalog = rs.getString("TABLE_CAT");
                String tableSchema = rs.getString("TABLE_SCHEM");
                String tableName = rs.getString("TABLE_NAME");
                // tableType:TABLE, tableCatalog:DEFAULT, tableSchema:PUBLIC, table:demo
                LOGGER.infov("tableType:{0}, tableCatalog:{1}, tableSchema:{2}, table:{3}", tableType,
                        tableCatalog, tableSchema, table);
                if (table.equalsIgnoreCase(tableName)) {
                    tableExists = true;
                    break;
                }
            }

            LOGGER.infov("createDDL table: {0} success: {1}", table, tableExists);
            rs.close();
            stmt.close();

            Assertions.assertTrue(tableExists);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    @Order(3)
    @Test
    void testSelect() {
        LOGGER.info("testSelect...");
        String sql = "select * from demo";

        LOGGER.infov("sql: {0}", sql);

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();

            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String name = rsmd.getColumnName(i);
                String type = rsmd.getColumnTypeName(i);
                LOGGER.infov("name:{0}, type:{1}", name, type);
            }

            rs.close();
            stmt.close();

            Assertions.assertTrue(columnCount > 0);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }
}
