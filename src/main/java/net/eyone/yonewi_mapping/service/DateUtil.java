package net.eyone.yonewi_mapping.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public final class DateUtil {
    private DateUtil() {}

    // Accept formats like "DD/MM/YYYY HH:mm" or "DD/MM/YYYY HH:mm:ss" or ISO already
    private static final DateTimeFormatter[] INPUT_FORMATS = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.FRANCE),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.FRANCE),
            DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
    };

    private static final DateTimeFormatter ISO_INSTANT = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static String toIsoDateTime(String input) {
        if (input == null || input.isBlank()) return null;
        for (DateTimeFormatter fmt : INPUT_FORMATS) {
            try {
                LocalDateTime ldt = LocalDateTime.parse(input.trim(), fmt);
                return ldt.atZone(ZoneId.systemDefault()).toOffsetDateTime().format(ISO_INSTANT);
            } catch (DateTimeParseException ignored) {
            }
        }
        // As a fallback, return input (maybe already ISO or date-only)
        return input;
    }
}
