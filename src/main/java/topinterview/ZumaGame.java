package topinterview;

import java.util.HashSet;
import java.util.Set;

public class ZumaGame {
    int res = 0;
    Set<String> memo = new HashSet<>();
    public int findMinStep(String board, String hand) {
        // how to store the map
        // R - 0, Y - 1, B -
        board.replace('R', '0');
        board.replace('Y', '1');
        board.replace('B', '2');
        board.replace('G', '3');
        board.replace('W', '4');
        int[] left = new int[5];
        for(char c: hand.toCharArray()){
            if(c == 'R'){
                left[0]++;
            }
            // continue with others
        }

        for(int i = 0; i < board.length();i ++){

        }

    }

    private void find(String board, int pos, int[] left, int used){
        if(board.length() == 0){
            res = Math.min(res, used);
        }
        for(int i = pos; i < board.length();i++){
            int cur = board.charAt(i) - '0';
            if(i + 1 < board.length() && board.charAt(i) != board.charAt(i + 1)){
                // there is only 1
                if(left[i] >= 2){
                    left[i] = left[ i] - 2;
                    // process to check if any other can be remove. if left == right && # >=3, remove and do next
                    find(board.substring(0, i) + board.substring(i+1), i,  left, used + 2);

                }else{
                    continue;
                }
            }
        }
    }
}
