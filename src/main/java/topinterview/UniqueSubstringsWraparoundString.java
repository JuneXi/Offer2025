package topinterview;

import java.util.HashSet;
import java.util.Set;

public class UniqueSubstringsWraparoundString {
    public int findSubstringInWraproundString(String s) {
        if(s.length()<2){
            return s.length();
        }
        Set<String> res = new HashSet<>();
        int l = 0;
        int r = 0;
        // add first character
        // r from 0 to s.length - 1
        while(r < s.length()){
            while(r < s.length() - 1 && isValid(s.charAt(r), s.charAt(r + 1))){
                r++;
            }
            if(r == s.length() - 1){
                res.add(s.substring(r, r+1));
                if(isValid(s.charAt(r- 1),s.charAt(r))){
                    res.add(s.substring(l));
                }
            }
            res.add(s.substring(l,r));
            r++;
            l = r;

        }
        return res.size();


    }
    private boolean isValid(char a, char b){
        if(a - b == 1 ||(a == 'z' && b =='a')){
            return true;
        } else {
            return false;
        }
    }
}
