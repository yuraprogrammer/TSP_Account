/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.vis;

import com.alexprom.tsp_account.report_db.CountersInitData;
import com.alexprom.tsp_account.report_db.TSPReport;
import com.alexprom.tsp_account.report_db.TankDic;
import com.alexprom.tsp_account.report_db.TanksToAccount;
import com.alexprom.tsp_account.report_db.VPlotn20;
import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbPreferences;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author yura_
 */
public class createXML {
    static DocumentBuilderFactory dbf;
    static DocumentBuilder db = null;
    static Document doc = null;
    static File xmlFile;
    
    public createXML(EntityManager em, Date reportDate) throws TransformerConfigurationException, TransformerException{
        //Здесь нужно сделать вычитку из свойств пути сохранения XML                                
        
        DateFormat dfSmall = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat dfLarge = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dfPer = new SimpleDateFormat("01.MM.yyyy");
        BigDecimal totalCounters = BigDecimal.ZERO;
        String rDate = dfDate.format(reportDate);
        if (checkExists(rDate, em)){
            dbf = DocumentBuilderFactory.newInstance();
        
            try {
                db = dbf.newDocumentBuilder();
            } catch (ParserConfigurationException err) {
                System.out.println("Unable create document!!!");
            }
       
            doc = db.newDocument();
            Element rootElement = doc.createElement("ZVIT");
            doc.appendChild(rootElement);
            Element transport = doc.createElement("TRANSPORT");
            rootElement.appendChild(transport);
            
            Element version = doc.createElement("VERSION");
            version.appendChild(doc.createTextNode("4.1"));
            transport.appendChild(version);
            
            Element fileDate = doc.createElement("CREATEDATE");
            fileDate.appendChild(doc.createTextNode(dfSmall.format(reportDate)));
            transport.appendChild(fileDate);
            
            Element orgElement = doc.createElement("ORG");
            rootElement.appendChild(orgElement);
            
            Element fieldElement = doc.createElement("FIELDS");
            orgElement.appendChild(fieldElement);
            
            Element edrpouElement = doc.createElement("EDRPOU");
            edrpouElement.appendChild(doc.createTextNode("32679601"));
            fieldElement.appendChild(edrpouElement);
            
            Element cardElement = doc.createElement("CARD");
            orgElement.appendChild(cardElement);                        
            
            Element fieldsElement = doc.createElement("FIELDS");
            Element perType = doc.createElement("PERTYPE");
            perType.appendChild(doc.createTextNode("0"));
            fieldsElement.appendChild(perType);
            Element perDate = doc.createElement("PERDATE");
            String PerDate = dfPer.format(reportDate);
            perDate.appendChild(doc.createTextNode(PerDate));
            fieldsElement.appendChild(perDate);
            Element notationElement = doc.createElement("NOTATION");
            fieldsElement.appendChild(notationElement);
            Element charCodeElement = doc.createElement("CHARCODE");
            charCodeElement.appendChild(doc.createTextNode("J0210401"));
            fieldsElement.appendChild(charCodeElement);
            cardElement.appendChild(fieldsElement);
                        
            Element docElement = doc.createElement("DOCUMENT");
                        
            docElement.appendChild(createDocumentRow(doc, "0", "0", "HZ", "1"));
            docElement.appendChild(createDocumentRow(doc, "0", "0", "EDRPOU", "32679601"));
            docElement.appendChild(createDocumentRow(doc, "0", "0", "NAME", "Товариство з обмеженою відповідальністю \"АЛЕКСПРОМ\""));
            docElement.appendChild(createDocumentRow(doc, "0", "0", "REL1", "1003764"));
            docElement.appendChild(createDocumentRow(doc, "0", "0", "N1", dfSmall.format(reportDate)));
            docElement.appendChild(createDocumentRow(doc, "0", "0", "N2", "08:00:01"));
            docElement.appendChild(createDocumentRow(doc, "0", "0", "N3", dfSmall.format(reportDate)));
            docElement.appendChild(createDocumentRow(doc, "0", "0", "N4", "17:00:00"));
            docElement.appendChild(createDocumentRow(doc, "0", "0", "RUKINN", "2935513415"));
            docElement.appendChild(createDocumentRow(doc, "0", "0", "RUK", "Колісник Сергій Олексійович"));
            docElement.appendChild(createDocumentRow(doc, "0", "0", "POS", "Директор"));
            docElement.appendChild(createDocumentRow(doc, "0", "1", "RECNO", "0"));                        
            
            //Данные из базы
            Query q_tanks = em.createNamedQuery("TanksToAccount.findAll");            
            List<TanksToAccount> tanks = q_tanks.getResultList();
            Query q_counters = em.createNamedQuery("CountersInitData.findAll");
            List<CountersInitData> counters = q_counters.getResultList();
            int tanksCount = tanks.size();
            int countersCount = counters.size();
            int deviceCount = tanks.size()+counters.size();
            int[] tankId = new int[deviceCount];
            String[] sensorSerial = new String[deviceCount];
            for (int i=0; i<tanksCount; i++){
                tankId[i]=tanks.get(i).getTankId();
                sensorSerial[i] = tanks.get(i).getSensorSerial();
            }
            for (int i=tanksCount; i<deviceCount; i++){
                tankId[i] = counters.get(i-tanksCount).getCounterId();
                sensorSerial[i] = counters.get(i-tanksCount).getSensorSerial();
            }
            BigDecimal startMass = BigDecimal.ZERO, endMass; 
            int j=0;
            for (int i=0; i<tankId.length; i++){
                
                //docElement.appendChild(createDocumentRow(doc, "0", "1", "TAB1_A2", "2909199020"));//Код топлива
                //docElement.appendChild(createDocumentRow(doc, "0", "1", "TAB1_A3", "Інші ефіри прості, ефіроспирти, ефірофеноли, ефіроспиртофеноли, пероксиди спиртів, пероксиди простих ефірів, пероксиди кетонів (визначеного або невизначеного хімічного складу) та їх галогеновані, сульфовані, нітровані або нітрозовані похідні, крім ди"));
                //if ((tankId[i]!=47) && (tankId[i]!=48) && (tankId[i]!=55)){
                    Query tankName = em.createNamedQuery("TankDic.findByTankId");
                    tankName.setParameter("tankId", tankId[i]);
                    List<TankDic> tankList = tankName.getResultList();
                    //Получение наименования резервуара
                    for (int s=0; s<tankList.size(); s++){
                        if (tankId[i]<100){
                            docElement.appendChild(createDocumentRow(doc, String.valueOf(i), "1", "TAB1_A5", "Р"+tankList.get(s).getTankName()));//Емкость
                            docElement.appendChild(createDocumentRow(doc, String.valueOf(i), "1", "TAB1_A6", sensorSerial[i]));//Серийный номер уровнемера
                        }
                    }                
                    //Выборка данных для каждого резервуара
                    Query query = em.createNamedQuery("TSPReport.findByDateTankID");
                    query.setParameter("daqDate", dfDate.format(reportDate));
                    query.setParameter("tankID", tankId[i]);
                    List<TSPReport> list = query.getResultList(); 
                    if (list.isEmpty()){
                        if (tankId[i]<100){
                            docElement.appendChild(createDocumentRow(doc, String.valueOf(i), "1", "TAB1_A7", "0.00"));//Объем при +15
                            docElement.appendChild(createDocumentRow(doc, String.valueOf(i), "1", "TAB1_A8", "0.00"));//Объем при +15
                            docElement.appendChild(createDocumentRow(doc, String.valueOf(i), "1", "TAB1_A", String.valueOf(i+1)));//№ п/п
                        }else{
                            docElement.appendChild(createDocumentRow(doc, String.valueOf(j), "3", "RECNO", String.valueOf(j)));
                            docElement.appendChild(createDocumentRow(doc, String.valueOf(j), "3", "TAB3_A3", "Паливо моторне альтернативне"));
                            docElement.appendChild(createDocumentRow(doc, String.valueOf(j), "3", "TAB3_A4", sensorSerial[i]));
                            docElement.appendChild(createDocumentRow(doc, String.valueOf(j), "3", "TAB3_A5", "0.00"));
                            docElement.appendChild(createDocumentRow(doc, String.valueOf(j), "3", "TAB3_A", String.valueOf(j+1)));                                                                                            
                            j++;
                        }
                    }else{   
                        for (int z=0; z<list.size(); z++){
                        //Определение плотности топлива при +20 °С
                            Query p20 = em.createQuery("SELECT v FROM VPlotn20 v WHERE v.plotn like '"
                                +String.valueOf(list.get(z).getTDensity()).replace(",", ".")+"%' AND v.temperName = "+String.format("%d", Math.round(list.get(z).getTTemper().doubleValue())));                                        
                            List<VPlotn20> p20List = p20.getResultList();                        
                            if (p20List.isEmpty()){
                                if (tankId[i]<100){
                                    docElement.appendChild(createDocumentRow(doc, String.valueOf(i), "1", "TAB1_A"+String.valueOf(7+z), "0.00"));//Объем при +15
                                    docElement.appendChild(createDocumentRow(doc, String.valueOf(i), "1", "TAB1_A", String.valueOf(i+1)));//№ п/п
                                }else{
                                    docElement.appendChild(createDocumentRow(doc, String.valueOf(j), "3", "RECNO", String.valueOf(j)));
                                    docElement.appendChild(createDocumentRow(doc, String.valueOf(j), "3", "TAB3_A3", "Паливо моторне альтернативне"));
                                    docElement.appendChild(createDocumentRow(doc, String.valueOf(j), "3", "TAB3_A4", sensorSerial[i]));
                                    docElement.appendChild(createDocumentRow(doc, String.valueOf(j), "3", "TAB3_A5", "0.00"));
                                    docElement.appendChild(createDocumentRow(doc, String.valueOf(j), "3", "TAB3_A", String.valueOf(j+1)));                                                                                                
                                    j++;
                                }    
                            }else{                            
                                for (int k=0;k<p20List.size(); k++){                               
                                    if (tankId[i]<100){
                                    //Вычисление массы топлива
                                        BigDecimal mass = list.get(z).getTVolume().multiply(list.get(z).getTDensity());
                                        Double density20 = Double.parseDouble(p20List.get(k).getPlotn20().replace(",", "."));
                                        //Вычисление плотности топлива при +15 °С
                                        Double density15 = density20+5.0*(0.000903-0.00132*(density20-0.7));
                                    //Вычисление объема топлива при +15 °С
                                        BigDecimal volume15 = mass.divide(BigDecimal.valueOf(density15), BigDecimal.ROUND_UP);                            
                                        docElement.appendChild(createDocumentRow(doc, String.valueOf(i), "1", "TAB1_A"+String.valueOf(7+z), String.format("%.2f", volume15).replace(",", ".")));//Объем при +15
                                        docElement.appendChild(createDocumentRow(doc, String.valueOf(i), "1", "TAB1_A", String.valueOf(i+1)));//№ п/п
                                    }else{                            
                                        if (z<list.size()-1){
                                            startMass = list.get(z).getTLevel();
                                        }else{
                                            endMass = list.get(z).getTLevel();
                                            Double density20 = Double.parseDouble(p20List.get(k).getPlotn20().replace(",", "."));
                                            //Вычисление плотности топлива при +15 °С
                                            Double density15 = density20+5.0*(0.000903-0.00132*(density20-0.7));
                                            //Вычисление объема топлива при +15 °С
                                            BigDecimal volume15 = (endMass.subtract(startMass)).divide(BigDecimal.valueOf(density15), BigDecimal.ROUND_UP);
                                            totalCounters = totalCounters.add(volume15);
                                            docElement.appendChild(createDocumentRow(doc, String.valueOf(j), "3", "RECNO", String.valueOf(j)));
                                            docElement.appendChild(createDocumentRow(doc, String.valueOf(j), "3", "TAB3_A3", "Паливо моторне альтернативне"));
                                            docElement.appendChild(createDocumentRow(doc, String.valueOf(j), "3", "TAB3_A4", sensorSerial[i]));
                                            docElement.appendChild(createDocumentRow(doc, String.valueOf(j), "3", "TAB3_A5", String.format("%.2f", volume15).replace(",", ".")));
                                            docElement.appendChild(createDocumentRow(doc, String.valueOf(j), "3", "TAB3_A", String.valueOf(j+1)));                                                                                                        
                                            j++;
                                        }
                                    }
                                }
                            }
                        }                    
                    }                                                                                        
                //}
            }
            docElement.appendChild(createDocumentRow(doc, "0", "4", "RECNO", "0"));
            docElement.appendChild(createDocumentRow(doc, "0", "4", "TAB4_A3", "Паливо моторне альтернативне"));
            docElement.appendChild(createDocumentRow(doc, "0", "4", "TAB4_A4", String.format("%.2f", totalCounters).replace(",", ".")));
            docElement.appendChild(createDocumentRow(doc, "0", "4", "TAB4_A", "1"));
            cardElement.appendChild(docElement);

            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();
            DOMSource source = new DOMSource(doc);
            String path = NbPreferences.forModule(saveXMLPanel.class).get("path", "C:");
            StreamResult result = new StreamResult(new File(path+File.separator+dfLarge.format(reportDate)+".xml"));
            transformer.transform(source, result);
            NotifyDescriptor xmlDone = new NotifyDescriptor.Confirmation("Файл xml успешно сформирован!!!");
            Object resultXML = DialogDisplayer.getDefault().notify(xmlDone);
        }else{
            NotifyDescriptor errorTime = new NotifyDescriptor.Message("Склад на выбранную дату не закрыт. Сформировать XML невозможно.", NotifyDescriptor.WARNING_MESSAGE);
            Object resultErrorTime = DialogDisplayer.getDefault().notify(errorTime);
        }
                                                    
    }
    
    private static Element createDocumentRow(Document doc, String line, String tab, String name, String value){        
        Element rowElement = doc.createElement("ROW");
        
        Attr lineAttr = doc.createAttribute("LINE");
        lineAttr.setValue(line);               
        Attr tabAttr = doc.createAttribute("TAB");
        tabAttr.setValue(tab);                
        Attr nameAttr = doc.createAttribute("NAME");
        nameAttr.setValue(name);
        
        rowElement.setAttributeNode(lineAttr);
        rowElement.setAttributeNode(tabAttr);
        rowElement.setAttributeNode(nameAttr);
        
        Element valueElement = doc.createElement("VALUE");
        valueElement.appendChild(doc.createTextNode(value));
        rowElement.appendChild(valueElement);
        return rowElement;
    }
    
    private boolean checkExists(String storeDate, EntityManager em){
        boolean exists=false;
        Query query = em.createQuery("SELECT t.daqDate, t.daqTime FROM TSPReport t WHERE t.daqDate = :daqDate GROUP BY t.daqDate, t.daqTime" );
        query.setParameter("daqDate", storeDate);
        List<TSPReport> list;
        list = query.getResultList();
        if (list.size()==2){exists = true;}
        return exists;
    }
}
