/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.vis;

import com.alexprom.tsp_account.report_db.CountersInitData;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author yura_
 */
public class countersRootNode extends AbstractNode{
    private CountersInitData counter;
    private String title;
    
    public countersRootNode(CountersInitData counter, String key) {
        super(Children.LEAF, Lookups.singleton(counter));
        this.counter=counter;
        this.title=key;
        setDisplayName(key);
    }
    
    @Override
    public Action getPreferredAction() {
        return new countersRootNode.counterNodeAction(counter, title);
    } 
    
    @Override
    public Image getIcon(int type){
        return ImageUtilities.loadImage("com/alexprom/tsp_account/vis/counter_ico.png");
    }
    
    public class counterNodeAction implements Action{
        private String title;
        private CountersInitData selCounter;
        
        public CountersInitData getSelCounter() {
            return selCounter;
        }

        public void setSelCounter(CountersInitData selCounter) {
            this.selCounter = selCounter;
        }
        
        public counterNodeAction(){
            super();
        }
        
        public counterNodeAction(String value){
            super();
            title=value;
        }
                
        public counterNodeAction(CountersInitData counter, String value){
            super();
            selCounter=counter;
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
            counterVisData tc = new counterVisData();
            tc.setCounterDataObject(selCounter);
            tc.setDisplayName(title);            
            tc.open();
            tc.requestActive();
        }
        
    }
}
