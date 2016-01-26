package com.anypresence.gw.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.anypresence.gw.JSONParser;

public class JSONParserTest {

    @Test
    public void test_ParseMap() {
        String data = "{'apple': 'yummy', 'tofu':'ok'}";

        @SuppressWarnings("unchecked")
        HashMap<String, String> val = new JSONParser().parse(data,
                HashMap.class);
        Assert.assertTrue(val.get("apple").equals("yummy"));
        Assert.assertTrue(val.get("tofu").equals("ok"));
    }

    @Test
    public void test_ParseJson() {
        String data = "{'apple': 'yummy', 'tofu':'ok'}";

        JSONParser jsonParser = new JSONParser();
        HashMap<String, String> result = jsonParser.parse(data, HashMap.class);

        Assert.assertTrue(result.get("apple").equals("yummy"));
    }

}
