package com.jlhood.retweetcounter.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jlhood.retweetcounter.Leaderboard;
import com.jlhood.retweetcounter.dagger.AppComponent;
import com.jlhood.retweetcounter.dagger.DaggerAppComponent;

public class GetLeaderboardHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private final Leaderboard leaderboard;

    public GetLeaderboardHandler() {
        AppComponent component = DaggerAppComponent.create();
        leaderboard = component.leaderboard();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(GSON.toJson(leaderboard.load()));
    }
}
