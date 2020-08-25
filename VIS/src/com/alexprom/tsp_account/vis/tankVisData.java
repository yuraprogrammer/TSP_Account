/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.vis;

import com.alexprom.connection.settings.dbConnectionSettingsPanel;
import com.alexprom.tsp_account.daq.CustomRenderer;
import com.alexprom.tsp_account.daq.DataLoggerTopComponent;
import com.alexprom.tsp_account.daq.TankData;
import com.alexprom.tsp_account.report_db.GlobalEntityManager;
import com.alexprom.tsp_account.report_db.TSPReport;
import com.alexprom.tsp_account.report_db.TanksToAccount;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author yura_
 */

@TopComponent.Description(
        preferredID = "tankVisDataTopComponent",
        iconBase = "com/alexprom/tsp_account/vis/tank_ico.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(
        mode = "editor",
        openAtStartup = false)
/*@ActionID(
        category = "Window",
        id = "com.alexprom.tsp_account.vis.tankVisDataTopComponent")
@ActionReference(
        path = "Menu/Window")
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_tankVisDataAction")*/
@NbBundle.Messages({
    "CTL_tankVisDataAction=tankVisData"
})



public class tankVisData extends TopComponent implements Runnable{

   private TanksToAccount tankInitData;
   //private DAQ_Thread daqThread;
   private TankData[] data;
   private int dataIndex=-1;
   private Thread formThread;   
   private final tankPanelChart chart;
   private XYDataset dataset;
   private final ChartPanel panel;
   private BigDecimal newDensity32=BigDecimal.ZERO, newTemperature32=BigDecimal.ZERO;
   private DataLoggerTopComponent tc;
   private EntityManager em;
   private boolean devicesChanged=false, changedOld, changedNew;
    /**
     * Creates new form tankVisData
     */
    public tankVisData() {
        initComponents();
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.FALSE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_SLIDING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.TRUE);
        chart = new tankPanelChart("", "Взлив, мм", "");
        Dimension dim = new Dimension();
        dim.setSize(400, 250);
        panel = new ChartPanel(chart.getChart());        
        panel.setPreferredSize(dim);
        jPanel3.add(panel);
        tc = (DataLoggerTopComponent)WindowManager.getDefault().findTopComponent("DataLoggerTopComponent");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbCurrentLevel = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        lbCurrentVolume = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lbCurrentDensity = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lbCurrentTemperature = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        lbFilling = new javax.swing.JProgressBar();
        lbMaxLevel = new javax.swing.JTextField();
        lbMinLevel = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCurrentDay = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblLastWeek = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(946, 656));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.jPanel1.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 16, 0, 0);
        jPanel1.add(jLabel1, gridBagConstraints);

        lbCurrentLevel.setEditable(false);
        lbCurrentLevel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbCurrentLevel.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbCurrentLevel.setText(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.lbCurrentLevel.text")); // NOI18N
        lbCurrentLevel.setToolTipText(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.lbCurrentLevel.toolTipText")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 83;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(27, 27, 0, 0);
        jPanel1.add(lbCurrentLevel, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 16, 0, 0);
        jPanel1.add(jLabel2, gridBagConstraints);

        lbCurrentVolume.setEditable(false);
        lbCurrentVolume.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbCurrentVolume.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbCurrentVolume.setText(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.lbCurrentVolume.text")); // NOI18N
        lbCurrentVolume.setToolTipText(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.lbCurrentVolume.toolTipText")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 83;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 27, 0, 0);
        jPanel1.add(lbCurrentVolume, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.jLabel3.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 16, 0, 0);
        jPanel1.add(jLabel3, gridBagConstraints);

        lbCurrentDensity.setEditable(false);
        lbCurrentDensity.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbCurrentDensity.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbCurrentDensity.setText(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.lbCurrentDensity.text")); // NOI18N
        lbCurrentDensity.setToolTipText(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.lbCurrentDensity.toolTipText")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 83;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 27, 0, 0);
        jPanel1.add(lbCurrentDensity, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.jLabel4.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 16, 0, 0);
        jPanel1.add(jLabel4, gridBagConstraints);

        lbCurrentTemperature.setEditable(false);
        lbCurrentTemperature.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbCurrentTemperature.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbCurrentTemperature.setText(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.lbCurrentTemperature.text")); // NOI18N
        lbCurrentTemperature.setToolTipText(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.lbCurrentTemperature.toolTipText")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 83;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 27, 17, 0);
        jPanel1.add(lbCurrentTemperature, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.jLabel5.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 18, 0, 0);
        jPanel1.add(jLabel5, gridBagConstraints);

        lbFilling.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbFilling.setOrientation(1);
        lbFilling.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.ipady = 88;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(27, 6, 17, 0);
        jPanel1.add(lbFilling, gridBagConstraints);

        lbMaxLevel.setEditable(false);
        lbMaxLevel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbMaxLevel.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbMaxLevel.setText(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.lbMaxLevel.text")); // NOI18N
        lbMaxLevel.setToolTipText(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.lbMaxLevel.toolTipText")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 48;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(27, 6, 0, 18);
        jPanel1.add(lbMaxLevel, gridBagConstraints);

        lbMinLevel.setEditable(false);
        lbMinLevel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbMinLevel.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbMinLevel.setText(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.lbMinLevel.text")); // NOI18N
        lbMinLevel.setToolTipText(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.lbMinLevel.toolTipText")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 48;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 17, 18);
        jPanel1.add(lbMinLevel, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.jPanel4.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        tblCurrentDay.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tblCurrentDay.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Время", "Взлив, мм", "Объем, л", "t, °C", "p, кг/л"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCurrentDay.setEnabled(false);
        tblCurrentDay.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblCurrentDay);
        if (tblCurrentDay.getColumnModel().getColumnCount() > 0) {
            tblCurrentDay.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.tblCurrentDay.columnModel.title0")); // NOI18N
            tblCurrentDay.getColumnModel().getColumn(1).setHeaderValue(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.tblCurrentDay.columnModel.title1")); // NOI18N
            tblCurrentDay.getColumnModel().getColumn(2).setHeaderValue(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.tblCurrentDay.columnModel.title2")); // NOI18N
            tblCurrentDay.getColumnModel().getColumn(3).setHeaderValue(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.tblCurrentDay.columnModel.title3")); // NOI18N
            tblCurrentDay.getColumnModel().getColumn(4).setHeaderValue(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.tblCurrentDay.columnModel.title4")); // NOI18N
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.jPanel4.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.jPanel4.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        tblLastWeek.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Дата:", "Время", "Взлив, мм", "Объем, л", "t, °C", "p, кг/л"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblLastWeek.setEnabled(false);
        tblLastWeek.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblLastWeek);
        if (tblLastWeek.getColumnModel().getColumnCount() > 0) {
            tblLastWeek.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.tblLastWeek.columnModel.title5")); // NOI18N
            tblLastWeek.getColumnModel().getColumn(1).setHeaderValue(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.tblCurrentDay.columnModel.title0")); // NOI18N
            tblLastWeek.getColumnModel().getColumn(2).setHeaderValue(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.tblCurrentDay.columnModel.title1")); // NOI18N
            tblLastWeek.getColumnModel().getColumn(3).setHeaderValue(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.tblCurrentDay.columnModel.title2")); // NOI18N
            tblLastWeek.getColumnModel().getColumn(4).setHeaderValue(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.tblCurrentDay.columnModel.title3")); // NOI18N
            tblLastWeek.getColumnModel().getColumn(5).setHeaderValue(org.openide.util.NbBundle.getMessage(tankVisData.class, "tankVisData.tblCurrentDay.columnModel.title4")); // NOI18N
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(184, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField lbCurrentDensity;
    private javax.swing.JTextField lbCurrentLevel;
    private javax.swing.JTextField lbCurrentTemperature;
    private javax.swing.JTextField lbCurrentVolume;
    private javax.swing.JProgressBar lbFilling;
    private javax.swing.JTextField lbMaxLevel;
    private javax.swing.JTextField lbMinLevel;
    private javax.swing.JTable tblCurrentDay;
    private javax.swing.JTable tblLastWeek;
    // End of variables declaration//GEN-END:variables

   

    @Override
    public void componentOpened() {
        Preferences pref = NbPreferences.forModule(dbConnectionSettingsPanel.class);
        pref.addPreferenceChangeListener(new PreferenceChangeListener() {
        @Override
        public void preferenceChange(PreferenceChangeEvent evt) {                        
            updatePersistence();        
        }
        });        
                                
        updatePersistence();
        getTask();
        if (dataIndex==-1){
            close();
        }else{
            formThread = new Thread(this);
            formThread.setPriority(Thread.MIN_PRIORITY);
            formThread.start();
        }
    }
    
    public void updatePersistence(){                        
        GlobalEntityManager gem = new GlobalEntityManager();        
        em = gem.getEm();        
    }
    
    @Override
    public void componentClosed() {
        
    }
    
    public void setTankDataObject(TanksToAccount tank){
        tankInitData = tank;
    }

    private TanksToAccount getTankDataObject(){
        return tankInitData;
    }
    
    private void getTask(){        
        data = tc.getTankData();
        dataIndex=-1;
        if ((tankInitData==null) || (data==null)){ 
            close();
        }else{
            for (int i=0; i<data.length; i++){            
                if (data[i].getTankId()==tankInitData.getTankId()) dataIndex=i;
            }        
            checkEdition();        
        }
    }
    
    private void checkEdition(){
        if (data[dataIndex].getTankId()==47){
            lbCurrentDensity.setEditable(true);
            lbCurrentTemperature.setEditable(true);
            lbCurrentDensity.setText(String.format("%.4f", data[dataIndex].getTankDensity()));
            lbCurrentTemperature.setText(String.format("%.1f", data[dataIndex].getTankTemperature()));
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
            lbCurrentDensity.getDocument().addDocumentListener(density32Listener);
           
                    
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
            lbCurrentTemperature.getDocument().addDocumentListener(temperature32Listener);
        
        }
    }
    
    private void density32_Change(boolean edited){
        if (edited){
            if (!lbCurrentDensity.getText().isEmpty()){
                //oldDensity_32 = newDensity_32;                
                try{
                    newDensity32 = BigDecimal.valueOf(Double.parseDouble(lbCurrentDensity.getText().replace(",", ".")));
                }catch (java.lang.NumberFormatException e){
                    showNumberErroMessage();
                }
            
            }else{
                newDensity32 = BigDecimal.ZERO;
            }
            if (data[dataIndex].getDensityTag().getPlc().Connected){
                
                data[dataIndex].getDensityTag().getPlc().writeFloatData(data[dataIndex].getDensityS7().getDbNum(), 
                        data[dataIndex].getDensityS7().getStartAddress(), 
                        data[dataIndex].getDensityS7().getByteCnt(), 
                        newDensity32.doubleValue());
            }
        }
    }
    
    private void temperature32_Change(boolean edited){
        if (edited){
            if (!lbCurrentTemperature.getText().isEmpty()){
                //oldTemperature_32 = newTemperature_32;                
                try{
                    newTemperature32 = BigDecimal.valueOf(Double.parseDouble(lbCurrentTemperature.getText().replace(",", ".")));
                }catch (java.lang.NumberFormatException e){
                    showNumberErroMessage();
                }
            
            }else{
                newTemperature32 = BigDecimal.ZERO;
            }
            if (data[dataIndex].getDensityTag().getPlc().Connected){
                
                
                data[dataIndex].getTemperatureTag().getPlc().writeFloatData(data[dataIndex].getTemperatureS7().getDbNum(), 
                                                                    data[dataIndex].getTemperatureS7().getStartAddress(), 
                                                                    data[dataIndex].getTemperatureS7().getByteCnt(), 
                                                                    newTemperature32.doubleValue());
            }
        }
    }
    
    public void showNumberErroMessage(){
        NotifyDescriptor nd = new NotifyDescriptor.Message("Неверный формат введенных данных!!!", NotifyDescriptor.ERROR_MESSAGE);
        Object res = DialogDisplayer.getDefault().notify(nd);
    }
    
    private void updateVis(){        
        
        data = tc.getTankData();
        if (data!=null){
            lbMaxLevel.setText(String.format("%.1f", data[dataIndex].getLevelTag().getMaxValue()));
            lbMinLevel.setText(String.format("%.1f", data[dataIndex].getLevelTag().getMinValue()));
            lbCurrentLevel.setText(String.format("%.1f", data[dataIndex].getTankLevel()));
            lbCurrentVolume.setText(String.format("%.1f", data[dataIndex].getTabkVolume()));
        
            if (data[dataIndex].getTankId()!=47){
                lbCurrentDensity.setText(String.format("%.4f", data[dataIndex].getTankDensity()));
                lbCurrentTemperature.setText(String.format("%.1f", data[dataIndex].getTankTemperature()));
            }
            float fillLevel = data[dataIndex].getTankLevel().floatValue()/
                ((float)data[dataIndex].getLevelTag().getMaxValue()-(float)data[dataIndex].getLevelTag().getMinValue())*100;
            lbFilling.setValue(Math.round(fillLevel));
            Date currentDate = new Date();
            float[] newData = new float[1];
            newData[0] = data[dataIndex].getTankLevel().floatValue();
            chart.getRange().setRange(data[dataIndex].getLevelTag().getMinValue(), data[dataIndex].getLevelTag().getMaxValue());
            chart.getDataset().advanceTime();
            chart.getDataset().appendData(newData);
            chart.getChart().fireChartChanged();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Query query = em.createNamedQuery("TSPReport.findByDateTankID");
            query.setParameter("tankID", data[dataIndex].getTankId());
            query.setParameter("daqDate", df.format(currentDate));
            List<TSPReport> currentDay = query.getResultList();
            int cnt = currentDay.size();
            try{
                if (cnt!=0){            
                    DefaultTableModel tanksModel = new DefaultTableModel();
                    tanksModel.setColumnIdentifiers(new String[]{"Время", "Взлив, мм", "Объем, л", "t, °C", "р, кг/л"});
                    int i=0;
                    for (TSPReport currentTankData : currentDay){                
                        tblCurrentDay.getModel().setValueAt(currentTankData.getDaqTime(), i, 0);
                        tblCurrentDay.getModel().setValueAt(String.format("%.1f", currentTankData.getTLevel()), i, 1);
                        tblCurrentDay.getModel().setValueAt(String.format("%.1f", currentTankData.getTVolume()), i, 2);
                        tblCurrentDay.getModel().setValueAt(String.format("%.1f",currentTankData.getTTemper()), i, 3);
                        tblCurrentDay.getModel().setValueAt(String.format("%.4f", currentTankData.getTDensity()), i, 4);
                        i++;
                    }
                    int c = tblCurrentDay.getColumnModel().getColumnCount();
                    for (int j=0; j<c; j++){
                        TableColumn col = tblCurrentDay.getColumnModel().getColumn(j);
                        col.setCellRenderer(new CustomRenderer());
                    }
                }
                query = em.createNamedQuery("TSPReport.findByTankID");
                query.setParameter("tankID", data[dataIndex].getTankId());
                List<TSPReport> lastWeek = query.getResultList();
                int cntWeek = lastWeek.size();
                if (cntWeek!=0){
                    if (cntWeek>=14){
                        for (int j=cntWeek-14; j<cntWeek; j++){
                            tblLastWeek.getModel().setValueAt(lastWeek.get(j).getDaqDate(), j-cntWeek+14, 0);
                            tblLastWeek.getModel().setValueAt(lastWeek.get(j).getDaqTime(), j-cntWeek+14, 1);
                            tblLastWeek.getModel().setValueAt(String.format("%.1f", lastWeek.get(j).getTLevel()), j-cntWeek+14, 2);
                            tblLastWeek.getModel().setValueAt(String.format("%.1f", lastWeek.get(j).getTVolume()), j-cntWeek+14, 3);
                            tblLastWeek.getModel().setValueAt(String.format("%.1f", lastWeek.get(j).getTTemper()), j-cntWeek+14, 4);
                            tblLastWeek.getModel().setValueAt(String.format("%.4f", lastWeek.get(j).getTDensity()), j-cntWeek+14, 5);                    
                        }
                    }else{
                        for (int j=0; j<cntWeek; j++){
                            tblLastWeek.getModel().setValueAt(lastWeek.get(j).getDaqDate(), j, 0);
                            tblLastWeek.getModel().setValueAt(lastWeek.get(j).getDaqTime(), j, 1);
                            tblLastWeek.getModel().setValueAt(String.format("%.1f", lastWeek.get(j).getTLevel()), j, 2);
                            tblLastWeek.getModel().setValueAt(String.format("%.1f", lastWeek.get(j).getTVolume()), j, 3);
                            tblLastWeek.getModel().setValueAt(String.format("%.1f", lastWeek.get(j).getTTemper()), j, 4);
                            tblLastWeek.getModel().setValueAt(String.format("%.4f", lastWeek.get(j).getTDensity()), j, 5);
                        }
                    }            
                    int c = tblLastWeek.getColumnModel().getColumnCount();
                    for (int i=0; i<c; i++){
                        TableColumn col = tblLastWeek.getColumnModel().getColumn(i);                            
                        col.setCellRenderer(new CustomRenderer());
                    }
                }
            }catch (java.lang.ArrayIndexOutOfBoundsException ex){
            
            }
        }        
    }
           
    synchronized void resumeUpdateVis(){
        
        getTask();
        notify();        
    }
    
    @Override
    public void run() {
        while (true){            
            devicesChanged=tc.devicesDAQ_Restarted;
            changedNew=devicesChanged;
            synchronized(this){
                while (devicesChanged){
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            System.out.println("Tanks restarted");
            resumeUpdateVis();
            updateVis();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                
            }
            }
            
            changedOld=changedNew;
        }
    }

    
}   
