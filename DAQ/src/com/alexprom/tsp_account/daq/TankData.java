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
public final class TankData implements iTankData{
    private int tankId=1;
    private BigDecimal level=BigDecimal.ZERO, volume=BigDecimal.ZERO, temperature=BigDecimal.ZERO, density=BigDecimal.ZERO;
    private String dataDate, dataTime;
    private PlcTag levelTag, temperatureTag, densityTag;
    private String serialNum;
    private SimaticAddressParser levelS7, temperatureS7, densityS7;
    
    public TankData(){
        setTankId(tankId);
        setTankLevel(level);
        setTankVolume(volume);
        setTankTemperature(temperature);
        setTankDensity(density);
    }
    @Override
    public void setTankId(int id) {
        tankId=id;
    }

    @Override
    public int getTankId() {
        return tankId;
    }

    @Override
    public void setTankLevel(BigDecimal value) {
        level=value;
    }

    @Override
    public BigDecimal getTankLevel() {
        return level;
    }

    @Override
    public void setTankVolume(BigDecimal value) {
        volume=value;
    }

    @Override
    public BigDecimal getTabkVolume() {
        return volume;
    }

    @Override
    public void setTankTemperature(BigDecimal value) {
        temperature=value;
    }

    @Override
    public BigDecimal getTankTemperature() {
        return temperature;
    }

    @Override
    public void setTankDensity(BigDecimal value) {
        density=value;
    }

    @Override
    public BigDecimal getTankDensity() {
        return density;
    }

    @Override
    public void setDataDate(String value) {
        dataDate=value;
    }

    @Override
    public String getDataDate() {
        return dataDate;
    }

    @Override
    public void setDataTime(String value) {
        dataTime=value;
    }

    @Override
    public String getDataTime() {
        return dataTime;
    }
    
    @Override
    public String getTankSerial() {
        return serialNum;
    }

    @Override
    public void setLevelTag(PlcTag tag) {
        levelTag = tag;
    }

    @Override
    public void setTemperatureTag(PlcTag tag) {
        temperatureTag = tag;
    }

    @Override
    public void setDensityTag(PlcTag tag) {
        densityTag = tag;
    }

    @Override
    public PlcTag getLevelTag() {
        return levelTag;
    }

    @Override
    public PlcTag getTemperatureTag() {
        return temperatureTag;
    }

    @Override
    public PlcTag getDensityTag() {
        return densityTag;
    }

    @Override
    public void setTankSerial(String serial) {
        serialNum = serial;
    }

    @Override
    public void setLevelS7(SimaticAddressParser attr) {
        levelS7 = attr;
    }

    @Override
    public SimaticAddressParser getLevelS7() {
        return levelS7;
    }

    @Override
    public void setTemperatureS7(SimaticAddressParser attr) {
        temperatureS7 = attr;
    }

    @Override
    public SimaticAddressParser getTemperatureS7() {
        return temperatureS7;        
    }

    @Override
    public void setDensityS7(SimaticAddressParser attr) {
        densityS7 = attr;
    }

    @Override
    public SimaticAddressParser getDensityS7() {
        return densityS7;
    }
    
}
