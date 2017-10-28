/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.neu.zhiyao.assignment2.server.dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author allisonjin
 */
public class ResetTable {

    private static final String RFID_LIFT_TABLE = "RFIDLiftData";
    private static final String SKIER_DAILY_STAT_TABLE = "SkierDailyStat";
    private static final String RFID_LIFT_ID = "Id";
    private static final String SKIER_ID = "SkierId";
    private static final String DAY_NUM = "DayNum";
    private static final String SKIER_DAY_NUM_INDEX = "SkierId-DayNum-index";
    
//    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
//                .build();
    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_WEST_1)
//                .withCredentials(new ProfileCredentialsProvider("myProfile"))
                .build();
    private static DynamoDB dynamoDB = new DynamoDB(client);
    
    public static void main(String[] args) {
        
        try {
            deleteTable(RFID_LIFT_TABLE);
            deleteTable(SKIER_DAILY_STAT_TABLE);
            
            createTable(RFID_LIFT_TABLE, 5L, 5L, RFID_LIFT_ID, "S", SKIER_ID, "N");
            createTable(SKIER_DAILY_STAT_TABLE, 5L, 5L, SKIER_ID, "N", DAY_NUM, "N");
            
//            deleteRFIDItems(2);
            
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

            if (RFID_LIFT_TABLE.equals(tableName)) {
                attributeDefinitions.add(new AttributeDefinition(DAY_NUM, "N"));
                GlobalSecondaryIndex globalSecondaryIndex = new GlobalSecondaryIndex()
                        .withIndexName(SKIER_DAY_NUM_INDEX)
                        .withKeySchema(new KeySchemaElement(sortKeyName, KeyType.HASH),
                                new KeySchemaElement(DAY_NUM, KeyType.RANGE))
                        .withProvisionedThroughput(new ProvisionedThroughput(5L, 500L))
                        .withProjection(new Projection().withProjectionType(ProjectionType.ALL));
                List<GlobalSecondaryIndex> globalSecondaryIndexes = new ArrayList<>();
                globalSecondaryIndexes.add(globalSecondaryIndex);
                request.setGlobalSecondaryIndexes(globalSecondaryIndexes);
            }
            
            request.setAttributeDefinitions(attributeDefinitions);

            System.out.println("Issuing CreateTable request for " + tableName);
            Table table = dynamoDB.createTable(request);
            System.out.println("Waiting for " + tableName + " to be created...this may take a while...");
            table.waitForActive();
            
            System.out.println("CreateTable done!");
        } catch (Exception e) {
            System.err.println("CreateTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }
    
    private static void deleteRFIDItems(int dayNum) {
        System.out.println("Deleting...");
        Table table = dynamoDB.getTable(RFID_LIFT_TABLE);
        Index index = table.getIndex(SKIER_DAY_NUM_INDEX);
        
        for (int i = 1; i <= 40000; i++) {
            try {
                QuerySpec spec = new QuerySpec()
                        .withKeyConditionExpression("SkierId = :val1 and DayNum = :val2")
                        .withValueMap(new ValueMap().withNumber(":val1", i).withNumber(":val2", dayNum));

                ItemCollection<QueryOutcome> items = index.query(spec);
                Iterator<Item> iter = items.iterator();
                while (iter.hasNext()) {
                    String id = iter.next().getString("Id");
                    DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                        .withPrimaryKey(new PrimaryKey("Id", id, "SkierId", i));
                    table.deleteItem(deleteItemSpec);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } 
        
        System.out.println("Delete done!");
        
    }
}
