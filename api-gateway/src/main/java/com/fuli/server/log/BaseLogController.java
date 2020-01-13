package com.fuli.server.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author create by XYJ
 * @Date 2019/11/1 10:12
 **/
@RestController
@RequestMapping("/log")
@Log4j2
@Api("日志管理")
public class BaseLogController {


    @GetMapping("/testlevel")
    @ApiOperation("日志打印级别查看测试")
    public String home() {
        log.trace("打印的系统日志级别：trace");
        log.debug("打印的系统日志级别：debug");
        log.info("打印的系统日志级别：info");
        log.warn("打印的系统日志级别：warn");
        log.error("打印的系统日志级别：error");
        return "sucess：root日志打印的级别，请前往控制台查看打印结果";
    }

    /**
     * logback动态修改包名的日志级别
     *
     * @param level       日志级别
     * @param packageName 包名
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/level")
    @ApiOperation("动态修改日志级别")
    public String updateLogbackLevel(@RequestParam(value = "level") String level,
                                     @RequestParam(value = "packageName", defaultValue = "-1") String packageName) {

        if (packageName.equals("-1")) {
            // 默认值-1，更改全局日志级别；否则按传递的包名或类名修改日志级别。
            setAllLogLevel(level);
        } else {
            setLogLevel(packageName, level);
        }
        return "success";
    }


    /**
     * 设置所有的日志级别
     *
     * @param level 日志级别
     */
    public static void setAllLogLevel(String level) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<Logger> loggerList = loggerContext.getLoggerList();
        for (Logger logger : loggerList) {
            logger.setLevel(Level.toLevel(level));
        }
    }

    /**
     * 修改某一个包下的日志级别
     *
     * @param name  需要修改日志级别的包名或类名
     * @param level 新的日志级别
     */
    public static void setLogLevel(String name, String level) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<Logger> loggerList = loggerContext.getLoggerList();
        for (Logger logger : loggerList) {
            if (logger.getName().contains(name)){
                logger.setLevel(Level.toLevel(level));
            }

        }
    }

}
