/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.vis;

import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author yura_
 */
public class deviceFactory extends ChildFactory<String>{
        
    public deviceFactory(){
    
    }
    
    @Override
    protected boolean createKeys(List<String> toPopulate) {
        toPopulate.add("Резервуары");
        toPopulate.add("Счетчики");
        
        return true;
    }
    
    @Override
    protected Node createNodeForKey(String key){
        if (key.equals("Резервуары")){
            AbstractNode node = new AbstractNode(Children.create(new tankFactory(), true));
            node.setDisplayName(key);
            node.setIconBaseWithExtension("com/alexprom/tsp_account/vis/rvs_ico.png");
            return node;                        
        }else if (key.equals("Счетчики")){
            AbstractNode node = new AbstractNode(Children.create(new counterFactory(), true));
            node.setDisplayName(key);
            node.setIconBaseWithExtension("com/alexprom/tsp_account/vis/counter_ico.png");
            return node;
        }else{
            return new AbstractNode(Children.LEAF);
        }                
    }
}
