package com.seckill.service.impl;

import com.seckill.dao.SeckillDao;
import com.seckill.dao.SuccessKilledDao;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.entity.SuccessKilled;
import com.seckill.enums.SeckillStateEnum;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import com.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * 哈哈
 */
public class SeckillServiceImpl implements SeckillService {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    private SeckillDao seckillDao;
    private SuccessKilledDao successKilledDao;
    private final String salt="asdfasfljk1@#sdf123";
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }
    private String getMD5(long seckillId){
        String base =seckillId+"/"+salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
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
        String md5=getMD5(seckillId);//TODO
        return new Exposer(true,md5,seckillId);
    }

    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, SeckillCloseException, RepeatKillException {
        if(md5==null||!md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑:减库存+记录购买行为
        Date nowDate = new Date();

        try {
            int reduceNumber = seckillDao.reduceNumber(seckillId, nowDate);
            if(reduceNumber<=0){
                //没有更新到记录,秒杀结束
                throw new SeckillException("seckill closed");
            }else{
                //更新秒杀记录
                int successedKill = successKilledDao.insertSuccessedKill(seckillId, userPhone);
                if(successedKill<=0){
                    //重复秒杀
                    throw new RepeatKillException("重复秒杀");
                }else{
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);

                }

            }
        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            //所有编译期异常,转化为运行期异常
            throw new SeckillException("seckill error"+e.getMessage());
        }
    }
}
