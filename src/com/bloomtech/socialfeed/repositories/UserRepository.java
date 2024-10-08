package com.bloomtech.socialfeed.repositories;

import com.bloomtech.socialfeed.models.User;
import com.bloomtech.socialfeed.validators.UserInfoValidator;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;



public class UserRepository {
    private static final String USER_DATA_PATH = "src/resources/UserData.json";

    private static final UserInfoValidator userInfoValidator = new UserInfoValidator();

    public UserRepository() {
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();

        // Create Gson instance
        Gson gson = new Gson();

        // Define the type for the ArrayList<User>
        Type userListType = new TypeToken<List<User>>() {}.getType();

        // Read JSON file and parse it
        try (FileReader reader = new FileReader(USER_DATA_PATH)) {
            List<User> users = gson.fromJson(reader, userListType);
            if (users != null) {
                allUsers.addAll(users); // Safely add users if parsing was successful
            }

        } catch (IOException e) {
            //FIXME: This should probably be replaced with more robust logging
            e.printStackTrace();
        }

        return allUsers;
    }

    public Optional<User> findByUsername(String username) {
        return getAllUsers()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public void save(User user) {
        List<User> allUsers = getAllUsers();

        Optional<User> existingUser = allUsers.stream()
                .filter(u -> u.getUsername().equals(user.getUsername()))
                .findFirst();

        if (!existingUser.isEmpty()) {
            throw new RuntimeException("User with name: " + user.getUsername() + " already exists!");
        }


        allUsers.add(user);
        //Write allUsers to UserData.json
        try (FileWriter userDataWriter = new FileWriter(USER_DATA_PATH)) {
            Gson userDataGSON = new GsonBuilder().setPrettyPrinting().create();
            String json = userDataGSON.toJson(allUsers);
            userDataWriter.write(json);


        } catch (IOException e) {
            //FIXME: This should probably be replaced with more robust logging
            e.printStackTrace();
        }

    }
}



