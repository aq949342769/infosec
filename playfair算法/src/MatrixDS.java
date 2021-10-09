import java.util.HashMap;
import java.util.HashSet;

public class MatrixDS {
    // 定义一个数据结构，其中包含关键字，替换矩阵，替换矩阵辅助哈希表（用于提升查询的速度）
    private final String key;
    private char[][] matrix;
    // 辅助哈希表（K，V -> 字母，字母所在x，y坐标）
    private HashMap<Character, int[]> queryMap;

    public MatrixDS(String key) {
        this.key = deduplication(key);//去重
    }

    // 关键字去重函数
    public static String deduplication(String key) {
        String realKey = key.trim();
        int len = realKey.length();
        StringBuilder sb = new StringBuilder();
        HashSet<Character> hs = new HashSet<>();
        for (int i = 0; i < len; i++) {
            char ch = realKey.charAt(i);
            // 把i/j都当作i
            if (ch == 'j' && !hs.contains('i')) {
                sb.append('i');
                hs.add('i');
            } else if (!hs.contains(ch)) {
                sb.append(ch);
                hs.add(ch);
            }
        }
        return sb.toString();
    }

    // 初始化矩阵
    public void initMatrix() {
        matrix = new char[5][5];
        queryMap = new HashMap<>();
        int keyLen = key.length();
        int i = 0;
        // 把关键字字母加入到矩阵
        // 行：i / 5
        // 列：i % 5
        for (; i < keyLen; ++i) {
            matrix[i / 5][i % 5] = key.charAt(i);
            queryMap.put(key.charAt(i), new int[]{i / 5, i % 5});
        }
        // 填补剩余空位
        for (int j = 'a'; j <= 'z' && i < 25; ++j) {
            if (!queryMap.containsKey((char) j)) {
                // 不使用j
                if (j == 'j') continue;
                matrix[i / 5][i % 5] = (char) j;
                queryMap.put((char) j, new int[]{i / 5, i % 5});
                ++i;
            }
        }
    }

    public char[][] getMatrix() {
        return matrix;
    }

    public HashMap<Character, int[]> getQueryMap() {
        return queryMap;
    }
}
