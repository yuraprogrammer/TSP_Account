/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.vis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.persistence.EntityManager;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

@ActionID(
        category = "File",
        id = "com.alexprom.tsp_account.vis.showReport"
)
@ActionRegistration(
        iconBase = "com/alexprom/tsp_account/vis/preview.png",
        displayName = "#CTL_showReport"
)
@ActionReference(path = "Menu/File", position = 2150)
@Messages("CTL_showReport=Просмотр отчета")
public final class showReport implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        selectReport frm = new selectReport();
        DialogDescriptor dd = new DialogDescriptor(frm, "Выберите дату", true,
                        DialogDescriptor.OK_CANCEL_OPTION, DialogDescriptor.OK_OPTION, null);
        dd.setModal(true);        
        Object result = DialogDisplayer.getDefault().notify(dd);
        if (null != result && DialogDescriptor.OK_OPTION == result) {
            TankDataTopComponent tdtc = (TankDataTopComponent)WindowManager.getDefault().findTopComponent("TankDataTopComponent");
            EntityManager em = tdtc.em;
            if (em!=null){
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                TSP_DayReport report = new TSP_DayReport(em, df.format(frm.getActDate()));
            }else{
                NotifyDescriptor d = new NotifyDescriptor.Message("Не установлена связь с базой данных. Выполните настройки соединения и повторите попытку.", NotifyDescriptor.ERROR_MESSAGE);
                Object result2 = DialogDisplayer.getDefault().notify(d);
            }
        }
    }
}
