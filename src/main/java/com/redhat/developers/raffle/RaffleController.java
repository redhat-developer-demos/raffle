package com.redhat.developers.raffle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@RestController
public class RaffleController {

    private Log log = LogFactory.getLog(RaffleController.class.getName());

    @Autowired
    private MyFollowers followersComponent;

    @GetMapping(value="/")
    public String rootMessage(){
        return "Call /user/hashtag";
    }


    @GetMapping(value="/{value}")
    public String user(String value){
        return rootMessage();
    }

    @GetMapping(value="/{user}/{hashtag}", produces = MediaType.TEXT_HTML_VALUE)
    public void endpoint(@PathVariable("user") String user,
                         @PathVariable("hashtag") String hashtag,
                         HttpServletResponse response)
            throws IOException, TwitterException {
        PrintWriter writer = response.getWriter();
        writer.write("<html>");
//        String queryString = "#" + hashtag + " @" + user + " filter:media -filter:retweets";
        String queryString = "#" + hashtag + " -filter:retweets";
        log.info("Query String: " + queryString);
        List<String> users = performQuery(queryString, writer);
        writer.write("Enabled users: " + users);
        performRaffle(users, writer);
        writer.write("</html>");
        writer.flush();
    }

    private void performRaffle(List<String> users, PrintWriter writer) {
        Random rand = new Random();
        String winnnerUser = users.get(rand.nextInt(users.size()));
        writer.write("<h2>And the Winner is....</h2>");
        writer.write("<h3>" + winnnerUser + "</h3>");
        writer.write("<h2>Thank you!!!</h2>");
    }

    private List<String> performQuery(String queryString, PrintWriter writer) throws TwitterException {
        List<String> users4Raffle = new ArrayList<>();
        //List of my followers
        Set<User> myFollowers = followersComponent.getFollowers();
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query(queryString);
        QueryResult result;
        writer.write("<h3>Found the following users and tweets: </h3><br/><hr/>");
        do {
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {
                User u = tweet.getUser();
                // Only my followers
                if (myFollowers.contains(u)){
                    writer.write("<b><a href='https://twitter.com/"  + u.getScreenName() + "'>@"
                            + u.getScreenName() + "</a></b> " +
                            "- <i>" + tweet.getText() +
                            " - <b>Follows me?</b> " + myFollowers.contains(u) + "  </i>");
                    writer.write("<hr/>");
                    writer.write("<br/>");
                    users4Raffle.add(u.getScreenName());
                }
            }
        } while ((query = result.nextQuery()) != null);
        return users4Raffle;
    }
}
