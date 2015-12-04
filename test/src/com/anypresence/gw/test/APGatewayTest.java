package com.anypresence.gw.test;

import java.net.HttpURLConnection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.anypresence.gw.*;

import static org.mockito.Mockito.*;

public final class APGatewayTest {
	@Mock
	HttpURLConnection mockHttpConnection;
	
	
	@Test
	public void test_BuilderSettings() {
		APGateway.Builder builder = new APGateway.Builder();
		builder.url("http://localhost");
		
		APGateway gw = builder.build();
		Assert.assertEquals("http://localhost", gw.getUrl());
	}
	
	@Test
	public void test_Connect() {
		APGateway.Builder builder = new APGateway.Builder();
		builder.url("http://localhost:3000/api/v1/foo");
		builder.method(HTTPMethod.GET);
		
		APGateway gw = builder.build();
		
		gw.execute();
		String response = gw.readResponse();
		
		Assert.assertTrue(!response.isEmpty());
	}
	
}
