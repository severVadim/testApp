package utils;

import org.apache.commons.codec.binary.Base64;

public class Util {

    public static String decodeJwtToken(String token) {
        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(token.split("\\.")[1]));
        return body;
    }
}
