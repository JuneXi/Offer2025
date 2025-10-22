package model;

public class Trie {
    // if any word end in this node
    boolean isWord;
    // arrya of next character, lenght of 26, if next[0] is not null, then next char is 'a'
    Trie[] next;
    String val;

    public Trie() {
        this.next = new Trie[26];
    }

    public boolean isWord() {
        return isWord;
    }

    public void setWord(boolean word) {
        isWord = word;
    }

    public Trie[] getNext() {
        return next;
    }

    public void setNext(Trie[] next) {
        this.next = next;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}


