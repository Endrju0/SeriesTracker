package seriestracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SqliteDB {
    private ArrayList<SingleSerie> al = new ArrayList<>();
    public final int MAX_SEASONS = 100;
    public final int MAX_EPISODES = 300;
    public String dbName = "SeriesDB";
    
    
    public SqliteDB() { //trying connect to database
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC"); //Jar file that I added to our 
            c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".sqlite"); //controller
            stmt = c.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS \"main\".\"serie\" (\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"season\" INTEGER, \"episode\" INTEGER, \"title\" VARCHAR, \"status\" INTEGER NOT NULL)");
            refreshList();
            stmt.close();
            c.close();
        } catch(ClassNotFoundException | SQLException e) {
            System.err.println(e);
        }
        System.out.println("Table creation succeded");
    }
    
    public void loadToList() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:SeriesDB.sqlite"); 
            
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM serie");
            int id, season, episode, statusid;
            String title;
            
            while(rs.next()) {
                id = rs.getInt("id");
                season = rs.getInt("season");
                episode = rs.getInt("episode");
                title = rs.getString("title");
                statusid = rs.getInt("status");
                
                al.add(new SingleSerie(id, season, episode, title, statusid));
            }
            rs.close();
            stmt.close();
            c.close();    
        } catch(ClassNotFoundException | SQLException e) {
            System.err.println(e);
        }
    }
    
    public void refreshList() {
        al.clear();
        loadToList();
    }
    
    public String[] loadView(int type) {
        SingleSerie tmp;
        String[] value = new String[al.size()];
        for(int i=0; i<al.size(); i++) {
             tmp = al.get(i);
             if(tmp.getStatus() == type) value[i] = tmp.toString();
        }
        return value;
    }
    
    public void add(int season, int episode, String title, int status) {
        if(status < 1 || status > 3) throw new IllegalArgumentException("Status out of bounds (1,3)");
        if(episode < 0 || episode > MAX_EPISODES ) throw new IllegalArgumentException("Episodes out of bounds (0,"+ MAX_EPISODES + ")");
        if(season < 0 || season > MAX_SEASONS) throw new IllegalArgumentException("Seasons out of bounds (0,"+ MAX_SEASONS + ")");
        
        Connection c = null;
        Statement stmt = null;
        
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:SeriesDB.sqlite"); 
            
            stmt = c.createStatement();
            String query = "INSERT INTO serie(season, episode, title, status) VALUES (" + season + "," + episode + "," + "\"" + title + "\"," + status + ")"; 
            stmt.executeUpdate(query);
            
            stmt.close();
            c.close();
        } catch(ClassNotFoundException | SQLException e) {
            System.err.println(e);
        }
    }
    
    public void remove(String title) {
        SingleSerie obj;
        
        Connection c = null;
        Statement stmt = null;
        
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:SeriesDB.sqlite"); 
            
            stmt = c.createStatement();
            for(int i=0; i<al.size(); i++) {
                obj = al.get(i);
                if(obj.getTitle().equalsIgnoreCase(title)) {
                    String query = "DELETE FROM serie WHERE id='" + obj.getId() + "'";
                    stmt.executeUpdate(query);
                    System.out.println("Deletion succeded");
                }
            }
            
            stmt.close();
            c.close();
        } catch(ClassNotFoundException | SQLException e) {
            System.err.println(e);
        }
        refreshList();
    }
    
    public SingleSerie getByID(int id) {
        SingleSerie obj = new SingleSerie();
        
        Connection c = null;
        Statement stmt = null;
        
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:SeriesDB.sqlite"); 
            
            String query = "SELECT * FROM serie WHERE id=" + id;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            obj.setSeason(rs.getInt("season"));
            obj.setEpisode(rs.getInt("episode"));
            obj.setTitle(rs.getString("title"));
            obj.setStatus(rs.getInt("status")); 
            
            rs.close();
            stmt.close();
            c.close();
        } catch(ClassNotFoundException | SQLException e) {
            System.err.println(e);
        }
        return obj;
    }
    
    public SingleSerie getByTitle(String title) {
        SingleSerie obj = new SingleSerie();
        
        Connection c = null;
        Statement stmt = null;
        
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:SeriesDB.sqlite"); 
            
            String query = "SELECT * FROM serie WHERE title=\"" + title + "\"";
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            obj.setSeason(rs.getInt("season"));
            obj.setEpisode(rs.getInt("episode"));
            obj.setTitle(rs.getString("title"));
            obj.setStatus(rs.getInt("status")); 
            
            rs.close();
            stmt.close();
            c.close();
        } catch(ClassNotFoundException | SQLException e) {
            System.err.println(e);
        }
        return obj;
    }
    
    public void updateByID(SingleSerie obj) {
        if(obj.getStatus() < 1 || obj.getStatus() > 3) throw new IllegalArgumentException("Status out of bounds (1,3)");
        if(obj.getEpisode() < 0 || obj.getEpisode() > MAX_EPISODES ) throw new IllegalArgumentException("Episodes out of bounds (0,"+ MAX_EPISODES + ")");
        if(obj.getSeason() < 0 || obj.getSeason() > MAX_SEASONS) throw new IllegalArgumentException("Seasons out of bounds (0,"+ MAX_SEASONS + ")");
        
        Connection c = null;
        Statement stmt = null;
        
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:SeriesDB.sqlite"); 
            
            stmt = c.createStatement();
            String query = "UPDATE serie SET season='" + obj.getSeason() + "', episode ='" + obj.getEpisode() + "', title='" + obj.getTitle() + "',status='" + obj.getStatus() + "' WHERE id=" + obj.getId() + ";";
            stmt.executeUpdate(query);
            
            stmt.close();
            c.close();
        } catch(ClassNotFoundException | SQLException e) {
            System.err.println(e);
        }
        refreshList();
    }
    
    public void updateByTitle(SingleSerie obj, String old) {
        if(obj.getStatus() < 1 || obj.getStatus() > 3) throw new IllegalArgumentException("Status out of bounds (1,3)");
        if(obj.getEpisode() < 0 || obj.getEpisode() > MAX_EPISODES ) throw new IllegalArgumentException("Episodes out of bounds (0,"+ MAX_EPISODES + ")");
        if(obj.getSeason() < 0 || obj.getSeason() > MAX_SEASONS) throw new IllegalArgumentException("Seasons out of bounds (0,"+ MAX_SEASONS + ")");
       
        Connection c = null;
        Statement stmt = null;
        
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:SeriesDB.sqlite"); 
            
            stmt = c.createStatement();
            String query = "UPDATE serie SET season='" + obj.getSeason() + "', episode ='" + obj.getEpisode() + "', title='" + obj.getTitle() + "',status='" + obj.getStatus() + "' WHERE title=\"" + old + "\";";
            stmt.executeUpdate(query);
            
            stmt.close();
            c.close();
        } catch(ClassNotFoundException | SQLException e) {
            System.err.println(e);
        }
        refreshList();
    }
    
    public boolean isInList(String title) { //search if title exists in db
        SingleSerie tmp;
        for(int i=0; i<al.size(); i++) {
             tmp = al.get(i);
             if(tmp.getTitle().equalsIgnoreCase(title)) return true;
        }
        return false;
    }
    
    public boolean isInList(int id) { //search if id exists in db
        SingleSerie tmp;
        for(int i=0; i<al.size(); i++) {
            tmp = al.get(i);
            if(tmp.getId() == id) return true;
        }
        return false;
    }
    
     public boolean isInList(String title, int id) { //search if title exists in database (without id from param)
        SingleSerie tmp;
        for(int i=0; i<al.size(); i++) {
             tmp = al.get(i);
             if(tmp.getTitle().equalsIgnoreCase(title) && (tmp.getId() != (id))) return true;
        }
        return false;
    }
     
}