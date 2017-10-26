package edu.neu.zhiyao.assignment2.server.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import edu.neu.zhiyao.assignment2.server.entity.RFIDLiftData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;
import javax.ws.rs.Path;

@Path("RFIDLiftDao")
@Singleton
public class RFIDLiftDao extends BaseDao<RFIDLiftData> {
    
    public RFIDLiftDao() {
        super(RFIDLiftData.class);
    }
    
    public List<RFIDLiftData> findBySkierIdAndDayNum(int skierId, int dayNum) {
        Map<String, AttributeValue> values = new HashMap<>();
        values.put(":val1", new AttributeValue().withN(String.valueOf(skierId)));
        values.put(":val2", new AttributeValue().withN(String.valueOf(dayNum)));
        
        DynamoDBQueryExpression<RFIDLiftData> queryExpression
                = new DynamoDBQueryExpression<RFIDLiftData>()
                    .withKeyConditionExpression("SkierId = :val1 and DayNum = :val2")
                    .withExpressionAttributeValues(values)
                    .withIndexName("SkierId-DayNum-index")
                    .withConsistentRead(false);
        return find(queryExpression);
    }
    
}
