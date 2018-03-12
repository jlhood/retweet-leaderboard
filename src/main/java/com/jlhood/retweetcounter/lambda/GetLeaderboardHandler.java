package com.jlhood.retweetcounter.lambda;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jlhood.retweetcounter.Leaderboard;
import com.jlhood.retweetcounter.dagger.AppComponent;
import com.jlhood.retweetcounter.dagger.DaggerAppComponent;

public class GetLeaderboardHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    private static final Map<String, String> CORS_HEADERS = ImmutableMap.of("Access-Control-Allow-Origin", "*");

    private final Leaderboard leaderboard;

    public GetLeaderboardHandler() {
        AppComponent component = DaggerAppComponent.create();
        leaderboard = component.leaderboard();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withHeaders(CORS_HEADERS)
                .withBody(GSON.toJson(leaderboard.load(getLimit(request))));
    }

    private Integer getLimit(APIGatewayProxyRequestEvent request) {
        if (request.getQueryStringParameters() == null) {
            return null;
        }
        try {
            return Integer.parseInt(request.getQueryStringParameters().get("limit"));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
