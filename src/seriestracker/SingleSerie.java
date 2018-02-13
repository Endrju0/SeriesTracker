package seriestracker;


public class SingleSerie {
    private int id, season, episode;
    private String title;
    private int statusid;
    
    public SingleSerie() {
    }
    
    public SingleSerie(int id, int season, int episode, String title, int statusid) {
        this.id = id;
        this.season = season;
        this.episode = episode;
        this.title = title;
        this.statusid = statusid;
    }

    public int getId() {
        return id;
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }

    public String getTitle() {
        return title;
    }

    public int getStatusid() {
        return statusid;
    }
    
    @Override
    public String toString() {
        return "id: " + id + " | S" + season + "E" + episode + " | Title: " + title + ", Status: " + statusid;
    }
    
}
