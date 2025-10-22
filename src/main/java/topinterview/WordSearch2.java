package topinterview;

import model.Trie;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WordSearch2 {
    public List<String> findWords(char[][] board, String[] words) {

        List<String> res = new ArrayList<>();

        //1. build a word dic
        Trie root = buildDic(words);

        //2. go through board, using DFS and the pre-build dic to check word
        int[][] dir = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                int cur = board[i][j] - 'a';
                // if any word start with current location
                if(root.getNext()[cur] != null){
                dfs(i,j,board,res, root.getNext()[cur], dir);}
            }
        }
        return res.stream().distinct().collect(Collectors.toList());

    }

    // if there is a word end in (i , j)
    // if it is not a word but dic has next, check next char
    private void dfs(int i, int j, char[][] board, List<String> res, Trie root, int[][] dir) {
        //if a word end in current location
        if(root.isWord()){
            res.add(root.getVal());
        }

        char temp = board[i][j];
        board[i][j] = '*';
        // check neighbour
        for (int[] a: dir){
                int newI = i + a[0];
                int newJ = j + a[1];
                // boundry and visited check
                if( newI < 0 || newI > board.length - 1 || newJ < 0 || newJ > board[0].length - 1 || board[newI][newJ] == '*'){
                    continue;
                }
                // neighbour not a word
            int index = board[newI][newJ] - 'a';
                if(root.getNext()[index] == null){
                    continue;
                }
                dfs(newI, newJ, board, res, root.getNext()[index],dir );
        }
        // restore
        board[i][j] = temp;


    }

    private Trie buildDic(String[] words){
        Trie root = new Trie();
        Trie head = root;
        for(String word: words){
            for(int i = 0; i < word.length(); i++){
                int cur = word.charAt(i) - 'a';
                if(head.getNext()[cur] == null){
                    Trie node = new Trie();
                    head.getNext()[cur] = node;
                }
                head = head.getNext()[cur];
            }
            head.setWord(true);
            head.setVal(word);
            head = root;
        }
        return root;
    }
}

