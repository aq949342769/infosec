import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File("src");
        String curPath = file.getAbsolutePath();
        Frame frame = new Frame();
        FileDialog f1 = new FileDialog(frame, "选择要加密的文件", FileDialog.LOAD);
        FileDialog f2 = new FileDialog(frame, "选择要解密的文件", FileDialog.LOAD);
        FileDialog f3 = new FileDialog(frame, "选择密钥文件", FileDialog.LOAD);

        f1.setDirectory(curPath);
        f2.setDirectory(curPath);
        f3.setDirectory(curPath);

        Button b1 = new Button("加密文件");
        Button b2 = new Button("解密文件");
        Button b3 = new Button("选择密钥");

        // 获取明文数据
        final File[] msgFile = {null};
        final File[] keyFile = {new File("src/key.txt")};//默认选择工程内的密钥

        // 加密
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f1.setVisible(true);
                msgFile[0] = new File(f1.getDirectory() + f1.getFile());
                try {
                    encrypt(msgFile[0], keyFile[0]);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
         }
        );
        // 解密
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f2.setVisible(true);
                try {
                    decrypt(new File(f2.getDirectory() + f2.getFile()), keyFile[0]);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        // 获取密钥
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f3.setVisible(true);
                keyFile[0] = new File(f3.getDirectory() + f3.getFile());

            }
        });

        frame.add(b1, BorderLayout.NORTH);
        frame.add(b2, BorderLayout.CENTER);
        frame.add(b3, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

    }

    // 加密过程
    public static void encrypt(File msgFile, File keyFile) throws IOException {
        // 获取密钥
        FileInputStream fis = new FileInputStream(msgFile);
        String fileName = msgFile.getName();
        File cipherText = new File("src", "CText_" + fileName);
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
    public static void decrypt(File cypherFile, File keyFile) throws IOException {
        if (cypherFile.getName().startsWith("CText_")) {
            String fileName = cypherFile.getName();
            String[] cText_s = fileName.split("_");
            FileInputStream fis = new FileInputStream(cypherFile);
            FileOutputStream fos = new FileOutputStream("src/MText_"+ cText_s[1]);
            List<Integer> key = getKey(keyFile);
            int by, count = 0;
            while ((by = fis.read()) != -1) {
                fos.write(by ^ key.get((count + 1) % key.size()));
            }
            fis.close();
            fos.close();
        } else {
            System.out.println("该文件不是加密文件");
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
