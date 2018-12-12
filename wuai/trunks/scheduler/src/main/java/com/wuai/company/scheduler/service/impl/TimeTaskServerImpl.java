package com.wuai.company.scheduler.service.impl;



import com.wuai.company.scheduler.dao.TimeTaskDao;
import com.wuai.company.entity.TimeTask;
import com.wuai.company.scheduler.service.TimeTaskServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 任务时刻的serviceImpl
 */
@Transactional
@Service
public class TimeTaskServerImpl implements TimeTaskServer {
    @Autowired
    private TimeTaskDao timeTaskDao;



    @Override
    public List<TimeTask> findTimeTaskAll() {

        return timeTaskDao.findTimeTaskAll();
    }

    @Override
    public void saveTimeTask(TimeTask task) {
        timeTaskDao.saveTimeTask(task);
    }

    @Override
    public void updateTimeTask(TimeTask task) {
        timeTaskDao.updateTimeTask(task);
    }


    @Override
    public void deleteTimeTask(TimeTask task) {
        timeTaskDao.deleteTimeTask(task);
    }


}
