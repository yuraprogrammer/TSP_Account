/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.daq;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author yura_
 */
public class CustomRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);        
        
            if ((row%2) != 0){
                setForeground(Color.blue);                
                setBackground(Color.white);
            }else{
                setForeground(Color.white);
                setBackground(Color.gray);
            }
        setHorizontalAlignment(JLabel.CENTER);
        
        return c;
    }
    
    @Override
    public Font getFont(){
        Font font = new Font("Tahoma", Font.BOLD, 11);
        
        return font;
    }
    
}
