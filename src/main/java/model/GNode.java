package model;

import java.util.ArrayList;

public class GNode {
    public String val;
    public int from;

    public int to;
    public ArrayList<GNode> nexts;

    public GNode(String val) {
        this.val = val;
        this.from = 0;
        this.to = 0;
        this.nexts = new ArrayList<>();
    }
}
