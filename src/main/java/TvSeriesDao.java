import java.sql.*;
import java.util.List;

interface TVSeriesDao {
    public Connection getConnection();

    public void add(Series series);

    public void update(Series series);

    public void delete(String titleToDelete);

    public List<Series> filterByGenre(String genre);

    public void createTable();

    public void dropTable();
}
