package topinterview;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupAnagrams {
    public List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> res = new ArrayList<>();
        Set<String> selected = new HashSet<>();
        for (int i = 0; i < strs.length; i++) {
            if(!selected.contains(strs[i])){
            List<String> temp = new ArrayList<>();
            temp.add(strs[i]);
            // find all anagrams
            for (int j = i + 1; j < strs.length; j++) {
                if(strs[i].equals(strs[j])){
                    temp.add(strs[j]);
                    continue;
                }
                if (!selected.contains(strs[j]) && isAnagrams(strs[i], strs[j])) {
                    temp.add(strs[j]);
                }
            }
                selected.addAll(temp);
            res.add(temp);
            }
        }
        return res;

    }

    // find all anagram for s in strs from index i
    public boolean isAnagrams(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        int[] a = new int[26];
        for (int i = 0; i < s.length(); i++) {
            a[s.charAt(i) - 'a']++;
        }
        for (int i = 0; i < s.length(); i++) {
            a[t.charAt(i) - 'a']--;
            if (a[t.charAt(i) - 'a'] < 0) {
                return false;
            }
        }
        return true;
    }

}
