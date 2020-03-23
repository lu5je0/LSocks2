package com.lu5je0.lsocks2.config;

public interface ConfigReader<T> {
    T readFromFile(String configName, Class<T> clazz) throws Exception;
}
