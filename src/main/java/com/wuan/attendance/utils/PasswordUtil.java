package com.wuan.attendance.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtil {

    private static final int SALT_LENGTH = 16;

    public static String encode(String password) {
        String salt = generateSalt();
        String hash = hashPassword(salt + password);
        return salt + hash;
    }

    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[SALT_LENGTH];
        // 使用 SecureRandom 类生成一组随机字节
        random.nextBytes(saltBytes);
        // 然后将这组字节转化为十六进制字符串作为盐值
        return bytesToHex(saltBytes);
    }

    private static String hashPassword(String password) {
        try {
            // 使用 SHA-256 算法进行哈希
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] byteData = md.digest();
            // 然后将结果转化为十六进制字符串
            return bytesToHex(byteData);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        String salt = hashedPassword.substring(0, SALT_LENGTH * 2); // 获取盐
        String hash = hashedPassword.substring(SALT_LENGTH * 2); // 获取哈希
        return MessageDigest.isEqual(hashPassword(salt + password).getBytes(), hash.getBytes()); // 检查密码是否匹配
        /*
        MessageDigest.isEqual是一个静态方法，用于比较两个字节数组是否相等。最主要的特性是它是"时间常量的"，这意味着无论输入的内容是什么，
        这个方法执行的时间是相同的。这个特性非常重要，因为它可以防止通过测量比较操作的时间来获取敏感信息（例如密码或哈希值）的攻击，
        这种攻击被称为"时间侧通道攻击"。
         */
    }

    /*
    将字节数组转化为十六进制字符串。
    因为字节的值范围是 0-255，直接输出为字符可能无法正常显示，转化为十六进制字符串可以方便地进行显示和存储。
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
