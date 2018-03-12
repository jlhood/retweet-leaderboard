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
    private static final Comparator<RetweetCount> COMPARATOR = new RetweetCountComparator();

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
                .sorted(COMPARATOR)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public void update(List<RetweetCount> retweetCounts) {
        LeaderboardRecord record = getOrDefault();
        retweetCounts.forEach(c -> updateCount(record, c));
        mapper.save(record, mapperConfig);
    }

    private void updateCount(LeaderboardRecord record, RetweetCount newCount) {
        log.info("Processing new retweet count: {}", newCount);
        record.getRetweetCounts().putIfAbsent(newCount.getUsername(), newCount);
        RetweetCount currentCount = record.getRetweetCounts().get(newCount.getUsername());
        boolean isBetterScore = COMPARATOR.compare(newCount, currentCount) < 0;
        if (isBetterScore) {
            log.info("Updating count from {} to {}", currentCount, newCount);
            record.getRetweetCounts().put(newCount.getUsername(), newCount);
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
