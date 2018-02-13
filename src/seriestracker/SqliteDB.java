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
            Class.forName("org.sqlite.JDBC"); //Jar file that I added to our library
            c = DriverManager.getConnection("jdbc:sqlite:SeriesDB.sqlite"); //controller
            System.out.println("Connected to database");
            loadToList();
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
    
    public void getAllSerie() {
        try { 
            this.stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM serie");
            
            while(rs.next()) {
                int id = rs.getInt("id");
                int season = rs.getInt("season");
                int episode = rs.getInt("episode");
                String title = rs.getString("title");
                int statusid = rs.getInt("status_id");              
                System.out.println(id + "| S" + season + "E" + episode + "| Title: " + title + ", Status: " + statusid);
            }
            
            rs.close();
        } catch(Exception e) {
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
                statusid = rs.getInt("status_id");
                
                al.add(new SingleSerie(id, season, episode, title, statusid));
            }
            rs.close();     
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    
    public String[] loadView(int type) {
        SingleSerie tmp;
        String[] value = new String[al.size()];
        for(int i=0; i<al.size(); i++) {
             tmp = al.get(i);
             if(tmp.getStatusid() == type) value[i] = tmp.toString();
        }
//        System.out.println("Content of al to string: " + value[0]);
        return value;
    }
}
