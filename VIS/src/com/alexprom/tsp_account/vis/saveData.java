/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.vis;

import com.alexprom.tsp_account.daq.CounterData;
import com.alexprom.tsp_account.daq.DAQ_Thread;
import com.alexprom.tsp_account.daq.DataLoggerTopComponent;
import com.alexprom.tsp_account.daq.TankData;
import com.alexprom.tsp_account.report_actions.TSPReportJpaController;
import com.alexprom.tsp_account.report_db.TSPReport;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;

/**
 *
 * @author yura_
 */
public final class saveData {
    
    private Long reportId;
    public boolean saved=false;
    
    public saveData(EntityManagerFactory emf, EntityManager em){
        
        DataLoggerTopComponent tdtc = (DataLoggerTopComponent)WindowManager.getDefault().findTopComponent("DataLoggerTopComponent");
        //DAQ_Thread thread = new DAQ_Thread(tdtc.getPlc());
        Date createDate = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat tf = new SimpleDateFormat("hh:mm");
        String createString = df.format(createDate);
        String timeString = tf.format(createDate);
        try {
            saved=false;
            em.getTransaction().begin();
            for (TankData storeData : tdtc.getTankData()){                     
                reportId = getNewActId(em);
                TSPReport openStoreAct = new TSPReport();
                TSPReportJpaController openStoreNew = new TSPReportJpaController(emf);
                openStoreAct.setId(reportId);
                openStoreAct.setTankID(storeData.getTankId());
                openStoreAct.setDaqDate(createString);
                openStoreAct.setDaqTime(timeString);
                openStoreAct.setTLevel(storeData.getTankLevel());
                openStoreAct.setTVolume(storeData.getTabkVolume());
                openStoreAct.setTDensity(storeData.getTankDensity());
                openStoreAct.setTTemper(storeData.getTankTemperature());
                openStoreNew.create(openStoreAct);
            }
            for (CounterData  storeData : tdtc.getCounterData()){
                reportId = getNewActId(em);
                TSPReport openStoreAct = new TSPReport();
                TSPReportJpaController openStoreNew = new TSPReportJpaController(emf);
                openStoreAct.setId(reportId);
                openStoreAct.setTankID(storeData.getCounterId());
                openStoreAct.setDaqDate(createString);
                openStoreAct.setDaqTime(timeString);
                openStoreAct.setTLevel(storeData.getCounterMass());
                openStoreAct.setTVolume(storeData.getCounterVolume());
                if (storeData.getCounterVolume().doubleValue()!=0.0){
                    //openStoreAct.setTDensity(storeData.getCounterMass().divide(storeData.getCounterVolume(), 3, RoundingMode.CEILING));
                    openStoreAct.setTDensity(storeData.getCounterDensity());
                }else{
                    openStoreAct.setTDensity(BigDecimal.ONE);
                }
                openStoreAct.setTTemper(storeData.getCounterTemperature());
                openStoreNew.create(openStoreAct);
            }
            em.getTransaction().commit();            
            saved=true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            Exceptions.printStackTrace(ex);
        }
    }
    
    private Long getNewActId(EntityManager em) {        
        Long id;
        Query query = em.createQuery("SELECT MAX(a.id) FROM TSPReport a");
        Object maxId = query.getSingleResult();
        if (maxId!=null){
            id = (Long)maxId+1;
        }else{
            id=null;
        }
        return id;
    }
        
}
