import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import seriestracker.SqliteDB;
import seriestracker.SingleSerie;

/**
 *
 * @author Endrju
 */

public class SqliteDBJUnitTest {
    private SqliteDB db;
    private SingleSerie obj;
    private final String title = "str";
    
    public SqliteDBJUnitTest() {
        db = new SqliteDB();
    }
    
//    @Before
//    public void setUpGetByTitle() {
//        db.remove(title);
//        db.add(2, 3, title, 2);
//    }
//    
//    
//    @Test
//    public void getByTitleTest() {
//        obj = db.getByTitle(title);
//        assertEquals(obj.getStatus(),2);
//        assertEquals(obj.getEpisode(),3);
//        assertEquals(obj.getTitle(), title);
//        assertEquals(obj.getStatus(),2);
//    }
//    
//    
//    @After
//    public void tearDown() {
//        db.remove(title);
//    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void addSeasonBoundsTest() {
        db.add(0, 0, title, 1);
        db.add(-1, 0, title, 1);
        db.add('A', 0, title, 1);
        db.add(db.MAX_SEASONS, 0, title, 1);
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void addEpisodeBoundsTest() {
        db.add(0, 0, title, 1);
        db.add(0, -1, title, 1);
        db.add(0, 'A', title, 1);
        db.add(0, db.MAX_EPISODES, title, 1);
    }

    @Test
    public void addTitleTest() {
        db.add(0, 0, "str", 1);
        db.add(0, 0, null, 1);
 
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void addStatusTest() {
        db.add(0, 0, title, 1);
        db.add(0, 0, title, -1);
        db.add(0, 0, title, 4);
    }
}
