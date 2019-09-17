package lsocks2.config;

public class ProxyServerConfig {
    private int port;

    private boolean logging;

    private ProxyServerConfig() {
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static ProxyServerConfig defaultProxyServerConfig() {
        ProxyServerConfig config = new ProxyServerConfig();
        config.setPort(1082);
        config.setLogging(true);

        return config;
    }

    public boolean isLogging() {
        return logging;
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }
}