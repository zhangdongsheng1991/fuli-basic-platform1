package com.fuli.server.configuration.redisson;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @Description:  Redisson配置
 * @Date: 2018/4/23 9:54
 * @Version: 1.0
 * @Author : YOUJIN
 */
@ConfigurationProperties(prefix = "redisson")   //读取配置文件，装配成bean
@RefreshScope
public class RedissonProperties {
    //超时时间
    private int timeout = 10000;
    //地址
    private String address;
    //密码
    private String password;
    //连接数
    private int connectionPoolSize = 64;
    //最小空闲连接数 缺省：10
    private int connectionMinimumIdleSize=10;
    //集群slave连接数
    private int slaveConnectionPoolSize = 250;
    //集群master连接数
    private int masterConnectionPoolSize = 250;
    //集群模式的地址
    private String[] sentinelAddresses;
    //集群master
    private String masterName;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getSlaveConnectionPoolSize() {
        return slaveConnectionPoolSize;
    }

    public void setSlaveConnectionPoolSize(int slaveConnectionPoolSize) {
        this.slaveConnectionPoolSize = slaveConnectionPoolSize;
    }

    public int getMasterConnectionPoolSize() {
        return masterConnectionPoolSize;
    }

    public void setMasterConnectionPoolSize(int masterConnectionPoolSize) {
        this.masterConnectionPoolSize = masterConnectionPoolSize;
    }

    public String[] getSentinelAddresses() {
        return sentinelAddresses;
    }

    public void setSentinelAddresses(String sentinelAddresses) {
        this.sentinelAddresses = sentinelAddresses.split(",");
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getConnectionPoolSize() {
        return connectionPoolSize;
    }

    public void setConnectionPoolSize(int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }

    public int getConnectionMinimumIdleSize() {
        return connectionMinimumIdleSize;
    }

    public void setConnectionMinimumIdleSize(int connectionMinimumIdleSize) {
        this.connectionMinimumIdleSize = connectionMinimumIdleSize;
    }
}
