package com.boyceliu.proxypool.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
@Data
public  class ProxyEntity implements Serializable{
    //ip	端口号	代理位置	代理类型	验证时间
    private String ip;
    private int port;
    private String location;
    private String agentType;
    private Date lastValidateTime;
    private boolean usable;

    public ProxyEntity() {}

    public String getKey() {
        return String.format("%s:%s", ip, port);
    }
    @Override
    public String toString() {
        return "RawProxy{" + "ip='" + ip + '\'' + ", port=" + port + ", location='" + location + '\'' +
                ", agentType='" + agentType + '\'' + ", lastValidateTime='" + lastValidateTime + '\'' +
                ", usable=" + usable + '}';
    }
}
