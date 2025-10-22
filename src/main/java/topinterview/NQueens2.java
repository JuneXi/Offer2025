package topinterview;

import java.util.ArrayList;
import java.util.List;

public class NQueens2 {
    int res = 0;

    public int totalNQueens(int n) {
        List<Integer> l = new ArrayList<>();
        //try with each row
        helper(0, n, l);
        return res;

    }

    private void helper(int i, int n, List<Integer> l) {
        if (l.size() == n) {
            res++;
            return;
        }
        for (int j = 0; j < n; j++) {
            if (valid(i, j, l, n)) {
                i++;
                l.add( i * n + j);
                helper(i, n, l);
                l.remove(l.size() - 1);
                i--;
            }
        }
    }

    private boolean valid(int i, int j, List<Integer> l, int n) {
        for (Integer cur : l) {
            int x = cur / n;
            int y = cur % n;
            if (x == i || y == j || x + y == i + j || y - x == j - i) {
                return false;
            }
        }
        return true;
    }
}
