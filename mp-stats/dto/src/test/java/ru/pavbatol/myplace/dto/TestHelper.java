package ru.pavbatol.myplace.dto;

import java.util.HashMap;
import java.util.Map;

public class TestHelper {
    public static Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = idx > 0 ? pair.substring(0, idx) : pair;
            String value = idx > 0 && pair.length() > idx + 1 ? pair.substring(idx + 1) : null;
            queryParams.put(key, value);
        }

        return queryParams;
    }
}
