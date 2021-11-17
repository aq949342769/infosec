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

    static int[] exgcd(int a, int b, int x, int y) {
        if (a < b) return exgcd(b, a, x, y);
        else if (b == 0) {
            x = 1;
            y = 0;
            return new int[]{a, x, y};
        } else {


        }
    }

    // 计算私钥（乘法逆元）
    static int InvMod(int a, int m) {

    }

    // static int ExpMod(int a, int n, int m) {
    // }

    public static void main(String[] args) {
        System.out.println(isPrime(11111));
    }
}
