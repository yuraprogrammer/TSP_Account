/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.vis;

import com.alexprom.tsp_account.report_db.GlobalEntityManager;
import com.alexprom.tsp_account.report_db.TankDic;
import com.alexprom.tsp_account.report_db.TanksToAccount;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.Action;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;

import org.openide.util.lookup.Lookups;

/**
 *
 * @author yura_
 */
public class tankRootNode extends AbstractNode{
    
    /*public tankRootNode(Children children, Lookup lookup) {
        super(children, lookup);
    }*/
    private final TanksToAccount currentTank;
    private String title;
    public tankRootNode(TanksToAccount tank, String key){
        super(Children.LEAF, Lookups.singleton(tank));        
        this.currentTank=tank;
        this.title=key;
        setDisplayName(key);
    }
    
    @Override
    public Action getPreferredAction() {
        return new tankNodeAction(currentTank, title);
    } 
    
    @Override
    public Image getIcon(int type){
        return ImageUtilities.loadImage("com/alexprom/tsp_account/vis/rvs_ico.png");
    }
    
    @Override
    protected Sheet createSheet(){
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        TanksToAccount ttt = getLookup().lookup(TanksToAccount.class);
        GlobalEntityManager gem = new GlobalEntityManager();
        EntityManager em = gem.getEm();
        Query query = em.createNamedQuery("TankDic.findByTankId");
        query.setParameter("tankId", ttt.getTankId());
        List<TankDic> tankProperties = query.getResultList();
        Sheet.Set set2 = Sheet.createPropertiesSet();
        set2.setDisplayName("Размеры");
        set2.setName("Dimensions");
        try {
                            
                Property serialProp = new PropertySupport.Reflection(ttt, String.class, "getSensorSerial", null);
                Property indexProp = new PropertySupport.Reflection(tankProperties.get(0), String.class, "getTankName", null);                
                Property insideDiameter = new PropertySupport.Reflection(tankProperties.get(0), Integer.class, "getInsideDiameter", null);
                Property cylinderLength = new PropertySupport.Reflection(tankProperties.get(0), Integer.class, "getCylinderLength", null);
                Property conusHeigth = new PropertySupport.Reflection(tankProperties.get(0), Integer.class, "getConusHeight", null);
                Property bandCount = new PropertySupport.Reflection(tankProperties.get(0), Short.class, "getBandCount", null);
                
                indexProp.setDisplayName("Идентификатор");
                serialProp.setDisplayName("Серийный номер");
                insideDiameter.setDisplayName("Внутренний диаметр");
                cylinderLength.setDisplayName("Длина цилиндра");
                conusHeigth.setDisplayName("Высота конуса");
                bandCount.setDisplayName("Количество поясов");
                
                
                set.put(indexProp);
                set.put(serialProp);
                set2.put(insideDiameter);
                set2.put(cylinderLength);
                set2.put(conusHeigth);
                set2.put(bandCount);
                set2.setValue("tabName", "Размеры");
    } catch (NoSuchMethodException ex) {
        ErrorManager.getDefault();
    }
        
        sheet.put(set);
        sheet.put(set2);
        return sheet;
    }
    
    public class tankNodeAction implements Action{
        private String title;
        private TanksToAccount selTank;

        public TanksToAccount getSelTank() {
            return selTank;
        }

        public void setSelTank(TanksToAccount selTank) {
            this.selTank = selTank;
        }
        
        public tankNodeAction(){
            super();
        }
        
        public tankNodeAction(String value){
            super();
            title=value;
        }
                
        public tankNodeAction(TanksToAccount tank, String value){
            super();
            selTank=tank;
            title=value;
        }
        
        public void setTitle(String value){
            title=value;
        }
        
        @Override
        public Object getValue(String key) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void putValue(String key, Object value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setEnabled(boolean b) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            tankVisData tc = new tankVisData();
            tc.setTankDataObject(selTank);
            tc.setDisplayName(title);            
            tc.open();
            tc.requestActive();
        }        
}
}

