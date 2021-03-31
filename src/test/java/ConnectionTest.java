import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ConnectionTest {
    private static TVSeriesDao tvSeriesDao;
    private static Connection connection;

    @BeforeAll
    public static void setup() {
        tvSeriesDao = new PostgreTVSeriesDao();
        connection = tvSeriesDao.getConnection();
    }

    @Test
    public void testGetConnectionNotNull() throws SQLException {
        assertFalse(connection == null);

    }

    @AfterAll
    public static void tearDown() throws SQLException {
        connection.close();
    }

}
