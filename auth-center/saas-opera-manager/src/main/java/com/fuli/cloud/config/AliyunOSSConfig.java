package com.fuli.cloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description :
 * @Author : WS
 * @CreateDate : 2019/5/8 14:43
 * @Version : 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "aliyunoss")
public class AliyunOSSConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    /**
     * 一级目录根据环境变化 本地:local 测试:test 开发:dev
     */
    private String folderhost;
    /**
     * 二级目录 channel/company
     */
    private String foldsecond;

}
