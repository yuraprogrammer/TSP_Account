/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alexprom.tsp_account.vis;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author yura_
 */
public class tankPanelChart {
    private JFreeChart chart;
    private DynamicTimeSeriesCollection dataset;
    private float[] series;
    private double minValue=0;
    private ValueAxis domain;
    private ValueAxis range;
    private DateAxis axis;
    private XYPlot plot;
    
    private XYPlot getPlot(){
        return this.plot;
    }
    
    private void setPlot(XYPlot plot){
        this.plot=plot;
    }
    
    public ValueAxis getDomain(){
        return this.domain;
    }
    
    public void setDomain(ValueAxis domain){
        this.domain=domain;        
    }
    
    public ValueAxis getRange(){
        return this.range;
    }
    
    public void setRange(ValueAxis range){
        this.range=range;        
    }
    
    public DateAxis getAxis(){
        return this.axis;
    }
    
    public void setAxis(DateAxis axis){
        this.axis=axis;        
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
    private double maxValue=100;
    
    public float[] getSeries() {
        return series;
    }

    public void setSeries(float[] series) {
        this.series = series;
    }
    
    public DynamicTimeSeriesCollection getDataset() {
        return dataset;
    }

    public void setDataset(DynamicTimeSeriesCollection dataset) {
        this.dataset = dataset;
    }
    
    public JFreeChart getChart() {
        return chart;
    }

    public void setChart(JFreeChart chart) {
        this.chart = chart;
    }
    
    
    
    public tankPanelChart(String title, String categoryLabel, String valueLabel){
        this.series = createSeries();
        this.dataset = createDefaultDataset(this.series);
        chart = createChart(title, categoryLabel, valueLabel, this.dataset);
    }
    
    private JFreeChart createChart(String title, String categoryAxisLabel, String valueAxisLabel, XYDataset dataset){
        JFreeChart graph = ChartFactory.createTimeSeriesChart(title, 
                                                        categoryAxisLabel, 
                                                        valueAxisLabel, 
                                                        dataset,                                                         
                                                        true, 
                                                        true, false);
        
        plot = graph.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        range = plot.getRangeAxis();
        axis = (DateAxis) plot.getDomainAxis();
        axis.setTickUnit(new DateTickUnit(DateTickUnit.SECOND, 5));
        axis.setDateFormatOverride(new SimpleDateFormat("hh:mm:ss"));
        
        range.setRange(minValue, maxValue);
        
        return graph;
    }
    
    public void chartRefresh(XYDataset dataset){
        this.chart.getXYPlot().setDataset(dataset);
    }
    
    private float[] createSeries(){
        float[] s1 = new float[10];
        s1[0] = (float)181.8;
        s1[1] = (float)167.3;
        s1[2] = (float)153.8;
        s1[3] = (float)167.6;
        s1[4] = (float)158.8;
        s1[5] = (float)148.3;
        s1[6] = (float)153.9;
        s1[7] = (float)142.7;
        s1[8] = (float)123.2;
        s1[9] = (float)131.8;
        return s1;
    }
    
    private DynamicTimeSeriesCollection createDefaultDataset(float[] s1){
                
        DynamicTimeSeriesCollection ds = new DynamicTimeSeriesCollection(1, 30, new Second());
        Date date = new Date();
        ds.setTimeBase(new Second(date));
        ds.addSeries(s1, 0, "");        

        return ds;
    }
}
