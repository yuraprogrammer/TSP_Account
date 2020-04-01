package com.alexprom.tsp_account.daq;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Logger;
import org.libnodave.Nodave;
import org.libnodave.PLCinterface;
import org.libnodave.TCPConnection;
import remoteagent.lib.ProcessA;

public class JPlcAgent{
    private InputStream plcInputStream = null;
    private OutputStream plcOutputStream = null;
    private Socket sock;
    private PLCinterface di;
    public TCPConnection dc;
    private Connection dataBase;
    private Properties sysProp;
    private String host = "localhost";
    public boolean Connected = false;
    private int LifeCycle = 0;    
    private int cycleTime = 1000;
    private ProcessA levelE38, levelE39, levelE40, volE38, volE39, volE40;
    private double maxLevel = 0.0;
    private Logger agentLog;
    int increment, increment_old = 0;
    private String plcName; 
    private int iFace = 1;
    
    public JPlcAgent(String plcHost, String name, int iFace){
        setiFace(iFace);
        setPlcName(name);
        createSocket(plcHost);
        setInterface(plcInputStream, plcOutputStream, this.iFace);
        if (di.initAdapter()==0){
            System.out.println("PLC initialization complete");            
        }
        setConnection(di);
        try{
            int res = dc.connectPLC();
            Connected = res==0;
            if (Connected){
                System.out.println("Connect to PLC success");                
            }
        }catch(java.lang.NullPointerException ex){
            System.out.println("PLC connection not established");            
        }        
    }
           
    private void createSocket(String host){
        try {
            sock = new Socket(host, 102);
            plcInputStream = sock.getInputStream();
            plcOutputStream = sock.getOutputStream();
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }catch (java.lang.NullPointerException eee){
            
        }
        
    }

    public int getiFace() {
        return iFace;
    }

    public void setiFace(int iFace) {
        this.iFace = iFace;
    }
    
    
    private void setInterface(InputStream is, OutputStream os, int iFace){
        di = new PLCinterface(os,is,"IF".concat(String.valueOf(iFace)),2,Nodave.PROTOCOL_ISOTCP);
    }
    
    private void setConnection(PLCinterface i){
        dc = new TCPConnection(i, 0, 1);
    }
    
    private int tryReconnect() throws java.lang.NullPointerException, Exception{
        int res=-1;
        
            dc.disconnectPLC();
            di.disconnectAdapter();
            sock.close();
            createSocket(host);
            setInterface(plcInputStream, plcOutputStream, this.iFace);
        if (di.initAdapter()==0){
            System.out.println("PLC initialization complete");            
        }
        setConnection(di);
        
            res = dc.connectPLC();
            Connected = res==0;
            if (Connected){
                System.out.println("Connect to PLC success");                
            }else{
                System.out.println("PLC connection not established");            
            }
        return res;
    }
    
    public ProcessA readDBData(int numDB, int startOffset, int cnt){
        int res=-1;
        ProcessA plcVal = new ProcessA();
        byte[] by;
        
        res = dc.readBytes(Nodave.DB, numDB, startOffset, cnt, null);
        plcVal.setValue(dc.getFloat(0));
        plcVal.setFault(res!=0);
        return plcVal;
    }                
    
    public float readFloatData(int numDB, int startOffset, int cnt){
        int res=-1;
        float plcVal;
        byte[] by;
        
        res = dc.readBytes(Nodave.DB, numDB, startOffset, cnt, null);
        plcVal = dc.getFloat(0);
        
        return plcVal;
    }
    
    public boolean readBooleanData(int numDB, int startOffset, int bit){
        boolean result=false;
        byte[] by;
        int res = dc.readBytes(Nodave.DB, numDB, startOffset, 1, null);
        byte[] bytes = new byte[1];
        bytes[0] = (byte)dc.getBYTE();
        boolean[] bits=new boolean[8];
        for (int i=0; i<8; i++){
            if ((bytes[i / 8] & (1 << (7 - (i % 8)))) > 0){
                bits[i] = true;
                if ((7-i)==bit){
                    result=true;
                }
            }
        }
        return result;
    }
    
    public byte readIntData(int area, int numDB, int startOffset, int cnt){
        int res=-1;
        byte[] by;
        res = dc.readBytes(area, numDB, startOffset, cnt, null);
        
        return (byte) dc.getBYTE();
    }
    
    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName;
    }
        
}
