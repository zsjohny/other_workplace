package com.jiuy.core.service.task;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;

import com.jiuy.core.service.notifacation.NotifacationService;
import com.jiuyuan.constant.ThirdPartService;

public class NotificationDisposingJob {
	
	@Resource
	private NotifacationService notificationServiceImpl;
	
    public void execute() {
		try {
			notificationServiceImpl.updateSendNotification(ThirdPartService.GETUI_APP_ID, ThirdPartService.GETUI_APP_KEY,
					ThirdPartService.GETUI_MASTER_SECRET, ThirdPartService.GETUI_HOST, 5 * DateUtils.MILLIS_PER_MINUTE);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}    
}
