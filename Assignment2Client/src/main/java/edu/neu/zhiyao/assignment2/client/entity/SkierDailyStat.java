/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.neu.zhiyao.assignment2.client.entity;

public class SkierDailyStat {
    
    private int skierId;
    private int dayNum;
    private int totalVert;
    private int rideNum;
    
    public SkierDailyStat() {
        
    }
    
    public SkierDailyStat(int skierId, int dayNum, int totalVert, int rideNum) {
        this.skierId = skierId;
        this.dayNum = dayNum;
        this.totalVert = totalVert;
        this.rideNum = rideNum;
    }
    
    public int getSkierId() {
        return skierId;
    }

    public void setSkierId(int skierId) {
        this.skierId = skierId;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }

    public int getTotalVert() {
        return totalVert;
    }

    public void setTotalVert(int totalVert) {
        this.totalVert = totalVert;
    }

    public int getLiftNum() {
        return rideNum;
    }

    public void setLiftNum(int rideNum) {
        this.rideNum = rideNum;
    }
    
}
