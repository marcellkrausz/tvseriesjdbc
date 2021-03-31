import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class TVSeriesDaoTest {
    private static TVSeriesDao tvSeriesDao;
    private static Connection connection;
    private static Statement statement;

    @BeforeAll
    public static void setup() throws SQLException {
        tvSeriesDao = new PostgreTVSeriesDao();
        connection = tvSeriesDao.getConnection();
        statement = connection.createStatement();
    }

    @BeforeEach
    void init() throws SQLException {
        statement.execute("DROP TABLE IF EXISTS series;");
        statement.execute("CREATE TABLE series (title varchar, num_of_seasons integer, genre varchar, PRIMARY KEY(title));");
    }

    private List<Series> generateExpectedSeriesForAdd() {
        return new ArrayList<Series>() {
            {
                add(new Series("Star Trek voyager", 7, "sci-fi"));
            }
        };
    }

    @Test
    public void testAdd() throws SQLException {
        Series seriesToAdd = new Series("Star Trek voyager", 7, "sci-fi");
        tvSeriesDao.add(seriesToAdd);
        ResultSet rs = statement.executeQuery("select * from series");
        assertTrue(areSameSeries(generateExpectedSeriesForAdd(), convertResults(rs)));
    }

    private void prepareDbForUpdateTest() throws SQLException {
        statement.execute("INSERT INTO series (title, num_of_seasons, genre) VALUES ('Star Trek voyager', 5, 'something about aliens')");
    }

    private List<Series> generateExpectedSeriesForUpdate() {
        return new ArrayList<Series>() {
            {
                add(new Series("Star Trek voyager", 7, "sci-fi"));
            }
        };
    }

    @Test
    public void testUpdate() throws SQLException {
        prepareDbForUpdateTest();
        List<Series> expectedSeries = generateExpectedSeriesForUpdate();
        Series seriesToUpdate = new Series("Star Trek voyager", 7, "sci-fi");
        tvSeriesDao.update(seriesToUpdate);
        ResultSet rs = statement.executeQuery("select * from series");
        assertTrue(areSameSeries(expectedSeries, convertResults(rs)));
    }

    @Test
    public void testDelete() throws SQLException {
        statement.execute("INSERT INTO series (title, num_of_seasons, genre) VALUES ('Star Trek voyager', 7, 'something about aliens')");
        String titleToDelete = "Star Trek voyager";
        tvSeriesDao.delete(titleToDelete);
        ResultSet rs = statement.executeQuery("select count(*) AS rowcount from series");
        rs.next();
        assertEquals(0, rs.getInt("rowcount"));
    }


    private void prepareDbForSelectTest() throws SQLException {
        statement.execute("INSERT INTO series (title, num_of_seasons, genre) VALUES ('Star Trek voyager', 7, 'sci-fy')");
        statement.execute("INSERT INTO series (title, num_of_seasons, genre) VALUES ('Game of thrones', 8, 'fantasy')");
        statement.execute("INSERT INTO series (title, num_of_seasons, genre) VALUES ('Friends', 10, 'comedy')");
        statement.execute("INSERT INTO series (title, num_of_seasons, genre) VALUES ('The IT Crowd', 5, 'comedy')");
    }

    private List<Series> generateExpectedSeriesForSelect() {
        return new ArrayList<Series>() {
            {
                add(new Series("Friends", 10, "comedy"));
                add(new Series("The IT Crowd", 5, "comedy"));
            }
        };
    }

    @Test
    public void testSelectByGenre() throws SQLException {
        prepareDbForSelectTest();
        List<Series> result = tvSeriesDao.filterByGenre("comedy");
        List<Series> expectedSeries = generateExpectedSeriesForSelect();
        assertTrue(areSameSeries(result, expectedSeries));
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        connection.close();
    }

    private static boolean areSameSeries(List<Series> series, List<Series> allSpecificSeries) {
        return hasAllSpecificSeries(series, allSpecificSeries) && series.size() == allSpecificSeries.size();
    }

    private static boolean hasAllSpecificSeries(List<Series> series, List<Series> allSpecificSeries) {
        for (Series specificSeries : allSpecificSeries) {
            if (!hasSpecificSeries(series, specificSeries)) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasSpecificSeries(List<Series> series, Series specificSeries) {
        for (Series actualSeries : series) {
            if (actualSeries.getTitle().equals(specificSeries.getTitle())
                    && actualSeries.getNumberOfSeasons() == specificSeries.getNumberOfSeasons()
                    && actualSeries.getGenre().equals(specificSeries.getGenre())) {
                return true;
            }
        }
        return false;
    }

    private List<Series> convertResults(ResultSet rs) throws SQLException {
        List<Series> results = new ArrayList<>();
        String title;
        Integer numberOfSeasons;
        String genre;
        while (rs.next()) {
            title = rs.getString("title");
            numberOfSeasons = rs.getInt("num_of_seasons");
            genre = rs.getString("genre");
            results.add(new Series(title, numberOfSeasons, genre));
        }
        return results;
    }
}
