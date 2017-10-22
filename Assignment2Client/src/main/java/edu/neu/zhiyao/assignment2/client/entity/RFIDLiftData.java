package edu.neu.zhiyao.assignment2.client.entity;

import java.io.Serializable;

/**
 *
 * Simple class to wrap the data in a RFId lift pass reader record
 */
public class RFIDLiftData implements Serializable, Comparable<RFIDLiftData> {
    
    private String id;
    private int resortId;
    private int dayNum;
    private int skierId;
    private int liftId;
    private int time;

    public RFIDLiftData() {
        
    }
    
    public RFIDLiftData(int resortId, int dayNum, int skierId, int liftId, int time) {
        this.resortId = resortId;
        this.dayNum = dayNum;
        this.skierId = skierId;
        this.liftId = liftId;
        this.time = time;
    }

    public RFIDLiftData(String id, int resortId, int dayNum, int skierId, int liftId, int time) {
        this(resortId, dayNum, skierId, liftId, time);
        this.id = id;
    }
    
    

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public int getResortId() {
        return resortId;
    }

    public void setResortId(int resortId) {
        this.resortId = resortId;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }

    public int getSkierId() {
        return skierId;
    }

    public void setSkierId(int skierId) {
        this.skierId = skierId;
    }

    public int getLiftId() {
        return liftId;
    }

    public void setLiftId(int liftId) {
        this.liftId = liftId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
   
    @Override
    public int compareTo(RFIDLiftData compareData) {
        int compareTime = ((RFIDLiftData) compareData).getTime();
        
        //ascending order
        return this.time - compareTime ;
    }
    
}
