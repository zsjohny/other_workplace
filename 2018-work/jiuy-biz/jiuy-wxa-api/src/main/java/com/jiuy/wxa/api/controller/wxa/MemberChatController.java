package com.jiuy.wxa.api.controller.wxa;

import com.store.dao.mapper.MemberMapper;
import com.store.entity.member.ShopMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.store.service.MessageReceiveService.uuidRelationMap;

/**
 * 微信通话的
 */
@RestController
@RequestMapping("wechat")
public class MemberChatController {

    @Autowired
    private MemberMapper memberMapper;

    private static final Logger logger = LoggerFactory.getLogger(MemberChatController.class);

    @RequestMapping("save")
    public void saveInShop(Long memberId, Long storeId) {
        logger.info("发送消息 memberId={}, storeId={}", memberId, storeId);
        ShopMember user = memberMapper.findMemberById(memberId);
        if (user == null) {
            logger.warn("没有用户信息 memberId={}", memberId);
            return;
        }
        uuidRelationMap.put(user.getBindWeixin(), storeId);
    }


}
