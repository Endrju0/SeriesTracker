package seriestracker;


public class SingleSerie {
    private int id, season, episode;
    private String title;
    private int status;
    
    public SingleSerie() {
    }
    
    public SingleSerie(int id, int season, int episode, String title, int status) {
        this.id = id;
        this.season = season;
        this.episode = episode;
        this.title = title;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "id: " + id + " | S" + season + "E" + episode + " | Title: " + title;
    }
    
}
