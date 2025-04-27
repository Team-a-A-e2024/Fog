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
                // stmt.execute("DROP TABLE IF EXISTS test.users CASCADE");;

                // Drop sequences
                // stmt.execute("DROP SEQUENCE IF EXISTS test.users_id_seq");

                // Recreate test tables based on public tables
                // stmt.execute("CREATE TABLE test.users AS (SELECT * FROM public.users) WITH NO DATA");

                // Recreate sequences
                // stmt.execute("CREATE SEQUENCE test.users_id_seq");
                // stmt.execute("ALTER TABLE test.users ALTER COLUMN id SET DEFAULT nextval('test.users_id_seq')");
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
                // stmt.execute("DELETE FROM test.users CASCADE");

                // Reset sequence
                // stmt.execute("SELECT setval('test.users_id_seq', 1, false)");

                // Insert test data
                /* stmt.execute("INSERT INTO test.users (id, email, password, role, credit) " +
                        "VALUES " +
                        "(DEFAULT, 'admin@olsker.dk', 'Cupc4k3!', 'admin', 999999999), " +
                        "(DEFAULT, 'cph-ab632@cphbusiness.dk', 'Test1', null, 0), " +
                        "(DEFAULT, 'cph-ea178@cphbusiness.dk', 'Test2', null, 0), " +
                        "(DEFAULT, 'cph-fb157@cphbusiness.dk', 'Test3', null, 0), " +
                        "(DEFAULT, 'cph-ta241@cphbusiness.dk', 'Test4', null, 0);");
                 */

                // Update sequence to row number + 1
                // stmt.execute("SELECT setval('test.users_id_seq', COALESCE((SELECT MAX(id) FROM test.users)+1, 1), false)");

            } catch (SQLException e) {
                throw new DatabaseException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
