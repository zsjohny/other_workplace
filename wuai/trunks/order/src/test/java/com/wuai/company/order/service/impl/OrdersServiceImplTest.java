package com.wuai.company.order.service.impl;

import com.wuai.company.entity.TrystCancel;
import com.wuai.company.entity.TrystOrders;
import com.wuai.company.enums.CancelEnum;
import com.wuai.company.order.dao.TrystOrdersDao;
import com.wuai.company.order.mapper.TrystSceneMapper;
import com.wuai.company.order.service.OrdersService;
import com.wuai.company.order.service.TrystOrdersService;
import com.wuai.company.party.dao.PartyDao;
import com.wuai.company.pms.service.PmsService;
import com.wuai.company.user.domain.Push;
import com.wuai.company.user.push.PushUtils;
import com.wuai.company.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrdersServiceImplTest {

        @Autowired
        private OrdersService ordersService;

        @Autowired
        private PmsService pmsService;

        @Autowired
        private PartyDao partyDao;

        @Autowired
        private UserService userService;

        @Autowired
        private TrystOrdersService trystOrdersService;

        @Autowired
        private TrystOrdersDao trystOrdersDao;

        @Autowired
        TrystSceneMapper trystSceneMapper;


        @Test
        public void activePic() throws Exception {
                List<TrystOrders> trystOrdersList = trystOrdersDao.findTrystOrdersList(0, null);   //按最新发布的排序
                System.out.println(trystOrdersList.size());
        }

        @Test
        public void grabTest(){
                TrystCancel trystCancel = trystOrdersDao.findCancelTryst(1581, CancelEnum.TRYST.getCode(),"2018-03-03");
        }

}