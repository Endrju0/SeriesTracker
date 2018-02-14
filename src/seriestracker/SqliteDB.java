package seriestracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SqliteDB {
    Connection c = null;
    Statement stmt = null;
    ArrayList<SingleSerie> al = new ArrayList<SingleSerie>();
    
    SqliteDB() { //trying connect to database
        try {
            Class.forName("org.sqlite.JDBC"); //Jar file that I added to our 
            c = DriverManager.getConnection("jdbc:sqlite:SeriesDB.sqlite"); //controller
            System.out.println("Connected to database");
            this.stmt = c.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS \"main\".\"serie\" (\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"season\" INTEGER, \"episode\" INTEGER, \"title\" VARCHAR, \"status\" INTEGER NOT NULL)");
            refreshList();
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    
    public void closeConnection() {
        try {
            c.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    public void loadToList() {
        try {
            this.stmt = c.createStatement();
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
        } catch(Exception e) {
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
             if(tmp.getStatusid() == type) value[i] = tmp.toString();
        }
        return value;
    }
    
    public void add(int season, int episode, String title, int status) {
        try { 
            this.stmt = c.createStatement();
            String query = "INSERT INTO serie(season, episode, title, status) VALUES (" + season + "," + episode + "," + "\"" + title + "\"," + status + ")"; 
            stmt.executeUpdate(query);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    public void remove(String title) {
        SingleSerie obj;
        try {
            this.stmt = c.createStatement();
            for(int i=0; i<al.size(); i++) {
                obj = al.get(i);
                if(obj.getTitle().equalsIgnoreCase(title)) {
                    String query = "DELETE FROM serie WHERE id='" + obj.getId() + "'";
                    stmt.executeUpdate(query);
                    System.out.println("Success");
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        refreshList();
    }
}
