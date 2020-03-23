package com.lu5je0.lsocks2.config;

public class ServerConfig {
    private Integer port;

    private String password;

    private String encryptMethod;

    private Boolean enableNettyLogging;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncryptMethod() {
        return encryptMethod;
    }

    public void setEncryptMethod(String encryptMethod) {
        this.encryptMethod = encryptMethod;
    }

    public Boolean getEnableNettyLogging() {
        return enableNettyLogging;
    }

    public void setEnableNettyLogging(Boolean enableNettyLogging) {
        this.enableNettyLogging = enableNettyLogging;
    }
}
