package com.jlhood.retweetcounter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

import com.google.common.base.Preconditions;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class Leaderboard {
    private static final String RECORD_KEY = "leaderboard";

    @NonNull
    private final DynamoDBMapper mapper;
    private final DynamoDBMapperConfig mapperConfig;

    public Leaderboard(@NonNull final DynamoDBMapper mapper, @NonNull final String tableName) {
        this.mapper = mapper;
        this.mapperConfig = new DynamoDBMapperConfig.Builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(tableName))
                .build();
    }

    public List<RetweetCount> load(int limit) {
        Preconditions.checkArgument(limit > 0, "limit must be a positive number");
        return getOrDefault().getRetweetCounts().values().stream()
                .sorted(Comparator.comparing(RetweetCount::getCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public void update(List<RetweetCount> retweetCounts) {
        LeaderboardRecord record = getOrDefault();
        retweetCounts.stream()
                .filter(c -> c.getCount() > 0)
                .forEach(c -> updateCount(record, c));
        mapper.save(record, mapperConfig);
    }

    private void updateCount(LeaderboardRecord record, RetweetCount current) {
        log.info("Processing retweet count: {}", current);
        record.getRetweetCounts().putIfAbsent(current.getUsername(), current);
        RetweetCount previous = record.getRetweetCounts().get(current.getUsername());
        if (current.getCount() > previous.getCount()) {
            log.info("Updating count from {} to {}", previous, current);
            record.getRetweetCounts().put(current.getUsername(), current);
        }
    }

    private LeaderboardRecord getOrDefault() {
        LeaderboardRecord loadPrototype = LeaderboardRecord.builder()
                .id(RECORD_KEY)
                .build();
        return Optional.ofNullable(mapper.load(loadPrototype, mapperConfig))
                .orElse(LeaderboardRecord.builder()
                        .id(RECORD_KEY)
                        .retweetCounts(new HashMap<>())
                        .build());
    }
}
