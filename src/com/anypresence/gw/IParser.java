package com.anypresence.gw;

import java.util.Map;

public interface IParser {
    public <T> T parse(String mapping, Class<T> clazz);
}
