package com.jlhood.retweetcounter;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Wither
@Data
@DynamoDBTable(tableName = "")
public class LeaderboardRecord {
    @DynamoDBHashKey
    private String id;

    @DynamoDBVersionAttribute
    private Long recordVersionNumber;

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = RetweetCountsMapConverter.class)
    private Map<String, RetweetCount> retweetCounts;
}
