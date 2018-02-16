package seriestracker;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
    private JButton bAdd, bEdit, bRemove;
    private String[] listHolder;
    private SpinnerModel smStatus = new SpinnerNumberModel(1, 1, 3, 1); //default value,lower bound,upper bound,increment by
    private SpinnerModel smSeason = new SpinnerNumberModel(0, 0, 100, 1);
    private SpinnerModel smEpisode = new SpinnerNumberModel(0, 0, 300, 1);
    private JSpinner fSeason = new JSpinner(smSeason);
    private JSpinner fEpisode = new JSpinner(smEpisode);
    private JTextField fTitle = new JTextField();
    private JTextField fOption = new JTextField();
    private JSpinner fStatus = new JSpinner(smStatus);
    private Object[] addMsg = {
        "Season:", fSeason,
        "Episode:", fEpisode,
        "Title:", fTitle,
        "Status:", fStatus,
    };
    private Object[] removeMsg = {
        "Title of serie you want to remove:", fTitle,
    };
    private String[] editOptions = { "By ID", "By title" };
    private JComboBox cbOptions = new JComboBox(editOptions);
    private String[] statusOptions = { "Watching", "Completed", "Plan to watch" };
    private JComboBox cbStatus = new JComboBox(statusOptions);
    private JLabel lTitle = new JLabel("Title");
    private JLabel lSeason = new JLabel("Season");
    private JLabel lEpisode = new JLabel("Episode");
    private JLabel lStatus = new JLabel("Status");
    
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
        
        bEdit = new JButton("Edit");
        bEdit.setBounds(180,30,80,20);
        add(bEdit);
        bEdit.addActionListener(this);
        
        bRemove = new JButton("Remove");
        bRemove.setBounds(180,55,80,20);
        add(bRemove);
        bRemove.addActionListener(this);
        
        fStatus.setToolTipText("1 - Watching, 2 - Completed, 3 - Plan to watch");
    }
    
    public void changeModel(int x) {
        modelView.clear();
        listHolder = db.loadView(x);
        int tmp = listHolder.length;
        
        for(int i=0; i<tmp; i++) {
            if(listHolder[i] != null) modelView.addElement(listHolder[i]);
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
            int option = JOptionPane.showConfirmDialog(this, addMsg, "Adding new serie", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION)
            {
                int value1 = (int) fSeason.getValue();
                int value2 = (int) fEpisode.getValue();
                String value3 = fTitle.getText();
                int value4 = (int) fStatus.getValue();
                db.add(value1, value2, value3, value4);
                
                db.refreshList();
                if(rbWatching.isSelected()) changeModel(1);
                if(rbCompleted.isSelected()) changeModel(2);
                if(rbPlanToWatch.isSelected()) changeModel(3);
            }
        } else if ( src == bRemove ) {
            int result = JOptionPane.showConfirmDialog(this, removeMsg, "Removing serie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String val = fTitle.getText();
                db.remove(val);

                if(rbWatching.isSelected()) changeModel(1);
                if(rbCompleted.isSelected()) changeModel(2);
                if(rbPlanToWatch.isSelected()) changeModel(3);
            }
        } else if (src == bEdit) {
            SingleSerie tmp;
            Object[] obj = {cbOptions, fOption};
            int result = JOptionPane.showConfirmDialog(null, obj, "Please choose method to select serie", JOptionPane.OK_CANCEL_OPTION);cbOptions.isEnabled();
            String cbValue = (String) cbOptions.getSelectedItem();
            if (result == JOptionPane.OK_OPTION) {
                String optionValue = fOption.getText();
                if(cbValue.equals("By ID")) {
                    int id = Integer.parseInt(optionValue);
                    tmp = db.getByID(id);
                    
                    SpinnerModel smEditSeason = new SpinnerNumberModel(tmp.getSeason(), 0, 100, 1);
                    SpinnerModel smEditEpisode = new SpinnerNumberModel(tmp.getEpisode(), 0, 300, 1);
                    fTitle = new JTextField(tmp.getTitle());
                    fSeason = new JSpinner(smEditSeason);
                    fEpisode = new JSpinner(smEditEpisode);
                    int tmpStatus = tmp.getStatusid();
                    int tmpStatus2 = 1;
                    if (tmpStatus == 1) {
                        cbStatus.setSelectedItem(statusOptions[0]);
                        tmpStatus2 = 1;
                    } else if (tmpStatus == 2) {
                        cbStatus.setSelectedItem(statusOptions[1]);
                        tmpStatus2 = 2;
                    } else {
                        cbStatus.setSelectedItem(statusOptions[2]);
                        tmpStatus2 = 3;
                    }

                    Object[] obj2 = {lTitle, fTitle, lSeason, fSeason, lEpisode, fEpisode, lStatus, cbStatus};
                    int res = JOptionPane.showConfirmDialog(null, obj2, "Edit", JOptionPane.OK_CANCEL_OPTION);
                    
                    if(res == JOptionPane.OK_OPTION) {
                        tmp.setId(id);
                        tmp.setSeason((int) fSeason.getValue());
                        tmp.setEpisode((int) fEpisode.getValue());
                        tmp.setTitle(fTitle.getText());
                        tmp.setStatusid(tmpStatus2);
                        db.updateByID(tmp);
                    }
                    
                } else {
                    tmp = db.getByTitle(optionValue);
                    System.out.println("tytul " + tmp.getTitle() + " season " + tmp.getSeason() + " episode " + tmp.getEpisode() + " status " + tmp.getStatusid());
                }
                if(rbWatching.isSelected()) changeModel(1);
                if(rbCompleted.isSelected()) changeModel(2);
                if(rbPlanToWatch.isSelected()) changeModel(3);
            }         
        }
        
    }
}
