package com.anypresence.gw.test;

import org.junit.Assert;

import org.junit.Test;

import com.anypresence.gw.Utilities;

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
}
