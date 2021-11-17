import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.ArrayList;

public class Main {
    static int gcd(int a, int b) {
        if (a == 0 || b == 0) return 0;
        int r = a % b;
        if (r == 0) return b;
        return gcd(b, r);
    }

    static boolean isPrime(int a) {
        if (a % 2 == 0)
            return false;
        int end = (int) Math.sqrt(a);
        for (int i = 3; i <= end; i++) {
            if (a % i == 0)
                return false;
        }
        return true;
    }

    // 递归法
    // static int exgcd(int a, int b, Int x, Int y) {
    //     if (a < b) return exgcd(b, a, y, x);
    //     else if (b == 0) {
    //         x.set(1);
    //         y.set(0);
    //         return a;
    //     } else {
    //         Int x1 = new Int();
    //         int d = exgcd(b, b % a, x1, x);
    //         y.set(x1.get() - a / b * x.get());
    //         return d;
    //     }
    //
    // }
    // 迭代法
    static int exgcd(int a, int b, Int x, Int y) {
        if (a < b) return exgcd(b, a, y, x);
        int m = 0, n = 1;
        x.set(1);
        y.set(0);
        while (b != 0) {
            int d = a / b, t;
            t = m;
            m = x.get() - d * t;
            x.set(t);
            t = n;
            n = y.get() - d * t;
            y.set(t);
            t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    // 计算私钥（乘法逆元）返回-1 表示逆元不存在
    static int InvMod(int fin, int e) {
        Int x = new Int();
        Int y = new Int();
        if (exgcd(fin, e, x, y) == 1) {
            x.set(x.get() % e);
            return y.get() >= 0 ? y.get() : fin + y.get();
        } else {
            return -1;
        }
    }


    /**
     * @param a 明文/密文
     * @param n 公钥/私钥
     * @param m 模长
     * @return 密文/明文
     */
    static int ExpMod(int a, int n, int m) {
        ArrayList<Integer> byarr = new ArrayList<>();
        // 计算指数n的二进制
        while (n != 0) {
            byarr.add(n % 2);
            n /= 2;
        }
        // 计算a的中间结果
        ArrayList<Integer> tmp = new ArrayList<>();
        int byarrLen = byarr.size();
        int preNum = 0;
        for (int i = 0; i < byarrLen; i++) {
            if (i == 0) {
                tmp.add(a % m);
                preNum = a % m;
            } else {
                tmp.add((preNum * preNum) % m);
                preNum = preNum * preNum % m;
            }
        }
        // 根据二进制和a的中间结果计算最终结果
        int res = 1;
        for (int i = byarrLen - 1; i >= 0; i--) {
            if (byarr.get(i) != 0) {
                res = (res * tmp.get(i)) % m;
            }

        }
        return res;
    }

    public static void main(String[] args) {
        // 选取质数
        int p = 19, q = 23;
        int fin = (p - 1) * (q - 1);
        int n = p * q;
        // 公钥
        int e = 17;
        // 加密
        int c = ExpMod(47, 17, 437);
        System.out.println("ctext:" + c);
        // 计算密钥
        int d = InvMod(fin, e);
        System.out.println("mtext:" + ExpMod(c, d, n));


    }
}
