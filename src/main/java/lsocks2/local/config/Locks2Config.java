package lsocks2.local.config;

public class Locks2Config {
    private int port;

    private boolean logging;

    private Locks2Config() {
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static Locks2Config defaultConfig() {
        Locks2Config config = new Locks2Config();
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