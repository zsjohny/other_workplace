package com.finace.miscroservice.distribute_task.util;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

/**
 * 时间校验帮助
 */
public class CronUtil {
    private CronUtil() {

    }


    /**
     * 校验有效的cron
     *
     * @param cron
     * @return
     */
    public static Boolean checkValidCron(String cron) {
        Boolean validFlag = Boolean.FALSE;
        try {
            CronExpression cronExpression = new CronExpression(cron);

            Date nowDate = new Date();
            Date validDate = cronExpression.getNextValidTimeAfter(nowDate);

            if (validDate != null && validDate.after(nowDate)) {
                validFlag = Boolean.TRUE;


            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return validFlag;
    }

}
