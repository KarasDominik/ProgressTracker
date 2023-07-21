package pl.karasdominik.progressTracker;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class InputValidator {

    private static final int MIN_NUMBER_OF_CHARACTERS_FOR_INPUT = 6;
    private static final int MAX_NUMBER_OF_CHARACTERS_FOR_INPUT = 20;

    private static final String ALLOWED_CHARACTERS_PATTERN = "^[a-zA-Z0-9!@#$%^&*()_+-]+$";
    private static final Pattern pattern = Pattern.compile(ALLOWED_CHARACTERS_PATTERN);

    public boolean isUsernameAndPasswordValid(User user){
        return isInputValid(user.getUsername()) && isInputValid(user.getPassword());
    }

    private boolean isInputValid(String input) {
        return hasAppropriateNumberOfCharacters(input) && containsOnlyAllowedCharacters(input);
    }

    private boolean hasAppropriateNumberOfCharacters(String input){
        return input.length() >= MIN_NUMBER_OF_CHARACTERS_FOR_INPUT &&
                input.length() <= MAX_NUMBER_OF_CHARACTERS_FOR_INPUT;
    }

    private boolean containsOnlyAllowedCharacters(String input){
        return pattern.matcher(input).matches();
    }
}

