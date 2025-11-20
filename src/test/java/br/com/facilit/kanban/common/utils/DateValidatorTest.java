package br.com.facilit.kanban.common.utils;

import static br.com.facilit.kanban.common.util.DateValidator.isValid;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DateValidatorTest {

	@Test
    void isValid_Should_ReturnTrue_When_DateIsValid() {
        assertTrue(isValid("2025-11-30", "uuuu-MM-dd"));
    }

    @Test
    void isValid_Should_ReturnFalse_When_DateIsInvalid() {
        assertFalse(isValid("2025-02-31", "uuuu-MM-dd"));
    }

    @Test
    void isValid_Should_ReturnFalse_When_FormatIsInvalid() {
        assertFalse(isValid("30/11/2025", "uuuu-MM-dd"));
    }

    @Test
    void isValid_Should_ReturnFalse_When_PatternIsNull() {
        assertFalse(isValid("2025-11-30", null));
    }

    @Test
    void isValid_Should_ReturnFalse_When_DateIsNull() {
        assertFalse(isValid(null, "uuuu-MM-dd"));
    }

    @Test
    void isValid_Should_ReturnFalse_When_DateIsEmpty() {
        assertFalse(isValid("", "uuuu-MM-dd"));
    }

}
