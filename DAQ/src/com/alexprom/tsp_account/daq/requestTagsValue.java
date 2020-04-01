/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.daq;

import java.math.BigDecimal;
import org.openide.util.Exceptions;

/**
 *
 * @author yura_
 */
public class requestTagsValue implements Runnable{
    public final TankData[] tankData = new TankData[5];
    
    public requestTagsValue(){
        super();
    }

    @Override
    public void run() {
        while (true){
            for (int i=0; i<5; i++){
                tankData[i].setTankId((int) Math.rint(Math.random()));
                tankData[i].setTankLevel(BigDecimal.valueOf(Math.random()));
                tankData[i].setTankVolume(BigDecimal.valueOf(Math.random()));
                tankData[i].setTankTemperature(BigDecimal.valueOf(Math.random()));
                tankData[i].setTankDensity(BigDecimal.valueOf(Math.random()));
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
