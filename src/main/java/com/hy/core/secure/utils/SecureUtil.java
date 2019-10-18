package com.hy.core.secure.utils;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SecureUtil {
    private final static String UTF8 = Charsets.UTF_8.name();
    private final static String ALG_AES = "AES";
    private final static String AES_KEY = "wtf-wtf-wtf-wtf-";

    private final static int CHAR = 1;
    private final static int NUM = 0;

    public static void main(String[] args) throws Exception {
//        String enc = saltPassword("11");
//        System.out.println("校验：" + verifyPassword("11", enc));

        String aesOrig = encryptWithAes("hello");
        System.out.println("加密：" + aesOrig);
        System.out.println("解密：" + decryptWithAes(aesOrig));
    }

    //****************************MD5******************************//
    /**
     * 获取十六进制字符串形式的MD5摘要
     *
     * @param src 需要获得MD5摘要的字符串
     * @return 获取到的十六进制的MD5摘要
     */
    public static String toMd5Hex(String src) {
        byte[] bs;
        try {
            bs = toMd5Byte(src.getBytes(UTF8));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return new String(new Hex().encode(bs));
    }

    private static byte[] toMd5Byte(byte[] data) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        md5.update(data);
        return md5.digest();
    }

    //****************************BASE64******************************//
    public static String toBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String toBase64(String src) {
        return toBase64(src, UTF8);
    }

    public static String toBase64(String src, String charsetName) {
        try {
            return toBase64(src.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] fromBase64B(byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
    }

    public static byte[] fromBase64B(String src) {
        return fromBase64B(src, UTF8);
    }

    public static byte[] fromBase64B(String src, String charsetName) {
        try {
            return fromBase64B(src.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String fromBase64S(String src) {
        return fromBase64S(src, UTF8);
    }

    public static String fromBase64S(String src, String charsetName) {
        return new String(fromBase64B(src, charsetName));
    }

    //****************************Random******************************//
    /**
     * 获得指定长度的随机字符串
     * 参照ASCII码表 65到90为大写字母，97到122为小写字母
     *
     * @param length 要生成的字符串长度
     * @return 返回生成的随机字符串
     */
    public static String genRandomString(int length) {
        StringBuffer buf = new StringBuffer();
        Random random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            int charOrNum = random.nextInt(2) % 2 == 0 ? CHAR : NUM;
            switch (charOrNum) {
                case CHAR:
                    int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                    buf.append((char) (random.nextInt(26) + temp));
                    break;
                case NUM:
                    buf.append(random.nextInt(10));
                    break;
                default:
                    break;
            }
        }
        return buf.toString();
    }

    //****************************Salt******************************//
    public static String saltPassword(String pwd) {
        String salt = genRandomString(16);
        //生成加盐密码字符串
        StringBuffer buf = new StringBuffer();
        buf.append(pwd).append(salt);

        //生成加盐密码字符串的十六进制MD5摘要
        String md5Hex = toMd5Hex(buf.toString());

        //加盐密码的MD5进行加盐
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = md5Hex.charAt(i / 3 * 2);
            cs[i + 1] = salt.charAt(i / 3);
            cs[i + 2] = md5Hex.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    public static boolean verifyPassword(String clearTextPwd, String encryptedPwd) {
        //加盐密码的md5
        char[] pwdSaltMd5 = new char[32];
        //加盐的char数组
        char[] saltChar = new char[16];

        for (int i = 0; i < 48; i += 3) {
            pwdSaltMd5[i / 3 * 2] = encryptedPwd.charAt(i);
            pwdSaltMd5[i / 3 * 2 + 1] = encryptedPwd.charAt(i + 2);
            saltChar[i / 3] = encryptedPwd.charAt(i + 1);
        }

        String salt = new String(saltChar);
        StringBuffer sb = new StringBuffer();
        sb.append(clearTextPwd).append(salt);
        return toMd5Hex(sb.toString()).equals(new String(pwdSaltMd5));
    }

    //****************************AES******************************//
    public static String encryptWithAes(String src) {
        try {
            SecretKeySpec spec = new SecretKeySpec(AES_KEY.getBytes(UTF8), ALG_AES);
            Cipher cipher = Cipher.getInstance(ALG_AES);
            cipher.init(Cipher.ENCRYPT_MODE, spec);

            byte[] bytes = src.getBytes(UTF8);
            byte[] doFinal = cipher.doFinal(bytes);
            return Base64.getEncoder().encodeToString(doFinal);
        } catch (Exception e) {
            return null;
        }
    }

    public static String decryptWithAes(String src) {
        try {
            SecretKeySpec spec = new SecretKeySpec(AES_KEY.getBytes(UTF8), ALG_AES);
            Cipher cipher = Cipher.getInstance(ALG_AES);
            cipher.init(Cipher.DECRYPT_MODE, spec);

            byte[] bytes = src.getBytes(UTF8);
            byte[] decodeBytes = Base64.getDecoder().decode(bytes);
            byte[] doFinal = cipher.doFinal(decodeBytes);
            return new String(doFinal, UTF8);
        } catch (Exception e) {
            return null;
        }
    }
}
