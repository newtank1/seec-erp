package com.nju.edu.erp.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class JSONReader {

    /**
     * 获取json字符串对应的属性
     * @param jsonStr
     * @param property
     * @return
     */
    public static Object getByProperty(String jsonStr, String property) {
        HashMap<String, Object> map;
        try {
            map = new ObjectMapper().readValue(jsonStr, new TypeReference<HashMap<String, Object>>() {});
            if (map.get(property) != null) {
                return map.get(property);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
