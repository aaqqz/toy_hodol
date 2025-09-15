package com.hodol.toy_hodol.common.crypto;

public interface  PasswordEncoder {

    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
