package com.jlhood.retweetcounter.dagger;

/**
 * Helper class for fetching environment values.
 */
public final class Env {
    public static final String LEADERBOARD_TABLE_NAME_KEY = "LEADERBOARD_TABLE_NAME";
    public static final String LEADERBOARD_LIMIT_KEY = "LEADERBOARD_LIMIT";

    private Env() {
    }

    public static String getLeaderboardTableName() {
        return System.getenv(LEADERBOARD_TABLE_NAME_KEY);
    }

    public static int getLeaderboardLimit() {
        return Integer.parseInt(System.getenv(LEADERBOARD_LIMIT_KEY));
    }
}
