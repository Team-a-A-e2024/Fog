package app.test;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SetupDatabase {
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=test";
    private static final String DB = "fog";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public static void createTables() throws DatabaseException {
        // Set connection pools

        try {
            Connection connection = connectionPool.getConnection();

            try (Statement stmt = connection.createStatement()) {
                // Drop tables
                stmt.execute("DROP TABLE IF EXISTS test.users CASCADE");
                stmt.execute("DROP TABLE IF EXISTS test.materials CASCADE");
                stmt.execute("DROP TABLE IF EXISTS test.partslist CASCADE");

                // Drop sequences
                stmt.execute("DROP SEQUENCE IF EXISTS test.users_id_seq");
                stmt.execute("DROP SEQUENCE IF EXISTS test.materials_id_seq");
                stmt.execute("DROP SEQUENCE IF EXISTS test.partslist_id_seq");

                // Recreate test tables based on public tables
                stmt.execute("CREATE TABLE test.users AS (SELECT * FROM public.users) WITH NO DATA");
                stmt.execute("CREATE TABLE test.materials AS (SELECT * FROM public.materials) WITH NO DATA");
                stmt.execute("CREATE TABLE test.partslist AS (SELECT * FROM public.partslist) WITH NO DATA");

                // Recreate sequences
                stmt.execute("CREATE SEQUENCE test.users_id_seq");
                stmt.execute("CREATE SEQUENCE test.materials_id_seq");
                stmt.execute("CREATE SEQUENCE test.partslist_id_seq");
                stmt.execute("ALTER TABLE test.users ALTER COLUMN id SET DEFAULT nextval('test.users_id_seq')");
                stmt.execute("ALTER TABLE test.materials ALTER COLUMN id SET DEFAULT nextval('test.materials_id_seq')");
                stmt.execute("ALTER TABLE test.partslist ALTER COLUMN id SET DEFAULT nextval('test.partslist_id_seq')");
            }
            catch (SQLException e) {
                throw new DatabaseException(e.getMessage());
            }

        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static void seedTables() throws DatabaseException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                // Delete data
                stmt.execute("DELETE FROM test.users CASCADE");
                stmt.execute("DELETE FROM test.materials CASCADE");
                stmt.execute("DELETE FROM test.partslist CASCADE");

                // Reset sequence
                stmt.execute("SELECT setval('test.users_id_seq', 1, false)");
                stmt.execute("SELECT setval('test.materials_id_seq', 1, false)");
                stmt.execute("SELECT setval('test.partslist_id_seq', 1, false)");

                // Insert test data
                stmt.execute("INSERT INTO test.users (id, email, password, role) " +
                        "VALUES " +
                        "(DEFAULT, 'admin', 'admin', 'admin'), " +
                        "(DEFAULT, 'test1', 'Test1', 'salesman'), " +
                        "(DEFAULT, 'test2', 'Test2', 'salesman'), " +
                        "(DEFAULT, 'test3', 'Test3', 'salesman'), " +
                        "(DEFAULT, 'test4', 'Test4', 'salesman');");

                stmt.execute("INSERT INTO test.materials (id, description, unit, price)" +
                        "VALUES" +
                        "(DEFAULT, '97x97 mm. trykimp. Stolpe', 'Stk.', 42.95), " +
                        "(DEFAULT, '45x195 mm. spærtræ ubh.', 'Stk.', 45.95);");

                stmt.execute("INSERT INTO test.partslist (id, order_id, material_id, quantity, description, length)" +
                        "VALUES " +
                        "(DEFAULT, 1, 1, 1, 'Test wood', 600);");


                // Update sequence to row number + 1
                stmt.execute("SELECT setval('test.users_id_seq', COALESCE((SELECT MAX(id) FROM test.users)+1, 1), false)");
                stmt.execute("SELECT setval('test.materials_id_seq', COALESCE((SELECT MAX(id) FROM test.materials)+1, 1), false)");
                stmt.execute("SELECT setval('test.partslist_id_seq', COALESCE((SELECT MAX(id) FROM test.partslist)+1, 1), false)");

            } catch (SQLException e) {
                throw new DatabaseException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
