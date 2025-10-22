package topinterview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinationSum {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(candidates);
        for(int i = 0; i < candidates.length;i++){
            List<Integer> comb = new ArrayList<>();
            helper(res, comb, candidates, target, i);
        }
        return res;

    }
    private void helper(List<List<Integer>> res, List<Integer> comb, int[] candidates, int target, int i){
        if(target == 0){
            res.add(new ArrayList<>(comb));
            return;
        }
        for(int j = i; j < candidates.length;j++){
            if(candidates[j] <= target){
                comb.add(candidates[j]);
                helper(res, comb, candidates, target - candidates[j], j);
                comb.remove(comb.size() - 1);
            } else {
                return;
            }
        }

    }
}
