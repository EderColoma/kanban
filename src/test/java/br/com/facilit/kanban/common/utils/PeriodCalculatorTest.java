package br.com.facilit.kanban.common.utils;

import static br.com.facilit.kanban.common.util.PeriodCalculator.calculateRemainingPercentage;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class PeriodCalculatorTest {

	@Test
    void calculateRemainingPercentage_Should_ReturnZero_When_StartDateIsNull() {
        assertEquals(0, calculateRemainingPercentage(null, now().plusDays(10)));
    }

    @Test
    void calculateRemainingPercentage_Should_ReturnZero_When_EndDateIsNull() {
        assertEquals(0, calculateRemainingPercentage(now(), null));
    }

    @Test
    void calculateRemainingPercentage_Should_ReturnZero_When_TodayIsAfterEndDate() {
        assertEquals(0, calculateRemainingPercentage(now().minusDays(5), now().minusDays(1)));
    }

    @Test
    void calculateRemainingPercentage_Should_ReturnZero_When_TotalDaysIsZero() {
        final LocalDate today = now();
        final long result = calculateRemainingPercentage(today, today);
        assertEquals(0, result);
    }

    @Test
    void calculateRemainingPercentage_Should_Return_ZeroWhen_EndDateIsBeforeStartDate() {
        final LocalDate today = now();
        final long result = calculateRemainingPercentage(today.plusDays(5), today);
        assertEquals(0, result);
    }

    @Test
    void calculateRemainingPercentage_Should_Return100_When_TodayIsBeforeStartDate() {
        assertEquals(100, calculateRemainingPercentage(now().plusDays(5), now().plusDays(15)));
    }

    @Test
    void calculateRemainingPercentage_Should_ReturnPercentage_When_PeriodIsValid() {
        final LocalDate start = now().minusDays(2);
        final LocalDate end = now().plusDays(8);

        assertEquals(80, calculateRemainingPercentage(start, end));
    }

    @Test
    void calculateRemainingPercentage_Should_ReturnZero_When_TodayEqualsEndDate() {
        final LocalDate start = now().minusDays(5);
        final LocalDate end = now();

        assertEquals(0, calculateRemainingPercentage(start, end));
    }

    @Test
    void calculateRemainingPercentage_Should_NotExceed100() {
        assertEquals(100, calculateRemainingPercentage(now().plusDays(5), now().plusDays(6)));
    }

    @Test
    void calculateRemainingPercentage_Should_NotBeLessThanZero() {
        assertEquals(0, calculateRemainingPercentage(now().minusDays(10), now().minusDays(1)));
    }

}
