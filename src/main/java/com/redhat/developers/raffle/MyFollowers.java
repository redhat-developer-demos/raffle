package com.redhat.developers.raffle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import twitter4j.*;

import java.util.HashSet;
import java.util.Set;

@Component
public class MyFollowers {

    private static Set<User> followers = null;
    private Log log = LogFactory.getLog(RaffleController.class.getName());

    public Set<User> getFollowers() throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        log.info("Collecting the list of my followers: " + twitter.getScreenName());
        if (followers == null){
            followers = new HashSet<>();
            long cursor = -1;
            PagableResponseList<User> users;
            do {
                PagableResponseList<User> usersResponse = twitter.getFollowersList(twitter.getId(), cursor, 200, true, false);
                cursor = usersResponse.getNextCursor();
                followers.addAll(usersResponse);
            } while ( cursor > 0);
            log.info("Followers loaded. Count: " + followers.size());
        }
        return followers;
    }
}
