/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.vis;

import com.alexprom.tsp_account.report_db.GlobalEntityManager;
import com.alexprom.tsp_account.report_db.TanksToAccount;
import com.alexprom.tsp_account.report_db.TankDic;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author yura_
 */
public class tankFactory extends ChildFactory<TanksToAccount>{
    
    private final GlobalEntityManager gem = new GlobalEntityManager();
    private final EntityManager em = gem.getEm();
        
    @Override
    protected boolean createKeys(List toPopulate) {
        Query query = em.createNamedQuery("TanksToAccount.findAll");
        List<TanksToAccount> list = query.getResultList();           
        for (TanksToAccount tanks : list){
            toPopulate.add(tanks);
        }
                
        return true;
    }
    
    @Override
    protected Node createNodeForKey(TanksToAccount key) {
        Query query = em.createNamedQuery("TankDic.findByTankId");
        query.setParameter("tankId", key.getTankId());
        List<TankDic> tankName = query.getResultList();
        String newTankName="";
        for (TankDic name : tankName){
            newTankName = name.getTankName();
        }
        tankRootNode newNode = new tankRootNode(key, "Резервуар: "+newTankName);
       
        return newNode;
    }

}
