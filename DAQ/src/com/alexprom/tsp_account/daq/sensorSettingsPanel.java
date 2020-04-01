/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.daq;

import com.alexprom.tsp_account.report_actions.CountersInitDataJpaController;
import com.alexprom.tsp_account.report_actions.TanksToAccountJpaController;
import com.alexprom.tsp_account.report_actions.exceptions.NonexistentEntityException;
import com.alexprom.tsp_account.report_db.CountersInitData;
import com.alexprom.tsp_account.report_db.GlobalEntityManager;
import com.alexprom.tsp_account.report_db.SystemTags;
import com.alexprom.tsp_account.report_db.TankDic;
import com.alexprom.tsp_account.report_db.TanksToAccount;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

public final class sensorSettingsPanel extends javax.swing.JPanel {

    private final sensorSettingsOptionsPanelController controller;
    private DefaultTableModel tanksModel;
    private DefaultTableModel countersModel;
    GlobalEntityManager gem = new GlobalEntityManager();
    EntityManager em = gem.getEm();
    EntityManagerFactory emf = gem.getEmf();
    List<TanksToAccount> tanks;
    List<SystemTags> tags;
    List<TankDic> tankDic;
    int tankCode=-1;
    int levelTagCode=-1;
    int temperatureTagCode = -1;
    int densityTagCode=-1;
    int massTagCode=-1;
    int volumeTagCode=-1;
    JComboBox<String> tankSelect;
    JComboBox<String> levelTagSelect;
    JComboBox<String> temperatureTagSelect;
    JComboBox<String> densityTagSelect;
    DefaultComboBoxModel tagNamesModel;
    DefaultComboBoxModel tankNamesModel;
    JComboBox<String> massTagSelect;
    JComboBox<String> volumeTagSelect;
    JComboBox<String> tempCounterTagSelect;
    JComboBox<String> densityCounterTagSelect;
    List<CountersInitData> countersList;
    
    sensorSettingsPanel(sensorSettingsOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        Preferences pref = NbPreferences.forModule(TagManagementPanel.class);
        pref.addPreferenceChangeListener(new PreferenceChangeListener() {
            @Override
            public void preferenceChange(PreferenceChangeEvent evt) {
                System.out.println("Tags was updated");
                updatePanel();
            }
        });
        // TODO listen to changes in form fields and call controller.changed()
    }

    private void updatePanel(){
        this.controller.update();
    }
    
    private void initSensorSettings(){
        tanksModel = new DefaultTableModel();
        tanksModel.setColumnIdentifiers(new String[]{"Резервуар","Серийный №","Тег уровня", "Тег температуры", "Тег плотности"});
        Query query = em.createNamedQuery("TanksToAccount.findAll");
        tanks = query.getResultList();
        tagNamesModel = new DefaultComboBoxModel();
        tankNamesModel = new DefaultComboBoxModel();
        
        query = em.createNamedQuery("TankDic.findAll");
        tankDic = query.getResultList();        
        for (TankDic tank_Dic : tankDic){
            em.refresh(tank_Dic);
            tankNamesModel.addElement(tank_Dic.getTankName());
        }        
        tankSelect = new JComboBox(tankNamesModel);                
        
        query = em.createNamedQuery("SystemTags.findAll");
        tags = query.getResultList(); 
        
        for (SystemTags system_Tags : tags){
            em.refresh(system_Tags);
            tagNamesModel.addElement(system_Tags.getTagName());
        }
                        
        for (TanksToAccount tanksToAccount : tanks){
            em.refresh(tanksToAccount);
            query = em.createNamedQuery("TankDic.findByTankId");
            query.setParameter("tankId", tanksToAccount.getTankId());
            List<TankDic> assignedTank = query.getResultList();            
            query = em.createNamedQuery("SystemTags.findById");
            query.setParameter("id", tanksToAccount.getLevelTag());
            List<SystemTags> levelTagName = query.getResultList();
            query.setParameter("id", tanksToAccount.getTemperatureTag());
            List<SystemTags> temperatureTagName = query.getResultList();
            query.setParameter("id", tanksToAccount.getDensityTag());
            List<SystemTags> densityTagName = query.getResultList();
            String[] newRow = new String[5];
            for (TankDic tankRow : assignedTank){                
                newRow[0] = tankRow.getTankName();
            }
            newRow[1] = tanksToAccount.getSensorSerial();
            for (SystemTags levelTags : levelTagName){
                em.refresh(levelTags);
                newRow[2] = levelTags.getTagName();
            }
            for (SystemTags temperatureTags : temperatureTagName){
                em.refresh(temperatureTags);
                newRow[3] = temperatureTags.getTagName();
            }
            for (SystemTags densityTags : densityTagName){
                em.refresh(densityTags);
                newRow[4] = densityTags.getTagName();
            }                            
            tanksModel.addRow(newRow);            
        }
        tanksModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = jTable2.getEditingRow();
                int column = jTable2.getEditingColumn();
                if (row!=-1) editTankFields(row, column);                
            }
        });
        
        jTable2.setModel(tanksModel);
        
        
        levelTagSelect = new JComboBox(tagNamesModel);                
        temperatureTagSelect = new JComboBox(tagNamesModel);                                
        densityTagSelect = new JComboBox(tagNamesModel);        
        
        massTagSelect = new JComboBox(tagNamesModel);
        volumeTagSelect = new JComboBox(tagNamesModel);                
        tempCounterTagSelect = new JComboBox(tagNamesModel);
        densityCounterTagSelect = new JComboBox(tagNamesModel);                
        
        DefaultCellEditor tankSelector = new DefaultCellEditor(tankSelect);
        DefaultCellEditor levelTagSelector = new DefaultCellEditor(levelTagSelect);
        DefaultCellEditor temperatureTagSelector = new DefaultCellEditor(temperatureTagSelect);
        DefaultCellEditor densityTagSelector = new DefaultCellEditor(densityTagSelect);
        
        DefaultCellEditor massTagSelector = new DefaultCellEditor(massTagSelect);
        DefaultCellEditor volumeTagSelector = new DefaultCellEditor(volumeTagSelect);
        DefaultCellEditor tempCounterTagSelector = new DefaultCellEditor(tempCounterTagSelect);
        DefaultCellEditor densityCounterTagSelector = new DefaultCellEditor(densityCounterTagSelect);
        
        jTable2.getColumnModel().getColumn(0).setCellEditor(tankSelector);
        jTable2.getColumnModel().getColumn(2).setCellEditor(levelTagSelector);
        jTable2.getColumnModel().getColumn(3).setCellEditor(temperatureTagSelector);
        jTable2.getColumnModel().getColumn(4).setCellEditor(densityTagSelector);
        
        
        countersModel = new DefaultTableModel();
        countersModel.setColumnIdentifiers(new String[] {"Серийный №", "Тег массы", "Тег объема", "Тег температуры", "Тег плотности"});
        query = em.createNamedQuery("CountersInitData.findAll");
        countersList = query.getResultList();
        
        for (CountersInitData countersToAccount : countersList){
            query = em.createNamedQuery("SystemTags.findById");
            query.setParameter("id", countersToAccount.getMassTag());
            List<SystemTags> massTagName = query.getResultList();
            query.setParameter("id", countersToAccount.getVolumeTag());
            List<SystemTags> volumeTagName = query.getResultList();
            query.setParameter("id", countersToAccount.getTemperatureTag());
            List<SystemTags> temperatureTagName = query.getResultList();
            query.setParameter("id", countersToAccount.getDensityTag());
            List<SystemTags> densityTagName = query.getResultList();
            String[] countersData = new String[5];
            countersData[0] = countersToAccount.getSensorSerial();
            for (SystemTags mass : massTagName){
                countersData[1] = mass.getTagName();
            }
            for (SystemTags volume : volumeTagName){
                countersData[2] = volume.getTagName();
            }
            for (SystemTags temperature : temperatureTagName){
                countersData[3] = temperature.getTagName();
            }
            for (SystemTags density : densityTagName){
                countersData[4] = density.getTagName();
            }
            countersModel.addRow(countersData);
        }
        
        countersModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = jTable1.getEditingRow();
                int column = jTable1.getEditingColumn();
                if (row!=-1) editCountersData(row, column);
            }
        });
        jTable1.setModel(countersModel);
        
        jTable1.getColumnModel().getColumn(1).setCellEditor(massTagSelector);
        jTable1.getColumnModel().getColumn(2).setCellEditor(volumeTagSelector);
        jTable1.getColumnModel().getColumn(3).setCellEditor(tempCounterTagSelector);
        jTable1.getColumnModel().getColumn(4).setCellEditor(densityCounterTagSelector);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnNewCounter = new javax.swing.JButton();
        btnDelCounter = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        btnNewTank = new javax.swing.JButton();
        btnDelTank = new javax.swing.JButton();
        btnUpdateReferences = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(821, 559));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.jPanel1.border.title"))); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Серийный № прибора", "Тег массы", "Тег объема", "Тег температуры", "Тег плотности"
            }
        ));
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.jTable1.columnModel.title1")); // NOI18N
            jTable1.getColumnModel().getColumn(1).setHeaderValue(org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.jTable1.columnModel.title2")); // NOI18N
            jTable1.getColumnModel().getColumn(2).setHeaderValue(org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.jTable1.columnModel.title0_1")); // NOI18N
            jTable1.getColumnModel().getColumn(3).setHeaderValue(org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.jTable1.columnModel.title3")); // NOI18N
            jTable1.getColumnModel().getColumn(4).setHeaderValue(org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.jTable1.columnModel.title4")); // NOI18N
        }

        org.openide.awt.Mnemonics.setLocalizedText(btnNewCounter, org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.btnNewCounter.text")); // NOI18N
        btnNewCounter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCounterActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(btnDelCounter, org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.btnDelCounter.text")); // NOI18N
        btnDelCounter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelCounterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 605, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNewCounter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDelCounter, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(btnNewCounter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelCounter)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.jPanel2.border.title"))); // NOI18N

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Емкость:", "Серийный № прибора", "Тег уровня", "Тег температуры", "Тег плотности"
            }
        ));
        jTable2.setColumnSelectionAllowed(true);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable2);
        jTable2.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.jTable1.columnModel.title0")); // NOI18N
            jTable2.getColumnModel().getColumn(1).setHeaderValue(org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.jTable1.columnModel.title1")); // NOI18N
            jTable2.getColumnModel().getColumn(2).setHeaderValue(org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.jTable1.columnModel.title2")); // NOI18N
            jTable2.getColumnModel().getColumn(3).setHeaderValue(org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.jTable1.columnModel.title3")); // NOI18N
            jTable2.getColumnModel().getColumn(4).setHeaderValue(org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.jTable1.columnModel.title4")); // NOI18N
        }

        org.openide.awt.Mnemonics.setLocalizedText(btnNewTank, org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.btnNewTank.text")); // NOI18N
        btnNewTank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewTankActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(btnDelTank, org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.btnDelTank.text")); // NOI18N
        btnDelTank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelTankActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(btnUpdateReferences, org.openide.util.NbBundle.getMessage(sensorSettingsPanel.class, "sensorSettingsPanel.btnUpdateReferences.text")); // NOI18N
        btnUpdateReferences.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateReferencesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 630, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNewTank, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDelTank, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(btnUpdateReferences, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(btnNewTank)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelTank)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpdateReferences)
                .addContainerGap(166, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(307, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(198, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewTankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewTankActionPerformed
        TanksToAccount newTank = new TanksToAccount();
        TanksToAccountJpaController newTankAction = new TanksToAccountJpaController(emf);
        int newTankId = getNewTankId(em);
        String[] newTankData = {"1", "1111111"+String.valueOf(newTankId), "-1", "-1", "-1"};
        newTank.setId(newTankId);
        newTank.setTankId(Integer.parseInt(newTankData[0]));
        newTank.setSensorSerial(newTankData[1]);
        newTank.setLevelTag(Integer.parseInt(newTankData[2]));
        newTank.setTemperatureTag(Integer.parseInt(newTankData[3]));
        newTank.setDensityTag(Integer.parseInt(newTankData[4]));
        try {
            newTankAction.create(newTank);
            tanksModel.addRow(newTankData);
            tanks.add(newTank);                    
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }//GEN-LAST:event_btnNewTankActionPerformed

    private void btnUpdateReferencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateReferencesActionPerformed
        updatePanel();
    }//GEN-LAST:event_btnUpdateReferencesActionPerformed

    private void btnDelTankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelTankActionPerformed
        int rowToDel = jTable2.getSelectedRow();
        if (rowToDel!=-1){
            TanksToAccountJpaController delTank = new TanksToAccountJpaController(emf);
            try {
                delTank.destroy(tanks.get(rowToDel).getId());
                tanksModel.removeRow(rowToDel);
                tanks.remove(rowToDel);
            } catch (NonexistentEntityException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }//GEN-LAST:event_btnDelTankActionPerformed

    private void btnNewCounterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCounterActionPerformed
        CountersInitData newCounter = new CountersInitData();
        CountersInitDataJpaController newCounterAction = new CountersInitDataJpaController(emf);
        int newCounterId = getNewCounterId(em);
        int counterId = newCounterId+100;
        String[] newCounterData= new String[]{"11111"+String.valueOf(counterId), "-1", "-1", "-1", "-1"};
        newCounter.setId(newCounterId);
        newCounter.setCounterId(counterId);
        newCounter.setSensorSerial(newCounterData[0]);
        newCounter.setMassTag(Integer.parseInt(newCounterData[1]));
        newCounter.setVolumeTag(Integer.parseInt(newCounterData[2]));
        newCounter.setTemperatureTag(Integer.parseInt(newCounterData[3]));
        newCounter.setDensityTag(Integer.parseInt(newCounterData[4]));
        try {
            newCounterAction.create(newCounter);
            countersModel.addRow(newCounterData);
            countersList.add(newCounter);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }//GEN-LAST:event_btnNewCounterActionPerformed

    private void btnDelCounterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelCounterActionPerformed
        int delRow = jTable1.getSelectedRow();
        if (delRow!=-1){
            CountersInitDataJpaController delCounter = new CountersInitDataJpaController(emf);
            try {
                delCounter.destroy(countersList.get(delRow).getId());
                countersModel.removeRow(delRow);
                countersList.remove(delRow);
            } catch (NonexistentEntityException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }//GEN-LAST:event_btnDelCounterActionPerformed

    void load() {
        initSensorSettings();        
    }

    void store() {
        NbPreferences.forModule(this.getClass()).putDouble("Dummy", Math.random());
    }

    private void editTankFields(int row, int column){
        TanksToAccount editedTank = tanks.get(row);              
                switch (column) {
                    case 0:{
                        editedTank.setTankId((int)tankDic.get(tankSelect.getSelectedIndex()).getTankId());
                        break;
                    }
                    case 1:{
                        editedTank.setSensorSerial(jTable2.getValueAt(row, column).toString());
                        break;
                    }
                    case 2:{
                        editedTank.setLevelTag(tags.get(levelTagSelect.getSelectedIndex()).getId());                        
                        break;
                    }
                    case 3:{
                        editedTank.setTemperatureTag(tags.get(temperatureTagSelect.getSelectedIndex()).getId());
                        break;
                    }
                    case 4:{
                        editedTank.setDensityTag(tags.get(densityTagSelect.getSelectedIndex()).getId());
                        break;
                    }
                }
                
                try {
                    TanksToAccountJpaController edited = new TanksToAccountJpaController(emf);
                    edited.edit(editedTank);
                    //controller.applyChanges();
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
    }
    
    private void editCountersData(int row, int column){
        CountersInitData editedCounter = countersList.get(row);
        switch (column){
            case 0:{
                editedCounter.setSensorSerial(jTable1.getValueAt(row, column).toString());
                break;
            }
            case 1:{
                editedCounter.setMassTag(tags.get(massTagSelect.getSelectedIndex()).getId());
                break;
            }
            case 2:{
                editedCounter.setVolumeTag(tags.get(volumeTagSelect.getSelectedIndex()).getId());
                break;
            }
            case 3:{
                editedCounter.setTemperatureTag(tags.get(temperatureTagSelect.getSelectedIndex()).getId());
                break;
            }
            case 4:{
                editedCounter.setDensityTag(tags.get(densityTagSelect.getSelectedIndex()).getId());
                break;
            }
        }
        CountersInitDataJpaController edited = new CountersInitDataJpaController(emf);
        try {
            edited.edit(editedCounter);
            //controller.applyChanges();
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    boolean valid() {
        // TODO check whether form is consistent and complete
        return true;
    }

    private int getNewTankId(EntityManager em) {        
        int id;
        Query query = em.createQuery("SELECT MAX(a.id) FROM TanksToAccount a");
        Object maxId = query.getSingleResult();
        if (maxId!=null){
            id = (int)maxId+1;
        }else{
            id=1;
        }
        return id;
    }
    
    private int getNewCounterId(EntityManager em) {        
        int id;
        Query query = em.createQuery("SELECT MAX(a.id) FROM CountersInitData a");
        Object maxId = query.getSingleResult();
        if (maxId!=null){
            id = (int)maxId+1;
        }else{
            id=1;
        }
        return id;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelCounter;
    private javax.swing.JButton btnDelTank;
    private javax.swing.JButton btnNewCounter;
    private javax.swing.JButton btnNewTank;
    private javax.swing.JButton btnUpdateReferences;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
