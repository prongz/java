/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elltechs._gui_oxy_rev02;

import java.io.Serializable;

/**
 *
 * @author Soheil
 */
public class OxySensorCalib implements Serializable {

    private double y1 = 0;
    private float x1 = 0;
    private double y2 = 0;
    private float x2 = 0;
    private double y3 = 0;
    private float x3 = 0;
    private double y4 = 0;
    private float x4 = 0;

    public status getSysStatus() {
        return sysStatus;
    }

    public enum status {
        NOTSET,
        TWOPOINT,
        THREEPOINT,
        FOURPOINT,
        FUALT
    }

    private status sysStatus = status.NOTSET;
    private  oxySensorData osd;

    public OxySensorCalib(oxySensorData osd) {
        this.osd = osd;
    }

    public void setX1Y1(float y1) {
        this.x1 = this.osd.getOxygenRate();
        this.y1 = y1;
    }

    public void setX2Y2(float y2) {
        this.x2 = this.osd.getOxygenRate();
        this.y2 = y2;
    }

    public void setX3Y3(float y3) {
        this.x3 = this.osd.getOxygenRate();
        this.y3 = y3;
    }

  

    public void setX4Y4(float y4) {
        this.x4 = this.osd.getOxygenRate();
        this.y4 = y4;
    }

      public void setY1(double y1) {
        this.y1 = y1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public void setY3(double y3) {
        this.y3 = y3;
    }

    public void setX3(float x3) {
        this.x3 = x3;
    }

    public void setY4(double y4) {
        this.y4 = y4;
    }

    public void setX4(float x4) {
        this.x4 = x4;
    }
    
    public void setSysStatus(status sysStatus) {
        this.sysStatus = sysStatus;
    }

    public oxySensorData getOsd() {
        return osd;
    }

    public void setOsd(oxySensorData osd) {
        this.osd = osd;
    }

    public double getY1() {
        return y1;
    }

    public float getX1() {
        return x1;
    }

    public double getY2() {
        return y2;
    }

    public float getX2() {
        return x2;
    }

    public double getY3() {
        return y3;
    }

    public float getX3() {
        return x3;
    }

    public double getY4() {
        return y4;
    }

    public float getX4() {
        return x4;
    }

    private float twoPointCalibrate(oxySensorData osd) {
        float calibratedOxy;
        double a = (float) ((y2 - y1) / (x2 - x1));
        double b = y1 - a * x1;
        calibratedOxy = (float) (a * osd.getOxygenRate() + b);
        return calibratedOxy;
    }

    private float treePointCalibrate(oxySensorData osd) {
        float calibratedOxy;
        double a = (1 / (x1 - x3)) * ((y1 - y2) / (x1 - x2) - (y2 - y3) / (x2 - x3));
        double b = (y1 - y2) / (x1 - x2) - a * (x1 + x2);
        double c = y1 - a * Math.pow(x1, 2) - b * x1;
        double x = this.osd.getOxygenRate();
        calibratedOxy = (float) (a * x * x + b * x + c);
        return calibratedOxy;
    }

    private float fourPointCalibrate(oxySensorData osd) {
        float calibratedOxy;

        double a = (1 / (x4 - x1))
                * (1 / (x4 - x2)
                * ((y4 - y3) / (x4 - x3)
                - (y3 - y2) / (x3 - x2))
                - 1 / (x3 - x1)
                * ((y3 - y2) / (x3 - x2)
                - (y2 - y1) / (x2 - x1)));
        double b = 1 / (x4 - x1)
                * ((y3 - y2) / (x3 - x2)
                - (y2 - y1) / (x2 - x1))
                - a
                * (x3 + x2 + x1);
        double c = (y2-y1)/(x2-x1) - a* (x2*x2 + x2*x1 + x1*x1)
                -b*(x1+x2);
        double d = y1 - a*x1*x1*x1 + b * x1*x1 + c * x1;
        
        float oxy = osd.getOxygenRate();
        
        calibratedOxy = (float) (a * Math.pow(oxy, 3) + b * Math.pow(oxy,2) + c * oxy + d);
        
        return calibratedOxy;
    }
    
    public float calibratedOxyRate() {
        float calibratedOxyRate;
        switch (this.sysStatus) {
            case NOTSET:
                calibratedOxyRate = this.osd.getOxygenRate();
                break;
            case TWOPOINT:
                calibratedOxyRate = this.twoPointCalibrate(osd);
                break;
            case THREEPOINT:
                calibratedOxyRate = this.treePointCalibrate(osd);
                break;
            case FOURPOINT:
                calibratedOxyRate = this.fourPointCalibrate(osd);
                break;
            default:
                calibratedOxyRate = this.osd.getOxygenRate();
        }
        return calibratedOxyRate;

    }

}
