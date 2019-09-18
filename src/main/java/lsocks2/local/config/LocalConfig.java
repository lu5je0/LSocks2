package lsocks2.local.config;

public class LocalConfig {
    private int port;

    private boolean enableNettyLogging;

    private LocalConfig() {
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static LocalConfig defaultConfig() {
        LocalConfig config = new LocalConfig();
        config.setPort(1082);
        config.setEnableNettyLogging(true);

        return config;
    }

    public boolean isEnableNettyLogging() {
        return enableNettyLogging;
    }

    public void setEnableNettyLogging(boolean enableNettyLogging) {
        this.enableNettyLogging = enableNettyLogging;
    }
}