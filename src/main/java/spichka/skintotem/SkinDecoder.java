package spichka.skintotem;

import java.util.Base64;

public class SkinDecoder {

    public static String decodeBase64(String encodedString) {
        return new String(Base64.getDecoder().decode(encodedString));
    }
}
