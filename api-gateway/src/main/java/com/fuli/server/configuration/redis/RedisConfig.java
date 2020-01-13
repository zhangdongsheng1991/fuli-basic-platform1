package com.fuli.server.configuration.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Description:    redisConfig，将key和value全部序列化 防止redis的可以出现乱码情况
 * @Author:         xyj
 * @CreateDate:     2019/5/25 17:22
 * @Version:        1.0
*/
@Configuration
public class RedisConfig {

	@Autowired(required=false)
	private LettuceConnectionFactory lettuceConnectionFactory;

	/**
	 * 没有此属性就不会装配bean 如果是单个redis 将此注解注释掉
	 * @return
	 */
	@Primary
	@Bean("redisTemplate")
	@ConditionalOnProperty(name = "spring.redis.cluster.nodes", matchIfMissing = false)
	public RedisTemplate<String, Object> getRedisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(lettuceConnectionFactory);

		RedisSerializer stringSerializer = new StringRedisSerializer();
		RedisSerializer redisObjectSerializer = new RedisObjectSerializer();
		// key的序列化类型
		redisTemplate.setKeySerializer(stringSerializer);
		// value的序列化类型
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.setValueSerializer(redisObjectSerializer);
		// value的序列化类型
		redisTemplate.setHashValueSerializer(redisObjectSerializer);
		redisTemplate.afterPropertiesSet();
		//redisTemplate.opsForValue().set("hello", "world");
		return redisTemplate;

		/*RedisTemplate redisTemplate = new RedisTemplate();
		RedisSerializer stringSerializer = new StringRedisSerializer();
		redisTemplate.setConnectionFactory(lettuceConnectionFactory);
		redisTemplate.setKeySerializer(stringSerializer);
		redisTemplate.setValueSerializer(stringSerializer);
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.setHashValueSerializer(stringSerializer);
		return redisTemplate;*/
	}

	@Primary
	@Bean("redisTemplate")
	@ConditionalOnProperty(name = "spring.redis.host", matchIfMissing = true)
	public RedisTemplate<String, Object> getSingleRedisTemplate( ) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		RedisSerializer redisObjectSerializer = new RedisObjectSerializer();
		redisTemplate.setConnectionFactory(lettuceConnectionFactory);
		// key的序列化类型
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		// value的序列化类型
		redisTemplate.setValueSerializer(redisObjectSerializer);
		redisTemplate.setHashValueSerializer(redisObjectSerializer); 
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

}
