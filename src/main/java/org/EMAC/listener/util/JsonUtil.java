package org.EMAC.listener.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class JsonUtil {
    private static final Gson gson = new Gson();

    public static String convertToJson(List<Long> timestamps) {
        return gson.toJson(timestamps);
    }

    public static List<Long> convertFromJson(String json) {
        Type listType = new TypeToken<List<Long>>(){}.getType();
        return gson.fromJson(json, listType);
    }
}

