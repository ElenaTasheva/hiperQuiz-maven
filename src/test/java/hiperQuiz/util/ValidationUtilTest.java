package hiperQuiz.util;

import hiperQuiz.model.enums.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {


    @Test
    void validateStringReturnsTrueWhenStringIsValid() {
        boolean validate;
       validate =   ValidationUtil.validateString("test", 3, 20);
       assertTrue(validate);
        validate = ValidationUtil.validateString("no", 3, 20);
        assertFalse(validate);

    }

    @Test
    void validateGenderReturnsGenderIfTheInputIsCorrect() {
        String female = "f";
        String male = "male";
        Gender gender1 = ValidationUtil.validateGender(female);
        Gender gender2 = ValidationUtil.validateGender(male);
        assertEquals(Gender.FEMALE, gender1);
        assertEquals(Gender.MALE, gender2);


    }

    @Test
    void validateGenderReturnsNullWhenInputIsNotCorrect() {
        String female = "not correct";
        Gender gender1 = ValidationUtil.validateGender(female);
        assertNull(gender1);


    }

    @Test
    @DisplayName("Returns true of false if email maches regex")
    void validateEmail() {
        assertTrue(ValidationUtil.validateEmail("elena@abv.bg"));
        assertFalse(ValidationUtil.validateEmail("12a"));

    }

    @Test
    @DisplayName("Returns true or false if the input is a number")
    void validateInt() {
        assertFalse(ValidationUtil.validateInt("a"));
        assertTrue(ValidationUtil.validateInt("1"));
    }
}