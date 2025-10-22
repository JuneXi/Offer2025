package dfs;

public class WordSearch {
    public boolean exist(char[][] board, String word) {
        int[][] visited = new int[board.length][board[0].length];
        for(int i = 0; i < board.length;i++){
            for(int j = 0;j < board[0].length; j++){
                boolean res = dfs(i, j, board, word, 0, visited);
                if(res){
                    return res;
                }
            }
        }
        return false;

    }

    public boolean dfs(int i, int j, char[][] board, String word, int cur, int[][] visited) {
        if(i < 0 || i > board.length - 1 || j < 0 || j > board[0].length - 1){
            return false;
        }
        if(visited[i][j] == 1){
            return false;
        }
        //if last char
        if (cur == word.length() - 1) {
            if (board[i][j] == word.charAt(cur)) {
                return true;
            } else {
                return false;
            }
        }
        //if char match
        if (board[i][j] == word.charAt(cur)) {
            // visit
            visited[i][j] = 1;
            if(dfs(i -1, j, board, word, cur + 1, visited) || dfs(i, j -1, board, word, cur + 1, visited)||dfs(i +1, j , board, word, cur + 1, visited) || dfs(i, j+1, board, word, cur +1, visited)){
                return true;
            }

        }
        visited[i][j] = 0;
        return false;
    }

}
