package com.jlhood.retweetcounter.dagger;

import javax.inject.Singleton;

import com.jlhood.retweetcounter.TweetProcessor;

import dagger.Component;

/**
 * Application component interface.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    TweetProcessor tweetProcessor();
}
