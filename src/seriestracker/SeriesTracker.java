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

    private final SqliteDB db = new SqliteDB();
    private String[] listHolder;
    private final String[] statusOptions = { "Watching", "Completed", "Plan to watch" };
    private final String[] editOptions = { "By ID", "By title" };
    
    private final DefaultListModel modelView = new DefaultListModel();
    private JList lView;
    private JScrollPane spView;
    
    private ButtonGroup bgType;
    private JRadioButton rbWatching, rbCompleted, rbPlanToWatch;
    
    private JLabel laType;
    private JButton bAdd, bEdit, bRemove;
    
    private final SpinnerModel smSeason = new SpinnerNumberModel(0, 0, 100, 1); //default value,lower bound,upper bound,increment by
    private final SpinnerModel smEpisode = new SpinnerNumberModel(0, 0, 300, 1);
    private JSpinner fSeason = new JSpinner(smSeason);
    private JSpinner fEpisode = new JSpinner(smEpisode);
    
    private JTextField fTitle = new JTextField();
    private final JTextField fOption = new JTextField();
    
    private final JComboBox cbStatus = new JComboBox(statusOptions);
    private final JComboBox cbOptions = new JComboBox(editOptions);
    
    private final JLabel lTitle = new JLabel("Title");
    private final JLabel lSeason = new JLabel("Season");
    private final JLabel lEpisode = new JLabel("Episode");
    private final JLabel lStatus = new JLabel("Status");
    
    private final Object[] addMsg = {
        "Title:", fTitle,
        "Season:", fSeason,
        "Episode:", fEpisode,
        "Status:", cbStatus,
    };
    private final Object[] removeMsg = {
        "Title of serie you want to remove:", fTitle,
    };
    Object[] editFields = {cbOptions, fOption};
    
    public SeriesTracker() {
        initGUI();
    }
    private void initGUI() {
        setSize(290,390);
        setResizable(false);
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
        bAdd.setBounds(200,5,80,20);
        add(bAdd);
        bAdd.addActionListener(this);
        
        bEdit = new JButton("Edit");
        bEdit.setBounds(200,30,80,20);
        add(bEdit);
        bEdit.addActionListener(this);
        
        bRemove = new JButton("Remove");
        bRemove.setBounds(200,55,80,20);
        add(bRemove);
        bRemove.addActionListener(this);
        
        fTitle.setToolTipText("Title of serie shouldn't be empty");
    }
    public void changeModel(int x) {
        modelView.clear();
        listHolder = db.loadView(x);
        int tmp = listHolder.length;
        
        for(int i=0; i<tmp; i++) {
            if(listHolder[i] != null) modelView.addElement(listHolder[i]);
        }
    }
    
    public int parseInt(String str) {
        int val;
        try {
            val = Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            val = -1;
        }
        return val;
    }
    
    public static void main(String[] args) {
        SeriesTracker gui = new SeriesTracker();
        gui.setVisible(true);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        db.closeConnection(); //shutdown hook
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
            if(fTitle.getText().isEmpty() || fTitle.getText().matches("^\\s+$")) {
                JOptionPane.showMessageDialog(this,
                "Title shouldn't be empty!",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            } else if (option == JOptionPane.OK_OPTION) {
                int value1 = (int) fSeason.getValue();
                int value2 = (int) fEpisode.getValue();
                String value3 = fTitle.getText();
                int value4 = cbStatus.getSelectedIndex()+1;
                
                if(db.isInList(value3)) {
                    JOptionPane.showMessageDialog(this,
                    "Serie with that title already exists!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
                } else {
                    db.add(value1, value2, value3, value4);
                    fTitle.setText("");
                    fSeason.setValue(0);
                    fEpisode.setValue(0);
                    cbStatus.setSelectedItem(statusOptions[0]);
                }
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
            int result = JOptionPane.showConfirmDialog(null, editFields, "Please choose method to select serie", JOptionPane.OK_CANCEL_OPTION);cbOptions.isEnabled();
            String cbValue = (String) cbOptions.getSelectedItem();
            try {
            if (result == JOptionPane.OK_OPTION) {
                String optionValue = fOption.getText();
                if(optionValue.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "This field shouldn't be empty!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else if(!optionValue.matches("^\\d+$") && cbValue.equals(editOptions[0])) {
                    JOptionPane.showMessageDialog(this,
                            "ID should be numeric value!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
               } else if(!db.isInList(parseInt(optionValue)) && cbValue.equals(editOptions[0])) {
                    JOptionPane.showMessageDialog(this,
                            "Serie with that id doesn't exists!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else if(!db.isInList(optionValue) && cbValue.equals(editOptions[1])) {
                    JOptionPane.showMessageDialog(this,
                            "Serie with that title doesn't exists!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else if (cbValue.equals(editOptions[0])) { //ID
                    int id = Integer.parseInt(optionValue);
                    tmp = db.getByID(id);
                    SpinnerModel smEditSeason = new SpinnerNumberModel(tmp.getSeason(), 0, 100, 1);
                    SpinnerModel smEditEpisode = new SpinnerNumberModel(tmp.getEpisode(), 0, 300, 1);
                    fTitle = new JTextField(tmp.getTitle());
                    fSeason = new JSpinner(smEditSeason);
                    fEpisode = new JSpinner(smEditEpisode);
                    int tmpStatus = tmp.getStatus();
                    switch (tmpStatus) {
                        case 1:
                            cbStatus.setSelectedItem(statusOptions[0]);
                            break;
                        case 2:
                            cbStatus.setSelectedItem(statusOptions[1]);
                            break; 
                        default:
                            cbStatus.setSelectedItem(statusOptions[2]);
                            break;
                    }
                    
                    Object[] fields = {lTitle, fTitle, lSeason, fSeason, lEpisode, fEpisode, lStatus, cbStatus};
                    int res = JOptionPane.showConfirmDialog(null, fields, "Edit", JOptionPane.OK_CANCEL_OPTION);
                    if(fTitle.getText().isEmpty() || fTitle.getText().matches("^\\s+$")) {
                    JOptionPane.showMessageDialog(this,
                        "Title shouldn't be empty!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                    } else if(res == JOptionPane.OK_OPTION) {
                        tmp.setId(id);
                        tmp.setSeason((int) fSeason.getValue());
                        tmp.setEpisode((int) fEpisode.getValue());
                        tmp.setTitle(fTitle.getText());
                        tmp.setStatus(cbStatus.getSelectedIndex()+1);
                        
                        if(db.isInList(tmp.getTitle(), tmp.getId())) {
                            JOptionPane.showMessageDialog(this,
                            "Serie with that title already exists!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                        } else if(tmp.getTitle().isEmpty()) {
                            JOptionPane.showMessageDialog(this,
                            "Serie title shouldn't be empty!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                        } else {
                            db.updateByID(tmp);
                        }
                    }
                } else { //by Title
                    tmp = db.getByTitle(optionValue);
                    String oldName = tmp.getTitle();
                    
                    SpinnerModel smEditSeason = new SpinnerNumberModel(tmp.getSeason(), 0, 100, 1);
                    SpinnerModel smEditEpisode = new SpinnerNumberModel(tmp.getEpisode(), 0, 300, 1);
                    fTitle = new JTextField(optionValue);
                    fSeason = new JSpinner(smEditSeason);
                    fEpisode = new JSpinner(smEditEpisode);
                    int tmpStatus = tmp.getStatus();
                    switch (tmpStatus) {
                        case 1:
                            cbStatus.setSelectedItem(statusOptions[0]);
                            break;
                        case 2:
                            cbStatus.setSelectedItem(statusOptions[1]);
                            break;
                        default:
                            cbStatus.setSelectedItem(statusOptions[2]);
                            break;
                    }
                    Object[] fields = {lTitle, fTitle, lSeason, fSeason, lEpisode, fEpisode, lStatus, cbStatus};
                    int res = JOptionPane.showConfirmDialog(null, fields, "Edit", JOptionPane.OK_CANCEL_OPTION);
                    if(fTitle.getText().isEmpty() || fTitle.getText().matches("^\\s+$")) {
                    JOptionPane.showMessageDialog(this,
                        "Title shouldn't be empty!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                    } else if(res == JOptionPane.OK_OPTION) {
                        tmp.setSeason((int) fSeason.getValue());
                        tmp.setEpisode((int) fEpisode.getValue());
                        tmp.setTitle(fTitle.getText());
                        tmp.setStatus(cbStatus.getSelectedIndex()+1);
                        
                        if(db.isInList(tmp.getTitle()) && !oldName.equalsIgnoreCase(tmp.getTitle())) {
                            JOptionPane.showMessageDialog(this,
                            "Serie with that title already exists!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                        } else if(tmp.getTitle().isEmpty()) {
                            JOptionPane.showMessageDialog(this,
                            "Serie title shouldn't be empty!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                        } else {
                            db.updateByTitle(tmp, oldName);
                        }
                    }
                } 
                if(rbWatching.isSelected()) changeModel(1);
                if(rbCompleted.isSelected()) changeModel(2);
                if(rbPlanToWatch.isSelected()) changeModel(3);
            }
            } catch (NumberFormatException ex) {
                System.err.println("Err: " + ex.getMessage());
            }
        } 
    }
}