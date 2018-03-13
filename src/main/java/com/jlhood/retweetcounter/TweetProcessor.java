package com.jlhood.retweetcounter;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class TweetProcessor implements Consumer<List<String>> {
    @NonNull
    private final Leaderboard leaderboard;

    @Override
    public void accept(List<String> tweetsAsJson) {
        log.info("Processing {} tweets", tweetsAsJson.size());
        leaderboard.update(tweetsAsJson.stream()
                .map(Tweet::new)
                .filter(this::isEligible)
                .map(this::toRetweetCount)
                .collect(Collectors.toList()));
    }

    private boolean isEligible(Tweet tweet) {
        return !tweet.isNativeRetweet() && tweet.getRetweetCount() > 0;
    }

    private RetweetCount toRetweetCount(Tweet tweet) {
        return new RetweetCount(tweet.getUsername(), tweet.getRetweetCount(), tweet.getFavoriteCount());
    }
}
