import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class PostgreTVSeriesDao implements TVSeriesDao {
    public Connection getConnection() {
        Connection connection = null;
        String userName = "marcellkrausz";
        String password = "Amcsifoci93";
        String host = "localhost";
        String databaseName = "tvseries";


        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://" + host + ":5432/" + databaseName, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void add(Series series) {
        String sql = "INSERT INTO SERIES VALUES( ? ,? ,? );";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, series.getTitle());
            statement.setInt(2, series.getNumberOfSeasons());
            statement.setString(3, series.getGenre());

            statement.executeQuery();

            System.out.println("Inserted records into the table...");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void update(Series series) {
        String sqlUpdate = "UPDATE series SET title = ?, num_of_seasons = ? , genre = ? WHERE title = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(sqlUpdate);

            pstmt.setString(1, series.getTitle());
            pstmt.setInt(2, series.getNumberOfSeasons());
            pstmt.setString(3, series.getGenre());
            pstmt.setString(4, series.getTitle());

            pstmt.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void delete(String titleToDelete) {
        String sqlDelete = "DELETE FROM series WHERE title = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(sqlDelete);

            pstmt.setString(1, titleToDelete);
            pstmt.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Series> filterByGenre(String genre) {
        String sqlFilter = "SELECT * FROM series WHERE genre = ?";
        try (Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sqlFilter);
            pstmt.setString(1, genre);
            ResultSet rs = pstmt.executeQuery();

            List<Series> result = new ArrayList<>();
            while (rs.next()) {
                String title = rs.getString(1);
                int num_of_series = rs.getInt(2);
                String genre2 = rs.getString(3);

                Series series = new Series(title, num_of_series, genre);
                series.setTitle(title);
                series.setNumberOfSeasons(num_of_series);
                series.setGenre(genre2);
                result.add(series);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTable() {
        try (Connection connection = getConnection()) {
            String sql = "CREATE TABLE series"
                    + "(title varchar(255), "
                    + "num_of_seasons INTEGER not NULL, "
                    + "genre varchar(255), "
                    + "PRIMARY KEY ( title ))";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void dropTable() {
        try (Connection connection = getConnection()) {

            String sql = "DROP TABLE SERIES";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.execute();

            System.out.println("Table  deleted in given database...");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}