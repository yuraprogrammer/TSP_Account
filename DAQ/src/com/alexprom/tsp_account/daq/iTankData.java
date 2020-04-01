package com.alexprom.tsp_account.daq;

import java.math.BigDecimal;
import remoteagent.lib.PlcTag;
import remoteagent.lib.SimaticAddressParser;

public interface iTankData {
    void setTankId(int id);
    int getTankId();
    void setTankLevel(BigDecimal value);
    BigDecimal getTankLevel();
    void setTankVolume(BigDecimal value);
    BigDecimal getTabkVolume();
    void setTankTemperature(BigDecimal value);
    BigDecimal getTankTemperature();
    void setTankDensity(BigDecimal value);
    BigDecimal getTankDensity();
    void setDataDate(String value);
    String getDataDate();
    void setDataTime(String value);
    String getDataTime();
    void setLevelTag(PlcTag tag);
    PlcTag getLevelTag();
    void setTemperatureTag(PlcTag tag);
    PlcTag getTemperatureTag();
    void setDensityTag(PlcTag tag);
    PlcTag getDensityTag();
    void setTankSerial(String serial);
    String getTankSerial();
    void setLevelS7(SimaticAddressParser attr);
    SimaticAddressParser getLevelS7();
    void setTemperatureS7(SimaticAddressParser attr);
    SimaticAddressParser getTemperatureS7();
    void setDensityS7(SimaticAddressParser attr);
    SimaticAddressParser getDensityS7();
}
