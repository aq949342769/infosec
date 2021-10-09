import netscape.security.UserTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 获取密钥
        String key = "monaaaaaarccciiicchy";
        // 生成替换矩阵
        MatrixDS matrixDS = new MatrixDS(key);
        matrixDS.initMatrix();

        // 测试
        char[][] n = matrixDS.getMatrix();
        for (char[] ints : n) {
            for (char anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
        // 获取明文对
        List<String> mText = getDoubleChar("ballokijkllkkon");//[ba, lk, lo, ki, jk, lk, lk, ko, nk]
        System.out.println(mText);
        // 明文加密
        List<String> cText = playFair(matrixDS.getMatrix(), matrixDS.getQueryMap(), mText);
        System.out.println(cText);
        // 密文解密
        List<String> c_mText = dePlayFair(matrixDS.getMatrix(), matrixDS.getQueryMap(), cText);
        System.out.println(c_mText);
    }

    // 获取明文对
    public static List<String> getDoubleChar(String msg) {
        List<String> ls = new ArrayList<>();
        StringBuilder sb = new StringBuilder(msg);
        for (int i = 0; i < sb.length(); i += 2) {
            // 最后一组只有一个字母，补k
            if (i == sb.length() - 1)
                sb.insert(i + 1, 'k');
            if (sb.charAt(i) == sb.charAt(i + 1)) {
                if (sb.charAt(i) == 'k') {
                    // 同一组相同字母为k
                    sb.insert(i + 1, 'i');
                } else
                    sb.insert(i + 1, 'k');
            }
        }
        for (int i = 0; i < sb.length() - 1; i += 2) {
            ls.add((String.valueOf(sb.charAt(i))) + sb.charAt(i + 1));
        }
        return ls;
    }

    // 获取明文字母对应位置
    public static int[] pos(HashMap<Character, int[]> queryMatrix, char ch) {
        return ch == 'j' ? queryMatrix.get('i') : queryMatrix.get(ch);
    }

    // 加密算法
    public static List<String> playFair(char[][] matrix, HashMap<Character, int[]> queryMatrix, List<String> mText) {
        List<String> cText = new ArrayList<>();
        for (String str : mText) {
            // 1、分别字母对各字母在矩阵中的位置(x1,y1)(x2,y2)
            int[] pos1 = pos(queryMatrix, str.charAt(0));
            int[] pos2 = pos(queryMatrix, str.charAt(1));
            int x1 = pos1[0], y1 = pos1[1];
            int x2 = pos2[0], y2 = pos2[1];
            // 2、同一行：y = (y + 1) mod 5
            if (x1 == x2) {
                cText.add(String.valueOf(matrix[x1][(y1 + 1) % 5]) + matrix[x2][(y2 + 1) % 5]);
            }
            // 3、同一行：x = (x + 1) mod 5
            else if (y1 == y2) {
                cText.add(String.valueOf(matrix[(x1 + 1) % 5][y1]) + matrix[(x2 + 1) % 5][y2]);
            }
            // 4、对角线: y1 = y2, y2 = y1
            else {
                cText.add(String.valueOf(matrix[x1][y2]) + matrix[x2][y1]);
            }

        }
        return cText;
    }

    // 解密算法
    public static List<String> dePlayFair(char[][] matrix, HashMap<Character, int[]> queryMatrix, List<String> cText){
        List<String> mText = new ArrayList<>();
        for (String str : cText) {
            // 1、分别字母对各字母在矩阵中的位置(x1,y1)(x2,y2)
            int[] pos1 = pos(queryMatrix, str.charAt(0));
            int[] pos2 = pos(queryMatrix, str.charAt(1));
            int x1 = pos1[0], y1 = pos1[1];
            int x2 = pos2[0], y2 = pos2[1];
            // 2、同一行：y = (y + 4) mod 5
            if (x1 == x2) {
                mText.add(String.valueOf(matrix[x1][(y1 + 4) % 5]) + matrix[x2][(y2 + 4) % 5]);
            }
            // 3、同一行：x = (x + 4) mod 5
            else if (y1 == y2) {
                mText.add(String.valueOf(matrix[(x1 + 4) % 5][y1]) + matrix[(x2 + 4) % 5][y2]);
            }
            // 4、对角线: y1 = y2, y2 = y1
            else {
                mText.add(String.valueOf(matrix[x1][y2]) + matrix[x2][y1]);
            }

        }
        return mText;
    }

}
