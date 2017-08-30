package com.seckill.dao.cache;

import com.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {
    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    private JedisPool jedisPool;

    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    public Seckill getSeckill(long seckillId) {
        //redis操作逻逻辑
        try {
            Jedis jedis=jedisPool.getResource();
            try {
                String key="seckill:"+seckillId;
                //并没有实现内部序列化操作
                //典型缓存的逻辑是这样的
                //get ->byte() ->反序列化->Object(Seckill)
                //java的各种序列化,google的protoBuffer性能最好->protoStuff
                //很多序列化新能的指标: (1)序列化字节数最少
                //采用自定义序列化

            } finally {
                jedis.close();
            }
        } catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public String putSeckillId(Seckill seckill) {
        return null;
    }
}
