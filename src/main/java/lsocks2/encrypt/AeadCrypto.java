package lsocks2.encrypt;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadFactory;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.config.TinkConfig;
import com.google.crypto.tink.proto.KeyTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AeadCrypto implements ICrypto {
    public static Logger logger = LoggerFactory.getLogger(AeadCrypto.class);

    private Aead aead;

    private String password;

    private static final HashMap<String, KeyTemplate> keyTemplateMap = new HashMap<>();

    static {
        keyTemplateMap.put("CHACHA20_POLY1305", AeadKeyTemplates.CHACHA20_POLY1305);
        keyTemplateMap.put("CHACHA20_POLY1305".toLowerCase(), AeadKeyTemplates.CHACHA20_POLY1305);
    }

    public AeadCrypto(String encryptMethod, String password) {
        this.password = password;
        try {
            TinkConfig.register();
            KeysetHandle keysetHandle = KeysetHandle.generateNew(keyTemplateMap.get(encryptMethod));
            aead = AeadFactory.getPrimitive(keysetHandle);
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public byte[] encrypt(byte[] data) throws GeneralSecurityException {
        try {
            return aead.encrypt(data, password.getBytes());
        } catch (GeneralSecurityException e) {
            logger.error("加密数据出现错误", e);
            throw e;
        }
    }

    @Override
    public byte[] decrypt(byte[] data) throws GeneralSecurityException {
        try {
            return aead.decrypt(data, password.getBytes());
        } catch (GeneralSecurityException e) {
            logger.error("加密数据出现错误", e);
            throw e;
        }
    }

    public static Map<String, String> ciphers() {
        return keyTemplateMap.keySet().stream().collect(Collectors.toMap(o -> o, o -> AeadCrypto.class.getName()));
    }
}
