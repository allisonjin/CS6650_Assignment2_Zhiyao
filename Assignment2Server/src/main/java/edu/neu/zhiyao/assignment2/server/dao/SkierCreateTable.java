/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.neu.zhiyao.assignment2.server.dao;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author allisonjin
 */
public class SkierCreateTable {

    private static final String RFID_LIFT_TABLE = "RFIDLift";
    private static final String SKIER_DAILY_STAT_TABLE = "SkierDailyStat";
    private static final String RFID_LIFT_ID = "Id";
    private static final String SKIER_ID = "SkierId";
    private static final String DAY_NUM = "DayNum";
    
    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
                .build();
    private static DynamoDB dynamoDB = new DynamoDB(client);
    
    public static void main(String[] args) {
        
        try {
            deleteTable(RFID_LIFT_TABLE);
            deleteTable(SKIER_DAILY_STAT_TABLE);
            
            createTable(RFID_LIFT_TABLE, 10L, 5L, RFID_LIFT_ID, "S", SKIER_ID, "N");
            createTable(SKIER_DAILY_STAT_TABLE, 10L, 5L, SKIER_ID, "N", DAY_NUM, "N");
            
//            SkierDailyStatService service = new SkierDailyStatService();
//            service.createNewStats(1);
            
        } catch (Exception e) {
            System.err.println("Program failed:");
            System.err.println(e.getMessage());
        }
        
    }
    
    private static void deleteTable(String tableName) {
        Table table = dynamoDB.getTable(tableName);
        try {
            System.out.println("Issuing DeleteTable request for " + tableName);
            table.delete();
            System.out.println("Waiting for " + tableName + " to be deleted...this may take a while...");
            table.waitForDelete();

        }
        catch (Exception e) {
            System.err.println("DeleteTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }
    
    private static void createTable(String tableName, long readCapacityUnits, long writeCapacityUnits,
        String partitionKeyName, String partitionKeyType) {

        createTable(tableName, readCapacityUnits, writeCapacityUnits, partitionKeyName, partitionKeyType, null, null);
    }

    private static void createTable(String tableName, long readCapacityUnits, long writeCapacityUnits,
        String partitionKeyName, String partitionKeyType, String sortKeyName, String sortKeyType) {

        try {

            List<KeySchemaElement> keySchema = new ArrayList<>();
            keySchema.add(new KeySchemaElement().withAttributeName(partitionKeyName).withKeyType(KeyType.HASH)); // Partition
                                                                                                                 // key
            List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
            attributeDefinitions
                .add(new AttributeDefinition().withAttributeName(partitionKeyName).withAttributeType(partitionKeyType));

            if (sortKeyName != null) {
                keySchema.add(new KeySchemaElement().withAttributeName(sortKeyName).withKeyType(KeyType.RANGE)); // Sort
                                                                                                                 // key
                attributeDefinitions
                    .add(new AttributeDefinition().withAttributeName(sortKeyName).withAttributeType(sortKeyType));
            }

            CreateTableRequest request = new CreateTableRequest().withTableName(tableName).withKeySchema(keySchema)
                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(readCapacityUnits)
                    .withWriteCapacityUnits(writeCapacityUnits));

            request.setAttributeDefinitions(attributeDefinitions);

            System.out.println("Issuing CreateTable request for " + tableName);
            Table table = dynamoDB.createTable(request);
            System.out.println("Waiting for " + tableName + " to be created...this may take a while...");
            table.waitForActive();
            
            System.out.println("Success");
        }
        catch (Exception e) {
            System.err.println("CreateTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }
}
