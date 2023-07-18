package pl.karasdominik.progressTracker;

import java.util.regex.Pattern;

public class InputValidator {

    private static final String ALLOWED_CHARACTERS_PATTERN = "^[a-zA-Z0-9!@#$%^&*()_+-]+$";
    private static final Pattern pattern = Pattern.compile(ALLOWED_CHARACTERS_PATTERN);

    public static boolean isLoginValid(String login) {
        return pattern.matcher(login).matches();
    }

    public static boolean isPasswordValid(String password) {
        return pattern.matcher(password).matches();
    }
}
