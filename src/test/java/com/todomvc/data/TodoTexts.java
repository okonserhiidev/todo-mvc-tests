package com.todomvc.data;

import java.util.List;

public final class TodoTexts {

    public static final String SIMPLE_TEXT_1 = "Buy groceries";
    public static final String SIMPLE_TEXT_2 = "Walk the dog";
    public static final String SIMPLE_TEXT_3 = "Pet the cat";

    public static final String WITH_LEADING_TRAILING_SPACES = "   Trim me   ";
    public static final String TRIMMED = "Trim me";

    public static final String WHITESPACE_ONLY = "     ";

    public static final String UNICODE_EMOJI = "🥛 日本語";

    public static final String LONG_TEXT = "A".repeat(300);

    public static final String UPDATED_TITLE = "Updated text";

    public static final List<String> XSS_PAYLOADS = List.of(
            "<script>alert(1)</script>",
            "<img src=x onerror=alert(1)>",
            "javascript:alert(1)",
            "<svg/onload=alert(1)>",
            "\"><script>alert(1)</script>"
    );

    private TodoTexts() {
    }
}
