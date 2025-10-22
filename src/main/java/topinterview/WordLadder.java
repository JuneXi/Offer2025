package topinterview;

import java.util.*;
import java.util.stream.Collectors;

public class WordLadder {

    public static void main(String[] args) {
        WordLadder app = new WordLadder();
        String begin = "teach";
        String[] list = {"peale", "wilts", "place", "fetch", "purer", "pooch", "peace", "poach", "berra", "teach", "rheum", "peach"};
        app.ladderLength(begin, "place", new ArrayList<>(Arrays.asList(list)));
    }

    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        //0 . if end words not in wordlist, return 0
        if (wordList.stream().noneMatch(s -> s.equals(endWord))) {
            return 0;
        }
        Set<String> set = new HashSet<>(wordList);
        //2. BFS from begin words, find all next words, if == end, then we find it
        Queue<String> q = new LinkedList<>();
        int res = 1;
        q.offer(beginWord);
        Set<String> visited = new HashSet<>();
        visited.add(beginWord);
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                String word = q.poll();
                for (String nextWord : getNext(word, set)) {
                    if (nextWord.equals(endWord)) {
                        return res + 1;
                    } else {
                        if (visited.contains(nextWord)) {
                            continue;
                        }
                        q.offer(nextWord);
                        visited.add(nextWord);
                    }
                }
            }
            res = res + 1;

        }
        return 0;


    }

    private List<String> getNext(String beginWord, Set<String> set) {

            List<String> nextWord = new ArrayList<>();
            // try to exchange each location
            for (int i = 0; i < beginWord.length(); i++) {
                // exchange the char from a to z and see if it in hash
                for (int j = 'a'; j <= 'z'; j++) {
                    //skip itself
                    if (beginWord.charAt(i) == j) {
                        continue;
                    }
                    String newWord = beginWord.substring(0, i) + (char) j + beginWord.substring(i + 1);
                    if (set.contains(newWord)) {
                        nextWord.add(newWord);
                    }
                }
            }

        return nextWord;

    }


}
