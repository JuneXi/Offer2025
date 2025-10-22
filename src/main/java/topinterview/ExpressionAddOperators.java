package topinterview;

import java.util.ArrayList;
import java.util.List;

public class ExpressionAddOperators {
    public List<String> addOperators(String num, int target) {
        List<String> res = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        search(num, target, res, sb, 0, 0, 0);
        return res;

    }

    private void search(String num, int target, List<String> res, StringBuilder sb, long current, int pos, long pre){
        if(pos == num.length()){
            if(target == current){
                res.add(sb.toString());
                return;
            }
        }
        // TODO: check leading 0
        //initialize
        int len = sb.length(); // save current length for backtracking

        for(int i = pos; i < num.length(); i++){
            if(num.charAt(pos) == '0' && i != pos){
                return;
            }
            long cur = Long.parseLong(num.substring(pos, i + 1));
            if(pos == 0){
                search(num, target, res, sb.append(cur),cur,i+1, cur);
                sb.setLength(len);
            } else {
                search(num, target, res, sb.append("+").append(cur), current + cur, i + 1, cur);
                sb.setLength(len);
                // here should be -cur
                search(num, target, res, sb.append("-").append(cur), current - cur, i + 1, -cur);
                sb.setLength(len);
                // here it should be cur*pre
                search(num, target, res, sb.append("*").append(cur), current - pre + pre *cur, i + 1, cur*pre);
                sb.setLength(len);
            }

        }

    }
}
