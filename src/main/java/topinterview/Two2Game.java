package topinterview;

import java.util.ArrayList;
import java.util.List;

public class Two2Game {
    boolean res = false;
    public boolean judgePoint24(int[] cards) {
        search(cards, 0, 0, cards.length-1, 0, new ArrayList<>());
        return res;
    }

    // return the exp value from pos i - j
    private List<Long> search(String num, long current, int pos, int end, long pre, List<Long> val){
        if(pos == end){
            if(24 == current){
                res = true;
            }
            val.add(current);
        }


        for(int i = pos; i < num.length(); i++){

            // rewrite here to get the value
            // find the value of
            List<Long> cur = search(num, 0, pos, i,0, val);
            for(Long temp: cur){
                if(pos == 0){
                    search(num, temp, i + 1,num.length(), new ArrayList<>());
                } else {
                    search(num, current + temp, i +1, num.length(), temp, null );
                    search(num, current - temp, i +1, num.length(), -temp, new ArrayList<>());
                    search(num, current - pre + pre * temp, i + 1, num.length(),temp, new ArrayList<>());
                    search(num, current  - pre + pre/temp, i + 1, num.length(),temp, new ArrayList<>());
                }
            }


        }

    }
}
