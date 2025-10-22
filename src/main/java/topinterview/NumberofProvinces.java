package topinterview;

import java.util.HashSet;
import java.util.Set;

public class NumberofProvinces {
    public int findCircleNum(int[][] isConnected) {
        int l = isConnected.length;
        if(l == 1){
            return 1;
        }
        int[] father = new int[l];
        for(int i = 0; i < l; i++){
            father[i] = i; // each element is a single union
        }
        for(int i = 0; i < l; i++){
            for(int j = i + 1; j < l; j++){
                if(isConnected[i][j] == 1){
                    union(i, j, father);
                }
            }
        }
        Set<Integer> res = new HashSet<>();
        for(int i = 0; i < l; i++){
            res.add(father[i]);
        }
        return res.size();



    }

    private void union(int i, int j, int[] father){
        int ihead = findHead(i, father);
        int jhead = findHead(j, father);
        father[jhead] = ihead;
    }

    private boolean isConnected(int i, int j, int[] father){
        return findHead(i, father) == findHead(j, father);

    }

    private int findHead(int i, int[] father){
        if(father[i] == i){
            return i;
        } else {
            father[i] = findHead(father[i], father);
            return father[i];
        }
    }
}
