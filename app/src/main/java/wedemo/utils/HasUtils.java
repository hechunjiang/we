package wedemo.utils;


import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by sfy. on 2018/3/20 0020.
 */

public class HasUtils {
    private String key = "6b5695e8570e4176b84153a870634156";
    private String time = "1521537048";
    private String nonce_str = "";
    public static final String SOURCES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    private static HasUtils instance;

    public HasUtils() {

    }

    public static HasUtils getInstance() {
        if (instance == null) {
            instance = new HasUtils();
        }
        return instance;
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] textBytes = text.getBytes("UTF-8");
        md.update(textBytes, 0, textBytes.length);
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    /**
     * 随机串
     *
     * @return
     */
    public String nonceStr() {
        return generateString(new Random(), SOURCES, 11);
    }


    public String sign(String time, String nonce_str, String json, String ticket) throws NoSuchAlgorithmException, InvalidKeyException {
        String mJson;
        if (TextUtils.isEmpty(json) || json.equals("")) {
            mJson = "[]";
        } else {
            mJson = json;
        }
        String mStr = "time=" + time + "&nonce_str=" + nonce_str + "&json=" + mJson + "&ticket=" + ticket;
        final Charset asciiCs = Charset.forName("US-ASCII");
        final Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        final SecretKeySpec secret_key = new SecretKeySpec(asciiCs.encode(key).array(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        String sha1 = "";
        try {
            sha1 = SHA1(mStr);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        final byte[] mac_data = sha256_HMAC.doFinal(sha1.getBytes());

        String result = "";
        for (final byte element : mac_data) {
            result += Integer.toString((element & 0xff) + 0x100, 16).substring(1);
        }

        return result;
    }

    private String generateString(Random random, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }

}
