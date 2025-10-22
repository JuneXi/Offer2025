package topinterview;

import java.util.HashMap;
import java.util.Map;

public class ScrambleString {
    public boolean isScramble(String s1, String s2) {
        Map<String, Boolean> memory = new HashMap<>();

        return helper(s1, s2, memory);

    }

    // return true if from s1[i ...j] contains same element as s2[i...j]
    private boolean helper(String s1, String s2, Map<String, Boolean> map ){
        if(s1.length()== 1){
            return s1.equals(s2);
        }
        if(map.containsKey(s1+s2)){
            return map.get(s1+s2);
        }
        for(int i = 1; i < s1.length(); i++){
            boolean res = (helper(s1.substring(0, i), s2.substring(0,i), map) && helper(s1.substring(i), s2.substring(i),map))||
                    (helper(s1.substring(0,i), s2.substring(s2.length() - i), map)&&helper(s1.substring(i), s2.substring(0, s2.length() - i), map));
            if(res){
                return true;
            }
        }
        return false;

    }
}
