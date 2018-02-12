package seriestracker;

public class SeriesTracker {

    public static void main(String[] args) {
        SqliteDB db = new SqliteDB();
        db.getAllSerie();
        db.closeConnection();
    }
    
}
