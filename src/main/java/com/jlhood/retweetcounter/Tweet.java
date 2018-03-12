package com.jlhood.retweetcounter;

import com.google.common.base.Preconditions;

import org.json.JSONObject;

public class Tweet {
    private final JSONObject json;

    public Tweet(String tweetJson) {
        json = new JSONObject(tweetJson);
    }

    public String getId() {
        return json.getString("id_str");
    }

    public String getUsername() {
        return json.getJSONObject("user").getString("screen_name");
    }

    public boolean isRetweet() {
        return json.has("retweeted_status");
    }

    public boolean isQuoteTweet() {
        return json.getBoolean("is_quote_status");
    }

    public String getQuotedTweetId() {
        Preconditions.checkState(isQuoteTweet(), "Can't get quoted tweet id of tweet that's not a quote tweet");
        return json.getJSONObject("quoted_status").getString("id_str");
    }

    public long getRetweetCount() {
        return json.getLong("retweet_count");
    }

    public long getFavoriteCount() {
        return json.getLong("favorite_count");
    }
}
