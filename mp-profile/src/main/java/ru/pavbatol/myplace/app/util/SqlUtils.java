package ru.pavbatol.myplace.app.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SqlUtils {
    private static final char DEFAULT_ESCAPE_CHAR = '!';

    /**
     * Escapes special LIKE pattern characters ({@link #DEFAULT_ESCAPE_CHAR}, %, _) by prefixing them with the escape character.
     *
     * @param input the string to escape (null returns null)
     * @return escaped string, or original if no special chars found
     * @see #DEFAULT_ESCAPE_CHAR
     */
    public static String escapeSqlLikeWildcards(String input) {
        return escapeSqlLikeWildcards(input, DEFAULT_ESCAPE_CHAR);
    }

    /**
     * Escapes special LIKE pattern characters (%, _, and the specified escape character)
     * by prefixing them with the escape character.
     *
     * <p><b>Performance note:</b> Uses lazy initialization of StringBuilder - it will only
     * be allocated if escaping is actually needed. For strings without special characters,
     * the original string is returned immediately.
     *
     * <p>Example usage with escapeChar='!':
     * <pre>{@code
     * "50%"       → "50!%"
     * "file_"     → "file!_"
     * "hello!"    → "hello!!"
     * "plaintext" → "plaintext" (no allocation)
     * }</pre>
     *
     * @param input      the string to escape (null returns null)
     * @param escapeChar the escape character to use
     * @return escaped string, or original if no special chars found
     * @see #DEFAULT_ESCAPE_CHAR
     */
    public static String escapeSqlLikeWildcards(String input, char escapeChar) {
        if (input == null) {
            return null;
        }

        StringBuilder sb = null;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (isLikeWildcard(c, escapeChar)) {
                if (sb == null) {
                    sb = new StringBuilder(input.length() + 16);
                    sb.append(input, 0, i);
                }
                sb.append(escapeChar);
            }
            if (sb != null) {
                sb.append(c);
            }
        }

        return (sb != null) ? sb.toString() : input;
    }

    /**
     * Checks if the character is a special character that needs escaping in SQL LIKE.
     *
     * @param c          the character to check
     * @param escapeChar the current escape character (also needs escaping)
     * @return true if the character is %, _, or matches the escapeChar
     */
    private static boolean isLikeWildcard(char c, char escapeChar) {
        return c == '%' || c == '_' || c == escapeChar;
    }
}
