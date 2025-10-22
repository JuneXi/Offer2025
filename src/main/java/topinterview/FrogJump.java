package topinterview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrogJump {
    public boolean canCross(int[] stones) {

        int n = stones.length;
        if(n == 1){
            return true;
        }
        // map key:  stones position, value: stone index
        Map<Integer, Integer> map = new HashMap<>();
        // to store on ith stone, the k to jump to this stone
        Map<Integer, List<Integer>> steps = new HashMap<>();
        int[] diffs = new int[]{-1,0,1};
        for(int i = 0; i < n; i++){
            map.put(stones[i],i);
        }
        // initizlize

        List<Integer> l = new ArrayList<>();
        l.add(0);
        // there is only 1 way to jump to stones[1], which takes 1 step
        steps.put(0,l);
        for(int i = 1; i < n - 1; i++){
            List<Integer> pre_steps = steps.get(i);
            if(pre_steps == null){
                // no way to jump to this step
                continue;
            }
            for(int step: pre_steps){
                // try to jump to next
                for(int diff: diffs){
                    int k = step + diff;
                    if(k > 0){
                        // we can jump to current pos + K position
                        int new_pos = stones[i] + k;
                        if(map.containsKey(new_pos)){
                            // there is a stone on new position
                            int stone_index = map.get(new_pos);
                            if(steps.containsKey(stone_index)){
                                steps.get(stone_index).add(k);
                            } else {
                                List<Integer> temp = new ArrayList<>();
                                temp.add(k);
                                steps.put(stone_index, temp);
                            }
                        }
                    }
                }
            }
        }

        return steps.containsKey(n-1);

    }
}
