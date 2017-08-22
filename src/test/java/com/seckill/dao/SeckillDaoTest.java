package com.seckill.dao;

import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    @Resource
    SeckillDao seckillDao;
    @Test
    public void queryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
        /*
        1000元秒杀iphone6
Seckill{SeckillId=1000, name='1000元秒杀iphone6', number=100, startTime=Thu Jan 01 00:00:00 CST 2015, endTime=Fri Jan 02 00:00:00 CST 2015, createTime=Sun Aug 20 17:35:06 CST 2017}

         */
    }

    @Test
    public void reduceNumber() throws Exception {
        Date date = new Date();
        int i = seckillDao.reduceNumber(1000L, date);
        System.out.println(i);
    }


    @Test
    public void queryAll() throws Exception {
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for (Seckill seckill : seckills) {
            System.out.println(seckill);
        }
    }

}