package com.nju.edu.erp.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONReaderTest {

    @Test
    void getByProperty() {
        String metadata="{\n" +
                "\"check\":[\"1\",\"2\"],\n" +
                "\"exec\":{\n" +
                "\"pid\":\"3\",\n" +
                " \"num\":\"4\"\n" +
                " }\n" +
                "}";
        ArrayList<String> expectCheck=new ArrayList<>();
        expectCheck.add("1");
        expectCheck.add("2");
        assertEquals(expectCheck,JSONReader.getByProperty(metadata, "check"));
        HashMap<String,String> expectExec=new HashMap<>();
        expectExec.put("pid","3");
        expectExec.put("num","4");
        HashMap<String,String> actual = (HashMap<String, String>) JSONReader.getByProperty(metadata, "exec");
        assertEquals(expectExec,actual);
    }
}