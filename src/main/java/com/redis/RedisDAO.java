package com.redis;

/**
 * Created by Denys Kovalenko on 9/23/2014.
 */
public interface RedisDAO {
    public static final String RECORD_KEY = "key";

    public void save(String key, String value);

    public String retrieve(String key);

    public void delete(String key);
}
