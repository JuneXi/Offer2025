 # 🌳 二叉树类型⑥：视图与层级类 (Binary Tree View & Level Problems)

> **格式 / Format：** 中英文对照 | CN-EN side-by-side

---

## 一、概述 Overview

**中文讲解：** 视图与层级类问题围绕“层序遍历（BFS）”或“带层/坐标信息的 DFS”。常见任务包括按层分组、从不同方向观察、或在同层间建立连接。  
**English:** These problems revolve around **BFS level-order** or **DFS with level/coordinate tags**. Typical tasks: grouping by level, viewing from directions, or linking neighbors within the same level.

**核心范式 / Core Patterns**  
- **BFS**：队列逐层扫描，天然适合“每层统计/输出”。  
  **BFS**: Queue-based traversal. Natural for per-level aggregation.  
- **DFS+坐标**：维护 `(row, col/diag)` 来定义“可见性顺序”。  
  **DFS+coordinates**: Track `(row, col/diag)` to define view order.  
- **映射结构**：`Map<Integer, ...>` 以列/对角线聚合；使用 `TreeMap` 保证顺序。  
  **Maps**: `Map<Integer, ...>` groups by column/diagonal; `TreeMap` keeps order.

---

## 二、基础模板：层序遍历 Level Order Template

**思路 / Idea：** 每次固定当前层节点数 `size = q.size()`，完整处理一层后再入队下一层。  
**复杂度 / Complexity：** O(n) 时间，O(n) 空间。

```java
public List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> res = new ArrayList<>();
    if (root == null) return res;
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);

    while (!q.isEmpty()) {
        int size = q.size();
        List<Integer> level = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TreeNode node = q.poll();
            level.add(node.val);
            if (node.left != null) q.offer(node.left);
            if (node.right != null) q.offer(node.right);
        }
        res.add(level);
    }
    return res;
}
```

**要点 / Notes：** 固定层宽度避免“跨层污染”；任何“每层统计”都可嵌入 for-loop 内部。  
**Use:** Embed per-level logic in the inner loop safely.

---

## 三、右视图 Right Side View

**题意 / Task：** 从右侧观察，每层只看见一个节点。  
**思路 / Idea：** BFS 取每层“最后一个出队”的节点；或 DFS 右优先，遇到新层第一个节点即记录。  
**复杂度 / Complexity：** O(n)。

```java
public List<Integer> rightSideView(TreeNode root) {
    List<Integer> res = new ArrayList<>();
    if (root == null) return res;
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);

    while (!q.isEmpty()) {
        int size = q.size();
        for (int i = 0; i < size; i++) {
            TreeNode node = q.poll();
            if (i == size - 1) res.add(node.val); // 每层最后一个 | last of level
            if (node.left != null) q.offer(node.left);
            if (node.right != null) q.offer(node.right);
        }
    }
    return res;
}
```

**坑点 / Pitfalls：** 若用 DFS，请 **先右后左**；需要用 `depth` 只在“首次到达该深度”时记录。  
**DFS hint:** Visit right first and record the first node seen at each depth.

---

## 四、之字形层序 Zigzag Level Order

**思路 / Idea：** 层序基础上交替方向。用 `LinkedList` 双端插入，避免整体反转的额外成本。  
**复杂度 / Complexity：** O(n)。

```java
public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
    List<List<Integer>> res = new ArrayList<>();
    if (root == null) return res;
    Queue<TreeNode> q = new LinkedList<>();
    boolean leftToRight = true;
    q.offer(root);

    while (!q.isEmpty()) {
        int size = q.size();
        LinkedList<Integer> level = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            TreeNode node = q.poll();
            if (leftToRight) level.addLast(node.val);
            else level.addFirst(node.val);
            if (node.left != null) q.offer(node.left);
            if (node.right != null) q.offer(node.right);
        }
        res.add(level);
        leftToRight = !leftToRight;
    }
    return res;
}
```

**小结 / Tip：** 双端插入优于每层 `Collections.reverse(level)`。  
**Prefer:** Deque insert over per-level reversing.

---

## 五、层平均值 Average of Levels

**思路 / Idea：** 层序 + 累加求平均；用 `long` 防止溢出。  
**复杂度 / Complexity：** O(n)。

```java
public List<Double> averageOfLevels(TreeNode root) {
    List<Double> res = new ArrayList<>();
    if (root == null) return res;
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);

    while (!q.isEmpty()) {
        int size = q.size();
        long sum = 0;
        for (int i = 0; i < size; i++) {
            TreeNode node = q.poll();
            sum += node.val;
            if (node.left != null) q.offer(node.left);
            if (node.right != null) q.offer(node.right);
        }
        res.add(sum * 1.0 / size);
    }
    return res;
}
```

**扩展 / Variants：** 515 层最大值、找层最小值/中位数都在本模板内修改统计逻辑即可。  
**Extend:** Replace sum with max/min/median bookkeeping.

---

## 六、垂直与投影视图 Vertical / Top / Bottom / Diagonal

> **坐标定义 / Coordinates：** 令根 `row=0, col=0`。左孩子 `(row+1, col-1)`，右孩子 `(row+1, col+1)`。  
> **定义目的 / Why:** 用于“列优先”或“层内排序”的可重复判定。

### 6.1 垂直遍历 Vertical Order Traversal (LC 987 风格排序)

**规则 / Rule：** 先按 `col` 升序；同列内按 `row` 升序；若同 `(row,col)`，按 `val` 升序。  
**复杂度 / Complexity：** O(n log n) 由排序主导。

```java
class Solution {
    public List<List<Integer>> verticalTraversal(TreeNode root) {
        Map<Integer, List<int[]>> colMap = new TreeMap<>(); // col -> [row, val]
        dfs(root, 0, 0, colMap);
        List<List<Integer>> res = new ArrayList<>();
        for (List<int[]> bucket : colMap.values()) {
            bucket.sort((a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);
            List<Integer> cur = new ArrayList<>();
            for (int[] p : bucket) cur.add(p[1]);
            res.add(cur);
        }
        return res;
    }
    private void dfs(TreeNode node, int row, int col, Map<Integer, List<int[]>> map) {
        if (node == null) return;
        map.computeIfAbsent(col, k -> new ArrayList<>()).add(new int[]{row, node.val});
        dfs(node.left, row + 1, col - 1, map);
        dfs(node.right, row + 1, col + 1, map);
    }
}
```

**讲解 / Notes：** `TreeMap` 将列自动排序；每列内部再按 `(row,val)` 排。

### 6.2 顶部视图 Top View

**规则 / Rule：** 每列保留**第一次**遇到的节点（最靠上）。  
**实现 / Impl：** BFS + `putIfAbsent(col, val)`。

```java
public List<Integer> topView(TreeNode root) {
    List<Integer> res = new ArrayList<>();
    if (root == null) return res;
    Map<Integer, Integer> top = new TreeMap<>();
    Queue<Map.Entry<TreeNode, Integer>> q = new LinkedList<>();
    q.offer(new AbstractMap.SimpleEntry<>(root, 0));
    while (!q.isEmpty()) {
        var e = q.poll();
        TreeNode node = e.getKey();
        int col = e.getValue();
        top.putIfAbsent(col, node.val);
        if (node.left != null) q.offer(new AbstractMap.SimpleEntry<>(node.left, col - 1));
        if (node.right != null) q.offer(new AbstractMap.SimpleEntry<>(node.right, col + 1));
    }
    res.addAll(top.values());
    return res;
}
```

### 6.3 底部视图 Bottom View

**规则 / Rule：** 每列保留**最后**遇到的节点（最靠下）。  
**实现 / Impl：** BFS + `put(col, val)` 每次覆盖。

```java
public List<Integer> bottomView(TreeNode root) {
    List<Integer> res = new ArrayList<>();
    if (root == null) return res;
    Map<Integer, Integer> bottom = new TreeMap<>();
    Queue<Map.Entry<TreeNode, Integer>> q = new LinkedList<>();
    q.offer(new AbstractMap.SimpleEntry<>(root, 0));
    while (!q.isEmpty()) {
        var e = q.poll();
        TreeNode node = e.getKey();
        int col = e.getValue();
        bottom.put(col, node.val); // 覆盖 | overwrite
        if (node.left != null) q.offer(new AbstractMap.SimpleEntry<>(node.left, col - 1));
        if (node.right != null) q.offer(new AbstractMap.SimpleEntry<>(node.right, col + 1));
    }
    res.addAll(bottom.values());
    return res;
}
```

### 6.4 对角线视图 Diagonal View

**坐标 / Coord：** 同一对角线共享同一 `diag`；左孩子 `diag+1`，右孩子同 `diag`。  
**实现 / Impl：** DFS + `Map<Integer, List<Integer>>`。

```java
public List<List<Integer>> diagonalView(TreeNode root) {
    Map<Integer, List<Integer>> map = new TreeMap<>();
    dfs(root, 0, map);
    return new ArrayList<>(map.values());
}
private void dfs(TreeNode node, int diag, Map<Integer, List<Integer>> map) {
    if (node == null) return;
    map.computeIfAbsent(diag, k -> new ArrayList<>()).add(node.val);
    dfs(node.left, diag + 1, map);
    dfs(node.right, diag, map);
}
```

**应用 / Use:** 常用于“右上方向”视角的按对角分组。

---

## 七、层连接类 Next Pointer Connections

### 7.1 LC116 完美二叉树 Perfect Binary Tree

**思路 / Idea：** 利用上一层的 `.next` 已建立的链，O(1) 空间连接本层。

```java
public Node connect(Node root) {
    if (root == null) return null;
    Node leftmost = root;
    while (leftmost.left != null) {
        for (Node head = leftmost; head != null; head = head.next) {
            head.left.next = head.right;
            if (head.next != null) head.right.next = head.next.left;
        }
        leftmost = leftmost.left;
    }
    return root;
}
```

### 7.2 LC117 任意二叉树 General Binary Tree

**思路 / Idea：** 用 `dummy` 构造下一层链，在当前层通过 `.next` 横向扫描。

```java
public Node connect(Node root) {
    Node head = root;
    while (head != null) {
        Node dummy = new Node(0), cur = dummy;
        for (Node p = head; p != null; p = p.next) {
            if (p.left != null) { cur.next = p.left; cur = cur.next; }
            if (p.right != null) { cur.next = p.right; cur = cur.next; }
        }
        head = dummy.next;
    }
    return root;
}
```

**对比 / Compare：** 116 用结构对称性；117 需“自建层链”。均 O(n) 时间，O(1) 额外空间。

---

## 八、复杂度与技巧总结 Summary Table

| 类型 Type | 技术核心 Core | 输出控制 Output | 时间 Time |
|---|---|---|---|
| 层序 Level Order | BFS | 每层列表 | O(n) |
| 右视图 Right View | BFS/DFS | 每层最后 | O(n) |
| 之字形 Zigzag | BFS+Deque | 交替方向 | O(n) |
| 平均值 Average | BFS+Sum | 每层平均 | O(n) |
| 垂直 Vertical | DFS+坐标 | 列排序 | O(n log n) |
| 顶/底 Top/Bottom | BFS+Map | 首/尾可见 | O(n log n) |
| 对角 Diagonal | DFS | 对角分组 | O(n) |
| Next Pointer | 指针构链 | 横向相邻 | O(n) |

---

## 九、检查清单与易错点 Checklist & Pitfalls

- **固定层宽**：内层循环前取 `size = q.size()`，避免跨层混入。  
  **Fix level width** with `size` to prevent mixing nodes from next level.
- **长整型求和**：层平均值用 `long` 累加。  
  Use `long` to avoid overflow in averages.
- **DFS 右优先**：右视图若用 DFS，顺序应是 `right → left`。  
  Right-first for DFS right view.
- **坐标排序**：LC987 需要 `(col, row, val)` 排序顺序，易错。  
  Sort by `(col,row,val)` exactly for LC987.
- **TreeMap**：列或对角线输出常需有序映射。  
  Use `TreeMap` for ordered columns/diagonals.
- **O(1) 连接**：116/117 的 `.next` 解法都不需要额外队列。  
  116/117 can be done without extra queues.

---

## 十、总结 Conclusion

**中文：** 以“层”为核心单位，用 **BFS 控层**、**DFS 标坐标**，再配合 `Map/TreeMap` 完成分组与排序。掌握上述模板后，变体仅在“层内统计或排序规则”上变化。  
**English:** Treat level as the atomic unit. Use **BFS** for level control and **DFS** for coordinates. With `Map/TreeMap`, grouping and ordering are straightforward. Variants differ only in per-level aggregation and ordering rules.
