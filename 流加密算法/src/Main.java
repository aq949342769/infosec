import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        // 获取明文数据
        File msgFile = new File("src/msg.txt");
        File keyFile = new File("src/key.txt");
        // 加密
        encrypt(msgFile, keyFile);
        // 解密
        decrypt(msgFile, keyFile);
    }

    // 加密过程
    public static void encrypt(File msgFile, File keyFile) throws IOException {
        // 获取密钥
        FileInputStream fis = new FileInputStream(msgFile);
        String fileName = msgFile.getName();
        File cipherText = new File("src","CText_"+fileName);
        FileOutputStream fos = new FileOutputStream(cipherText);
        int by, count = 0;
        List<Integer> key = getKey(keyFile);
        while ((by = fis.read()) != -1) {
            // 运行算法得到密文
            fos.write(by ^ key.get((count + 1) % key.size()));
        }
        fis.close();
        fos.close();
    }

    // 解密函数
    public static void decrypt(File msgFile, File keyFile) throws IOException {
        String fileName = msgFile.getName();
        File cypherFile = new File("src","CText_"+fileName);
        if (cypherFile.exists()){
            FileInputStream fis = new FileInputStream(cypherFile);
            FileOutputStream fos = new FileOutputStream("src/MText_"+fileName);
            List<Integer> key = getKey(keyFile);
            int by, count = 0;
            while ((by = fis.read()) != -1) {
                fos.write(by ^ key.get((count + 1) % key.size()));
            }
            fis.close();
            fos.close();
        }else {
            System.out.println("没有此文件");
        }

    }
    // 获取密钥序列
    public static List<Integer> getKey(File key) throws IOException {
        List<Integer> res = new ArrayList<>();
        FileInputStream fis = new FileInputStream(key);
        int b;
        while ((b = fis.read()) != -1) {
            res.add(b);
        }
        return res;
    }
}
