package edu.neu.zhiyao.assignment2.server.dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class BaseDao<T> {

    private final Class<T> type;
    private final DynamoDB dynamoDB;
    private final DynamoDBMapper mapper;

    public BaseDao(Class<T> type) {
        this.type = type;
//        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
//                        "http://localhost:8000", "us-west-1"))
//                .build();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_WEST_1)
                .build();
        dynamoDB = new DynamoDB(client);
        mapper = new DynamoDBMapper(client);
    }

    public DynamoDB getDynamoDB() {
        return dynamoDB;
    }

    public T find(Object hashKey) {
        return find(hashKey, null);
    }
    
    public T find(Object hashKey, Object rangeKey) {
        System.out.println("Loading item...");
        T result = mapper.load(type, hashKey, rangeKey);
        System.out.println("Item loaded: " + result);
        return result;
    }
    
    public List<T> find(DynamoDBQueryExpression queryExpression) {
        return mapper.query(type, queryExpression);
    }

    public List<T> find(DynamoDBScanExpression scanExpression) {
        List<T> res;
        try {
            res = mapper.scan(type, scanExpression);
        } catch (ResourceNotFoundException ex) {
            res = new ArrayList<>();
        }
        return res;
    }
    
    public void saveOrUpdate(T item) {
        System.out.println("Saving item: " + item);
        mapper.save(item);
        System.out.println("Item saved!");
    }

}
