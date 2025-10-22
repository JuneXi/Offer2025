package topinterview;

import java.util.ArrayList;
import java.util.List;

public class TextJustification {
    public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> res = new ArrayList<>();
        int start = 0;
        int end = 0;
        while(end < words.length){
            int length = words[start].length() + 1;
            while(length <= maxWidth){
                if(length + words[end].length() <= maxWidth){
                    length += words[end].length() + 1;
                    end ++;
                } else {
                    break;
                }
            }
            //how many words can be put in this line
            int count = end - start + 1;
            if(count == 1){
                res.add(addEmpty(words[start], maxWidth - words[start].length()));
            }
            // how many empty we can have in this line
            int emptyCount = maxWidth - length + count;
            int t = emptyCount/count;
            int tail = emptyCount - (count - 1) *t;



        }
        return null;

    }
    private String addEmpty(String s, int n){
        StringBuilder sBuilder = new StringBuilder(s);
        while(n > 0){
            sBuilder.append(" ");
            n --;
        }
        s = sBuilder.toString();
        return s;
    }
}
