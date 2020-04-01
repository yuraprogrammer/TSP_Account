/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.vis;

import com.alexprom.connection.settings.dbConnectionSettingsPanel;
import com.alexprom.tsp_account.daq.CounterData;
import com.alexprom.tsp_account.daq.JPlcAgent;
import com.alexprom.tsp_account.daq.TagManagementPanel;
import com.alexprom.tsp_account.daq.TankData;
import com.alexprom.tsp_account.daq.sensorSettingsPanel;
import com.alexprom.tsp_account.report_db.CountersInitData;
import com.alexprom.tsp_account.report_db.GlobalEntityManager;
import com.alexprom.tsp_account.report_db.GradView;
import com.alexprom.tsp_account.report_db.SystemTags;
import com.alexprom.tsp_account.report_db.TankDic;
import com.alexprom.tsp_account.report_db.TanksToAccount;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.BorderFactory;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import remoteagent.lib.PlcTag;
import remoteagent.lib.SimaticAddressParser;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//com.alexprom.tsp_account.vis//TankData//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "TankDataTopComponent",
        iconBase = "com/alexprom/tsp_account/vis/cd disk.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "com.alexprom.tsp_account.vis.TankDataTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_TankDataAction",
        preferredID = "TankDataTopComponent"
)
@Messages({
    "CTL_TankDataAction=TankData",
    "CTL_TankDataTopComponent=TankData Window",
    "HINT_TankDataTopComponent=This is a TankData window"
})
public final class TankDataTopComponent extends TopComponent implements Runnable{
    
    public TankData tankData[];
    public CounterData counterData[];
    public Thread formThread;
    private JPlcAgent plc;    
    public EntityManagerFactory emf = null;
    public EntityManager em = null;
    private PlcTag[] tag;
    private Double oldDensity_32=0.0, newDensity_32=0.0, oldTemperature_32=0.0, newTemperature_32=0.0;
    private List<TanksToAccount> tanks;
    private List<CountersInitData> counters;
    //private Date logDate = new Date();
    //private DateFormat df = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
    
    public TankDataTopComponent() {
        
        initComponents();
        //setName(Bundle.CTL_TankDataTopComponent());
        //setToolTipText(Bundle.HINT_TankDataTopComponent());
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        
        DocumentListener density32Listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                density32_Change(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                density32_Change(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                density32_Change(true);
            }
        };
        lbDensity32.getDocument().addDocumentListener(density32Listener);
        
        DocumentListener temperature32Listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                temperature32_Change(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                temperature32_Change(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                temperature32_Change(true);
            }
        };
        lbTemperature32.getDocument().addDocumentListener(temperature32Listener);
        
        
    }

    private void density32_Change(boolean edited){
        if (edited){
            if (!lbDensity32.getText().isEmpty()){
                oldDensity_32 = newDensity_32;                
                try{
                    newDensity_32 = Double.parseDouble(lbDensity32.getText().replace(",", "."));                    
                }catch (java.lang.NumberFormatException e){
                    showNumberErroMessage();
                }
            
            }else{
                newDensity_32 = 0.0;
            }
        }
    }
    
    private void temperature32_Change(boolean edited){
        if (edited){
            if (!lbTemperature32.getText().isEmpty()){
                oldTemperature_32 = newTemperature_32;                
                try{
                    newTemperature_32 = Double.parseDouble(lbTemperature32.getText().replace(",", "."));                    
                }catch (java.lang.NumberFormatException e){
                    showNumberErroMessage();
                }
            
            }else{
                newTemperature_32 = 0.0;
            }
        }
    }
    
    public void showNumberErroMessage(){
        NotifyDescriptor nd = new NotifyDescriptor.Message("Неверный формат введенных данных!!!", NotifyDescriptor.ERROR_MESSAGE);
        Object res = DialogDisplayer.getDefault().notify(nd);
    }
     
    public void updatePersistence(){                        
        GlobalEntityManager gem = new GlobalEntityManager();
        emf = gem.getEmf();
        em = gem.getEm();        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel8 = new javax.swing.JPanel();
        label35 = new java.awt.Label();
        label36 = new java.awt.Label();
        label37 = new java.awt.Label();
        label38 = new java.awt.Label();
        lbLevel17 = new javax.swing.JTextField();
        lbVolume17 = new javax.swing.JTextField();
        lbTemperature17 = new javax.swing.JTextField();
        lbDensity17 = new javax.swing.JTextField();
        pbLevel17 = new javax.swing.JProgressBar();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        label27 = new java.awt.Label();
        label28 = new java.awt.Label();
        label29 = new java.awt.Label();
        label30 = new java.awt.Label();
        lbLevel15 = new javax.swing.JTextField();
        lbVolume15 = new javax.swing.JTextField();
        lbTemperature15 = new javax.swing.JTextField();
        lbDensity15 = new javax.swing.JTextField();
        pbLevel15 = new javax.swing.JProgressBar();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        label31 = new java.awt.Label();
        label32 = new java.awt.Label();
        label33 = new java.awt.Label();
        label34 = new java.awt.Label();
        lbLevel16 = new javax.swing.JTextField();
        lbVolume16 = new javax.swing.JTextField();
        lbTemperature16 = new javax.swing.JTextField();
        lbDensity16 = new javax.swing.JTextField();
        pbLevel16 = new javax.swing.JProgressBar();
        jLabel3 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        label39 = new java.awt.Label();
        label40 = new java.awt.Label();
        label41 = new java.awt.Label();
        label42 = new java.awt.Label();
        lbLevel26 = new javax.swing.JTextField();
        lbVolume26 = new javax.swing.JTextField();
        lbTemperature26 = new javax.swing.JTextField();
        lbDensity26 = new javax.swing.JTextField();
        pbLevel26 = new javax.swing.JProgressBar();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        label7 = new java.awt.Label();
        label8 = new java.awt.Label();
        label9 = new java.awt.Label();
        label10 = new java.awt.Label();
        lbLevel11 = new javax.swing.JTextField();
        lbVolume11 = new javax.swing.JTextField();
        lbTemperature11 = new javax.swing.JTextField();
        lbDensity11 = new javax.swing.JTextField();
        pbLevel11 = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        label43 = new java.awt.Label();
        label44 = new java.awt.Label();
        label45 = new java.awt.Label();
        label46 = new java.awt.Label();
        lbMass100 = new javax.swing.JTextField();
        lbVolume100 = new javax.swing.JTextField();
        lbTemperature100 = new javax.swing.JTextField();
        lbDensity100 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        label47 = new java.awt.Label();
        label48 = new java.awt.Label();
        label49 = new java.awt.Label();
        label50 = new java.awt.Label();
        lbMass101 = new javax.swing.JTextField();
        lbVolume101 = new javax.swing.JTextField();
        lbTemperature101 = new javax.swing.JTextField();
        lbDensity101 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        label51 = new java.awt.Label();
        label52 = new java.awt.Label();
        label53 = new java.awt.Label();
        label54 = new java.awt.Label();
        lbLevel32 = new javax.swing.JTextField();
        lbVolume32 = new javax.swing.JTextField();
        pbLevel32 = new javax.swing.JProgressBar();
        jLabel14 = new javax.swing.JLabel();
        lbDensity32 = new javax.swing.JFormattedTextField();
        lbTemperature32 = new javax.swing.JFormattedTextField();
        jPanel13 = new javax.swing.JPanel();
        label55 = new java.awt.Label();
        label56 = new java.awt.Label();
        label57 = new java.awt.Label();
        label58 = new java.awt.Label();
        lbLevel23 = new javax.swing.JTextField();
        lbVolume23 = new javax.swing.JTextField();
        lbTemperature23 = new javax.swing.JTextField();
        lbDensity23 = new javax.swing.JTextField();
        pbLevel23 = new javax.swing.JProgressBar();
        jLabel15 = new javax.swing.JLabel();

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jPanel8.border.title"))); // NOI18N
        jPanel8.setLayout(null);

        label35.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label35.text")); // NOI18N
        jPanel8.add(label35);
        label35.setBounds(360, 60, 22, 20);

        label36.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label36.text")); // NOI18N
        jPanel8.add(label36);
        label36.setBounds(360, 90, 11, 20);

        label37.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label37.text")); // NOI18N
        jPanel8.add(label37);
        label37.setBounds(360, 120, 18, 20);

        label38.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label38.text")); // NOI18N
        jPanel8.add(label38);
        label38.setBounds(360, 150, 24, 20);

        lbLevel17.setEditable(false);
        lbLevel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbLevel17.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbLevel17.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbLevel17.text")); // NOI18N
        jPanel8.add(lbLevel17);
        lbLevel17.setBounds(260, 60, 90, 21);

        lbVolume17.setEditable(false);
        lbVolume17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbVolume17.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbVolume17.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbVolume17.text")); // NOI18N
        jPanel8.add(lbVolume17);
        lbVolume17.setBounds(260, 90, 90, 21);

        lbTemperature17.setEditable(false);
        lbTemperature17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbTemperature17.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbTemperature17.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbTemperature17.text")); // NOI18N
        jPanel8.add(lbTemperature17);
        lbTemperature17.setBounds(260, 120, 90, 21);

        lbDensity17.setEditable(false);
        lbDensity17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDensity17.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbDensity17.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbDensity17.text")); // NOI18N
        jPanel8.add(lbDensity17);
        lbDensity17.setBounds(260, 150, 90, 21);

        pbLevel17.setOrientation(1);
        pbLevel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pbLevel17.setFocusable(false);
        pbLevel17.setString(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.pbLevel17.string")); // NOI18N
        jPanel8.add(pbLevel17);
        pbLevel17.setBounds(170, 50, 19, 100);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alexprom/tsp_account/vis/tank_h.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jLabel4.text")); // NOI18N
        jLabel4.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabel4.setOpaque(true);
        jPanel8.add(jLabel4);
        jLabel4.setBounds(16, 27, 236, 140);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jPanel6.border.title"))); // NOI18N
        jPanel6.setLayout(null);

        label27.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label27.text")); // NOI18N
        jPanel6.add(label27);
        label27.setBounds(360, 60, 22, 20);

        label28.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label28.text")); // NOI18N
        jPanel6.add(label28);
        label28.setBounds(360, 90, 11, 20);

        label29.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label29.text")); // NOI18N
        jPanel6.add(label29);
        label29.setBounds(360, 120, 18, 20);

        label30.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label30.text")); // NOI18N
        jPanel6.add(label30);
        label30.setBounds(360, 150, 24, 20);

        lbLevel15.setEditable(false);
        lbLevel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbLevel15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbLevel15.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbLevel15.text")); // NOI18N
        jPanel6.add(lbLevel15);
        lbLevel15.setBounds(260, 60, 90, 21);

        lbVolume15.setEditable(false);
        lbVolume15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbVolume15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbVolume15.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbVolume15.text")); // NOI18N
        jPanel6.add(lbVolume15);
        lbVolume15.setBounds(260, 90, 90, 21);

        lbTemperature15.setEditable(false);
        lbTemperature15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbTemperature15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbTemperature15.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbTemperature15.text")); // NOI18N
        jPanel6.add(lbTemperature15);
        lbTemperature15.setBounds(260, 120, 90, 21);

        lbDensity15.setEditable(false);
        lbDensity15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDensity15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbDensity15.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbDensity15.text")); // NOI18N
        jPanel6.add(lbDensity15);
        lbDensity15.setBounds(260, 150, 90, 21);

        pbLevel15.setOrientation(1);
        pbLevel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pbLevel15.setFocusable(false);
        pbLevel15.setString(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.pbLevel15.string")); // NOI18N
        jPanel6.add(pbLevel15);
        pbLevel15.setBounds(170, 50, 19, 100);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alexprom/tsp_account/vis/tank_h.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jLabel2.text")); // NOI18N
        jLabel2.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabel2.setOpaque(true);
        jPanel6.add(jLabel2);
        jLabel2.setBounds(16, 27, 236, 140);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jPanel7.border.title"))); // NOI18N
        jPanel7.setLayout(null);

        label31.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label31.text")); // NOI18N
        jPanel7.add(label31);
        label31.setBounds(360, 60, 22, 20);

        label32.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label32.text")); // NOI18N
        jPanel7.add(label32);
        label32.setBounds(360, 90, 11, 20);

        label33.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label33.text")); // NOI18N
        jPanel7.add(label33);
        label33.setBounds(360, 120, 18, 20);

        label34.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label34.text")); // NOI18N
        jPanel7.add(label34);
        label34.setBounds(360, 150, 24, 20);

        lbLevel16.setEditable(false);
        lbLevel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbLevel16.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbLevel16.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbLevel16.text")); // NOI18N
        jPanel7.add(lbLevel16);
        lbLevel16.setBounds(260, 60, 90, 21);

        lbVolume16.setEditable(false);
        lbVolume16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbVolume16.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbVolume16.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbVolume16.text")); // NOI18N
        jPanel7.add(lbVolume16);
        lbVolume16.setBounds(260, 90, 90, 21);

        lbTemperature16.setEditable(false);
        lbTemperature16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbTemperature16.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbTemperature16.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbTemperature16.text")); // NOI18N
        jPanel7.add(lbTemperature16);
        lbTemperature16.setBounds(260, 120, 90, 21);

        lbDensity16.setEditable(false);
        lbDensity16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDensity16.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbDensity16.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbDensity16.text")); // NOI18N
        jPanel7.add(lbDensity16);
        lbDensity16.setBounds(260, 150, 90, 21);

        pbLevel16.setOrientation(1);
        pbLevel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pbLevel16.setFocusable(false);
        pbLevel16.setString(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.pbLevel16.string")); // NOI18N
        jPanel7.add(pbLevel16);
        pbLevel16.setBounds(170, 50, 19, 100);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alexprom/tsp_account/vis/tank_h.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jLabel3.text")); // NOI18N
        jLabel3.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabel3.setOpaque(true);
        jPanel7.add(jLabel3);
        jLabel3.setBounds(16, 27, 236, 140);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jPanel9.border.title"))); // NOI18N
        jPanel9.setLayout(null);

        label39.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label39.text")); // NOI18N
        jPanel9.add(label39);
        label39.setBounds(360, 60, 22, 20);

        label40.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label40.text")); // NOI18N
        jPanel9.add(label40);
        label40.setBounds(360, 90, 11, 20);

        label41.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label41.text")); // NOI18N
        jPanel9.add(label41);
        label41.setBounds(360, 120, 18, 20);

        label42.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label42.text")); // NOI18N
        jPanel9.add(label42);
        label42.setBounds(360, 150, 24, 20);

        lbLevel26.setEditable(false);
        lbLevel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbLevel26.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbLevel26.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.1.text")); // NOI18N
        lbLevel26.setName("1"); // NOI18N
        jPanel9.add(lbLevel26);
        lbLevel26.setBounds(260, 60, 90, 21);

        lbVolume26.setEditable(false);
        lbVolume26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbVolume26.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbVolume26.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.1.text")); // NOI18N
        lbVolume26.setName("1"); // NOI18N
        jPanel9.add(lbVolume26);
        lbVolume26.setBounds(260, 90, 90, 21);

        lbTemperature26.setEditable(false);
        lbTemperature26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbTemperature26.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbTemperature26.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.1.text")); // NOI18N
        lbTemperature26.setName("1"); // NOI18N
        jPanel9.add(lbTemperature26);
        lbTemperature26.setBounds(260, 120, 90, 21);

        lbDensity26.setEditable(false);
        lbDensity26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDensity26.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbDensity26.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.1.text")); // NOI18N
        lbDensity26.setName("1"); // NOI18N
        jPanel9.add(lbDensity26);
        lbDensity26.setBounds(260, 150, 90, 21);

        pbLevel26.setOrientation(1);
        pbLevel26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pbLevel26.setFocusable(false);
        pbLevel26.setString(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.pbLevel26.string")); // NOI18N
        jPanel9.add(pbLevel26);
        pbLevel26.setBounds(170, 50, 19, 100);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alexprom/tsp_account/vis/tank_h.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jLabel5.text")); // NOI18N
        jLabel5.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabel5.setOpaque(true);
        jPanel9.add(jLabel5);
        jLabel5.setBounds(16, 27, 236, 140);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jPanel1.border.title"))); // NOI18N
        jPanel1.setLayout(null);

        label7.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label7.text")); // NOI18N
        jPanel1.add(label7);
        label7.setBounds(360, 60, 22, 20);

        label8.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label8.text")); // NOI18N
        jPanel1.add(label8);
        label8.setBounds(360, 90, 11, 20);

        label9.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label9.text")); // NOI18N
        jPanel1.add(label9);
        label9.setBounds(360, 120, 18, 20);

        label10.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label10.text")); // NOI18N
        jPanel1.add(label10);
        label10.setBounds(360, 150, 24, 20);

        lbLevel11.setEditable(false);
        lbLevel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbLevel11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbLevel11.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbLevel11.text")); // NOI18N
        jPanel1.add(lbLevel11);
        lbLevel11.setBounds(260, 60, 90, 21);

        lbVolume11.setEditable(false);
        lbVolume11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbVolume11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbVolume11.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbVolume11.text")); // NOI18N
        jPanel1.add(lbVolume11);
        lbVolume11.setBounds(260, 90, 90, 21);

        lbTemperature11.setEditable(false);
        lbTemperature11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbTemperature11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbTemperature11.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbTemperature11.text")); // NOI18N
        jPanel1.add(lbTemperature11);
        lbTemperature11.setBounds(260, 120, 90, 21);

        lbDensity11.setEditable(false);
        lbDensity11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDensity11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbDensity11.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbDensity11.text")); // NOI18N
        jPanel1.add(lbDensity11);
        lbDensity11.setBounds(260, 150, 90, 21);

        pbLevel11.setOrientation(1);
        pbLevel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pbLevel11.setFocusable(false);
        pbLevel11.setString(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.pbLevel11.string")); // NOI18N
        jPanel1.add(pbLevel11);
        pbLevel11.setBounds(170, 50, 19, 100);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alexprom/tsp_account/vis/tank_h.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jLabel1.text")); // NOI18N
        jLabel1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1);
        jLabel1.setBounds(16, 27, 236, 140);

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jPanel10.border.title"))); // NOI18N
        jPanel10.setLayout(null);

        label43.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label43.text")); // NOI18N
        jPanel10.add(label43);
        label43.setBounds(260, 90, 14, 20);

        label44.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label44.text")); // NOI18N
        jPanel10.add(label44);
        label44.setBounds(260, 60, 11, 20);

        label45.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label45.text")); // NOI18N
        jPanel10.add(label45);
        label45.setBounds(260, 120, 18, 20);

        label46.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label46.text")); // NOI18N
        jPanel10.add(label46);
        label46.setBounds(260, 150, 24, 20);

        lbMass100.setEditable(false);
        lbMass100.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbMass100.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbMass100.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbMass100.text")); // NOI18N
        jPanel10.add(lbMass100);
        lbMass100.setBounds(120, 90, 130, 21);

        lbVolume100.setEditable(false);
        lbVolume100.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbVolume100.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbVolume100.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbVolume100.text")); // NOI18N
        jPanel10.add(lbVolume100);
        lbVolume100.setBounds(120, 60, 130, 21);

        lbTemperature100.setEditable(false);
        lbTemperature100.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbTemperature100.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbTemperature100.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbTemperature100.text")); // NOI18N
        jPanel10.add(lbTemperature100);
        lbTemperature100.setBounds(120, 120, 130, 21);

        lbDensity100.setEditable(false);
        lbDensity100.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDensity100.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbDensity100.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbDensity100.text")); // NOI18N
        jPanel10.add(lbDensity100);
        lbDensity100.setBounds(120, 150, 130, 21);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jLabel6.text")); // NOI18N
        jPanel10.add(jLabel6);
        jLabel6.setBounds(20, 70, 37, 14);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jLabel7.text")); // NOI18N
        jPanel10.add(jLabel7);
        jLabel7.setBounds(20, 90, 34, 14);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jLabel8.text")); // NOI18N
        jPanel10.add(jLabel8);
        jLabel8.setBounds(20, 120, 80, 14);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jLabel9.text")); // NOI18N
        jPanel10.add(jLabel9);
        jLabel9.setBounds(20, 150, 70, 14);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jPanel11.border.title"))); // NOI18N
        jPanel11.setLayout(null);

        label47.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label47.text")); // NOI18N
        jPanel11.add(label47);
        label47.setBounds(260, 90, 14, 20);

        label48.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label48.text")); // NOI18N
        jPanel11.add(label48);
        label48.setBounds(260, 60, 11, 20);

        label49.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label49.text")); // NOI18N
        jPanel11.add(label49);
        label49.setBounds(260, 120, 18, 20);

        label50.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label50.text")); // NOI18N
        jPanel11.add(label50);
        label50.setBounds(260, 150, 24, 20);

        lbMass101.setEditable(false);
        lbMass101.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbMass101.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbMass101.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbMass101.text")); // NOI18N
        jPanel11.add(lbMass101);
        lbMass101.setBounds(120, 90, 130, 21);

        lbVolume101.setEditable(false);
        lbVolume101.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbVolume101.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbVolume101.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbVolume101.text")); // NOI18N
        jPanel11.add(lbVolume101);
        lbVolume101.setBounds(120, 60, 130, 21);

        lbTemperature101.setEditable(false);
        lbTemperature101.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbTemperature101.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbTemperature101.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbTemperature101.text")); // NOI18N
        jPanel11.add(lbTemperature101);
        lbTemperature101.setBounds(120, 120, 130, 21);

        lbDensity101.setEditable(false);
        lbDensity101.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDensity101.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbDensity101.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbDensity101.text")); // NOI18N
        jPanel11.add(lbDensity101);
        lbDensity101.setBounds(120, 150, 130, 21);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel10, org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jLabel10.text")); // NOI18N
        jPanel11.add(jLabel10);
        jLabel10.setBounds(20, 70, 37, 14);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel11, org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jLabel11.text")); // NOI18N
        jPanel11.add(jLabel11);
        jLabel11.setBounds(20, 90, 34, 14);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel12, org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jLabel12.text")); // NOI18N
        jPanel11.add(jLabel12);
        jLabel12.setBounds(20, 120, 80, 14);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel13, org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jLabel13.text")); // NOI18N
        jPanel11.add(jLabel13);
        jLabel13.setBounds(20, 150, 70, 14);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jPanel12.border.title"))); // NOI18N
        jPanel12.setLayout(null);

        label51.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label51.text")); // NOI18N
        jPanel12.add(label51);
        label51.setBounds(360, 60, 22, 20);

        label52.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label52.text")); // NOI18N
        jPanel12.add(label52);
        label52.setBounds(360, 90, 11, 20);

        label53.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label53.text")); // NOI18N
        jPanel12.add(label53);
        label53.setBounds(360, 120, 18, 20);

        label54.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label54.text")); // NOI18N
        jPanel12.add(label54);
        label54.setBounds(360, 150, 24, 20);

        lbLevel32.setEditable(false);
        lbLevel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbLevel32.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbLevel32.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbLevel32.text")); // NOI18N
        jPanel12.add(lbLevel32);
        lbLevel32.setBounds(260, 60, 90, 21);

        lbVolume32.setEditable(false);
        lbVolume32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbVolume32.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbVolume32.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbVolume32.text")); // NOI18N
        jPanel12.add(lbVolume32);
        lbVolume32.setBounds(260, 90, 90, 21);

        pbLevel32.setOrientation(1);
        pbLevel32.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pbLevel32.setFocusable(false);
        pbLevel32.setString(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.pbLevel32.string")); // NOI18N
        jPanel12.add(pbLevel32);
        pbLevel32.setBounds(170, 50, 19, 100);

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alexprom/tsp_account/vis/tank_h.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel14, org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jLabel14.text")); // NOI18N
        jLabel14.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabel14.setOpaque(true);
        jPanel12.add(jLabel14);
        jLabel14.setBounds(16, 27, 236, 140);

        lbDensity32.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbDensity32.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbDensity32.text")); // NOI18N
        lbDensity32.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lbDensity32FocusLost(evt);
            }
        });
        lbDensity32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbDensity32ActionPerformed(evt);
            }
        });
        lbDensity32.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lbDensity32KeyPressed(evt);
            }
        });
        jPanel12.add(lbDensity32);
        lbDensity32.setBounds(260, 150, 90, 20);

        lbTemperature32.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbTemperature32.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbTemperature32.text")); // NOI18N
        lbTemperature32.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lbTemperature32FocusLost(evt);
            }
        });
        lbTemperature32.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lbTemperature32KeyPressed(evt);
            }
        });
        jPanel12.add(lbTemperature32);
        lbTemperature32.setBounds(260, 120, 90, 20);

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jPanel13.border.title"))); // NOI18N
        jPanel13.setLayout(null);

        label55.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label55.text")); // NOI18N
        jPanel13.add(label55);
        label55.setBounds(360, 60, 22, 20);

        label56.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label56.text")); // NOI18N
        jPanel13.add(label56);
        label56.setBounds(360, 90, 11, 20);

        label57.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label57.text")); // NOI18N
        jPanel13.add(label57);
        label57.setBounds(360, 120, 18, 20);

        label58.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.label58.text")); // NOI18N
        jPanel13.add(label58);
        label58.setBounds(360, 150, 24, 20);

        lbLevel23.setEditable(false);
        lbLevel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbLevel23.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbLevel23.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbLevel23.text")); // NOI18N
        jPanel13.add(lbLevel23);
        lbLevel23.setBounds(260, 60, 90, 21);

        lbVolume23.setEditable(false);
        lbVolume23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbVolume23.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbVolume23.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbVolume23.text")); // NOI18N
        jPanel13.add(lbVolume23);
        lbVolume23.setBounds(260, 90, 90, 21);

        lbTemperature23.setEditable(false);
        lbTemperature23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbTemperature23.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbTemperature23.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbTemperature23.text")); // NOI18N
        jPanel13.add(lbTemperature23);
        lbTemperature23.setBounds(260, 120, 90, 21);

        lbDensity23.setEditable(false);
        lbDensity23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDensity23.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbDensity23.setText(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.lbDensity23.text")); // NOI18N
        jPanel13.add(lbDensity23);
        lbDensity23.setBounds(260, 150, 90, 21);

        pbLevel23.setOrientation(1);
        pbLevel23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pbLevel23.setFocusable(false);
        pbLevel23.setString(org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.pbLevel23.string")); // NOI18N
        jPanel13.add(pbLevel23);
        pbLevel23.setBounds(170, 50, 19, 100);

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/alexprom/tsp_account/vis/tank_h.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel15, org.openide.util.NbBundle.getMessage(TankDataTopComponent.class, "TankDataTopComponent.jLabel15.text")); // NOI18N
        jLabel15.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabel15.setOpaque(true);
        jPanel13.add(jLabel15);
        jLabel15.setBounds(16, 27, 236, 140);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                        .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lbDensity32FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lbDensity32FocusLost
        density32_Change(true);
    }//GEN-LAST:event_lbDensity32FocusLost

    private void lbDensity32KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lbDensity32KeyPressed
        if (evt.getKeyCode()==KeyEvent.VK_ENTER){
            density32_Change(true);
        }
    }//GEN-LAST:event_lbDensity32KeyPressed

    private void lbTemperature32FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lbTemperature32FocusLost
        temperature32_Change(true);
    }//GEN-LAST:event_lbTemperature32FocusLost

    private void lbTemperature32KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lbTemperature32KeyPressed
        if (evt.getKeyCode()==KeyEvent.VK_ENTER){
            temperature32_Change(true);
        }
    }//GEN-LAST:event_lbTemperature32KeyPressed

    private void lbDensity32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbDensity32ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbDensity32ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private java.awt.Label label10;
    private java.awt.Label label27;
    private java.awt.Label label28;
    private java.awt.Label label29;
    private java.awt.Label label30;
    private java.awt.Label label31;
    private java.awt.Label label32;
    private java.awt.Label label33;
    private java.awt.Label label34;
    private java.awt.Label label35;
    private java.awt.Label label36;
    private java.awt.Label label37;
    private java.awt.Label label38;
    private java.awt.Label label39;
    private java.awt.Label label40;
    private java.awt.Label label41;
    private java.awt.Label label42;
    private java.awt.Label label43;
    private java.awt.Label label44;
    private java.awt.Label label45;
    private java.awt.Label label46;
    private java.awt.Label label47;
    private java.awt.Label label48;
    private java.awt.Label label49;
    private java.awt.Label label50;
    private java.awt.Label label51;
    private java.awt.Label label52;
    private java.awt.Label label53;
    private java.awt.Label label54;
    private java.awt.Label label55;
    private java.awt.Label label56;
    private java.awt.Label label57;
    private java.awt.Label label58;
    private java.awt.Label label7;
    private java.awt.Label label8;
    private java.awt.Label label9;
    private javax.swing.JTextField lbDensity100;
    private javax.swing.JTextField lbDensity101;
    private javax.swing.JTextField lbDensity11;
    private javax.swing.JTextField lbDensity15;
    private javax.swing.JTextField lbDensity16;
    private javax.swing.JTextField lbDensity17;
    private javax.swing.JTextField lbDensity23;
    private javax.swing.JTextField lbDensity26;
    private javax.swing.JFormattedTextField lbDensity32;
    private javax.swing.JTextField lbLevel11;
    private javax.swing.JTextField lbLevel15;
    private javax.swing.JTextField lbLevel16;
    private javax.swing.JTextField lbLevel17;
    private javax.swing.JTextField lbLevel23;
    private javax.swing.JTextField lbLevel26;
    private javax.swing.JTextField lbLevel32;
    private javax.swing.JTextField lbMass100;
    private javax.swing.JTextField lbMass101;
    private javax.swing.JTextField lbTemperature100;
    private javax.swing.JTextField lbTemperature101;
    private javax.swing.JTextField lbTemperature11;
    private javax.swing.JTextField lbTemperature15;
    private javax.swing.JTextField lbTemperature16;
    private javax.swing.JTextField lbTemperature17;
    private javax.swing.JTextField lbTemperature23;
    private javax.swing.JTextField lbTemperature26;
    private javax.swing.JFormattedTextField lbTemperature32;
    private javax.swing.JTextField lbVolume100;
    private javax.swing.JTextField lbVolume101;
    private javax.swing.JTextField lbVolume11;
    private javax.swing.JTextField lbVolume15;
    private javax.swing.JTextField lbVolume16;
    private javax.swing.JTextField lbVolume17;
    private javax.swing.JTextField lbVolume23;
    private javax.swing.JTextField lbVolume26;
    private javax.swing.JTextField lbVolume32;
    private javax.swing.JProgressBar pbLevel11;
    private javax.swing.JProgressBar pbLevel15;
    private javax.swing.JProgressBar pbLevel16;
    private javax.swing.JProgressBar pbLevel17;
    private javax.swing.JProgressBar pbLevel23;
    private javax.swing.JProgressBar pbLevel26;
    private javax.swing.JProgressBar pbLevel32;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        updatePersistence();
        Preferences pref = NbPreferences.forModule(dbConnectionSettingsPanel.class);
        pref.addPreferenceChangeListener(new PreferenceChangeListener() {
        @Override
        public void preferenceChange(PreferenceChangeEvent evt) {                        
            updatePersistence();
            try {
                getTask();
            } catch (SAXException | IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        });        
        
        Preferences tagsMgmt = NbPreferences.forModule(TagManagementPanel.class);
        tagsMgmt.addPreferenceChangeListener(new PreferenceChangeListener(){
            @Override
            public void preferenceChange(PreferenceChangeEvent evt) {
                try {
                    
                    getTask();
                } catch (SAXException | IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }                
        });
        Preferences tankPref = NbPreferences.forModule(sensorSettingsPanel.class);
        tankPref.addPreferenceChangeListener(new PreferenceChangeListener() {
            @Override
            public void preferenceChange(PreferenceChangeEvent evt) {
                try {
                    getTask();
                } catch (SAXException | IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
        try {
            getTask();
            formThread = new Thread(this);
            formThread.start();
        } catch (SAXException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
    }

    @Override
    public void componentClosed() {
        
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }       
    
    private PlcTag[] setTagsAttributes(NodeList nodeList){
        PlcTag[] tag = new PlcTag[nodeList.getLength()];
        for (int i=0; i<nodeList.getLength(); i++){
            tag[i] = new PlcTag();
            NamedNodeMap attributes = nodeList.item(i).getAttributes();
            Node nameAttrib = attributes.getNamedItem("name");
            tag[i].setTagName(nameAttrib.getNodeValue());
            Node plcAttrib = attributes.getNamedItem("plcName");
            tag[i].setPlcName(plcAttrib.getNodeValue());
            Node addrAttrib = attributes.getNamedItem("S7Addr");
            tag[i].setS7Addr(addrAttrib.getNodeValue());                
            Node maxAttrib = attributes.getNamedItem("maxValue");
            tag[i].setMaxValue(Double.parseDouble(maxAttrib.getNodeValue()));
            Node minAttrib = attributes.getNamedItem("minValue");
            tag[i].setMinValue(Double.parseDouble(minAttrib.getNodeValue()));
        }
        return tag;
    }
    
    private SimaticAddressParser setSimaticTag(PlcTag plcTag){        
        SimaticAddressParser tags = new SimaticAddressParser(plcTag.getS7Addr());        
        return tags;
    }
    
    public void getTask() throws SAXException, IOException{
        
        Query query = em.createNamedQuery("SystemTags.findAll");
        List<SystemTags> tagsList = query.getResultList();
        int count = tagsList.size();
        if (count!=0){
            tag = new PlcTag[count]; 
            for (int i=0;i<count;i++){
                tag[i] = new PlcTag();
                tag[i].setTagId(tagsList.get(i).getId());
                tag[i].setTagName(tagsList.get(i).getTagName());
                tag[i].setS7Addr(tagsList.get(i).getTagAddress());
                tag[i].setMinValue(tagsList.get(i).getMinValue());
                tag[i].setMaxValue(tagsList.get(i).getMaxValue());
                tag[i].setPlcName(NbPreferences.forModule(TagManagementPanel.class).get("PLC_Address", "192.168.1.15"));
            }
        }
        
        query = em.createNamedQuery("TanksToAccount.findAll");
        tanks = query.getResultList();
        tankData = new TankData[tanks.size()];
        for (int i=0;i<tanks.size();i++){
            tankData[i] = new TankData();
            tankData[i].setTankId(tanks.get(i).getTankId());
            tankData[i].setTankSerial(tanks.get(i).getSensorSerial());
            for (PlcTag plcTags : tag){
                if (tanks.get(i).getLevelTag().equals(plcTags.getTagId())){
                   tankData[i].setLevelTag(plcTags);
                   tankData[i].setLevelS7(setSimaticTag(plcTags));                   
                }else if (tanks.get(i).getTemperatureTag().equals(plcTags.getTagId())){
                    tankData[i].setTemperatureTag(plcTags);
                    tankData[i].setTemperatureS7(setSimaticTag(plcTags));                    
                }else if(tanks.get(i).getDensityTag().equals(plcTags.getTagId())){
                    tankData[i].setDensityTag(plcTags);  
                    tankData[i].setDensityS7(setSimaticTag(plcTags));
                }
            }                        
        }
        
        query = em.createNamedQuery("CountersInitData.findAll");
        counters = query.getResultList();
        counterData = new CounterData[counters.size()];                
        for (int i=0; i<counters.size(); i++){
            counterData[i] = new CounterData();
            counterData[i].setCounterId(counters.get(i).getCounterId());
            counterData[i].setSensorSerial(counters.get(i).getSensorSerial());
            for (PlcTag plcTags : tag){
                if (counters.get(i).getMassTag().equals(plcTags.getTagId())){
                    counterData[i].setMassTag(plcTags);
                    counterData[i].setMassSimaticAttr(setSimaticTag(plcTags));                    
                }else if (counters.get(i).getVolumeTag().equals(plcTags.getTagId())){
                    counterData[i].setVolumeTag(plcTags);
                    counterData[i].setVolumeSimaticAttr(setSimaticTag(plcTags));                    
                }else if (counters.get(i).getTemperatureTag().equals(plcTags.getTagId())){
                    counterData[i].setTemperatureTag(plcTags);
                    counterData[i].setTemperatureSimaticAttr(setSimaticTag(plcTags));
                }else if (counters.get(i).getDensityTag().equals(plcTags.getTagId())){
                    counterData[i].setDensityRag(plcTags);
                    counterData[i].setDensitySimaticAttr(setSimaticTag(plcTags));                    
                }                    
            }                                        
        }          
        
        plc = new JPlcAgent(NbPreferences.forModule(TagManagementPanel.class).get("PLC_Address", "192.168.1.15"), NbPreferences.forModule(TagManagementPanel.class).get("PLC_Address", "192.168.1.15"), 1);
            
                                            
    }
    
    public void doTask(){       
        updateVis();
        Query cnt = em.createNamedQuery("SystemTags.findAll");
        List<SystemTags> tagsCnt = cnt.getResultList();
        int count = tagsCnt.size();
        if (count!=0){
            if (plc.Connected){
            for (TankData  accountTanks : tankData){                
                accountTanks.setTankLevel(BigDecimal.valueOf(plc.readFloatData(accountTanks.getLevelS7().getDbNum(), 
                        accountTanks.getLevelS7().getStartAddress(), 
                        accountTanks.getLevelS7().getByteCnt())));
                if (accountTanks.getTankId()!=47){
                    accountTanks.setTankTemperature(BigDecimal.valueOf(plc.readFloatData(accountTanks.getTemperatureS7().getDbNum(), 
                            accountTanks.getTemperatureS7().getStartAddress(), accountTanks.getTemperatureS7().getByteCnt())));
                    accountTanks.setTankDensity(BigDecimal.valueOf(plc.readFloatData(accountTanks.getDensityS7().getDbNum(), 
                            accountTanks.getDensityS7().getStartAddress(), 
                            accountTanks.getDensityS7().getByteCnt())));
                }else{
                    accountTanks.setTankTemperature(BigDecimal.valueOf(newTemperature_32));
                    accountTanks.setTankDensity(BigDecimal.valueOf(newDensity_32));
                }                
                Query query = em.createNamedQuery("GradView.findByTankIdLevel");
                query.setParameter("tankId", accountTanks.getTankId());
                query.setParameter("matLevel", accountTanks.getTankLevel());
                List<GradView> list;
                list = query.getResultList();
                if (!list.isEmpty()){
                    accountTanks.setTankVolume(BigDecimal.valueOf(list.get(0).getMatVolume().doubleValue()));
                }else{                    
                    accountTanks.setTankVolume(BigDecimal.ZERO);
                }                                
            }
            
            for (CounterData accountCounters : counterData){
                accountCounters.setCounterMass(BigDecimal.valueOf(plc.readFloatData(accountCounters.getMassSimaticAttr().getDbNum(), 
                        accountCounters.getMassSimaticAttr().getStartAddress(), 
                        accountCounters.getMassSimaticAttr().getByteCnt())));
                accountCounters.setCounterVolume(BigDecimal.valueOf(plc.readFloatData(accountCounters.getVolumeSimaticAttr().getDbNum(), 
                        accountCounters.getVolumeSimaticAttr().getStartAddress(), 
                        accountCounters.getVolumeSimaticAttr().getByteCnt())));
                accountCounters.setCounterTemperature(BigDecimal.valueOf(plc.readFloatData(accountCounters.getTemperatureSimaticAttr().getDbNum(), 
                        accountCounters.getTemperatureSimaticAttr().getStartAddress(), 
                        accountCounters.getTemperatureSimaticAttr().getByteCnt())));
                accountCounters.setCounterDensity(BigDecimal.valueOf(plc.readFloatData(accountCounters.getDensitySimaticAttr().getDbNum(), 
                        accountCounters.getDensitySimaticAttr().getStartAddress(), 
                        accountCounters.getDensitySimaticAttr().getByteCnt())));
            }
                        
            }            
            
        }
        
    }
    
    private void updateVis(){
        
        int i=0;
        for (TankData visTankData : tankData){
            Query query = em.createNamedQuery("TankDic.findByTankId");
            if (visTankData.getTankId()!=47){
            switch (i){
                case 1:{
                    query.setParameter("tankId", visTankData.getTankId());
                    List<TankDic> tankNames = query.getResultList();
                    for (TankDic names : tankNames){                
                        jPanel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ёмкость "+names.getTankName()+":")); 
                    }
            
                    lbLevel11.setText(String.format("%.1f", visTankData.getTankLevel()));
                    lbVolume11.setText(String.format("%.1f", visTankData.getTabkVolume()));
                    lbTemperature11.setText(String.format("%.1f", visTankData.getTankTemperature()));
                    lbDensity11.setText(String.format("%.4f", visTankData.getTankDensity()));
                    pbLevel11.setMaximum((int) Math.round(visTankData.getLevelTag().getMaxValue()));
                    pbLevel11.setValue(Math.round(visTankData.getTankLevel().floatValue()));
                    i++;
                    break;
                }
                case 2:{
                    query.setParameter("tankId", visTankData.getTankId());
                    List<TankDic> tankNames = query.getResultList();
                    for (TankDic names : tankNames){                
                        jPanel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ёмкость "+names.getTankName()+":")); 
                    }
                    lbLevel15.setText(String.format("%.1f", visTankData.getTankLevel()));
                    lbVolume15.setText(String.format("%.1f", visTankData.getTabkVolume()));
                    lbTemperature15.setText(String.format("%.1f", visTankData.getTankTemperature()));
                    lbDensity15.setText(String.format("%.4f", visTankData.getTankDensity()));
                    pbLevel15.setMaximum((int) Math.round(visTankData.getLevelTag().getMaxValue()));
                    pbLevel15.setValue(Math.round(visTankData.getTankLevel().floatValue()));
                    i++;
                    break;
                }
                case 3:{
                    query.setParameter("tankId", visTankData.getTankId());
                    List<TankDic> tankNames = query.getResultList();
                    for (TankDic names : tankNames){                
                        jPanel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ёмкость "+names.getTankName()+":")); 
                    }                
                    lbLevel16.setText(String.format("%.1f", visTankData.getTankLevel()));
                    lbVolume16.setText(String.format("%.1f", visTankData.getTabkVolume()));
                    lbTemperature16.setText(String.format("%.1f", visTankData.getTankTemperature()));
                    lbDensity16.setText(String.format("%.4f", visTankData.getTankDensity()));
                    pbLevel16.setMaximum((int) Math.round(visTankData.getLevelTag().getMaxValue()));
                    pbLevel16.setValue(Math.round(visTankData.getTankLevel().floatValue()));
                    i++;
                    break;
                }
                case 4:{
                    query.setParameter("tankId", visTankData.getTankId());
                    List<TankDic> tankNames = query.getResultList();
                    for (TankDic names : tankNames){                
                        jPanel8.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ёмкость "+names.getTankName()+":")); 
                    }                
                    lbLevel17.setText(String.format("%.1f", visTankData.getTankLevel()));
                    lbVolume17.setText(String.format("%.1f", visTankData.getTabkVolume()));
                    lbTemperature17.setText(String.format("%.1f", visTankData.getTankTemperature()));
                    lbDensity17.setText(String.format("%.4f", visTankData.getTankDensity()));
                    pbLevel17.setMaximum((int) Math.round(visTankData.getLevelTag().getMaxValue()));
                    pbLevel17.setValue(Math.round(visTankData.getTankLevel().floatValue()));
                    i++;
                    break;
                }
                case 0:{
                    query.setParameter("tankId", visTankData.getTankId());
                    List<TankDic> tankNames = query.getResultList();
                    for (TankDic names : tankNames){                
                        jPanel9.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ёмкость "+names.getTankName()+":")); 
                    }
                    lbLevel26.setText(String.format("%.1f", visTankData.getTankLevel()));
                    lbVolume26.setText(String.format("%.1f", visTankData.getTabkVolume()));
                    lbTemperature26.setText(String.format("%.1f", visTankData.getTankTemperature()));
                    lbDensity26.setText(String.format("%.4f", visTankData.getTankDensity()));
                    pbLevel26.setMaximum((int) Math.round(visTankData.getLevelTag().getMaxValue()));
                    pbLevel26.setValue(Math.round(visTankData.getTankLevel().floatValue()));
                    i++;
                    break;
                }
                case 5:{
                    query.setParameter("tankId", visTankData.getTankId());
                    List<TankDic> tankNames = query.getResultList();
                    for (TankDic names : tankNames){                
                        jPanel13.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ёмкость "+names.getTankName()+":")); 
                    }
                    lbLevel23.setText(String.format("%.1f", visTankData.getTankLevel()));
                    lbVolume23.setText(String.format("%.1f", visTankData.getTabkVolume()));
                    lbTemperature23.setText(String.format("%.1f", visTankData.getTankTemperature()));
                    lbDensity23.setText(String.format("%.4f", visTankData.getTankDensity()));                    
                    pbLevel23.setMaximum((int) Math.round(visTankData.getLevelTag().getMaxValue()));
                    pbLevel23.setValue(Math.round(visTankData.getTankLevel().floatValue()));
                    i++;
                    break;
                }                                    
            }
            }else{
                    jPanel12.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "РВС-32:"));                     
                    lbLevel32.setText(String.format("%.1f", visTankData.getTankLevel()));
                    lbVolume32.setText(String.format("%.1f", visTankData.getTabkVolume()));
                    if (!lbTemperature32.isFocusOwner())
                        lbTemperature32.setText(String.format("%.1f", visTankData.getTankTemperature()));                    
                    if (!lbDensity32.isFocusOwner())
                        lbDensity32.setText(String.format("%.4f", visTankData.getTankDensity()));
                    pbLevel32.setMaximum((int) Math.round(visTankData.getLevelTag().getMaxValue()));
                    pbLevel32.setValue(Math.round(visTankData.getTankLevel().floatValue()));
                
            }
        }
        
        lbMass100.setText(String.format("%.1f", counterData[0].getCounterMass()));
        lbVolume100.setText(String.format("%.1f", counterData[0].getCounterVolume()));
        lbTemperature100.setText(String.format("%.1f", counterData[0].getCounterTemperature()));
        lbDensity100.setText(String.format("%.4f", counterData[0].getCounterDensity()));
        
        lbMass101.setText(String.format("%.1f", counterData[1].getCounterMass()));
        lbVolume101.setText(String.format("%.1f", counterData[1].getCounterVolume()));
        lbTemperature101.setText(String.format("%.1f", counterData[1].getCounterTemperature()));
        lbDensity101.setText(String.format("%.4f", counterData[1].getCounterDensity()));
    }
    
    @Override
    public void run() {
        while(true){                        
            doTask();
            updatePersistence();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
