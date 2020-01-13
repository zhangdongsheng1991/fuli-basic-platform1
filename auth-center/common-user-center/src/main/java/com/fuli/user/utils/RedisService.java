package com.fuli.user.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description:    RedisService操作
 * @Author:         FZ
 * @CreateDate:     2019/4/18 11:47
 * @Version:        1.0
*/
@Component
public class RedisService {

	@Autowired
	public RedisTemplate redisTemplate;
    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 写入缓存设置时效时间
     * @param key
     * @param value
     * @param expireTime : 时间秒
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 批量删除对应的value
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0){
            redisTemplate.delete(keys);
        }
    }
    /**
     * 删除对应的value
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }
    /**
     * 读取缓存
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }
    /**
     * 哈希 添加
     * @param key
     * @param hashKey
     * @param value
     */
    public void hmSet(String key, Object hashKey, Object value){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key,hashKey,value);
    }

    /**
     * 哈希获取数据
     * @param key
     * @param hashKey
     * @return
     */
    public Object hmGet(String key, Object hashKey){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key,hashKey);
    }

    /**
     * 哈希添加Map
     * @param key
     * @param obj
     */
    public void hmSet(String key, Map<String,Object> obj){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.putAll(key,obj);
    }

    /**
     * 哈希获取Map数据
     * @param key
     * @return
     */
    public Map<Object, Object> hmGet(String key){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.entries(key);
    }

    /**
     * 列表添加
     * @param k
     * @param v
     */
    public void lPush(String k,Object v){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k,v);
    }

    /**
     * 列表获取
     * @param k
     * @param l
     * @param l1
     * @return
     */
    public List<Object> lRange(String k, long l, long l1){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k,l,l1);
    }

    /**
     * 集合添加
     * @param key
     * @param value
     */
    public void add(String key,Object value){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key,value);
    }

    /**
     * 集合获取
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 有序集合添加
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key,Object value,double scoure){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key,value,scoure);
    }

    /**
     * 有序集合获取
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> rangeByScore(String key,double scoure,double scoure1){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }

    /**
     * 设置过期时间
     * @param key 键值
     * @param expireTime 过期时间
     * @param timeUnit 过期时间单位
     */
    public void expire(Object key,long expireTime, TimeUnit timeUnit) {
    	redisTemplate.expire(key, expireTime, timeUnit);
    }

    /**
     * 获取过期时间
     *
     * @param key      键值
     * @param timeUnit 时间单位
     */
    public Long getExpireDateTime(Object key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     *  集合使用
     * @author      WFZ
     * @param       key ：
     * @param       hk ： 键
     * @param       object ： 值
     * @return      Result
     * @date        2019/9/10 18:39
     */
    public void boundHashOps(String key , String hk , Object object){
        redisTemplate.boundHashOps(key).put(hk, JSONObject.toJSONString(object));
    }

    /**
     *  根据键获取集合中指定值
     * @author      WFZ
     * @param       key ：
     * @param       hk ： 键
     * @return      Result
     * @date        2019/9/10 18:39
     */
    public String boundHashOps(String key , String hk){
       return (String) redisTemplate.boundHashOps(key).get(hk);
    }

    /**
     *  根据键删除指定值
     * @author      WFZ
     * @param       key ：
     * @param       hk ： 键
     * @return      Result
     * @date        2019/9/10 18:39
     */
    public void removeRedisCache(String key , String hk){
        redisTemplate.boundHashOps(key).delete(hk);
    }

}
