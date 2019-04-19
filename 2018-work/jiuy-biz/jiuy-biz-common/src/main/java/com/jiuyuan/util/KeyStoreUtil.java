package com.jiuyuan.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import org.apache.commons.io.IOUtils;

public class KeyStoreUtil {

    public static KeyStore loadKeyStore(File file, String pass) throws IOException, GeneralSecurityException {

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] password = pass.toCharArray();

        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            keyStore.load(input, password);
            return keyStore;
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
}
