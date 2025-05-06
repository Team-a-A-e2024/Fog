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
                stmt.execute("DROP TABLE IF EXISTS test.customers CASCADE");
                stmt.execute("DROP TABLE IF EXISTS test.orders CASCADE");
                stmt.execute("DROP TABLE IF EXISTS test.partslist CASCADE");
                stmt.execute("DROP TABLE IF EXISTS test.postal_code CASCADE");

                // Drop sequences
                stmt.execute("DROP SEQUENCE IF EXISTS test.users_id_seq");
                stmt.execute("DROP SEQUENCE IF EXISTS test.customers_id_seq");
                stmt.execute("DROP SEQUENCE IF EXISTS test.orders_id_seq");
                stmt.execute("DROP SEQUENCE IF EXISTS test.partslist_id_seq");
                stmt.execute("DROP SEQUENCE IF EXISTS test.postal_code_seq");

                // Recreate test tables based on public tables
                stmt.execute("CREATE TABLE test.users AS (SELECT * FROM public.users) WITH NO DATA");
                stmt.execute("CREATE TABLE test.customers AS (SELECT * FROM public.customers) WITH NO DATA");
                stmt.execute("CREATE TABLE test.orders AS (SELECT * FROM public.orders) WITH NO DATA");
                stmt.execute("CREATE TABLE test.partslist AS (SELECT * FROM public.partslist) WITH NO DATA");
                stmt.execute("CREATE TABLE test.postal_code AS (SELECT * FROM public.postal_code) WITH NO DATA");

                // Recreate sequences
                stmt.execute("CREATE SEQUENCE test.users_id_seq");
                stmt.execute("ALTER TABLE test.users ALTER COLUMN id SET DEFAULT nextval('test.users_id_seq')");
                stmt.execute("CREATE SEQUENCE test.customers_id_seq");
                stmt.execute("ALTER TABLE test.customers ALTER COLUMN id SET DEFAULT nextval('test.customers_id_seq')");
                stmt.execute("CREATE SEQUENCE test.orders_id_seq");
                stmt.execute("ALTER TABLE test.orders ALTER COLUMN id SET DEFAULT nextval('test.orders_id_seq')");
                stmt.execute("CREATE SEQUENCE test.partslist_id_seq");
                stmt.execute("ALTER TABLE test.partslist ALTER COLUMN id SET DEFAULT nextval('test.partslist_id_seq')");
                stmt.execute("CREATE SEQUENCE test.postal_code_seq");
                stmt.execute("ALTER TABLE test.postal_code ALTER COLUMN postal_code SET DEFAULT nextval('test.postal_code_seq')");

            } catch (SQLException e) {
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
                stmt.execute("DELETE FROM test.customers CASCADE");
                stmt.execute("DELETE FROM test.orders CASCADE");
                stmt.execute("DELETE FROM test.partslist CASCADE");
                stmt.execute("DELETE FROM test.postal_code CASCADE");

                // Reset sequence
                stmt.execute("SELECT setval('test.users_id_seq', 1, false)");
                stmt.execute("SELECT setval('test.customers_id_seq', 1, false)");
                stmt.execute("SELECT setval('test.orders_id_seq', 1, false)");
                stmt.execute("SELECT setval('test.partslist_id_seq', 1, false)");

                // Insert test data into user
                stmt.execute("INSERT INTO test.users (id, email, password, role) " +
                        "VALUES " +
                        "(DEFAULT, 'admin', 'admin', 'admin'), " +
                        "(DEFAULT, 'test1', 'Test1', 'salesman'), " +
                        "(DEFAULT, 'test2', 'Test2', 'salesman'), " +
                        "(DEFAULT, 'test3', 'Test3', 'salesman'), " +
                        "(DEFAULT, 'test4', 'Test4', 'salesman');");

                // Insert test data into customer
                stmt.execute("INSERT INTO test.customers (id, fullname, email, address, phone_number, user_id, postal_code) " +
                        "VALUES " +
                        "(DEFAULT, 'Customer1', 'Customer1', 'test1@test.dk', 'testaddress1', 12245678, 2, 1234), " +
                        "(DEFAULT, 'Customer2', 'Customer2', 'test2@test.dk', 'testaddress2', 13345678, 3, 1234), " +
                        "(DEFAULT, 'Customer3', 'Customer3', 'test3@test.dk', 'testaddress3', 12445678, null, 1234), " +
                        "(DEFAULT, 'Customer4', 'Customer4', 'test4@test.dk', 'testaddress4', 12355678, null, 1234);");

                stmt.execute("INSERT INTO test.orders (id, created_at, total, status, customer_id, partslist_id, length, width, comment) " +
                        "VALUES " +
                        "(DEFAULT, '2025-01-01', '1000', 'Godkendt', 1, 1, 10, 10, 'Ordre til Customer1'), " +
                        "(DEFAULT, '2025-01-02', '1000', 'Afventer', 2, 1, 10, 10, 'Ordre til Customer2'), " +
                        "(DEFAULT, '2025-01-03', '1000', 'Behandles', 4, 1, 10, 10, 'Ordre til Customer4');");

                stmt.execute("INSERT INTO test.partslist (id, name, description) " +
                        "VALUES " +
                        "(DEFAULT, 'Test partslist', 'Test description');");

                stmt.execute("INSERT INTO test.postal_code (postal_code, city) " +
                        "VALUES " +
                        "(1234, 'Test City');");

                // Update sequence to row number + 1
                stmt.execute("SELECT setval('test.users_id_seq', COALESCE((SELECT MAX(id) FROM test.users)+1, 1), false)");
                stmt.execute("SELECT setval('test.customers_id_seq', COALESCE((SELECT MAX(id) FROM test.customers)+1, 1), false)");
                stmt.execute("SELECT setval('test.orders_id_seq', COALESCE((SELECT MAX(id) FROM test.orders)+1, 1), false)");
                stmt.execute("SELECT setval('test.partslist_id_seq', COALESCE((SELECT MAX(id) FROM test.partslist)+1, 1), false)");

            } catch (SQLException e) {
                throw new DatabaseException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}