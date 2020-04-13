/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.vis;

import org.openide.modules.ModuleInstall;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        /*TopComponent tc =WindowManager.getDefault().findTopComponent("tankVisData");
        if (WindowManager.getDefault().isOpenedEditorTopComponent(tc)){
            tc.close();
        }*/
        
    }

}
