package com.fuli.cloud.commons.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectResult;
import com.fuli.cloud.config.AliyunOSSConfig;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * <pre>
 * Description:
 * </pre>
 *
 * @author chenyi
 * @date 2019/8/28
 */
public class OSSUtil {


    /**
     * @param aliyunOSSConfig 配置
     * @param objectName      设置文件路径
     * @author chenyi
     * @date 11:40 2019/8/28
     **/
    public String uploadFile(AliyunOSSConfig aliyunOSSConfig, String objectName, String sourceFilePath) throws FileNotFoundException {


        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(aliyunOSSConfig.getEndpoint(), aliyunOSSConfig.getAccessKeyId(),
                aliyunOSSConfig.getAccessKeySecret());
        PutObjectResult result = ossClient.putObject(aliyunOSSConfig.getBucketName(), objectName,
                new FileInputStream(sourceFilePath));
        if (result == null) {
            throw new RuntimeException("OSS文件服务器上传失败");
        }
        // 设置权限(公开读)
        ossClient.setBucketAcl(aliyunOSSConfig.getBucketName(), CannedAccessControlList.PublicRead);

        // 文件访问全url
        return "https://" + aliyunOSSConfig.getBucketName() + "." +
                aliyunOSSConfig.getEndpoint() + "/" + objectName;
    }
}
