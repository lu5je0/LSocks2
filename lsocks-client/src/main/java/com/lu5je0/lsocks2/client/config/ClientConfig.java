package com.lu5je0.lsocks2.client.config;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ClientConfig {
    public static ClientConfig INSTANCE;

    private Integer localPort;

    private Integer serverPort;

    private String serverHost;

    private String password;

    private String encryptMethod;

    private boolean enableNettyLogging;
}
