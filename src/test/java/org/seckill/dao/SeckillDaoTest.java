package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() {
        Date date = new Date();
        int i = seckillDao.reduceNumber(1000L, date);
        System.out.println(i);
    }

    @Test
    //1000元秒杀iphone6
    //Seckill{seckillId=1000, name='1000元秒杀iphone6', number=100, startTime=Sun Nov 01 13:00:00 CST 2015,
    // endTime=Mon Nov 02 14:00:00 CST 2015, createTime=Sun Jul 18 02:33:35 CST 2021}
    public void queryById() {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    //Seckill{seckillId=1000, name='1000元秒杀iphone6', number=100, startTime=Sun Nov 01 13:00:00 CST 2015,
    // endTime=Mon Nov 02 14:00:00 CST 2015, createTime=Sun Jul 18 02:33:35 CST 2021}
    //Seckill{seckillId=1001, name='500元秒杀ipad2', number=200, startTime=Sun Nov 01 13:00:00 CST 2015,
    // endTime=Mon Nov 02 14:00:00 CST 2015, createTime=Sun Jul 18 02:33:35 CST 2021}
    //Seckill{seckillId=1002, name='300元秒杀iphone6', number=300, startTime=Sun Nov 01 13:00:00 CST 2015,
    // endTime=Mon Nov 02 14:00:00 CST 2015, createTime=Sun Jul 18 02:33:35 CST 2021}
    //Seckill{seckillId=1003, name='200元秒杀iphone6', number=400, startTime=Sun Nov 01 13:00:00 CST 2015,
    // endTime=Mon Nov 02 14:00:00 CST 2015, createTime=Sun Jul 18 02:33:35 CST 2021}
    public void queryAll() {
        List<Seckill> seckillList = seckillDao.queryAll(0,100);
        for (Seckill seckill : seckillList) {
            System.out.println(seckill);
        }
    }
}