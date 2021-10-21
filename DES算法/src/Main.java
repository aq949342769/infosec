import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Main {
    final static int TIMES = 1000;
    final static int maxBits = 64;

    // 将字节数组输出为16进制串
    public static String ByteArrayToHex(byte[] bs) {
        StringBuilder res = new StringBuilder();
        for (byte b : bs) {
            res.append(String.format("%02X ", b & 0xff));
        }

        return res.toString();
    }

    public static void main(String[] args) {
        byte[] des_key = new byte[8];
        byte[] des_input = new byte[8];


        // 初始化明文
        // 0000000000000000 0000000000000000  8CA64DE9C1B123A7
        // 1111111111111111 1111111111111111  F40379AB9E0EC533
        // 初始化密钥和明文，输出结果是  F4 03 79 AB 9E 0E C5 33
        for (int i = 0; i < 8; i++) {
            des_key[i] = 0x11;
            des_input[i] = 0x11;
        }

        // 密钥固定
        System.out.println("key nochange:");
        ArrayList<Integer> keyFix_statistics = keyFix(des_key, des_input);
        for (int i = 0; i < keyFix_statistics.size(); i++) {
            System.out.println("改变" + (i + 1) + "位明文的密文改变位数平均值: " + keyFix_statistics.get(i));
        }

        // // 明文固定
        System.out.println("massage nochage:");
        ArrayList<Integer> msgFix_statistics = msgFIx(des_key, des_input);
        for (int i = 0; i < msgFix_statistics.size(); i++) {
            System.out.println("改变" + (i + 1) + "位密钥的密文改变位数平均值: " + msgFix_statistics.get(i));
        }
    }

    // 密钥固定统计
    public static ArrayList<Integer> keyFix(byte[] des_key, byte[] des_input) {

        // 存放结果
        byte[] keyFix_des_output = null;
        // 存放统计结果
        ArrayList<Integer> statistics = new ArrayList<>();

        // 随机因子
        int random_i;
        int random_j;
        // 初始加密
        byte[] des_cy = des(des_key, des_input);

        // 改变1～64位
        for (int bit = 1; bit <= maxBits; bit++) {
            int sum = 0;
            // 每改变bit个位的字节，测试TIMES=3000次取平均值
            for (int i = 0; i < TIMES; i++) {

                // 使用明文的副本模拟明文的改变
                byte[] clone_des_input = des_input.clone();

                // 用一个集合记录被改过的索引
                Set<String> changed = new HashSet<>();

                // 每次改变一个，知道改完bit个为止，要防止随机数相同
                for (int r = 0; r < bit; ) {
                    random_i = (int) (Math.random() * 8);
                    random_j = (int) (Math.random() * 8);
                    if (changed.contains("<" + random_i + ", " + random_j + ">"))
                        continue;
                    changed.add("<" + random_i + ", " + random_j + ">");
                    r++;

                    // System.out.println();
                    // System.out.println("i:" + random_i);
                    // System.out.println("j:" + random_j);
                    // System.out.print("改变前 ");
                    // for (byte b : clone_des_input) {
                    //     System.out.print(toBinary(b, 8) + " ");
                    // }
                    // System.out.println();

                    // 改变明文（第i字节中的第j位）
                    change(clone_des_input, random_i, random_j);

                    // System.out.print("改变后 ");
                    // for (byte b : clone_des_input) {
                    //     System.out.print(toBinary(b, 8) + " ");
                    // }
                    // System.out.println();
                }

                // 改变位后加密
                keyFix_des_output = des(des_key, clone_des_input);
                // System.out.println(ByteArrayToHex(des_output));
                int count = countChangeBits(des_cy, keyFix_des_output);
                // System.out.println("改变位数：" + count);
                sum += count;
            }
            // System.out.println("average：" + sum / TIMES);
            statistics.add(sum / TIMES);
        }

        return statistics;
    }

    // 明文固定统计
    public static ArrayList<Integer> msgFIx(byte[] des_key, byte[] des_input) {
        // 存放结果
        byte[] msgFix_des_output = null;
        // 存放统计结果
        ArrayList<Integer> statistics = new ArrayList<>();

        // 随机因子
        int random_i;
        int random_j;
        byte[] des_cy = des(des_key, des_input);

        // 改变1～64位
        for (int bit = 1; bit <= maxBits; bit++) {
            int sum = 0;
            // 每改变bit个位的字节，测试TIMES=3000次取平均值
            for (int i = 0; i < TIMES; i++) {

                // 使用密钥的副本模拟密钥的改变
                byte[] clone_key_input = des_key.clone();

                // 用一个集合记录被改过的索引
                Set<String> changed = new HashSet<>();

                // 每次改变一个，直到改完bit个为止，要防止随机数相同
                for (int r = 0; r < bit; ) {
                    random_i = (int) (Math.random() * 8);
                    random_j = (int) (Math.random() * 8);
                    if (changed.contains("<" + random_i + ", " + random_j + ">"))
                        continue;
                    changed.add("<" + random_i + ", " + random_j + ">");
                    r++;

                    // System.out.println();
                    // System.out.println("i:" + random_i);
                    // System.out.println("j:" + random_j);
                    // System.out.print("改变前 ");
                    // for (byte b : clone_des_input) {
                    //     System.out.print(toBinary(b, 8) + " ");
                    // }
                    // System.out.println();

                    // 改变密钥（第i字节中的第j位）
                    change(clone_key_input, random_i, random_j);

                    // System.out.print("改变后 ");
                    // for (byte b : clone_des_input) {
                    //     System.out.print(toBinary(b, 8) + " ");
                    // }
                    // System.out.println();
                }

                // 加密开始
                msgFix_des_output = des(clone_key_input, des_input);
                // System.out.println(ByteArrayToHex(des_output));
                int count = countChangeBits(des_cy, msgFix_des_output);
                // System.out.println("改变位数：" + count);
                sum += count;
            }
            // System.out.println("average：" + sum / TIMES);
            statistics.add(sum / TIMES);
        }

        return statistics;
    }

    // 统计单词改变位数之后的密文变化位数
    public static int countChangeBits(byte[] des_cy, byte[] des_output) {
        int count = 0;
        // 得到密文二进制字符串
        StringBuilder sb_des_cy = new StringBuilder();
        for (byte b : des_cy) {
            String s = toBinary(b, 8);
            // System.out.print(s);
            sb_des_cy.append(toBinary(b, 8));
        }
        StringBuilder sb_des_output = new StringBuilder();
        // System.out.println();
        for (byte b : des_output) {
            String s = toBinary(b, 8);
            // System.out.print(s);
            sb_des_output.append(toBinary(b, 8));
        }
        // System.out.println();
        for (int i = 0; i < sb_des_cy.length(); i++) {
            if (sb_des_cy.charAt(i) != sb_des_output.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    // 改变明文（第i字节中的第j位）
    public static void change(byte[] bytes, int i, int j) {
        // 得到原字节码的二进制字符串
        String s = toBinary(bytes[i], 8);
        StringBuilder sb = new StringBuilder(s);

        // 修改第i个字节码的第j位
        if (sb.charAt(j) == '1') {
            sb.replace(j, j + 1, "0");
        } else {
            sb.replace(j, j + 1, "1");
        }
        byte[] binaryArr = sb.toString().getBytes(StandardCharsets.UTF_8);
        byte changeByte = 0;

        // 将字符串还原成字节码
        for (int k = 0; k < binaryArr.length; k++) {
            if (binaryArr[k] == 48) {
                continue;
            }
            changeByte += (int) Math.pow(2, 7 - k);
        }

        // 替换原字节数组的对应字节码
        bytes[i] = changeByte;
    }

    // 转为二进制且保留左边的0
    public static String toBinary(int num, int digits) {
        String cover = Integer.toBinaryString(1 << digits).substring(1);
        String s = Integer.toBinaryString(num & 0xff);
        return s.length() < digits ? cover.substring(s.length()) + s : s;
    }

    // 注意 java 中密钥接口之间的关系
    // RSAPrivateKey， RSAPublicKey，  RSAKey，PublicKey，PrivateKey，Key
    private static byte[] des(byte[] des_key, byte[] des_input) {
        // des 加密算法
        Cipher des = null;

        // 存放 加密后的输出
        byte[] des_output = null;

        // 创建 DES密钥，供后面使用
        SecretKey secretKey = new SecretKeySpec(des_key, "DES");

        // 创建des 密码算法对象，指定电码本模式和无填充方式
        try {
            des = Cipher.getInstance("DES/ECB/NoPadding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        // 初始化 des 算法
        try {
            des.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        // 加密
        // des_output = des.doFinal(des_input);
        // for (byte b : des_key) {
        //     System.out.print(b+" ");
        // }
        // System.out.println();
        // for (byte b : des_input) {
        //     System.out.print(b+" ");
        // }
        // System.out.println();
        // // 输出加密结果
        // System.out.print(ByteArrayToHex(des_output));
        //
        // System.out.println();
        // 返回加密结果
        try {
            des_output = des.doFinal(des_input);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return des_output;
    }

}
