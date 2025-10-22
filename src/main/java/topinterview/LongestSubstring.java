package topinterview;

import java.util.HashSet;
import java.util.Set;

public class LongestSubstring {
    public int lengthOfLongestSubstring(String s) {
        int res = 0;
        int start = 0;
        int end = 0;
        Set<Character> unique = new HashSet<>();
        while(end < s.length()){
            while(!unique.contains(s.charAt(end))){
                unique.add(s.charAt(end));
                end++;
                res = Math.max(res, end - start + 1);
            }
            for(int i = start; i < end; i++){
                if(s.charAt(i) != s.charAt(end)){
                    unique.remove(s.charAt(i));
                    start++;
                } else {
                    start ++;
                    break;
                }
            }

        }
        return res;

    }
}
