import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

public class Main {

    // 将字节数组输出为16进制串
    public static String ByteArrayToHex(byte[] bs)
    {
        StringBuilder res = new  StringBuilder();
        for(byte b: bs)
        {
            res.append(String.format("%02X ", new Integer(b & 0xff)));
        }

        return res.toString();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        System.out.println("des example:");
        desExample();
    }

    // 注意 java 中密钥接口之间的关系
    // RSAPrivateKey， RSAPublicKey，  RSAKey，PublicKey，PrivateKey，Key

    private static void desExample() {
        // des 加密算法
        Cipher des = null;

        // 存放 密钥
        byte[] des_key = new byte[8];

        // 存放 明文
        byte[] des_input = new byte[8];

        // 存放 加密后的输出
        byte[] des_output = null;


        // 0000000000000000 0000000000000000  8CA64DE9C1B123A7
        // 1111111111111111 1111111111111111  F40379AB9E0EC533
        // 初始化密钥和明文，输出结果是  F4 03 79 AB 9E 0E C5 33
        for(int i=0; i < 8; i++)
        {
            des_key[i] = 0x11;
            des_input[i] = 0x11;
        }

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
        try {
            des_output = des.doFinal(des_input);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        // 输出加密结果
        System.out.print(ByteArrayToHex(des_output));

        System.out.println();
    }

}
