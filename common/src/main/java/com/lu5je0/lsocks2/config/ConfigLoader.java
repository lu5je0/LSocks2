package com.lu5je0.lsocks2.config;

public interface ConfigLoader<T> {
    T loadConfig(String configName, Class<T> clazz) throws Exception;

    void validate(T config);
}
