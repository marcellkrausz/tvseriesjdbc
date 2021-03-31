public class Series {
    private String title;
    private int numberOfSeasons;
    private String genre;

    public Series(String title, int numberOfSeasons, String genre) {
        this.title = title;
        this.numberOfSeasons = numberOfSeasons;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}