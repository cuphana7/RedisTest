package com.si;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by medialog on 2017. 5. 22..
 */
public class SenderTest {

    @Test
    public void testRedisConnect(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisPool pool = new JedisPool( "221.143.48.226", 6379);

        Jedis jedis = pool.getResource();

        jedis.set("foo", "bar");
        String value = jedis.get("foo");
        assertTrue( value != null && value.equals("bar") );

        jedis.del("foo");
        value = jedis.get("foo");
        assertTrue( value == null );

        if( jedis != null ){
            jedis.close();
        }
    }
}
