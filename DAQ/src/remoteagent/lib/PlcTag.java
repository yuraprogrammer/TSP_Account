package remoteagent.lib;

import com.alexprom.tsp_account.daq.JPlcAgent;

/**
 *
 * @author yura_
 */
public final class PlcTag {
    private String tagName = "NewTag";
    private String plcName = "PLC_1";
    private String S7Addr = "DB1.DBB0";
    private double minValue = 0.0;
    private double maxValue = 100000.0;
    private ProcessA currentValue = new ProcessA();
    private int tagId;
    private JPlcAgent plc;

    public JPlcAgent getPlc() {
        return plc;
    }

    public void setPlc(JPlcAgent plc) {
        this.plc = plc;
    }
    
    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
    
    public PlcTag(){
        
    }
    
    public PlcTag(String tagName, String plcName, String S7Addr, JPlcAgent plcAgent){
        setTagName(tagName);
        setPlcName(plcName);
        setS7Addr(S7Addr);
        setPlc(plcAgent);
    }            
    
    public String getTagName() {
        return tagName;
    }

    public final void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getPlcName() {
        return plcName;
    }

    public final void setPlcName(String plcName) {
        this.plcName = plcName;
    }

    public String getS7Addr() {
        return S7Addr;
    }

    public final void setS7Addr(String S7Addr) {
        this.S7Addr = S7Addr;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }    

    public ProcessA getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(ProcessA currentValue) {
        this.currentValue = currentValue;
    }
    
}
