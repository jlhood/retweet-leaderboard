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

### Install the aws-serverless-twitter-event-source app

The retweet-leaderboard app uses the [aws-serverless-twitter-event-source](https://github.com/awslabs/aws-serverless-twitter-event-source) app as a source of Tweet data. So after deploying the retweet-leaderboard to your account, you need to install the aws-serverless-twitter-event-source app to send twitter events to the retweet-leaderboard app.

Refer to the aws-serverless-twitter-event-source [README](https://github.com/awslabs/aws-serverless-twitter-event-source/blob/master/README.md) for general installation steps. There are some parameter settings specific to this app:

1. `TweetProcessorFunctionName` - This should be set to the name of the TweetProcessor function that you noted down when installing the retweet-leaderboard app.
1. `BatchSize` - aws-serverless-twitter-event-source default value is 15. I recommend setting this to a high value like 100 to minimize writes to the Leaderboard table.
1. `SearchText` - This controls what tweets are pulled in as part of the game. Generally, you add some uncommon combination of hashtags to search for to try to limit results to just people playing the game. The app's TweetProcessor has filtering logic to ensure only the original tweets (and not individual retweet records) are counted. It also has filtering logic to ensure a tweet has at least one retweet to be added to the leaderboard. However, rather than relying on the lambda function to filter out these records, it is more efficient (and cost effective) to do this filtering at the Twitter Search API layer. You can do this by adding `-filter:nativeretweets` and `min_retweets:1` to your `SearchText` parameter. So for example, if you tell your players to tweet a post containing the hashtags #serverless and #retweetgame in order to play, you would set the `SearchText` parameter to `#serverless #retweetgame -filter:nativeretweets min_retweets:1`.
    1. Also note, if you want to change the SearchText after deploying the app, you can always do this via the AWS Lambda Console by finding the TwitterSearchPoller lambda created by the aws-serverless-twitter-event-source app and changing its `SEARCH_TEXT` environment variable value. If you do this, you should also delete the Leaderboard record from the Leaderboard DynamoDB table so results associated with the old search are discarded.

### UI

The UI is a simple html page that you can open locally in your browser.

1. The UI is contained in `ui/leaderboard.html`. Either clone the github repo or download the file from github directly.
1. Edit the file. Find the constant called `ENDPOINT` and replace it with the API Gateway endpoint that was created when you deployed this serverless app. To find the endpoint:
    1. Go to the [Amazon API Gateway Console](https://console.aws.amazon.com/apigateway/home).
    1. Click on the Leaderboard API in the menu on the left.
    1. Click "Stages" on the menu on the left.
    1. Click on the "Prod" stage. The endpoint will be displayed. Copy this value into the `ENDPOINT` constant in the UI html file.
1. Load the UI file in your web browser. Tested in Google Chrome, Firefox, and Safari.

## License Summary

This code is made available under the MIT license. See the LICENSE file.
