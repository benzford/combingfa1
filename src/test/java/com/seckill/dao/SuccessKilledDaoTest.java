package com.seckill.dao;

import com.seckill.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    @Resource
    SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessedKill() throws Exception {
        int i = successKilledDao.insertSuccessedKill(1001L, 13529939393L);
        System.out.println(i);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        SuccessKilled successKilled = this.successKilledDao.queryByIdWithSeckill(1001L, 13529939393L);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }

}