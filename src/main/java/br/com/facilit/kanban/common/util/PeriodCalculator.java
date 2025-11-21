package br.com.facilit.kanban.common.util;

import static java.lang.Math.max;
import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Objects.isNull;

import java.time.LocalDate;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PeriodCalculator {

	public static int calculateRemainingPercentage(final LocalDate startDate, final LocalDate endDate) {
		if (isNull(startDate) || isNull(endDate)) {
            return 0;
        }

        final LocalDate today = now();

        if (today.isAfter(endDate)) {
            return 0;
        }

        final long totalDays = DAYS.between(startDate, endDate);

        if (totalDays <= 0) {
            return 0;
        }

        final long daysRemaining = DAYS.between(today, endDate);

        if (daysRemaining <= 0) {
            return 0;
        }

        if (today.isBefore(startDate)) {
            return 100;
        }

        final long percent = (daysRemaining * 100) / totalDays;
        return (int) max(percent, Math.min(0, 100));
    }

}
