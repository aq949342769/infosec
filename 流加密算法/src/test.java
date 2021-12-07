import java.nio.charset.StandardCharsets;

public class test {
    static public String solve (String str1, String str2) {
        if(str1 == "")return str2;
        if(str2 == "")return str1;
        String up,down;
        if(str1.length()>str2.length()){
            up = str1;
            down = str2;
        }
        else{
            up = str2;
            down =str1;
        }
        int up_len = up.length();
        int down_len = down.length();
        int min = up_len-down_len;
        StringBuilder ans = new StringBuilder();
        int inc = 0;
        for(int i = down_len-1; i >= 0; i--){
            if(up.charAt(i+min)=='0' && down.charAt(i)=='0' && inc==0){
                ans.append('0');
            }else if(up.charAt(i+min)=='0' && down.charAt(i)=='0' && inc==1){
                ans.append('1');
                inc = 0;
            }else if(up.charAt(i+min)=='0' && down.charAt(i)=='1' && inc==0){
                ans.append('1');
            }else if(up.charAt(i+min)=='0' && down.charAt(i)=='1' && inc==1){
                ans.append('0');
                inc = 1;
            }else if(up.charAt(i+min)=='1' && down.charAt(i)=='0' && inc==0){
                ans.append('1');
            }else if(up.charAt(i+min)=='1' && down.charAt(i)=='0' && inc==1){
                ans.append('0');
                inc = 1;
            }else if(up.charAt(i+min)=='1' && down.charAt(i)=='1' && inc==0){
                ans.append('0');
                inc = 1;
            }else{
                ans.append('1');
                inc = 1;
            }

        }
        if(min==0 && inc==1){
            ans.append('1');
            return ans.reverse().toString();
        }else {
            for(int i = min - 1 ; i >= 0; i--){

                if(inc ==  1 && up.charAt(i)=='1'){
                    ans.append('0');
                    inc = 1;
                }else if(inc == 1 && up.charAt(i)=='0'){
                    ans.append('1');
                    inc = 0;
                }else {
                    ans.append(up.charAt(i));
                }
            }
        }

        if(inc == 1){
            ans.append('1');
        }
        ans.reverse();
        return ans.toString();

    }
    public static void main(String[] args) {
        String s1 = "11011";
        String s2 = "1101";
        String s = solve(s1,s2);
        System.out.println(s);
    }
}
