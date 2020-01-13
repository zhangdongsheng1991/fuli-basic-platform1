package com.fuli.task.server.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fuli.task.server.model.entity.TaskJobLogs;
import com.fuli.task.server.model.PageParams;

/**
 * 异步通知日志接口
 * @Author create by XYJ
 * @Date 2019/10/11 12:52
 **/
public interface TaskJobLogsService {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<TaskJobLogs> findListPage(PageParams pageParams);

    /**
     * 添加日志
     *
     * @param log
     */
    void addLog(TaskJobLogs log);

    /**
     * 更细日志
     *
     * @param log
     */
    void modifyLog(TaskJobLogs log);


    /**
     * 根据主键获取日志
     *
     * @param logId
     * @return
     */
    TaskJobLogs getLog(String logId);
}
