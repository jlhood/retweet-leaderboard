package com.jlhood.retweetcounter.dagger;


import javax.inject.Singleton;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import com.jlhood.retweetcounter.Leaderboard;
import com.jlhood.retweetcounter.TweetProcessor;

import dagger.Module;
import dagger.Provides;

/**
 * Application DI wiring.
 */
@Module
public class AppModule {
    @Provides
    @Singleton
    public TweetProcessor provideTweetProcessor(final Leaderboard leaderboard) {
        return new TweetProcessor(leaderboard);
    }

    @Provides
    @Singleton
    public Leaderboard provideLeaderboard() {
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        return new Leaderboard(mapper, Env.getLeaderboardTableName());
    }
}
