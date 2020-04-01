/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.vis;

import com.alexprom.tsp_account.report_db.TSPReport;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.openide.*;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

@ActionID(
        category = "Tools",
        id = "com.alexprom.tsp_account.vis.openStore"
)
@ActionRegistration(
        iconBase = "com/alexprom/tsp_account/vis/up.png",
        displayName = "#CTL_openStore"
)
@ActionReference(path = "Menu/File", position = 1300)
@Messages("CTL_openStore=Открыть склад")
public final class openStore implements ActionListener {    
    
    private boolean checkExists(String storeDate, EntityManager em){
        boolean exists=false;
        Query query=em.createNamedQuery("TSPReport.findByDaqDate");
        query.setParameter("daqDate", storeDate);
        List<TSPReport> list;
        list = query.getResultList();
        if (!list.isEmpty()){exists = true;}
        return exists;
    }        
    
    @Override
    public void actionPerformed(ActionEvent e) {
        TankDataTopComponent tdtc = (TankDataTopComponent)WindowManager.getDefault().findTopComponent("TankDataTopComponent");
        EntityManager em = tdtc.em;
        EntityManagerFactory emf = tdtc.emf;
        
        Date createDate = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");        
        String createString = df.format(createDate);        
        if (checkExists(createString, em)){
            NotifyDescriptor notFinished = new NotifyDescriptor.Message("На сегодняшний день склад уже открыт!!!", NotifyDescriptor.WARNING_MESSAGE);
            Object resultNotFinished = DialogDisplayer.getDefault().notify(notFinished);
        }else{
            NotifyDescriptor create = new NotifyDescriptor.Confirmation("На сегодняшний день склад не открыт!!! Открыть склад?", "ТСП");
            Object createResult = DialogDisplayer.getDefault().notify(create);
            if (createResult==NotifyDescriptor.YES_OPTION){
                saveData newData = new saveData(emf, em);
                if (newData.saved){
                    NotifyDescriptor done = new NotifyDescriptor.Confirmation("Данные о состоянии ТСП на момент открытия склада успешно сохранены!!!");
                    Object resultDone = DialogDisplayer.getDefault().notify(done);
                } else {
                    NotifyDescriptor notSaved = new NotifyDescriptor.Message("Данные о состоянии ТСП на момент открытия склада не сохранены!!!", NotifyDescriptor.WARNING_MESSAGE);
                    Object resultNotSaved = DialogDisplayer.getDefault().notify(notSaved);
                }
            }
        }
    }
        
}
