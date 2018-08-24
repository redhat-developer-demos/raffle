# Raffle


## Configuring the application
Before using this app, go to <https://developer.twitter.com/en/apps> and create an App. (You might need to apply for a developer account)

Fill the fields:

- App name: <your twitter handle>_raffle. Example: rafabene_raffle
- Description: This app will search for specific hashtags and check if the user who posted the tweeter follows me and has a posted a picture.
- Website URL: https://developers.redhat.com
- Tell us how this app will be used: This app will search for specific hashtags and check if the user who posted the tweeter follows me and has a posted a picture.

For the new App, Get the Consumer API keys, Access token & access token secret (you might need to generate them), and place it on the twitter4j.properties

## Executing the application

Run with

    mvn spring-boot:run
    
Access the URL: `http://localhost:8080/<hashtag for the raffle>`

## Criteria for the Raffle

- The Tweet must contain specified #hashtag that you defined (part of the query string)
- The Tweet must mention your username (part of the query string)
- The Tweet must have a picture of you (part of the query string)
- The Tweet must not be a RT (part of the query string)
- The user must be your follower (optional)
