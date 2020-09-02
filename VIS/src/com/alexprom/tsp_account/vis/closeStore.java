/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.vis;

import com.alexprom.tsp_account.report_db.CountersInitData;
import com.alexprom.tsp_account.report_db.GlobalEntityManager;
import com.alexprom.tsp_account.report_db.TSPReport;
import com.alexprom.tsp_account.report_db.TanksToAccount;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.xml.transform.TransformerException;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Tools",
        id = "com.alexprom.tsp_account.vis.closeStore"
)
@ActionRegistration(
        iconBase = "com/alexprom/tsp_account/vis/down.png",
        displayName = "#CTL_closeStore"
)
@ActionReference(path = "Menu/File", position = 1450)
@Messages("CTL_closeStore=Закрыть склад")
public final class closeStore implements ActionListener {
    
    private boolean checkExists(String storeDate, EntityManager em){
        boolean exists=false;
        Query query=em.createNamedQuery("TSPReport.findByDaqDate");
        query.setParameter("daqDate", storeDate);
        List<TSPReport> list;
        list = query.getResultList();
        int stored = list.size();
        query = em.createNamedQuery("TanksToAccount.findAll");
        List<TanksToAccount> tanks = query.getResultList();
        query = em.createNamedQuery("CountersInitData.findAll");
        List<CountersInitData> counters = query.getResultList();
        int toStore = (tanks.size()+counters.size())*2;
        if (stored==toStore){exists = true;}
        return exists;
    }        
    
    private boolean checkTime(EntityManager em, String today){
        boolean checked=false;
        Date createDate = new Date();
        DateFormat tf = new SimpleDateFormat("hh:mm");
        String timeString = tf.format(createDate);
        Query query = em.createQuery("select t from TSPReport t where t.daqDate like '"+today+"%' and t.daqTime like '"+timeString+"%'");
        List<TSPReport> list = query.getResultList();
        if (list.isEmpty()){checked=true;}
        return checked;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        GlobalEntityManager gem = new GlobalEntityManager();
        EntityManager em = gem.getEm();
        EntityManagerFactory emf = gem.getEmf();
        Date createDate = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");        
        String createString = df.format(createDate);        
        if (checkExists(createString, em)){
            NotifyDescriptor notFinished = new NotifyDescriptor.Message("На сегодняшний день склад уже закрыт!!!", NotifyDescriptor.WARNING_MESSAGE);
            Object resultNotFinished = DialogDisplayer.getDefault().notify(notFinished);
        }else{
            if (checkTime(em, createString)){
                NotifyDescriptor create = new NotifyDescriptor.Confirmation("На сегодняшний день склад не закрыт!!! Закрыть склад?", "ТСП");
                Object createResult = DialogDisplayer.getDefault().notify(create);
                if (createResult==NotifyDescriptor.YES_OPTION){
                    saveData newData = new saveData(emf, em);
                    if (newData.saved){
                        NotifyDescriptor done = new NotifyDescriptor.Confirmation("Данные о состоянии ТСП на момент закрытия склада успешно сохранены!!!");
                        Object resultDone = DialogDisplayer.getDefault().notify(done);
                        try {
                            createXML xmlReport;                            
                            xmlReport = new createXML(em, createDate);
                        } catch (TransformerException ex) {
                            NotifyDescriptor xmlDone = new NotifyDescriptor.Confirmation(ex.getMessageAndLocation());
                            Object resultXML = DialogDisplayer.getDefault().notify(xmlDone);                            
                        }
                    } else {
                        NotifyDescriptor notSaved = new NotifyDescriptor.Message("Данные о состоянии ТСП на момент закрытия склада не сохранены!!!", NotifyDescriptor.WARNING_MESSAGE);
                        Object resultNotSaved = DialogDisplayer.getDefault().notify(notSaved);
                    }
                }
            }else{
                NotifyDescriptor errorTime = new NotifyDescriptor.Message("Время закрытия и открытия склада не должно совпадать! Данные не сохранены!", NotifyDescriptor.WARNING_MESSAGE);
                Object resultErrorTime = DialogDisplayer.getDefault().notify(errorTime);
            }
        }
    }
        
}
