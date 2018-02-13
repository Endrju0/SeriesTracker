package seriestracker;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

public class SeriesTracker  extends javax.swing.JFrame implements ActionListener {

    private SqliteDB db = new SqliteDB();
    private JList lView; 
    private JScrollPane spView;
    private ButtonGroup bgType;
    private JRadioButton rbWatching, rbCompleted, rbPlanToWatch;
    private JLabel laType;
    private String[] listHolder = db.loadView(1);
    
    public SeriesTracker() {
        setSize(300,400);
        setTitle("Serie Tracker");
        setLayout(null);
//        String test[] = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3", "Item 3" };
        lView = new JList(listHolder);
        add(lView);
        
        spView = new JScrollPane(lView);
        spView.setBounds(5, 80, 275, 275);
        add(spView);
        
        bgType = new ButtonGroup();
        rbWatching = new JRadioButton("Watching", true);
        rbWatching.setBounds(5,5,120,20);
        bgType.add(rbWatching);
        add(rbWatching);
        rbWatching.addActionListener(this);
        
        rbCompleted = new JRadioButton("Completed", false);
        rbCompleted.setBounds(5,25,120,20);
        bgType.add(rbCompleted);
        add(rbCompleted);
        rbCompleted.addActionListener(this);
        
        rbPlanToWatch = new JRadioButton("Plan to watch", false);
        rbPlanToWatch.setBounds(5,45,120,20);
        bgType.add(rbPlanToWatch);
        add(rbPlanToWatch);
        rbPlanToWatch.addActionListener(this);
        
        laType = new JLabel("Typ listy");
        laType.setBounds(5,60,120,20);
        add(laType);
    }
    
    public static void main(String[] args) {
        SeriesTracker gui = new SeriesTracker();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
//        db.closeConnection(); //dopisać do default close operation
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == rbWatching) {
            laType.setFont(new Font("SansSerif", Font.PLAIN, 12));
        } else if (src == rbCompleted) {
            laType.setFont(new Font("SansSerif", Font.PLAIN, 14));
        } else if (src == rbPlanToWatch) {
            laType.setFont(new Font("SansSerif", Font.PLAIN, 17));
        }
    }
    
}
