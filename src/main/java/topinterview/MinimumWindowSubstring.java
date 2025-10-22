package topinterview;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class MinimumWindowSubstring {

    public static void main(String[] args) {
        MinimumWindowSubstring app = new MinimumWindowSubstring();
        String s = "ADOBECODEBANC";
        String t = "ABC";
        app.minWindow(s, t);
    }

    public String minWindow(String s, String t) {
        if (s.length() < t.length()) {
            return "";
        }
        int[] count = new int[58];
        Set<Character> set = new HashSet<>();

        for (int i = 0; i < t.length(); i++) {
            count[t.charAt(i) - 'A']++;
            set.add(t.charAt(i));
        }
        int l = 0;
        int r = 0;
        int diff = t.length();
        //remeber the location where the next
        //Queue<Integer> q = new LinkedList<>();
        // 1 st char in t

        String res = "";
        int min = Integer.MAX_VALUE;
        while (r < s.length()) {

            if (set.contains(s.charAt(r))) {
                count[s.charAt(r) - 'A']--;
                // should only diff-- if this is necessaroy
                // e.g. if "ABC" and "BBB" should only diff-- once
                if(count[s.charAt(r) - 'A'] >= 0) {
                    diff--;
                }
            }
            // r move to next element
            // found a valid at [l, r-1]
            r++;


            System.out.println("find a diff == 0");
            // now we need to check l
            while (diff == 0) {
                if(min > r - l){
                    min = r - l;

                    res = s.substring(l, r);
                    System.out.println("new res :" + res);
                }
                // if l is needed,after remove it
                if (set.contains(s.charAt(l))) {
                    count[s.charAt(l) - 'A']++;
                    if (count[s.charAt(l) - 'A'] > 0) {
                        diff++;
                    }
                    l++;
                } else {
                    l++;
                }
            }

        }
        return res;

    }
}
