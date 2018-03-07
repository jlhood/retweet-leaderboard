package com.jlhood.retweetcounter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

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

    public List<RetweetCount> load() {
        return getOrDefault().getRetweetCounts().values().stream()
                .sorted(Comparator.comparing(RetweetCount::getCount).reversed())
                .collect(Collectors.toList());
    }

    public void update(List<RetweetCount> retweetCounts) {
        LeaderboardRecord record = getOrDefault();
        retweetCounts.forEach(c -> updateCount(record, c));
        mapper.save(record, mapperConfig);
    }

    private void updateCount(LeaderboardRecord record, RetweetCount count) {
        record.getRetweetCounts().putIfAbsent(count.getUsername(), count);
        if (count.getCount() > record.getRetweetCounts().get(count.getUsername()).getCount()) {
            record.getRetweetCounts().put(count.getUsername(), count);
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
