package com.imooc.api.config;

import com.github.tomakehurst.wiremock.common.Json;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.extend.RedisCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author hp
 * @date 2023-03-08 22:17
 * @explain
 */

@Component
public class RedisOperator {
   @Autowired
   private RedisTemplate<String, Object> redisTemplate;

   public boolean keyIsExit(String key){
      return redisTemplate.hasKey(key);
   }

   public long ttl(String key){
      return redisTemplate.getExpire(key);
   }

   public void expire(String key,long timeout){
      redisTemplate.expire(key,timeout, TimeUnit.SECONDS);
   }

   public long increment(String key,long delta){
      return redisTemplate.opsForValue().increment(key,delta);
   }

   public long decrement(String key,long delta){
      return redisTemplate.opsForValue().decrement(key,delta);
   }

   public Set<String> keys(String patterns){
      return redisTemplate.keys(patterns);
   }

   public void del(String key){
      redisTemplate.delete(key);
   }

   public void set(String key,String value){
      redisTemplate.opsForValue().set(key,value);
   }

   public void set(String key,String value,long timeout){
      redisTemplate.opsForValue().set(key,value,timeout,TimeUnit.SECONDS);
   }

   public void setnx60s(String key,String value){
      redisTemplate.opsForValue().setIfAbsent(key,value,60,TimeUnit.SECONDS);
   }

   public void setnx(String key,String value){
      redisTemplate.opsForValue().setIfAbsent(key,value);
   }
   
   public String get(String key){
     return (String) redisTemplate.opsForValue().get(key);
   }

   public List<String> multiGet(List<String> keys){
      List<Object> objects = redisTemplate.opsForValue().multiGet(keys);
      String objectToJson = JsonUtils.objectToJson(objects);
      List<String> stringList = JsonUtils.jsonToList(objectToJson, String.class);
      return stringList;
   }

   public List<Object> batchGet(List<String> keys) {

//		nginx -> keepalive
//		redis -> pipeline

      List<Object> result = redisTemplate.executePipelined(new RedisCallback<String>() {
         @Override
         public String doInRedis(RedisConnection connection) throws DataAccessException {
            StringRedisConnection src = (StringRedisConnection)connection;

            for (String k : keys) {
               src.get(k);
            }
            return null;
         }
      });

      return result;
   }

   public Integer getNum(String key){
      long increment = redisTemplate.opsForValue().increment(key, 0);
      return (int)increment;
   }
   // Hash（哈希表）

   /**
    * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
    *
    * @param key
    * @param field
    * @param value
    */
   public void hset(String key, String field, Object value) {
      redisTemplate.opsForHash().put(key, field, value);
   }

   /**
    * 实现命令：HGET key field，返回哈希表 key中给定域 field的值
    *
    * @param key
    * @param field
    * @return
    */
   public String hget(String key, String field) {
      return (String) redisTemplate.opsForHash().get(key, field);
   }

   /**
    * 实现命令：HDEL key field [field ...]，删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
    *
    * @param key
    * @param fields
    */
   public void hdel(String key, Object... fields) {
      redisTemplate.opsForHash().delete(key, fields);
   }

   /**
    * 实现命令：HGETALL key，返回哈希表 key中，所有的域和值。
    *
    * @param key
    * @return
    */
   public Map<Object, Object> hgetall(String key) {
      return redisTemplate.opsForHash().entries(key);
   }

   // List（列表）

   /**
    * 实现命令：LPUSH key value，将一个值 value插入到列表 key的表头
    *
    * @param key
    * @param value
    * @return 执行 LPUSH命令后，列表的长度。
    */
   public long lpush(String key, String value) {
      return redisTemplate.opsForList().leftPush(key, value);
   }

   /**
    * 实现命令：LPOP key，移除并返回列表 key的头元素。
    *
    * @param key
    * @return 列表key的头元素。
    */
   public String lpop(String key) {
      return (String) redisTemplate.opsForList().leftPop(key);
   }

   /**
    * 实现命令：RPUSH key value，将一个值 value插入到列表 key的表尾(最右边)。
    *
    * @param key
    * @param value
    * @return 执行 LPUSH命令后，列表的长度。
    */
   public long rpush(String key, String value) {
      return redisTemplate.opsForList().rightPush(key, value);
   }


}
