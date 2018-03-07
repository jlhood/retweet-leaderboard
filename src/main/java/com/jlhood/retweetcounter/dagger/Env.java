package com.jlhood.retweetcounter.dagger;

/**
 * Helper class for fetching environment values.
 */
public final class Env {
    public static final String TARGET_TWEET_ID_KEY = "TARGET_TWEET_ID";
    public static final String LEADERBOARD_TABLE_NAME_KEY = "LEADERBOARD_TABLE_NAME";

    private Env() {
    }

    public static String getTargetTweetId() {
        return System.getenv(TARGET_TWEET_ID_KEY);
    }

    public static String getLeaderboardTableName() {
        return System.getenv(LEADERBOARD_TABLE_NAME_KEY);
    }
}
