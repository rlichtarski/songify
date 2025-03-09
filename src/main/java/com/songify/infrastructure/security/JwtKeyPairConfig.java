package com.songify.infrastructure.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Configuration
@Log4j2
class JwtKeyPairConfig {

    @Bean
    KeyPair keyPair() throws NoSuchAlgorithmException, IOException {
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();

        saveKeyToFile("public_key.pem", keyPair.getPublic().getEncoded(), true);
        saveKeyToFile("private_key.pem", keyPair.getPrivate().getEncoded(), false);

        return keyPair;
    }

    private void saveKeyToFile(final String fileName, final byte[] keyBytes, final boolean isPublicKey) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(Paths.get("src", "main", "resources", fileName).toString())) {
            fos.write(getPemEncoded(keyBytes, isPublicKey));
        }
    }

    private byte[] getPemEncoded(byte[] keyBytes, boolean isPublicKey) {
        String pemHeader = isPublicKey ? "-----BEGIN PUBLIC KEY-----\n" : "-----BEGIN PRIVATE KEY-----\n";
        String pemFooter = isPublicKey ? "\n-----END PUBLIC KEY-----\n" : "\n-----END PRIVATE KEY-----\n";
        String pemEncoded = pemHeader + Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(keyBytes) + pemFooter;
        return pemEncoded.getBytes();
    }


}
