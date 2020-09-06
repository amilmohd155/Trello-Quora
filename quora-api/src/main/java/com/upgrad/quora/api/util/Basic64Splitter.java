package com.upgrad.quora.api.util;

import java.util.Base64;

public class Basic64Splitter {

    public static String[] splitter(String authorization) {
        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodeText = new String(decode);
        return decodeText.split(":");
    }

}
