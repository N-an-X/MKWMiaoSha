package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
                        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}", list);
    }

    @Test
    public void getById() {
        long id = 1000;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}", seckill);
    }

    @Test
    public void exportSeckillUrl() {
        long id = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            logger.info("exposer={}", exposer);
            long phone = 18545600008L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution execution = seckillService.executeSeckill(id,phone, md5);
                logger.info("result={}", execution);
            } catch (RepeatKillException | SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        } else {
            //秒杀为开启
           logger.warn("exposer={}", exposer);
        }
    }

    @Test
    public void executeSeckill() {
        long id = 1000;
        long phone = 18545600008L;
        try {
            SeckillExecution execution = seckillService.executeSeckill(id, phone, "ba25984bd4a5d4b550e847a6abc6ac82");
            logger.info("result={}", execution);
        }catch (RepeatKillException | SeckillCloseException e) {
            logger.error(e.getMessage());
        }

    }

    @Test
    public void executeSeckillProcedure() {
        long seckillId = 1001;
        long phone = 1368011101;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            logger.info(execution.getStateInfo());
        }
    }
}