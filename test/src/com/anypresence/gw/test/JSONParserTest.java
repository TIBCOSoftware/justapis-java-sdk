package com.anypresence.gw.test;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.anypresence.gw.JSONParser;

public class JSONParserTest {

	@Test
	public void test_ParseMap() {
		String data = "{'apple': 'yummy', 'tofu':'ok'}";
		
		Map<String,String> val = new JSONParser().parseData(data);
		Assert.assertTrue(val.get("apple").equals("yummy"));
		Assert.assertTrue(val.get("tofu").equals("ok"));
	}
	
}
