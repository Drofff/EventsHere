package service;

import java.io.Serializable;
import java.util.Base64;

public class EncryptingService implements Serializable {

    private static EncryptingService encryptingService;

    private EncryptingService() {}

    public static EncryptingService getInstance() {
        if (encryptingService == null) {
            encryptingService = new EncryptingService();
        }

        return encryptingService;
    }

    public String encrypt(String text) {

        return new String(Base64.getEncoder().encode(text.getBytes()));

    }

    public String decrypt(String code) {
        return new String(Base64.getDecoder().decode(code.getBytes()));
    }

}
