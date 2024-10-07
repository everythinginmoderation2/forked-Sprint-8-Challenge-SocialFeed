package com.bloomtech.socialfeed.validators;

import com.bloomtech.socialfeed.exceptions.EmailValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements Validator {
    public EmailValidator() {
    }

    private boolean isValidEmail(String email) {
        //Validate that email begins with a letter or number,
        // contains only letters, numbers, "." and "_", and
        //that it follows the pattern of name@domain.identifier
        String regex = "^([a-zA-Z]|[0-9])([a-zA-Z0-9._]+)@([a-zA-Z]+)\\.([a-zA-Z]+)$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    @Override
    public void validate(Object emailData) {
        String email = (String) emailData;
        if (!isValidEmail(email)) {
            throw new EmailValidationException("Invalid Email: Email address must include '@' before domain and a domain identifier after a '.'!");
        }
    }
}
