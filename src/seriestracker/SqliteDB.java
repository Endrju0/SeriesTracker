package seriestracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqliteDB {
    Connection c = null;
    Statement stmt = null;
    
    SqliteDB() { //trying connect to database
        try {
            Class.forName("org.sqlite.JDBC"); //Jar file that I added to our library
            c = DriverManager.getConnection("jdbc:sqlite:SeriesDB.sqlite"); //controller
            System.out.println("Connected to database");
        } catch(Exception e) {
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
}
