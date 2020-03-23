package com.lu5je0.lsocks2.config;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonConfigLoader extends AbstractConfigLoader {
    private static Logger logger = LoggerFactory.getLogger(JsonConfigLoader.class);

    private <T> T readFromFile(String filename, Class<T> type) throws IOException {
        return JSONObject.parseObject(this.getClass().getClassLoader().getResourceAsStream(filename), type);
    }

    @Override
    public LocalConfig decodeLocalConfig() throws IOException {
        return readFromFile("local_config.json", LocalConfig.class);
    }

    @Override
    public ServerConfig decodeServerConfig() throws IOException {
        return readFromFile("server_config.json", ServerConfig.class);
    }
}
