package com.lu5je0.lsocks2.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
public class JsonConfigReader<T> implements ConfigReader<T> {
    public T readFromFile(String configName, Class<T> clazz) throws Exception {
        log.info("Read config {}", configName);
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(configName);
        if (resourceAsStream == null) {
            throw new Exception("无法加载配置文件");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
        String configText = reader.lines().collect(Collectors.joining());

        return JSONObject.parseObject(configText, clazz);
    }
}
