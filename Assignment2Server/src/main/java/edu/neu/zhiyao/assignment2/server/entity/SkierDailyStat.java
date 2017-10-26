package edu.neu.zhiyao.assignment2.server.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "SkierDailyStat")
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
    
    @DynamoDBHashKey(attributeName = "SkierId")
    public int getSkierId() {
        return skierId;
    }

    public void setSkierId(int skierId) {
        this.skierId = skierId;
    }

    @DynamoDBRangeKey(attributeName = "DayNum")
    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }

    @DynamoDBAttribute(attributeName = "TotalVert")
    public int getTotalVert() {
        return totalVert;
    }

    public void setTotalVert(int totalVert) {
        this.totalVert = totalVert;
    }

    @DynamoDBAttribute(attributeName = "RideNum")
    public int getRideNum() {
        return rideNum;
    }

    public void setRideNum(int rideNum) {
        this.rideNum = rideNum;
    }
    
}
