package com.seckill.service.impl;

import com.seckill.dao.SeckillDao;
import com.seckill.dto.Exposer;
import com.seckill.entity.Seckill;
import com.seckill.entity.SuccessKilled;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import com.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class SeckillServiceImpl implements SeckillService {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    private SeckillDao seckillDao;
    private SuccessKilled successKilled;
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if(seckill==null){
            return new Exposer(false,seckillId);
        }
        Date startTime=seckill.getStartTime();
        Date endTime=seckill.getEndTime();
        Date now =new Date();
        if(now.getTime()<startTime.getTime()||now.getTime()>endTime.getTime()){
            return new Exposer(false,seckillId,now.getTime(),startTime.getTime(),endTime.getTime());
        }
        String md5=null;//TODO
        return new Exposer(true,md5,seckillId);
    }

    public void executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, SeckillCloseException, RepeatKillException {

    }
}
