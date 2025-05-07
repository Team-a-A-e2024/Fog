package app.test;

import app.entities.Customer;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import org.thymeleaf.processor.comment.ICommentStructureHandler;

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
                ;
                stmt.execute("DROP TABLE IF EXISTS test.customers CASCADE");
                stmt.execute("DROP TABLE IF EXISTS test.orders CASCADE");

                // Drop sequences
                stmt.execute("DROP SEQUENCE IF EXISTS test.users_id_seq");
                stmt.execute("DROP SEQUENCE IF EXISTS test.customers_id_seq");
                stmt.execute("DROP SEQUENCE IF EXISTS test.orders_id_seq");

                // Recreate test tables based on public tables
                stmt.execute("CREATE TABLE test.users AS (SELECT * FROM public.users) WITH NO DATA");
                stmt.execute("CREATE TABLE test.customers AS (SELECT * FROM public.customers) WITH NO DATA");
                stmt.execute("CREATE TABLE test.orders AS (SELECT * FROM public.orders)WITH NO DATA");

                // Recreate sequences
                stmt.execute("CREATE SEQUENCE test.users_id_seq");
                stmt.execute("ALTER TABLE test.users ALTER COLUMN id SET DEFAULT nextval('test.users_id_seq')");
                stmt.execute("CREATE SEQUENCE test.customers_id_seq");
                stmt.execute("ALTER TABLE test.customers ALTER COLUMN id SET DEFAULT nextval('test.customers_id_seq')");
                stmt.execute("CREATE SEQUENCE test.orders_id_seq");
                stmt.execute("ALTER TABLE test.orders ALTER COLUMN id SET DEFAULT nextval('test.orders_id_seq')");
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

                // Reset sequence
                stmt.execute("SELECT setval('test.users_id_seq', 1, false)");
                stmt.execute("SELECT setval('test.customers_id_seq',1, false)");
                stmt.execute("SELECT setval('test.orders_id_seq',1, false)");


                // Insert test data
                stmt.execute("INSERT INTO test.users (id, email, password, role) " +
                        "VALUES " +
                        "(DEFAULT, 'admin', 'admin', 'admin'), " +
                        "(DEFAULT, 'test1', 'Test1', 'salesman'), " +
                        "(DEFAULT, 'test2', 'Test2', 'salesman'), " +
                        "(DEFAULT, 'test3', 'Test3', 'salesman'), " +
                        "(DEFAULT, 'test4', 'Test4', 'salesman');");

                stmt.execute("""
                          INSERT INTO test.customers
                            (fullname, address, postal_code, email, phone_number, user_id)
                          VALUES
                            ('Peter Nielsen',      'Theisvej 7',        2300, 'peter@nielsen.dk',   22331122, 1),
                            ('Lone Larsen',        'Grækenlandsvej 3',  2300, 'lone@larsen.dk',     22118833, 2),
                            ('Elias Christensen',  'Kinavej 2',         2300, 'elias@chr.dk',       22993355, 3)
                        """);

                stmt.execute(""" 
                                INSERT INTO test.orders
                                (id,customer_id,total,created_at,status, comments,width,length)
                                VALUES
                        (1,1,45.0,'2025-07-05','awaiting','fast delivery',240,360)
                        """);

                stmt.execute("SELECT setval('test.users_id_seq', COALESCE((SELECT MAX(id) FROM test.users)+1, 1), false)");
                stmt.execute("""
                        SELECT setval('test.customers_id_seq',COALESCE( (SELECT MAX(id) FROM test.customers) + 1, 1 ),false);
                        """);
                stmt.execute("""
                            SELECT setval('test.orders_id_seq',COALESCE((SELECT MAX(id) FROM test.orders) + 1, 1),false);
                        """);
            } catch (SQLException e) {
                throw new DatabaseException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
