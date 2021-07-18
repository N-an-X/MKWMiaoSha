package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JedisPool jedisPool;

    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    //获取秒杀对象
    public Seckill getSeckill(long seckillId) {
        //Redis操作的逻辑
        try {
            Jedis jedis = jedisPool.getResource();
           try {
                String key = "seckill:" + seckillId;

             //没有实现内部序列化
               byte[] bytes = jedis.get(key.getBytes());
               if(bytes != null) {
                   Seckill seckill = schema.newMessage();
                   ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                   return seckill;
               }
               jedis.get(key);
           }finally {
               jedis.close();
           }
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    //放入秒杀对象
    public String putSeckill(Seckill seckill) {
        //set Object(Seckill) -> byte[]->发送给redis,序列化
        try{
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(
                        seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存
                int timeout = 60 * 60;//一小时
                return jedis.setex(key.getBytes(), timeout, bytes);
            }finally {
                jedis.close();
            }
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
