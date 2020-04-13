/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.daq;

import com.alexprom.tsp_account.report_db.CountersInitData;
import com.alexprom.tsp_account.report_db.GlobalEntityManager;
import com.alexprom.tsp_account.report_db.GradView;
import com.alexprom.tsp_account.report_db.SystemTags;
import com.alexprom.tsp_account.report_db.TanksToAccount;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;
import org.xml.sax.SAXException;
import remoteagent.lib.PlcTag;
import remoteagent.lib.SimaticAddressParser;

/**
 *
 * @author yura_
 */
public final class DAQ_Thread implements Runnable{
    private final Thread thread;
    private PlcTag[] tag;
    private final GlobalEntityManager gem = new GlobalEntityManager();
    private final EntityManager em = gem.getEm();
    public TankData tankData[];
    public CounterData counterData[];
    private List<TanksToAccount> tanks;
    private List<CountersInitData> counters;
    private JPlcAgent plc;    
    
    public DAQ_Thread(JPlcAgent plc){
        this.plc=plc;
        try {
            getTask();
        } catch (SAXException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
    
    public TankData[] getTankData() {
        return tankData;
    }

    public CounterData[] getCounterData() {
        return counterData;
    }
    
    public JPlcAgent getPlc() {
        return plc;
    }

    public void setPlc(JPlcAgent plc) {
        this.plc = plc;
    }
    private Double oldDensity_32=0.0, newDensity_32=0.0, oldTemperature_32=0.0, newTemperature_32=0.0;
        
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
                tag[i].setPlc(plc);
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
    }
    
    public void doTask(){       
        
        Query cnt = em.createNamedQuery("SystemTags.findAll");
        List<SystemTags> tagsCnt = cnt.getResultList();
        int count = tagsCnt.size();
        if (count!=0){
            if (plc.Connected){
            for (TankData  accountTanks : tankData){                
                accountTanks.setTankLevel(BigDecimal.valueOf(accountTanks.getLevelTag().getPlc().readFloatData(accountTanks.getLevelS7().getDbNum(), 
                        accountTanks.getLevelS7().getStartAddress(), 
                        accountTanks.getLevelS7().getByteCnt())));
                
                    accountTanks.setTankTemperature(BigDecimal.valueOf(accountTanks.getTemperatureTag().getPlc().readFloatData(accountTanks.getTemperatureS7().getDbNum(), 
                            accountTanks.getTemperatureS7().getStartAddress(), accountTanks.getTemperatureS7().getByteCnt())));
                    accountTanks.setTankDensity(BigDecimal.valueOf(accountTanks.getDensityTag().getPlc().readFloatData(accountTanks.getDensityS7().getDbNum(), 
                            accountTanks.getDensityS7().getStartAddress(), 
                            accountTanks.getDensityS7().getByteCnt())));
                
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
                accountCounters.setCounterMass(BigDecimal.valueOf(accountCounters.getMassTag().getPlc().readFloatData(accountCounters.getMassSimaticAttr().getDbNum(), 
                        accountCounters.getMassSimaticAttr().getStartAddress(), 
                        accountCounters.getMassSimaticAttr().getByteCnt())));
                accountCounters.setCounterVolume(BigDecimal.valueOf(accountCounters.getVolumeTag().getPlc().readFloatData(accountCounters.getVolumeSimaticAttr().getDbNum(), 
                        accountCounters.getVolumeSimaticAttr().getStartAddress(), 
                        accountCounters.getVolumeSimaticAttr().getByteCnt())));
                accountCounters.setCounterTemperature(BigDecimal.valueOf(accountCounters.getTemperatureTag().getPlc().readFloatData(accountCounters.getTemperatureSimaticAttr().getDbNum(), 
                        accountCounters.getTemperatureSimaticAttr().getStartAddress(), 
                        accountCounters.getTemperatureSimaticAttr().getByteCnt())));
                accountCounters.setCounterDensity(BigDecimal.valueOf(accountCounters.getDensityTag().getPlc().readFloatData(accountCounters.getDensitySimaticAttr().getDbNum(), 
                        accountCounters.getDensitySimaticAttr().getStartAddress(), 
                        accountCounters.getDensitySimaticAttr().getByteCnt())));
            }
                        
            }            
            
        }
        
    }
    @Override
    public void run() {
        while (true){
            doTask();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
    
}
