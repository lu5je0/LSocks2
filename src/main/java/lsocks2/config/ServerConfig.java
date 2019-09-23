package lsocks2.config;

public class ServerConfig {
    private int port;

    private String password;

    private String encryptMethod;

    private boolean enableNettyLogging;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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
