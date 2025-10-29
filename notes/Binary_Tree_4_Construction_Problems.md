# 🏗️ 二叉树构造类题目 Binary Tree Construction Problems

> 格式：题意（中/英）→ 思路/为何可行 → 算法步骤 → Java 模板 → 复杂度 → 易错点

---

## 0. 总览 Overview

**核心套路**：用一个遍历序列**定根**，用另一个序列**切左右**；或用**规则/性质**分治。  
**不变量**：每一层递归都在构造“当前子树的根及其左右边界”。

---

## 1. 105. 从前序与中序构造二叉树
### 题意 Problem
- **输入**：`preorder`（前序：根→左→右），`inorder`（中序：左→根→右），且所有值唯一。
- **输出**：重建该二叉树的根节点。
- **Goal**：Return the root of the tree uniquely determined by preorder and inorder.

### 思路与为何可行 Intuition
- 前序首元素必为当前子树根 `rootVal`。
- 中序中 `rootVal` 的位置将序列分为“左子树 | 根 | 右子树”，左子树大小 `leftSize` 可得。
- 前序中根后的 `leftSize` 个元素属于左子树，剩余属于右子树。递归两侧。
- 值唯一 ⇒ 中序定位唯一 ⇒ 结构唯一。用哈希表 `value -> index` 降低查找到 O(1)。

### 算法步骤 Steps
1. 预处理中序索引表 `pos`。  
2. 递归函数携带当前区间 `(pl..pr, il..ir)`。  
3. 取 `pre[pl]` 为根，在中序找索引 `k`，计算 `leftSize = k - il`。  
4. 划分并递归构造左、右子树。

| 参数    | 含义              |
| ----- | --------------- |
| `pre` | 前序遍历数组          |
| `pl`  | 当前子树在前序数组的左边界索引 |
| `pr`  | 当前子树在前序数组的右边界索引 |
| `in`  | 中序遍历数组          |
| `il`  | 当前子树在中序数组的左边界索引 |
| `ir`  | 当前子树在中序数组的右边界索引 |


### 代码 Java
```java
class Solution {
    Map<Integer,Integer> pos;
    public TreeNode buildTree(int[] pre, int[] in) {
        pos = new HashMap<>();
        for (int i = 0; i < in.length; i++) pos.put(in[i], i);
        return build(pre, 0, pre.length-1, in, 0, in.length-1);
    }
    private TreeNode build(int[] pre, int pl, int pr, int[] in, int il, int ir){
        if (pl > pr) return null;
        int rootVal = pre[pl];
        int k = pos.get(rootVal);
        int left = k - il;
        TreeNode r = new TreeNode(rootVal);
        r.left  = build(pre, pl+1, pl+left, in, il, k-1);
        r.right = build(pre, pl+left+1, pr, in, k+1, ir);
        return r;
    }
}
```

### 复杂度 Complexity
- 时间 O(n)，空间 O(n)（哈希+递归栈）。

### 易错点 Pitfalls
- 区间越界；前序/中序区间需**同规模**对应。
- 值不唯一将破坏“唯一定位”。

---

## 2. 106. 从中序与后序构造二叉树
### 题意 Problem
- **输入**：`inorder`（左→根→右），`postorder`（左→右→根），值唯一。
- **输出**：还原二叉树根节点。

### 思路与为何可行 Intuition
- 后序末尾 `post[pr]` 是根。
- 在中序找到根索引 `k`，左侧为左子树，右侧为右子树，左规模 `left = k - il`。
- 因为后序尾部先取走根，右子树对应的后序区间在**左子树区间之后**。按区间递归。

### 算法步骤 Steps
1. 建立中序索引表。  
2. 取 `post[pr]` 为根；分割中序；用左规模切分后序区间。  
3. 先构造左或右都可，只要区间正确；常用“左后序在前，右后序在后”。

### 代码 Java
```java
class Solution {
    Map<Integer,Integer> pos;
    public TreeNode buildTree(int[] in, int[] post) {
        pos = new HashMap<>();
        for (int i = 0; i < in.length; i++) pos.put(in[i], i);
        return build(in, 0, in.length-1, post, 0, post.length-1);
    }
    private TreeNode build(int[] in, int il, int ir, int[] post, int pl, int pr){
        if (pl > pr) return null;
        int rootVal = post[pr];
        int k = pos.get(rootVal);
        int left = k - il;
        TreeNode r = new TreeNode(rootVal);
        r.left  = build(in, il, k-1, post, pl, pl+left-1);
        r.right = build(in, k+1, ir, post, pl+left, pr-1);
        return r;
    }
}
```

### 复杂度 Complexity
- 时间 O(n)，空间 O(n)。

### 易错点 Pitfalls
- 后序切分顺序；确保右子树后序区间是 `pl+left .. pr-1`。

---

## 3. 889. 从前序与后序构造二叉树（满二叉树唯一）
### 题意 Problem
- **输入**：`preorder` 与 `postorder`，节点值唯一。
- **输出**：重建任意一棵与遍历一致的二叉树。若树**满二叉**（每个非叶都有两个孩子），解唯一。

### 思路与为何可行 Intuition
- `pre[0]` 是根。若不是叶子，则 `pre[1]` 是**左子树根**。
- 在 `post` 中找到 `pre[1]` 的索引 `k`，左子树大小 `leftSize = k - ql + 1`。据此划分。
- 非满树时，可能出现多解；题目允许返回任意合法构造。

### 算法步骤 Steps
1. 用哈希记录 `post` 中值的位置。  
2. 递归：取 `pre[pl]` 为根；若 `pl==pr` 返回叶子。  
3. 定位 `pre[pl+1]` 在 `post` 的位置求左规模，划分构造两侧。

### 代码 Java
```java
class Solution {
    Map<Integer,Integer> postPos;
    public TreeNode constructFromPrePost(int[] pre, int[] post) {
        postPos = new HashMap<>();
        for (int i = 0; i < post.length; i++) postPos.put(post[i], i);
        return build(pre, 0, pre.length-1, post, 0, post.length-1);
    }
    private TreeNode build(int[] pre, int pl, int pr, int[] post, int ql, int qr){
        if (pl > pr) return null;
        TreeNode r = new TreeNode(pre[pl]);
        if (pl == pr) return r;
        int leftRoot = pre[pl+1];
        int k = postPos.get(leftRoot);
        int leftSize = k - ql + 1;
        r.left  = build(pre, pl+1, pl+leftSize, post, ql, k);
        r.right = build(pre, pl+leftSize+1, pr, post, k+1, qr-1);
        return r;
    }
}
```

### 复杂度 Complexity
- 时间 O(n)，空间 O(n)。

### 易错点 Pitfalls
- 非满树时的**多解**事实；测试数据允许任意合法答案。

---

## 4. 654. 最大二叉树 Maximum Binary Tree
### 题意 Problem
- **输入**：数组 `nums`（互不相同）。
- **规则**：根为数组最大值；左子树来自最大值左侧子数组；右子树来自最大值右侧子数组。
- **输出**：按规则构造的唯一二叉树。

### 思路与为何可行 Intuition
- 朴素分治：每层找区间最大值作为根，再递归两边，最坏 O(n²)。
- 线性最优：**单调递减栈**一次扫描。  
  - 当前数更大时，不断弹栈；被弹出的成为当前节点的**左子树**；  
  - 弹栈后若栈非空，栈顶作为当前节点的**父/右连接**。

### 算法步骤 Steps（单调栈）
1. 维护递减栈；遍历每个 `x`：  
   - 新建节点 `cur`，弹出所有 `< x` 的节点并作为 `cur.left`；  
   - 若栈非空，将 `cur` 设为 `stack.peek().right`；  
   - 压栈 `cur`。  
2. 最后栈底元素即根。

### 代码 Java
```java
class Solution {
    public TreeNode constructMaximumBinaryTree(int[] a) {
        Deque<TreeNode> st = new ArrayDeque<>();
        for (int x : a) {
            TreeNode cur = new TreeNode(x);
            while (!st.isEmpty() && st.peek().val < x) cur.left = st.pop();
            if (!st.isEmpty()) st.peek().right = cur;
            st.push(cur);
        }
        TreeNode root = null;
        while (!st.isEmpty()) root = st.pop();
        return root;
    }
}
```

### 复杂度 Complexity
- 时间 O(n)，空间 O(n)。

### 易错点 Pitfalls
- 左连接与右连接方向别写反；保证“被弹出成为新更大节点的左子树”。

---

## 5. 108. 有序数组转换为平衡 BST
### 题意 Problem
- **输入**：升序数组 `nums`。
- **输出**：高度平衡的 BST（任意节点左右高度差 ≤ 1）。

### 思路与为何可行 Intuition
- 取区间中点为根，左右递归同理 ⇒ 高度平衡。

### 算法步骤 Steps
1. 递归构造 `[l..r]`：取 `m = (l+r)/2` 为根；  
2. 左子树 `[l..m-1]`，右子树 `[m+1..r]`。

### 代码 Java
```java
class Solution {
    public TreeNode sortedArrayToBST(int[] a) {
        return build(a, 0, a.length-1);
    }
    private TreeNode build(int[] a, int l, int r){
        if (l > r) return null;
        int m = l + (r-l)/2;
        TreeNode root = new TreeNode(a[m]);
        root.left = build(a, l, m-1);
        root.right = build(a, m+1, r);
        return root;
    }
}
```

### 复杂度 Complexity
- 时间 O(n)，空间 O(h)。

### 易错点 Pitfalls
- 中点取法避免溢出；闭区间模板统一。

---

## 6. 109. 有序链表转换为平衡 BST
### 题意 Problem
- **输入**：升序**单链表** `head`。
- **输出**：高度平衡的 BST。

### 思路与为何可行 Intuition
- 方案 A：每次用快慢指针找中点作为根，左右递归（找中点 O(n) × 层数 O(log n) ⇒ O(n log n)）。
- 方案 B（推荐）：**中序模拟** O(n)。  
  - 先计长度 `n`，维护全局指针 `head`；  
  - 递归构造 `[l..r]`：先递归左子树，**用当前链表节点值作为根**，指针前进，再构造右子树；  
  - 与中序次序一致，链表只遍历一遍。

### 代码 Java（方案 B）
```java
class Solution {
    ListNode head;
    public TreeNode sortedListToBST(ListNode h) {
        int n = 0; for (ListNode p=h; p!=null; p=p.next) n++;
        head = h;
        return build(0, n-1);
    }
    private TreeNode build(int l, int r){
        if (l > r) return null;
        int m = l + (r-l)/2;
        TreeNode left = build(l, m-1);
        TreeNode root = new TreeNode(head.val);
        head = head.next;
        TreeNode right = build(m+1, r);
        root.left = left; root.right = right;
        return root;
    }
}
```

### 复杂度 Complexity
- 方案 A：时间 O(n log n)，空间 O(h)。  
- 方案 B：时间 O(n)，空间 O(h)。

### 易错点 Pitfalls
- 指针推进顺序严格在“构造根”处推进一次；否则错位。

---

## 7. 统一检查表 Checklist
- 值是否唯一？若否，`value->index` 需改为“值+出现次序”。  
- 区间是否对齐？`pre/in/post` 切分规模一致。  
- 递归基与越界条件是否正确？  
- 是否需要哈希索引表？未用会退化 O(n²)。  
- 题目是否允许多解？如 889 非满树。

---

## 8. 复杂度总表 Summary

| 题号 | 名称 | 时间 | 空间 | 关键点 |
|---:|---|---|---|---|
| 105 | Pre+In 构造 | O(n) | O(n) | 前序定根，中序切分，哈希定位 |
| 106 | In+Post 构造 | O(n) | O(n) | 后序尾根，中序切分 |
| 889 | Pre+Post 构造 | O(n) | O(n) | pre[1] 定左根，post 定左规模 |
| 654 | 最大二叉树 | O(n) | O(n) | 单调栈一次构造 |
| 108 | 数组→平衡BST | O(n) | O(h) | 中点分治 |
| 109 | 链表→平衡BST | O(n) | O(h) | 中序模拟，一次遍历 |
