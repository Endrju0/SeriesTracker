package seriestracker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class SeriesTracker  extends javax.swing.JFrame implements ActionListener {

    private SqliteDB db = new SqliteDB();
    private JList lView; 
    private JScrollPane spView;
    
    public SeriesTracker() {
        setSize(300,400);
        setTitle("Serie Tracker");
        setLayout(null);
        
        String test[] = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3" };
        lView = new JList(db.loadView());
        add(lView);
        
        spView = new JScrollPane(lView);
        spView.setBounds(5, 50, 275, 305);
        add(spView);
    }
    
    public static void main(String[] args) {
        SeriesTracker gui = new SeriesTracker();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
//        db.closeConnection(); //dopisać do default close operation
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
