package topinterview;

import java.util.ArrayList;
import java.util.List;

public class Permutation {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        // remember which picked
        List<Integer> list = new ArrayList<>();
        helper(nums, res, list);

        return res;

    }

    public static void main(String[] args){
        int[] nums = {1,2,3};
        Permutation app = new Permutation();
        List<List<Integer>> res = app.permute(nums);
    }

    private void helper(int[] nums, List<List<Integer>> res, List<Integer> list) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }


        // continue
        for (int num : nums) {
            if (list.contains(num)) {
                continue;
            }

            list.add(num);
            // find a permutation

            // skip itself
            helper(nums, res, list);
            list.remove(list.size() - 1);

        }

    }
}
