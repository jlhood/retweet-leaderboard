package com.jlhood.retweetcounter;

import java.util.Comparator;

/**
 * Comparator for leaderboard ordering.
 */
public class RetweetCountComparator implements Comparator<RetweetCount> {
    @Override
    public int compare(RetweetCount left, RetweetCount right) {
        // retweet count descending order is default
        int retweetCompareResult = Long.compare(right.getRetweetCount(), left.getRetweetCount());
        if (retweetCompareResult != 0) {
            return retweetCompareResult;
        }
        // favorite count descending is used as a tie-breaker
        return Long.compare(right.getFavoriteCount(), left.getFavoriteCount());
    }
}
