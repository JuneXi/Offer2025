# 多叉树 / 泛型树 (N-ary Tree) 拓展类详解 · 中英对照版
---

## 📘 目录 Table of Contents
1. 定义与存储 Definition & Representation  
2. 遍历 Traversals  
3. 构建与序列化 Construction & Serialization  
4. 基本性质 Basic Properties  
5. 最近公共祖先 LCA  
6. 子树查询 Subtree Queries  
7. 树形动态规划 Tree DP  
8. 经典题型题解 Classic Problems Explained  
9. 常见易错点与总结 Common Pitfalls & Summary  

---

## 1️⃣ 定义与存储 Definition & Representation

**中文**：  
多叉树（N-ary Tree）是每个节点可拥有任意数量子节点的树结构。常见存储形式包括：  
- 邻接表 `List<Integer>[]`  
- 节点对象 `Node { val; List<Node> children; }`  
- 二叉化存储（左孩子右兄弟法）  

**English**:  
An N-ary Tree allows each node to have zero or more children. Common representations:  
- Adjacency list `List<Integer>[]`  
- Object-based `Node { val; List<Node> children; }`  
- Binary encoding (Left-Child Right-Sibling)

```java
class Node {
    public int val;
    public List<Node> children = new ArrayList<>();
    public Node(int v){ val = v; }
}
```

---

## 2️⃣ 遍历 Traversals

### 2.1 前序 Preorder / 后序 Postorder

**思路**：  
先序在访问根节点后访问子节点；后序则相反。  

**Idea**:  
Preorder visits root before children; Postorder visits children before root.

```java
void preorder(Node root, List<Integer> res){
    if (root == null) return;
    res.add(root.val);
    for (Node ch : root.children) preorder(ch, res);
}

void postorder(Node root, List<Integer> res){
    if (root == null) return;
    for (Node ch : root.children) postorder(ch, res);
    res.add(root.val);
}
```
**复杂度**：时间 O(n)，空间 O(h)。

### 2.2 层序 Level Order (BFS)

**思路**：利用队列逐层访问节点。  
**Idea**: Use a queue to traverse level by level.

```java
List<List<Integer>> levelOrder(Node root){
    List<List<Integer>> ans = new ArrayList<>();
    if (root == null) return ans;
    Queue<Node> q = new ArrayDeque<>();
    q.add(root);
    while(!q.isEmpty()){
        int size = q.size();
        List<Integer> level = new ArrayList<>();
        for(int i=0; i<size; i++){
            Node cur = q.poll();
            level.add(cur.val);
            for(Node ch: cur.children) q.add(ch);
        }
        ans.add(level);
    }
    return ans;
}
```

---

## 3️⃣ 构建与序列化 Construction & Serialization

### 3.1 从父数组构建 Build from Parent Array
```java
Node buildFromParent(int[] parent, int[] val){
    int n = parent.length;
    Node[] nodes = new Node[n];
    for(int i=0;i<n;i++) nodes[i] = new Node(val[i]);
    int root = -1;
    for(int i=0;i<n;i++){
        if (parent[i] == -1) root = i;
        else nodes[parent[i]].children.add(nodes[i]);
    }
    return nodes[root];
}
```

### 3.2 序列化与反序列化 Serialize / Deserialize
**格式**：`val childCount [child1 ... childK]`  

```java
void serialize(Node root, StringBuilder sb){
    if(root == null){ sb.append("# "); return; }
    sb.append(root.val).append(" ").append(root.children.size()).append(" ");
    for(Node ch: root.children) serialize(ch, sb);
}

Node deserialize(Deque<String> dq){
    String t = dq.pollFirst();
    if (t.equals("#")) return null;
    int val = Integer.parseInt(t);
    int k = Integer.parseInt(dq.pollFirst());
    Node root = new Node(val);
    for(int i=0;i<k;i++) root.children.add(deserialize(dq));
    return root;
}
```

---

## 4️⃣ 基本性质 Basic Properties

### 最大深度 / 节点数 / 叶子数
**思路**：后序递归。

```java
int maxDepth(Node r){
    if(r == null) return 0;
    int max = 0;
    for(Node ch: r.children) max = Math.max(max, maxDepth(ch));
    return max + 1;
}
```

### 直径 Diameter (LC 1522)

**思路**：合并每个节点的两条最长下行路径。  
**Idea**：Track top two longest child depths.

```java
class Diameter {
    int dia = 0;
    int dfs(Node r){
        if(r == null) return 0;
        int a=0, b=0;
        for(Node ch: r.children){
            int d = dfs(ch);
            if(d > a){ b=a; a=d; }
            else if(d > b) b=d;
        }
        dia = Math.max(dia, a + b + 1);
        return a + 1;
    }
}
```
**复杂度**：O(n)。  

---

## 5️⃣ 最近公共祖先 LCA

**思路**：二进制提升（Binary Lifting）。  
预处理每个节点的 2^k 祖先。  

```java
class LCA {
    int[][] up;
    int[] depth;
    List<Integer>[] g;
    int n, LOG;

    LCA(List<Integer>[] g, int root){
        this.g = g; n = g.length;
        LOG = 1; while((1<<LOG) <= n) LOG++;
        up = new int[LOG][n];
        depth = new int[n];
        dfs(root, -1, 0);
        for(int k=1;k<LOG;k++)
            for(int v=0;v<n;v++)
                up[k][v] = up[k-1][v] == -1 ? -1 : up[k-1][ up[k-1][v] ];
    }
    void dfs(int u, int p, int d){
        up[0][u] = p; depth[u] = d;
        for(int v: g[u]) if(v != p) dfs(v, u, d+1);
    }
    int lift(int u, int k){
        for(int i=0;i<LOG;i++) if(((k>>i)&1)==1) u = up[i][u];
        return u;
    }
    int lca(int a, int b){
        if(depth[a] < depth[b]){ int t=a;a=b;b=t; }
        a = lift(a, depth[a]-depth[b]);
        if(a==b) return a;
        for(int k=LOG-1;k>=0;k--){
            if(up[k][a] != up[k][b]){
                a = up[k][a];
                b = up[k][b];
            }
        }
        return up[0][a];
    }
}
```

**复杂度**：预处理 O(n log n)，查询 O(log n)。

---

## 6️⃣ 子树查询 Subtree Queries

**思路**：Euler Tour + 进出时间。子树映射为区间 `[tin, tout]`。

```java
class SubtreeIndex {
    int[] tin, tout, seq;
    int timer = 0;
    List<Integer>[] g;

    SubtreeIndex(List<Integer>[] g){
        this.g = g;
        int n = g.length;
        tin = new int[n];
        tout = new int[n];
        seq = new int[n];
    }
    void dfs(int u, int p){
        seq[tin[u] = timer++] = u;
        for(int v: g[u]) if(v != p) dfs(v, u);
        tout[u] = timer - 1;
    }
}
```

查询时在 `[tin[u], tout[u]]` 上做前缀和或区间操作。  

---

## 7️⃣ 树形动态规划 Tree DP

### 示例：最大独立集 Maximum Independent Set

**思路**：选/不选当前节点的最优值。  
**Idea**：Classic 0-1 DP on tree edges.

```java
void dfsMIS(int u, int p, List<Integer>[] g, int[] w, int[][] dp){
    dp[u][1] = w[u];
    for(int v: g[u]) if(v != p){
        dfsMIS(v, u, g, w, dp);
        dp[u][0] += Math.max(dp[v][0], dp[v][1]);
        dp[u][1] += dp[v][0];
    }
}
```
**复杂度**：O(n)。  

---

## 8️⃣ 经典题型题解 Classic Problems

| 题号 | 名称 | 思路 | 复杂度 |
|------|------|------|--------|
| LC 589 | N-ary Tree Preorder Traversal | DFS 前序 | O(n) |
| LC 590 | N-ary Tree Postorder Traversal | DFS 后序 | O(n) |
| LC 429 | N-ary Tree Level Order Traversal | BFS 队列 | O(n) |
| LC 559 | Maximum Depth of N-ary Tree | 后序递归 | O(n) |
| LC 1522 | Diameter of N-ary Tree | 双最大路径合并 | O(n) |
| LC 431 | Encode N-ary Tree to Binary Tree | 左孩子右兄弟 | O(n) |

---

## 9️⃣ 易错点与总结 Pitfalls & Summary

- 子节点列表可能为空，务必初始化。  
- 极深链需注意栈深限制。  
- 直径单位统一（节点数 vs 边数）。  
- LCA 根必须固定，森林需虚根处理。  
- Euler 区间边界一致性 `[tin, tout]`。  

---

✅ **时间复杂度总结**  
| 类别 | 复杂度 |
|------|---------|
| 遍历 | O(n) |
| 构建/序列化 | O(n) |
| LCA 预处理 | O(n log n) |
| LCA 查询 | O(log n) |
| 子树查询 | O(n + q log n) |
| 树形 DP | O(n) |

---

**完 End.**
