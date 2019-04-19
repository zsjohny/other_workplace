package com.store.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jiuy.core.util.StringUtil;
import com.jiuyuan.util.GetuiUtil;
import com.store.dao.mapper.MessageMapper;
import com.store.entity.message.Message;
import com.store.enumerate.MessageSendTypeeEnum;
import com.store.enumerate.MessageTypeEnum;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * <p>
 * 接收消息服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-06-21
 */
@Service
public class EventReceiveService {
	 private static final Log logger = LogFactory.get();

	@Autowired
	private StoreUserService storeUserService;
	
	@Autowired
	
	MessageMapper messageMapper;
	@Autowired
	MemberService memberService;
	
	
	
	

	
	
}
