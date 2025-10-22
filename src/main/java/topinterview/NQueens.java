package topinterview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NQueens {
    public static void main(String[] args){
        NQueens app = new NQueens();
        app.solveNQueens(4);
    }

    public List<List<String>> solveNQueens(int n) {
        // memory[i] = j mean i,j has a queen
        int[] memory = new int[n];
        Arrays.fill(memory, -1);

        List<List<String>> res = new ArrayList<>();
        List<String> sol = new ArrayList<>();
        helper(0, n, res, memory, sol);
        return res;

    }

    /*
    cur: current row index
    n: how many queen
     */
    private void helper(int cur, int n, List<List<String>> res, int[] m, List<String> sol){
        // find a solution
        if(cur == n){
            res.add(new ArrayList<>(sol));
            return;
        }
        // from row cur , try to put queen
        for(int i = cur; i < n; i++ ){
            //try from col 0 to n - 1
            for(int j = 0; j < n; j++){
                if(isValid(i,j, m)){
                    m[i] = j;
                    sol.add(buildRow(j, n));
                    helper(cur+1, n, res, m, sol);
                    m[i] = -1;
                    sol.remove(sol.size() - 1);
                }
            }
            // very important! if there is no J then we need to returen to previous level, not go to next row
            return;
        }
    }

    // if it works to have a queen on (i, j)
    private boolean isValid(int i, int j, int[] m){
        for(int x = 0; x < i; x++){
            // there is a queen on (i,m[i])
            if( j == m[x] || (i + j == x + m[x]) || (i -j == x - m[x])){
                return false;
            }
        }
        return true;
    }
    private String buildRow(int i, int n){
        StringBuilder sb = new StringBuilder();
        while(n-- >0){
            sb.append(".");
        }
        sb.replace(i,i+1,"Q");
        return sb.toString();
    }

}
