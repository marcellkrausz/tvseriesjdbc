import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeriesTest {
    private static Series series;

    @BeforeAll
    public static void setup() {
        series = new Series("Star Trek voyager", 7, "sci-fi");
    }

    @Test
    public void testGetTitle() {
        assertEquals("Star Trek voyager", series.getTitle());
    }

    @Test
    public void testGetNumberOfSeasons() {
        assertEquals(7, series.getNumberOfSeasons());
    }

    @Test
    public void testGetGenre() {
        assertEquals("sci-fi", series.getGenre());
    }
}
