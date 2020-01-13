package com.fuli.task.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.task.server.model.entity.TaskJobLogs;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SchedulerJobLogsMapper extends BaseMapper<TaskJobLogs> {
}
