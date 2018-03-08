package com.jlhood.retweetcounter;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TweetProcessor implements Consumer<List<String>> {
    @NonNull
    private final Leaderboard leaderboard;

    @Override
    public void accept(List<String> tweetsAsJson) {
        leaderboard.update(tweetsAsJson.stream()
                .map(Tweet::new)
                .map(this::toRetweetCount)
                .collect(Collectors.toList()));
    }

    private RetweetCount toRetweetCount(Tweet tweet) {
        return new RetweetCount(tweet.getUsername(), tweet.getRetweetCount());
    }
}
