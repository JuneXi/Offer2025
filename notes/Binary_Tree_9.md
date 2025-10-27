# 区间与子树比较类 (Subtree / Mirror / Symmetry)

---

## 📘 一、分类概述 Overview

**类型名称**：区间与子树比较类 (Subtree / Mirror / Symmetry)

**核心思想**：
比较两棵树（或同一棵树的两个部分）是否满足某种结构与数值关系。
本质是 **双树递归 (Double Tree Recursion)**，判断左右子树的对应关系。

---

## 📗 二、常见题型与思路 Common Problem Types

| 类型     | 英文名            | 常见题目                         | 思路                                     |
| ------ | -------------- | ---------------------------- | -------------------------------------- |
| ① 子树判断 | Subtree        | 572. Subtree of Another Tree | 判断 `root` 中是否存在与 `subRoot` 结构和值完全相同的子树 |
| ② 对称树  | Symmetric Tree | 101. Symmetric Tree          | 判断一棵树是否关于中心对称                          |
| ③ 镜像树  | Mirror Tree    | (扩展题)                        | 判断两棵树是否互为镜像                            |
| ④ 相同树  | Same Tree      | 100. Same Tree               | 判断两棵树结构和值是否完全一致                        |

---

## 📙 三、核心模板 Templates

### 1. 判断两棵树是否相同 — Same Tree

**题意**：
给定两棵树 `p` 和 `q`，判断是否结构和值完全相同。

**思路**：
同步 DFS 遍历两棵树。

* 若都为空，返回 true。
* 若一个空一个非空，返回 false。
* 否则比较当前节点值，再递归比较左右子树。

```java
public boolean isSameTree(TreeNode p, TreeNode q) {
    if (p == null && q == null) return true;
    if (p == null || q == null) return false;
    if (p.val != q.val) return false;
    return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(h)

---

### 2. 子树判断 — Subtree of Another Tree (LeetCode 572)

**题意**：
判断 `subRoot` 是否为 `root` 的一部分。

**思路**：

1. 若当前节点相同，尝试判断是否为相同树。
2. 否则继续在左右子树递归查找。

```java
public boolean isSubtree(TreeNode root, TreeNode subRoot) {
    if (root == null) return false;
    if (isSameTree(root, subRoot)) return true; // here is a diff cunction
    return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
}
```

**Time Complexity**: O(m × n)
**Optimization**: KMP / HashTree 可优化为 O(m + n)

---

### 3. 对称树 — Symmetric Tree (LeetCode 101)

**题意**：
判断一棵树是否关于中心对称。

**思路**：
判断左子树与右子树是否互为镜像。
定义 `isMirror(left, right)`：

* 都为空 → true
* 一个空 → false
* 值相等，左的左对右的右，左的右对右的左。

```java
public boolean isSymmetric(TreeNode root) {
    if (root == null) return true;
    return isMirror(root.left, root.right);
}

private boolean isMirror(TreeNode left, TreeNode right) {
    if (left == null && right == null) return true;
    if (left == null || right == null) return false;
    if (left.val != right.val) return false;
    return isMirror(left.left, right.right) && isMirror(left.right, right.left);
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(h)

---

### 4. 镜像树 — Mirror Tree (Two different trees)

**题意**：
判断两棵树是否互为镜像。

```java
public boolean isMirrorTree(TreeNode a, TreeNode b) {
    if (a == null && b == null) return true;
    if (a == null || b == null) return false;
    return (a.val == b.val)
        && isMirrorTree(a.left, b.right)
        && isMirrorTree(a.right, b.left);
}
```

---

## 📒 五、总结 Summary

| 题型        | 比较对象       | 判断方式           | 核心函数                       |
| --------- | ---------- | -------------- | -------------------------- |
| Same Tree | 两棵树        | 同步 DFS         | `isSameTree(p, q)`         |
| Subtree   | 主树中是否存在子结构 | 遍历 + Same Tree | `isSubtree(root, subRoot)` |
| Symmetric | 一棵树左右是否镜像  | 双指针镜像 DFS      | `isMirror(left, right)`    |
| Mirror    | 两棵不同树是否镜像  | 双树镜像 DFS       | `isMirrorTree(a, b)`       |

---

## 📔 六、易错点 Pitfalls

1. 空节点判断顺序：先判 null 再取值。
2. 对称树方向：左子树的左对右子树的右。
3. Subtree 判定：节点值相同时必须继续比较整个结构。
4. 性能优化：可使用哈希或序列化字符串匹配（如 KMP）提高效率。
