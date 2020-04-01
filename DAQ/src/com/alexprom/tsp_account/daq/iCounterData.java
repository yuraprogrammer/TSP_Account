/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.daq;

import java.math.BigDecimal;
import remoteagent.lib.PlcTag;
import remoteagent.lib.SimaticAddressParser;

/**
 *
 * @author yura_
 */
public interface iCounterData {
    void setCounterId(int id);
    int getCounterId();
    void setCounterMass(BigDecimal value);
    BigDecimal getCounterMass();
    void setCounterVolume(BigDecimal value);
    BigDecimal getCounterVolume();
    void setCounterTemperature(BigDecimal value);
    BigDecimal getCounterTemperature();
    void setCounterDensity(BigDecimal value);
    BigDecimal getCounterDensity();
    void setDataDate(String value);
    String getDataDate();
    void setDataTime(String value);
    String getDataTime();
    void setMassTag(PlcTag tag);
    PlcTag getMassTag();
    void setVolumeTag(PlcTag tag);
    PlcTag getVolumeTag();
    void setTemperatureTag(PlcTag tag);
    PlcTag getTemperatureTag();
    void setDensityRag(PlcTag tag);
    PlcTag getDensityTag();
    void setSensorSerial(String serial);    
    String getSensorSerial();
    void setMassSimaticAttr(SimaticAddressParser attr);
    SimaticAddressParser getMassSimaticAttr();
    void setVolumeSimaticAttr(SimaticAddressParser attr);
    SimaticAddressParser getVolumeSimaticAttr();
    void setTemperatureSimaticAttr(SimaticAddressParser attr);
    SimaticAddressParser getTemperatureSimaticAttr();
    void setDensitySimaticAttr(SimaticAddressParser attr);
    SimaticAddressParser getDensitySimaticAttr();
}
