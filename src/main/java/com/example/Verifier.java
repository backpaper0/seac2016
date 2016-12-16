package com.example;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class Verifier {

    private final Key key = new SecretKeySpec("secret".getBytes(), "HmacSHA256");

    public String hash(Long value) throws GeneralSecurityException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        byte[] hash = mac.doFinal(String.valueOf(value).getBytes());
        return IntStream.range(0, hash.length).mapToObj(i -> String.format("%02x", hash[i]))
                .collect(Collectors.joining());
    }

    public boolean verify(String hash, Long value) throws GeneralSecurityException {
        return Objects.equals(hash, hash(value));
    }
}
