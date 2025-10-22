package topinterview;

public class ShortestPalindrome {
    public String shortestPalindrome(String s) {
        // find the longest palindrome start with index 0;
        // use kmp with a trick
        // if we do s + # + s.reverse()
        //and then kmp on the new str, we will get it
        String r = s + "#" +  new StringBuilder(s).reverse();
        int[] kmp = new int[r.length()];
        // kmp[i] -> the longest prefix = postfix in string[0 - i];
        kmp[0] = 0;
        int length = 0;
        int i = 1;
        while(i < r.length()){
            if(r.charAt(i) == r.charAt(length)){
                length++;
                kmp[i] = length;
                i++;
            } else {
                if(length != 0) {
                    length = kmp[length - 1];
                } else {
                    kmp[i] = 0;
                    i++;
                }
            }
        }
        int res = kmp[r.length()-1];
        return new StringBuilder(s.substring(res)).reverse().append(s).toString();


    }

}
