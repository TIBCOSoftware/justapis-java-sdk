package com.anypresence.gw.test;

import org.junit.Assert;

import org.junit.Test;

import com.anypresence.gw.Utilities;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONObject;
import org.json.JSONArray;
import java.net.URI;
import java.net.URISyntaxException;

public class UtilitiesTest {
    @Test
    public void test_UpdateRelativeUrl() {
        String url = Utilities.updateUrl("http://localhost/foo", "bar");

        Assert.assertEquals("http://localhost/foo/bar", url);
    }

    @Test
    public void test_UpdateAbsoluteUrl() {
        String url = Utilities.updateUrl("http://localhost/foo",
                "http://fake.com/bar");

        Assert.assertEquals("http://fake.com/bar", url);
    }

    @Test
    public void test_ParseUrl() {        
        String parsedUri = Utilities.parseDomainFromUrl("http://localhost:1337/api/v1/foo");

        Assert.assertEquals("http://localhost:1337", parsedUri);
    }

    @Test
    public void test_TransformJson() {
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("number", 12334);
        map.put("text", "1245");
        JSONObject obj = Utilities.transformMapToJsonObject(map);

        Assert.assertTrue(obj.toString().matches(".*\"number\":12334.*"));
        Assert.assertTrue(obj.toString().matches(".*\"text\":\"1245\".*"));

    }
}
