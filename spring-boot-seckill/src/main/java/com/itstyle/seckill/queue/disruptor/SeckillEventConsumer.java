package com.itstyle.seckill.queue.disruptor;

import com.itstyle.seckill.service.ISeckillService;
import com.itstyle.seckill.service.impl.SeckillServiceImpl;
import com.lmax.disruptor.EventHandler;

/**
 * 消费者(秒杀处理器)
 * 创建者 科帮网
 */
public class
SeckillEventConsumer implements EventHandler<SeckillEvent> {

    //	private ISeckillService seckillService = (ISeckillService) SpringUtil.getBean("seckillService");
    private ISeckillService seckillService = new SeckillServiceImpl();

    @Override
    public void onEvent(SeckillEvent seckillEvent, long seq, boolean bool) throws Exception {
        seckillService.startSeckil(seckillEvent.getSeckillId(), seckillEvent.getUserId());
    }
}
