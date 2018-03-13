## Retweet Leaderboard

This serverless app is a demo that processes events from the [aws-serverless-twitter-event-source](https://github.com/awslabs/aws-serverless-twitter-event-source) serverless app and maintains a leaderboard of which Twitter handles have the highest retweet counts. Here's a screenshot of the UI:

![Leaderboard Screenshot](https://github.com/jlhood/retweet-leaderboard/raw/master/images/leaderboard-screenshot.png)

## Architecture

![App Architecture](https://github.com/jlhood/retweet-leaderboard/raw/master/images/app-architecture.png)

1. The aws-serverless-twitter-event-source app periodically invokes the TweetProcessor lambda function to process tweet search results.
1. The TweetProcessor updates the Leaderboard DynamoDB table with latest tweet counts for each Twitter user.
1. The UI makes a GET request to the LeaderboardApi, which invokes the GetLeaderboard lambda to return the latest results. Results are returned in leaderboard order where the largest number of retweets wins. If there is a tie in retweets, number of favorites is used to break the tie.

## Installation Steps

### Install the retweet-leaderboard app

1. [Create an AWS account](https://portal.aws.amazon.com/gp/aws/developer/registration/index.html) if you do not already have one and login
1. Go to this app's page on the [Serverless Application Repository](https://serverlessrepo.aws.amazon.com/applications/arn:aws:serverlessrepo:us-east-1:277187709615:applications~retweet-leaderboard) and click "Deploy"
1. Fill in the required parameters and click "Deploy" and wait for the deployment to complete.
1. Go to the [AWS Lambda Console](http://console.aws.amazon.com/lambda/home) and note down the name of the TweetProcessor function that was created by the deployment.
1. Follow the below steps for installing the aws-serverless-twitter-event-source app.
1. Install the aws-serverless-twitter-event-source app providing the TweetProcessor lambda function you noted in the previous step in the `TweetProcessorFunctionName` as an app parameter. See the aws-serverless-twitter-event-source.

### Install the aws-serverless-twitter-event-source app

The retweet-leaderboard app uses the [aws-serverless-twitter-event-source](https://github.com/awslabs/aws-serverless-twitter-event-source) app as a source of Tweet data. So after deploying the retweet-leaderboard to your account, you need to install the aws-serverless-twitter-event-source app to send twitter events to the retweet-leaderboard app.

Refer to the aws-serverless-twitter-event-source [README](https://github.com/awslabs/aws-serverless-twitter-event-source/blob/master/README.md) for general installation steps. There are two parameters settings specific to this app:

1. `TweetProcessorFunctionName` - This should be set to the name of the TweetProcessor function that you noted down when installing the retweet-leaderboard app.
1. `BatchSize` - aws-serverless-twitter-event-source default value is 15. I recommend setting this to the max value (100) to minimize writes to the Leaderboard table.
1. `SearchText` - This controls what tweets are pulled in as part of the game. Usually you should add some uncommon combination of hashtags to search for to try to limit results to just people playing the game. You should also add `-filter:nativeretweets min_retweets:1` to your query. `-filter:nativeretweets` ensures only the original tweets are returned by the search and not the records for each retweet. `min_retweets:1` ensures the leaderboard only contains Twitter handles who have received at least one retweet. So for example, if I tell my players to tweet a post containing the hashtags #serverless and #retweetgame in order to play, I would set the `SearchText` parameter to `#serverless #retweetgame -filter:nativeretweets min_retweets:1`.
    1. Also note, if you want to change the SearchText after deploying the app, you can always do this via the AWS Lambda Console by finding the TwitterSearchPoller lambda created by the aws-serverless-twitter-event-source and changing its `SEARCH_TEXT` environment variable value.

## License Summary

This code is made available under the MIT license. See the LICENSE file.
