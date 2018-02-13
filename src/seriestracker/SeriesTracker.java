package seriestracker;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class SeriesTracker  extends javax.swing.JFrame implements ActionListener {

    private SqliteDB db = new SqliteDB();
    private JList lView; 
    private DefaultListModel modelView = new DefaultListModel();
    private JScrollPane spView;
    private ButtonGroup bgType;
    private JRadioButton rbWatching, rbCompleted, rbPlanToWatch;
    private JLabel laType;
    JButton bAdd;
    private String[] listHolder;
    SpinnerModel smStatus = new SpinnerNumberModel(1, 1, 3, 1); //default value,lower bound,upper bound,increment by
    SpinnerModel smSeason = new SpinnerNumberModel(0, 0, 100, 1);
    SpinnerModel smEpisode = new SpinnerNumberModel(0, 0, 300, 1);
    JSpinner fSeason = new JSpinner(smSeason);
    JSpinner fEpisode = new JSpinner(smEpisode);
    JTextField fTitle = new JTextField();
    JSpinner fStatus = new JSpinner(smStatus);
    Object[] addMsg = {
        "Season:", fSeason,
        "Episode:", fEpisode,
        "Title:", fTitle,
        "Status:", fStatus,
    };
    
    public SeriesTracker() {
        setSize(300,400);
        setTitle("Series Tracker");
        setLayout(null);
        changeModel(1);
        lView = new JList(modelView);
        lView.setFont(new Font("Arial",Font.PLAIN,10));
        add(lView);
        
        spView = new JScrollPane(lView);
        spView.setBounds(5, 85, 275, 270);
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
        
        laType = new JLabel("List");
        laType.setBounds(5,65,120,20);
        add(laType);
        
        bAdd = new JButton("Add");
        bAdd.setBounds(180,5,80,20);
        add(bAdd);
        bAdd.addActionListener(this);
    }
    
    public void changeModel(int x) {
        modelView.clear();
        listHolder = db.loadView(x);
        int tmp = listHolder.length;
        
        for(int i=0; i<tmp; i++) {
            modelView.addElement(listHolder[i]);
        }
    }
    
    public static void main(String[] args) {
        SeriesTracker gui = new SeriesTracker();
        gui.setVisible(true);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        db.closeConnection(); //dopisać do default close operation
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == rbWatching) {
            changeModel(1);
        } else if (src == rbCompleted) {
            changeModel(2);
        } else if (src == rbPlanToWatch) {
            changeModel(3);
        } else if (src == bAdd) {
            //String name = JOptionPane.showInputDialog(this,"Title of the serie", null);
            int option = JOptionPane.showConfirmDialog(this, addMsg, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION)
            {
                int value1 = (int) fSeason.getValue();
                int value2 = (int) fEpisode.getValue();
                String value3 = fTitle.getText();
                int value4 = (int) fStatus.getValue();
            }
        }
    }
    
}
