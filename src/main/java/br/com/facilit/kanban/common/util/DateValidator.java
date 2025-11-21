package br.com.facilit.kanban.common.util;

import static java.time.LocalDate.parse;
import static java.time.format.ResolverStyle.STRICT;

import java.time.format.DateTimeFormatter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DateValidator {

	public static boolean isValid(final String date, final String pattern) {
        if (date == null || pattern == null) {
            return false;
        }

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withResolverStyle(STRICT);

        try {
            parse(date, formatter);
            return true;
        } catch (final Exception e) {
            return false;
        }
    }

}
