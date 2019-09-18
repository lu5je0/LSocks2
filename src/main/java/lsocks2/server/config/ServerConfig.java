package lsocks2.server.config;

public class ServerConfig {
    private int port;

    private boolean enableNettyLogging;

    public static ServerConfig defaultConfig() {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.port = 20443;
        serverConfig.enableNettyLogging = true;

        return serverConfig;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isEnableNettyLogging() {
        return enableNettyLogging;
    }

    public void setEnableNettyLogging(boolean enableNettyLogging) {
        this.enableNettyLogging = enableNettyLogging;
    }
}
