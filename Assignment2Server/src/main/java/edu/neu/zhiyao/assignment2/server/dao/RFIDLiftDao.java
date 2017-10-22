/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.neu.zhiyao.assignment2.server.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import edu.neu.zhiyao.assignment2.server.entity.RFIDLiftData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author allisonjin
 */
public class RFIDLiftDao extends BaseDao<RFIDLiftData> {
    
    public RFIDLiftDao() {
        super(RFIDLiftData.class);
    }
    
    public List<RFIDLiftData> findBySkierIdAndDayNum(int skierId, int dayNum) {
        Map<String, AttributeValue> values = new HashMap<>();
        values.put(":val1", new AttributeValue().withN(String.valueOf(skierId)));
        values.put(":val2", new AttributeValue().withN(String.valueOf(dayNum)));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("SkierId = :val1 and DayNum = :val2")
                .withExpressionAttributeValues(values);
        return find(scanExpression);
    }
    
}
