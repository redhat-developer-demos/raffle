package com.redhat.developers.raffle;

import org.apache.commons.logging.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import twitter4j.*;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;


@RestController
public class RaffleController {

    private Log log = LogFactory.getLog(RaffleController.class.getName());

    private Twitter twitter = new TwitterFactory().getInstance();

    private Set<String> users4Raffle = new java.util.TreeSet<>((o1, o2) -> o2.compareTo(o1));


    @GetMapping(value="/")
    public String rootMessage(){
        return "Call /hashtag";
    }


    @GetMapping(value="/{hashtag}", produces = MediaType.TEXT_HTML_VALUE)
    public void endpoint(@PathVariable("hashtag") String hashtag,  HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.write("<html>");
        try{
            String queryString = getQueryString(hashtag);
            writer.write("Query String: " + queryString + "<br/>");
            Map<String, List<Status>> tweets = performQuery(queryString, writer);
            writer.write("<h3>Enabled users: </h3>");
            writer.write(users4Raffle.toString());
            performRaffle(tweets, writer);
        }
        catch(twitter4j.TwitterException te){
            te.printStackTrace();
            writer.write("ERROR: " + te.getMessage());
        }finally {
            writer.write("</html>");
            writer.flush();
        }
    }

    private String getQueryString(String hashtag) throws TwitterException {
        // Must mention you, with a hashtag, a picture and must not be a RT
        String queryString = "@" + twitter.getScreenName() + " #" + hashtag + "  filter:media -filter:retweets";
        // For TEST only
        //String queryString = "@openshift #" + hashtag + " -filter:retweets";
        log.info("Query String: " + queryString);
        return queryString;
    }

    private void performRaffle(Map<String, List<Status>> tweets, PrintWriter writer) throws TwitterException {
        Random rand = new Random();
        List<String> usersList = new ArrayList<>(users4Raffle);
        String winnnerUser = usersList.get(rand.nextInt(usersList.size()));
        Relationship relationship = twitter.showFriendship(twitter.getScreenName(), winnnerUser);
        writer.write("<h2>And the Winner is....</h2>");
        writer.write("<h3><a href='https://twitter.com/"  + winnnerUser + "'>@" + winnnerUser + "</a></h3>");
        writer.write("<b>Follows you? </b>" +  relationship.isSourceFollowedByTarget());
        writer.write("<h4>Tweets from this user:</h4>");
        List<Status> userTweets = tweets.get(winnnerUser);
        for (Status tweet: userTweets){
            writer.write("<b>@" + winnnerUser + "</b> " +
                    "- <i><a href='https://twitter.com/" + winnnerUser + "/status/" +
                    tweet.getId() + "'>" + tweet.getText() + "</a></i>" +
                    " - Date: " + tweet.getCreatedAt()+ "<hr/>");
        }
        writer.write("<h2>Thank you!!!</h2>");
    }

    private Map<String, List<Status>> performQuery(String queryString, PrintWriter writer) throws TwitterException {
        Query query = new Query(queryString);
        Map<String, List<Status>> tweetsResponse = new HashMap<>();
        twitter4j.QueryResult result;
        writer.write("<h3>Found the following users and tweets: </h3><br/><hr/>");
        do {
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {
                User u = tweet.getUser();
                writer.write("<b>@" + u.getScreenName() + "</b> " +
                        "- <i><a href='https://twitter.com/" + u.getScreenName() + "/status/" +
                        tweet.getId() + "'>" + tweet.getText() + "</a></i> + ");
                writer.write("<hr/>");
                writer.write("<br/>");
                addDataToUser(tweetsResponse, tweet);
            }
        } while ((query = result.nextQuery()) != null);
        return tweetsResponse;
    }

    private void addDataToUser(Map<String,List<Status>> tweetsResponse, Status tweet) {
        List<Status> tweets = tweetsResponse.get(tweet.getUser().getScreenName());
        if (tweets == null){
            tweets = new ArrayList<>();
            tweetsResponse.put(tweet.getUser().getScreenName(), tweets);
            users4Raffle.add(tweet.getUser().getScreenName());
        }
        tweets.add(tweet);
    }
}
