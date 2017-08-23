package com.seckill.service;

import com.seckill.AppStart;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppStart.class)
public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckillList = seckillService.getSeckillList();
        logger.info("list={}", seckillList);
        //non transactional SqlSession
    }

    @Test
    public void getById() throws Exception {
        long id = 1000L;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}", seckill);
    }
    //集成测试代码完整逻辑,注意:可重复执行
    @Test
    public void exportSeckillUrl() throws Exception {
        long id = 1001L;
        Exposer exposer = seckillService.exposeSeckillUrl(id);
        if (exposer.isExposed()) {
            logger.info("exposer={}", exposer);
            long phone = 13523234343L;
            String md5 = "ac3413781015dc336014bd9016450799";
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(id, phone, md5);
                logger.info("result={}", seckillExecution);
            } catch (RepeatKillException e1) {
                logger.error(e1.getMessage());
            } catch (SeckillCloseException e2) {
                logger.error(e2.getMessage());
            }
            //SeckillExecution{seckillId=1000,
            // state=1,
            // stateInfo='秒杀成功',
            // successKilled=SuccessKilled{seckillId=1000,
            // userPhone=13523234343, state=0, createTime=Wed Aug 23 07:36:32 CST 2017}}
        } else {
            //秒杀未开启
            logger.warn("exposer={}", exposer);
        }
        //Exposer{exposed=true,
        // seckillId=1000,
        // md5='ac3413781015dc336014bd9016450799',
        // now=0, start=0, end=0}
    }

    @Test
    public void executeSeckill() throws Exception {
        long id = 1000L;
        long phone = 13523234343L;
        String md5 = "ac3413781015dc336014bd9016450799";
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(id, phone, md5);
            logger.info("result={}", seckillExecution);
        } catch (RepeatKillException e1) {
            logger.error(e1.getMessage());
        } catch (SeckillCloseException e2) {
            logger.error(e2.getMessage());
        }

    }

}