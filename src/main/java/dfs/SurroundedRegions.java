package dfs;

import java.util.LinkedList;
import java.util.Queue;

public class SurroundedRegions {
    public void solve(char[][] board) {
        int m = board.length;
        int n = board[0].length;

        for(int i = 0; i <board.length; i++){
            for (int j = 0; j < board[0].length; j++) {
                if(board[i][j] == 'X' || board[i][j] == 'A' || (i != 0 && j != 0 && i  != board.length - 1&& j != board[0].length - 1)){
                    continue;
                }

                Queue<Integer> q = new LinkedList<Integer>();
                board[i][j] = 'A';
                q.offer(i * m + j );
                while(!q.isEmpty()){
                    int cur = q.poll();
                    int x = cur / m;
                    int y = cur % m;
                    if(x > 0 && board[x - 1][y] == 'O'){
                        board[x - 1][y] = 'A';
                        q.offer(cur - m);
                    }
                    if(x < m - 1 && board[x + 1][y] == 'O'){
                        board[x + 1][y] = 'A';
                        q.offer(cur + m);
                    }
                    if(y > 0 && board[x][y - 1] == 'O'){
                        board[x][y - 1] = 'A';
                        q.offer(cur -1);
                    }
                    if(y < n - 1 && board[x][y + 1] == 'O'){
                        board[x][y + 1] = 'A';
                        q.offer(cur + 1);
                    }
                }
            }
        }

        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if(board[i][j] == 'O'){
                    board[i][j] = 'X';
                    continue;
                }
                if(board[i][j] == 'A'){
                    board[i][j] = 'O';
                }
            }
        }

    }


}
