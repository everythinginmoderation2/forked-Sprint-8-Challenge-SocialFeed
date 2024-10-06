package com.bloomtech.socialfeed.observerpattern;

import com.bloomtech.socialfeed.App;
import com.bloomtech.socialfeed.models.Post;
import com.bloomtech.socialfeed.models.User;

import java.util.ArrayList;
import java.util.List;

//TODO: Implement Observer Pattern
public class OUserFeed implements Observer {
    private User user;
    private List<Post> feed;

    public OUserFeed(User user) {
        this.user = user;
        this.feed = new ArrayList<>();
        //TODO: update OUserFeed in constructor after implementing observer pattern
        App.sourceFeed.attach(this);
    }

    public User getUser() {
        return user;
    }

    public List<Post> getFeed() {
        return feed;
    }

    @Override
    public void update() {
        List <Post> allPosts = App.sourceFeed.getPosts();
        List <String> usernameList = user.getFollowing();

        if (user != null) {
            for (Post post : allPosts) {
                for (String username : usernameList) {
                    if (username.equals(post.getUsername())) {
                        feed.add(post);
                    }
                }
            }
        }
    }
}
