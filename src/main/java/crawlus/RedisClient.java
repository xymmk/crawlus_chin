package crawlus;

import java.util.Map;

import redis.clients.jedis.Jedis;

public class RedisClient {
	private Jedis jedis = null;
	public RedisClient(){
	    if(this.jedis == null){
	    	this.jedis = new Jedis("localhost");
	    }
	}
	public void saveObject(String key, String value){
		this.jedis.set(key, value);
	}
	
	public String getRedisObject(String key){
		return this.jedis.get(key);
	}
	public Jedis getRedis(){
		return this.jedis;
	}

}
