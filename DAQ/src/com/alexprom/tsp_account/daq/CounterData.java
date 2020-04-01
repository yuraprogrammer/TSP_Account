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
public final class CounterData implements iCounterData{

    private int counterId=100;
    private BigDecimal mass=BigDecimal.ZERO, volume=BigDecimal.ZERO, temperature=BigDecimal.ZERO, density=BigDecimal.ZERO;
    private String dataDate, dataTime, sensorSerial;
    private PlcTag massTag, volumeTag, temperatureTag, densityTag;
    private SimaticAddressParser massS7, volumeS7, temperatureS7, densityS7;
    public CounterData(){
        setCounterId(counterId);
        setCounterVolume(volume);
        setCounterMass(mass);
        setCounterTemperature(temperature);
        setCounterDensity(density);
        setDataDate(dataDate);
        setDataTime(dataTime);
    }
    
    @Override
    public void setCounterId(int id) {
        counterId=id;
    }

    @Override
    public int getCounterId() {
        return counterId;
    }

    @Override
    public void setCounterMass(BigDecimal value) {
        mass = value;
    }

    @Override
    public BigDecimal getCounterMass() {
        return mass;
    }

    @Override
    public void setCounterVolume(BigDecimal value) {
        volume = value;
    }

    @Override
    public BigDecimal getCounterVolume() {
        return volume;
    }

    @Override
    public void setCounterTemperature(BigDecimal value) {
        temperature = value;
    }

    @Override
    public BigDecimal getCounterTemperature() {
        return temperature;
    }

    @Override
    public void setCounterDensity(BigDecimal value) {
        density = value;
    }

    @Override
    public BigDecimal getCounterDensity() {
        return density;
    }

    @Override
    public void setDataDate(String value) {
        dataDate = value;
    }

    @Override
    public String getDataDate() {
        return dataDate;
    }

    @Override
    public void setDataTime(String value) {
        dataTime = value;
    }

    @Override
    public String getDataTime() {
        return dataTime;
    }
    
    @Override
    public void setSensorSerial(String serial) {
        sensorSerial = serial;
    }

    @Override
    public String getSensorSerial() {
        return sensorSerial;
    }

    @Override
    public void setMassTag(PlcTag tag) {
        massTag = tag;
    }

    @Override
    public void setVolumeTag(PlcTag tag) {
        volumeTag = tag;
    }

    @Override
    public void setTemperatureTag(PlcTag tag) {
        temperatureTag = tag;
    }

    @Override
    public void setDensityRag(PlcTag tag) {
        densityTag = tag;
    }

    @Override
    public void setMassSimaticAttr(SimaticAddressParser attr) {
        massS7 = attr;
    }

    @Override
    public SimaticAddressParser getMassSimaticAttr() {
        return massS7;
    }

    @Override
    public void setVolumeSimaticAttr(SimaticAddressParser attr) {
        volumeS7 = attr;
    }

    @Override
    public SimaticAddressParser getVolumeSimaticAttr() {
        return volumeS7;
    }

    @Override
    public void setTemperatureSimaticAttr(SimaticAddressParser attr) {
        temperatureS7 = attr;
    }

    @Override
    public SimaticAddressParser getTemperatureSimaticAttr() {
        return temperatureS7;
    }

    @Override
    public void setDensitySimaticAttr(SimaticAddressParser attr) {
        densityS7 = attr;
    }

    @Override
    public SimaticAddressParser getDensitySimaticAttr() {
        return densityS7;
    }

    @Override
    public PlcTag getMassTag() {
        return massTag;
    }

    @Override
    public PlcTag getVolumeTag() {
        return volumeTag;
    }

    @Override
    public PlcTag getTemperatureTag() {
        return temperatureTag;
    }

    @Override
    public PlcTag getDensityTag() {
        return densityTag;
    }
    
}
