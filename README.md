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
    
Access the URL: http://localhost:8080/`<your username>`/`<hashtag for the raffle>`