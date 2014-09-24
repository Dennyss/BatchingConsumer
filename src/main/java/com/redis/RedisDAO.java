package com.redis;

import java.util.List;

/**
 * Created by Denys Kovalenko on 9/23/2014.
 */
public interface RedisDAO {

    public void save(String key, String value);

    public void saveAll(List<String> entries);

    public String retrieve(String key);

    public void delete(String key);
}
