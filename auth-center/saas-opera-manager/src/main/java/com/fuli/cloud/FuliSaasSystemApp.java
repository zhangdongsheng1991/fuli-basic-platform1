package com.fuli.cloud;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.fuli.cloud.commons.utils.DateUtil;
import com.fuli.logtrace.annotation.EnableLogTraceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;
import java.util.TimeZone;

/**
 * @Description: 启动类
 * @Author: WFZ
 * @CreateDate: 2019/5/5 10:42
 * @Version: 1.0
 */
@Configuration
@EnableFeignClients
@EnableDiscoveryClient
@EnableLogTraceConfig
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan(basePackages = "com.fuli.cloud.mapper.*")
public class FuliSaasSystemApp {

	public static void main(String[] args) {
		SpringApplication.run(FuliSaasSystemApp.class, args);
	}
	
	 /**解决时区问题，参考：https://www.jianshu.com/p/085eb3c3120e*/
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder ->
                jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone("GMT+8")).simpleDateFormat(DateUtil.PATTERN_DATETIME);
    }

    /**
     * 文件上传配置
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize(DataSize.of(10240, DataUnit.KILOBYTES));
        /// 总上传数据大小
        factory.setMaxRequestSize(DataSize.of(102400, DataUnit.KILOBYTES));
        return factory.createMultipartConfig();
    }

    /**
     * 加入下面方法防止参数中有特殊符号时报错
     * @return
     */
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "|{}[]\\"));
        return factory;
    }
    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return  new PaginationInterceptor();
    }

}
