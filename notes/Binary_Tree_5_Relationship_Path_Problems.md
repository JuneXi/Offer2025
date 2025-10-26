# 🔗 二叉树“关系与路径”题型 Relationship & Path Problems

> 关注节点间关系、从根到叶路径、任意两点距离/路径、指定距离集合等。统一用“前序记录路径”或“后序汇总/回溯”，必要时建无向图做 BFS。

---

## 0. 通用思路与特点 General Idea & Features

- **根→叶路径**：前序携带路径，到叶子落盘；回溯清理。时间 O(n)，空间 O(h)。  
- **两点关系 / 最近公共祖先（LCA）**：后序返回命中状态，上溯判定。时间 O(n)。  
- **距离 K（子到任意方向）**：把树视为**无向图**，从起点 BFS 到距离 K。时间 O(n)。  
- **路径和**：见属性类（112/113/124/437），此处列出根到叶（129）与列举路径（113）。

---

## 1. 236. Lowest Common Ancestor of a Binary Tree

### 题意
给定二叉树与节点 p、q，返回其最近公共祖先（离两者最近且为其祖先的节点）。

### 思路
后序递归：
- 命中 null / p / q 则返回该节点；
- 左右递归结果都非空 ⇒ 当前节点即 LCA；
- 只有一侧非空 ⇒ 往上返回那一侧。

### Java
```java
TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null || root == p || root == q) return root;
    TreeNode L = lowestCommonAncestor(root.left, p, q);
    TreeNode R = lowestCommonAncestor(root.right, p, q);
    if (L != null && R != null) return root;
    return L != null ? L : R;
}
```

**复杂度**：O(n) 时间，O(h) 空间。

---

## 2. 257. Binary Tree Paths

### 题意
输出所有从根到叶子的路径，格式如 `["1->2->5","1->3"]`。

### 思路
前序 DFS 携带一个可变列表 `path`；到叶子时拼接为字符串放入答案；回溯弹出。

### Java
```java
List<String> binaryTreePaths(TreeNode root) {
    List<String> ans = new ArrayList<>();
    Deque<Integer> path = new ArrayDeque<>();
    dfs(root, path, ans);
    return ans;
}
void dfs(TreeNode r, Deque<Integer> path, List<String> ans){
    if (r == null) return;
    path.addLast(r.val);
    if (r.left == null && r.right == null) {
        ans.add(format(path));
    } else {
        dfs(r.left, path, ans);
        dfs(r.right, path, ans);
    }
    path.removeLast();
}
String format(Deque<Integer> path){
    StringBuilder sb = new StringBuilder();
    Iterator<Integer> it = path.iterator();
    if (it.hasNext()) sb.append(it.next());
    while (it.hasNext()) sb.append("->").append(it.next());
    return sb.toString();
}
```

**复杂度**：O(n·L) 时间（L 为平均路径长度），O(h) 额外空间。

---

## 3. 113. Path Sum II

### 题意
找出所有**根到叶**路径，使得节点值之和等于目标 `targetSum`。返回每条路径的节点值列表。

### 思路
前序累加和 + 回溯记录路径。遇叶子且和等于目标则加入答案。

### Java
```java
List<List<Integer>> pathSum(TreeNode root, int target) {
    List<List<Integer>> ans = new ArrayList<>();
    Deque<Integer> path = new ArrayDeque<>();
    dfs(root, 0, target, path, ans);
    return ans;
}
void dfs(TreeNode r, int sum, int target, Deque<Integer> path, List<List<Integer>> ans){
    if (r == null) return;
    path.addLast(r.val);
    sum += r.val;
    if (r.left == null && r.right == null && sum == target) {
        ans.add(new ArrayList<>(path));
    } else {
        dfs(r.left, sum, target, path, ans);
        dfs(r.right, sum, target, path, ans);
    }
    path.removeLast();
}
```

**复杂度**：O(n) 节点访问，输出总大小记为 S，则 O(n + S)。空间 O(h)。

---

## 4. 129. Sum Root to Leaf Numbers

### 题意
每条根到叶路径视为一个数字（沿途拼接十进制），返回所有路径数字之和。

### 思路
前序携带当前值 `cur = cur*10 + r.val`，到叶子累加进结果。

### Java
```java
int sumNumbers(TreeNode root) {
    return dfs(root, 0);
}
int dfs(TreeNode r, int cur){
    if (r == null) return 0;
    cur = cur * 10 + r.val;
    if (r.left == null && r.right == null) return cur;
    return dfs(r.left, cur) + dfs(r.right, cur);
}
```

**复杂度**：O(n) 时间，O(h) 空间。

---

## 5. 863. All Nodes Distance K in Binary Tree

### 题意
给根节点 `root`、目标节点 `target`、整数 `k`，返回距 `target` 节点恰好为 `k` 的所有节点值，顺序任意。

### 思路
树转无向图 + BFS：
1) DFS 记录每个节点的父指针（或邻接表）。  
2) 从 `target` 出发 BFS，走 `left/right/parent` 三个方向，到层数为 `k` 时收集。

### Java
```java
List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
    Map<TreeNode, TreeNode> parent = new HashMap<>();
    buildParent(root, null, parent);
    List<Integer> ans = new ArrayList<>();
    Queue<TreeNode> q = new ArrayDeque<>();
    Set<TreeNode> vis = new HashSet<>();
    q.offer(target);
    vis.add(target);
    int dist = 0;
    while(!q.isEmpty()){
        int sz = q.size();
        if (dist == k){
            while (sz-- > 0) ans.add(q.poll().val);
            break;
        }
        while (sz-- > 0){
            TreeNode x = q.poll();
            for (TreeNode nb : neighbors(x, parent)){
                if (nb != null && vis.add(nb)) q.offer(nb);
            }
        }
        dist++;
    }
    return ans;
}
void buildParent(TreeNode r, TreeNode p, Map<TreeNode,TreeNode> par){
    if (r == null) return;
    par.put(r, p);
    buildParent(r.left, r, par);
    buildParent(r.right, r, par);
}
List<TreeNode> neighbors(TreeNode x, Map<TreeNode,TreeNode> par){
    List<TreeNode> res = new ArrayList<>(3);
    res.add(x.left); res.add(x.right); res.add(par.get(x));
    return res;
}
```

**复杂度**：O(n) 时间，O(n) 空间。

---

## 6. 习题加餐 Extra

- 112/113/129/437：路径和家族，见本套“属性类”与本节（113/129）。  
- 2096（从一个节点到另一个节点的最短路径指令）：LCA + 构造路径字符串。  
- 199（右视图）：层序或 DFS 右优先，属于视图类但常与路径/层级结合。

---

## 7. 易错点 Pitfalls

- 回溯遗漏 `path.removeLast()` 导致路径串污染。  
- `distance K` 未去重会反复走回父节点形成环。  
- 大数据下字符串拼接建议 `StringBuilder`。  
- 对于路径计数题，输出规模 S 需计入复杂度。

---

## 8. 复杂度总表 Summary

| 题号 | 名称 | 主解法 | 时间 | 空间 | 关键点 |
|---:|---|---|---|---|---|
| 236 | LCA | 后序DFS | O(n) | O(h) | 左右返回非空即根 |
| 257 | 根到叶路径 | 前序+回溯 | O(n·L) | O(h) | 叶子落盘 |
| 113 | 根到叶路径和II | 前序+回溯 | O(n+S) | O(h) | 累加与回溯 |
| 129 | 根到叶数字和 | 前序累积 | O(n) | O(h) | cur*10+val |
| 863 | 距离K | 建图+BFS | O(n) | O(n) | 三向邻居 + 去重 |

---

_English Summary_: Use preorder with backtracking for root-to-leaf lists and sums, postorder for LCA, and BFS on an undirected view of the tree for distance problems.
