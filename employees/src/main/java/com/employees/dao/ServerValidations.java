
package com.employees.dao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ServerValidations {

    private static final String PREFIX = "TEK";

    public String generateId(JSONArray employees) {

        if (employees == null || employees.isEmpty()) {
            return PREFIX + "1";
        }

        JSONObject last = (JSONObject) employees.get(employees.size() - 1);
        String lastId = (String) last.get("id");

        int num = 1;
        if (lastId != null && lastId.startsWith(PREFIX)) {
            num = Integer.parseInt(lastId.substring(PREFIX.length())) + 1;
        }

        return PREFIX + num;
    }
}

