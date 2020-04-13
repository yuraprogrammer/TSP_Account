/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.vis;

import com.alexprom.tsp_account.report_db.CountersInitData;
import com.alexprom.tsp_account.report_db.GlobalEntityManager;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author yura_
 */
public class counterFactory extends ChildFactory<CountersInitData>{

    private final GlobalEntityManager gem = new GlobalEntityManager();
    private final EntityManager em = gem.getEm();
    
    @Override
    protected boolean createKeys(List<CountersInitData> toPopulate) {
        Query query = em.createNamedQuery("CountersInitData.findAll");
        List<CountersInitData> list = query.getResultList();
                
        for (CountersInitData counters : list){
            toPopulate.add(counters);
        }
                
        return true;
    }
    
    @Override
    protected Node createNodeForKey(CountersInitData key) {
        return new countersRootNode(key, "Счетчик: № " + key.getSensorSerial());
    }
}
