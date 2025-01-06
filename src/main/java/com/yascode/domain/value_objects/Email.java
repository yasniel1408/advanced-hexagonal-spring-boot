package com.yascode.domain.value_objects;

import com.yascode.domain.errors.InvalidEmailException;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

@Getter @Setter
public class Email {
    private final String email;

    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public Email(String email) {
        if (!isValid(email)) {
            throw new InvalidEmailException(email);
        }
        this.email = email;
    }

    public static boolean isValid(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public String toString() {
        return "Email: " + email;
    }
}