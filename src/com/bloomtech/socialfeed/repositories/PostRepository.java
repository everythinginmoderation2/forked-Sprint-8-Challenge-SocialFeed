package com.bloomtech.socialfeed.repositories;

import com.bloomtech.socialfeed.helpers.LocalDateTimeAdapter;
import com.bloomtech.socialfeed.models.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostRepository {
    private static final String POST_DATA_PATH = "src/resources/PostData.json";

    public PostRepository() {
    }

    public List<Post> getAllPosts() {
        List<Post> allPosts = new ArrayList<>();
        //Create Gson instance
//        Gson gson = new Gson();
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

        //Define the type for the ArrayList<Post>
        Type postListType = new TypeToken<List<Post>>() {}.getType();

        //Read JSON file and parse it
        try (FileReader reader = new FileReader(POST_DATA_PATH)) {
            List<Post> posts = gson.fromJson(reader, postListType);
            if (posts != null) {
                allPosts.addAll(posts); // Safely add posts if parsing was successful
            }
        } catch (IOException e) {
            //FIXME: This should probably be replaced with more robust logging
            e.printStackTrace();
        }


        return allPosts;
    }

    public List<Post> findByUsername(String username) {
        return getAllPosts()
                .stream()
                .filter(p -> p.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    public List<Post> addPost(Post post) {
        List<Post> allPosts = getAllPosts();

        //Check that no duplicate posts are added to the allPosts list
        Optional<Post> existingPost = allPosts.stream()
                        .filter(p -> p.getBody().equals(post.getBody()))
                        .findFirst();

        //Throw error if attempt is made to add duplicate post to allPosts list
        if (!existingPost.isEmpty()) {
            throw new RuntimeException("Post with body: " + post.getBody() + " already exists!");
        }
        allPosts.add(post);

        //Write the new Post data to the PostData.json file
        try (FileWriter postDataWriter = new FileWriter(POST_DATA_PATH)) {
            Gson postDataGSON = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .setPrettyPrinting().create();
            String json = postDataGSON.toJson(allPosts);
            postDataWriter.write(json);

        } catch (IOException e) {
            //FIXME: This should probably be replaced with more robust logging
            e.printStackTrace();
        }


        //Return an updated list of all posts
        return allPosts;
    }
}
