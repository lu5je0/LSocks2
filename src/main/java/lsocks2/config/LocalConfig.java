package lsocks2.config;

public class LocalConfig {
    private int localPort;

    private int serverPort;

    private String serverHost;

    private String password;

    private String encryptMethod;

    private boolean enableNettyLogging;

    @Override
    public String toString() {
        return "LocalConfig{" +
                "localPort=" + localPort +
                ", serverPort=" + serverPort +
                ", serverHost='" + serverHost + '\'' +
                ", password='" + password + '\'' +
                ", encryptMethod='" + encryptMethod + '\'' +
                ", enableNettyLogging=" + enableNettyLogging +
                '}';
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
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

    public boolean isEnableNettyLogging() {
        return enableNettyLogging;
    }

    public void setEnableNettyLogging(boolean enableNettyLogging) {
        this.enableNettyLogging = enableNettyLogging;
    }
}