/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.vis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.persistence.EntityManager;
import javax.xml.transform.TransformerException;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

@ActionID(
        category = "Tools",
        id = "com.alexprom.tsp_account.vis.createCustomXML"
)
@ActionRegistration(
        iconBase = "com/alexprom/tsp_account/vis/xml_2419.png",
        displayName = "#CTL_createCustomXML"
)
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 0),
    @ActionReference(path = "Toolbars/File", position = 500)
})
@Messages("CTL_createCustomXML=Создать XML...")
public final class createCustomXML implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        
        try {
            selectReport frm = new selectReport();
            DialogDescriptor dd = new DialogDescriptor(frm, "Выберите дату", true,
                        DialogDescriptor.OK_CANCEL_OPTION, DialogDescriptor.OK_OPTION, null);
            dd.setModal(true);        
            Object result = DialogDisplayer.getDefault().notify(dd);
            if (null != result && DialogDescriptor.OK_OPTION == result) {
            TankDataTopComponent tdtc = (TankDataTopComponent)WindowManager.getDefault().findTopComponent("TankDataTopComponent");
            EntityManager em = tdtc.em;
            if (em!=null){                
                createXML xml = new createXML(em, frm.getActDate());                
            }else{
                NotifyDescriptor d = new NotifyDescriptor.Message("Не установлена связь с базой данных. Выполните настройки соединения и повторите попытку.", NotifyDescriptor.ERROR_MESSAGE);
                Object result2 = DialogDisplayer.getDefault().notify(d);
            }
        }
            
        } catch (TransformerException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
