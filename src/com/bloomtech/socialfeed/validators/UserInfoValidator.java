package com.bloomtech.socialfeed.validators;

import com.bloomtech.socialfeed.exceptions.UserValidationException;
import com.bloomtech.socialfeed.models.Role;
import com.bloomtech.socialfeed.models.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInfoValidator implements Validator {

    private boolean isValidUsername(String username) {
        //validate username begins with an uppercase letter, is at least 4 characters long, and only contains
        //letters, numbers, and underscores
        String regex = "^[A-Z][a-zA-Z0-9_]{3,}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(username);
        return m.matches();
    }

    private boolean isValidPassword(String password) {
        //validate password contains at least 8 characters, an uppercase, and a lowercase letter.
        //valid symbols include: !@#$%^&*
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    @Override
    public void validate(Object userData) {

        User user = (User) userData;

        if (!isValidUsername(user.getUsername())) {
            throw new UserValidationException("Invalid Username: Username must be at least 4 characters long, " +
                    "must begin with an uppercase letter, and may not contain special characters or spaces!");
        }
        if (!isValidPassword(user.getPassword())) {
            throw new UserValidationException("Invalid Password: Password must be at least 8 characters long, " +
                    "contain at least one uppercase letter, one lowercase letter, and one special character!");
        }
        if (user.getRole() == null) { user.setRole(Role.USER); }
    }
}
