package pl.karasdominik.progressTracker;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class InputValidatorTest {

    private static User user;
    private static InputValidator testedValidator;

    @BeforeClass
    public static void createUser(){
        user = new User();
        testedValidator = new InputValidator();
    }

    @Test
    public void canCreateUserWithoutLogin(){
        user.setUsername("");
        user.setPassword("ValidPassword");

        assertFalse(testedValidator.isUsernameAndPasswordValid(user));
    }

    @Test
    public void canCreateUserWithoutPassword(){
        user.setUsername("ValidLogin");
        user.setPassword(" ");

        assertFalse(testedValidator.isUsernameAndPasswordValid(user));
    }

    @Test
    public void canCreateUsernameWithPolishCharacters(){
        user.setUsername("Usernameźćę");
        user.setPassword("ValidPassword");

        assertFalse(testedValidator.isUsernameAndPasswordValid(user));
    }

    @Test
    public void canCreateUserWithTwoCharactersUsername(){
        user.setUsername("do");
        user.setPassword("ValidPassword");

        assertFalse(testedValidator.isUsernameAndPasswordValid(user));
    }

    @Test
    public void canCreateUserWithThirtyCharactersUsername(){
        user.setUsername("UserUserUserUserUserUserUserUs");
        user.setPassword("ValidPassword");

        assertFalse(testedValidator.isUsernameAndPasswordValid(user));
    }

    @Test
    public void canCreateUserWithThreeCharactersPassword(){
        user.setUsername("ValidUsername");
        user.setPassword("Pass");

        assertFalse(testedValidator.isUsernameAndPasswordValid(user));
    }

    @Test
    public void canCreateUserWithThirtyCharactersPassword(){
        user.setUsername("ValidUsername");
        user.setPassword("PasswPasswPasswPasswPasswPassw");

        assertFalse(testedValidator.isUsernameAndPasswordValid(user));
    }

    @Test
    public void canCreateUserWithValidCredentails(){
        user.setUsername("validUsername");
        user.setPassword("validPassword");

        assertTrue(testedValidator.isUsernameAndPasswordValid(user));
    }
}