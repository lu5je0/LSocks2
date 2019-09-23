package lsocks2.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public abstract class ConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(JsonConfigLoader.class);

    protected abstract LocalConfig decodeLocalConfig() throws IOException;

    protected abstract ServerConfig decodeServerConfig() throws IOException;

    public void loadServerConfig() throws Exception {
        ServerConfig serverConfig = decodeServerConfig();
        loadServerConfig(serverConfig);
    }

    public void loadLocalConfig() throws Exception {
        LocalConfig localConfig = decodeLocalConfig();
        loadLocalConfig(localConfig);
    }

    private void loadLocalConfig(LocalConfig localConfig) throws Exception {
        Optional<LocalConfig> config = Optional.of(localConfig);
        final LocalConfig configInstance = ConfigHolder.LOCAL_CONFIG;

        configInstance.setEnableNettyLogging(config.map(LocalConfig::isEnableNettyLogging).orElse(false));
        configInstance.setLocalPort(config.map(LocalConfig::getLocalPort).orElse(1080));

        configInstance.setServerHost(config.map(LocalConfig::getServerHost)
                .orElseThrow(() -> new Exception("ServerHost未指定")));

        configInstance.setServerPort(config.map(LocalConfig::getServerPort)
                .orElseThrow(() -> new Exception("ServerPort未指定")));

        configInstance.setEncryptMethod(config.map(LocalConfig::getEncryptMethod)
                .orElseThrow(() -> new Exception("EncryptMethod未指定")));

        configInstance.setPassword(config.map(LocalConfig::getPassword)
                .orElseThrow(() -> new Exception("password不能为空")));
    }

    private void loadServerConfig(ServerConfig serverConfig) throws Exception {
        Optional<ServerConfig> config = Optional.of(serverConfig);
        final ServerConfig configInstance = ConfigHolder.SERVER_CONFIG;

        configInstance.setPort(config.map(ServerConfig::getPort)
                .orElseThrow(() -> new Exception("未指定port")));

        configInstance.setEncryptMethod(config.map(ServerConfig::getEncryptMethod)
                .orElseThrow(() -> new Exception("EncryptMethod未指定")));

        configInstance.setPassword(config.map(ServerConfig::getPassword)
                .orElseThrow(() -> new Exception("password不能为空")));

        configInstance.setEnableNettyLogging(config.map(ServerConfig::getEnableNettyLogging)
                .orElse(false));
    }
}
