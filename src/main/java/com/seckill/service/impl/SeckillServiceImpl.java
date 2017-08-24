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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.xml.ws.ServiceMode;
import java.util.Date;
import java.util.List;

/**
 * 哈哈
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
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

    public Exposer exposeSeckillUrl(long seckillId) {
        //优化点:缓存优化
        /**
         * get from cache
         * if null
         *      get db
         * else
         *      cache
         * login
         */
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
    @Transactional
    /**
     * 使用注解控制事务方法的优点
     * 1.开发团队达成一致,明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短,不要穿插其他的网络操作RPC/HTTP请求(毫秒级别)或者玻璃到事务方法外部
     * 3.不是所有方法都需要事务,如只有一条修改操作,只读操作不需要事务控制
     */
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
