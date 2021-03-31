import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TableCreationDeletionTest {
    private static TVSeriesDao tvSeriesDao;
    private static Connection connection;
    private static Statement statement;

    @BeforeAll
    public static void setup() throws SQLException {
        tvSeriesDao = new PostgreTVSeriesDao();
        connection = tvSeriesDao.getConnection();
        statement = connection.createStatement();
    }

    @Test
    public void testCreateTable() throws SQLException {
        statement.execute("DROP TABLE IF EXISTS series;");
        tvSeriesDao.createTable();
        ResultSet rs = statement.executeQuery("select count(*) AS rowcount FROM pg_tables WHERE tablename  = 'series';");
        rs.next();
        assertEquals(1, rs.getInt("rowcount"));
    }

    @Test
    public void testDropTable() throws SQLException {
        statement.execute("DROP TABLE IF EXISTS series;");
        statement.execute("CREATE TABLE series (title varchar, num_of_seasons integer, genre varchar, PRIMARY KEY(title));");
        tvSeriesDao.dropTable();
        ResultSet rs = statement.executeQuery("select count(*) AS rowcount FROM pg_tables WHERE tablename  = 'series';");
        rs.next();
        assertEquals(0, rs.getInt("rowcount"));

    }

    @AfterAll
    public static void tearDown() throws SQLException {
        connection.close();
    }
}
