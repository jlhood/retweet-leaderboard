package com.jlhood.retweetcounter.lambda;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.jlhood.retweetcounter.TweetProcessor;
import com.jlhood.retweetcounter.dagger.AppComponent;
import com.jlhood.retweetcounter.dagger.DaggerAppComponent;

public class TweetProcessorHandler implements RequestHandler<List<String>, Void> {
    private final TweetProcessor processor;

    public TweetProcessorHandler() {
        AppComponent component = DaggerAppComponent.create();
        processor = component.tweetProcessor();
    }

    @Override
    public Void handleRequest(List<String> tweets, Context context) {
        processor.accept(tweets);
        return null;
    }
}
