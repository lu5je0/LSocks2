package com.lu5je0.lsocks2.server.config;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ServerConfig {
    public static ServerConfig INSTANCE = new ServerConfig();

    private Integer port;

    private String password;

    private String encryptMethod;

    private Boolean enableNettyLogging;
}
