# 🌳 二叉树属性计算类题目 Binary Tree Property Problems

---

## 一、通用思路 General Idea

属性计算类题目主要用于计算树的“数值特征”，如深度、高度、路径和、直径等。  
**核心特征：后序遍历（postorder）**——先计算子树，再根据左右子树信息组合出当前节点的结果。

### 通用框架
```java
Info dfs(TreeNode root){
    if(root == null) return new Info(...base values...);
    Info L = dfs(root.left);
    Info R = dfs(root.right);
    // Combine L and R to produce current result
    return merge(L, R, root);
}
```
`Info` 是自定义结构，用于封装子树信息（高度、平衡性、路径值等）。  
> 后序遍历天然适合自底向上计算。每个节点只访问一次，因此复杂度通常 O(n)。

---

## 二、类型与特点 Features

| 类别 | 思路 | 特点 | 常见题 |
|------|------|------|--------|
| 高度/深度类 | max(left,right)+1 | 基础模板题 | 104 |
| 平衡性判断 | 同时返回高度与标记 | 提前剪枝 | 110 |
| 路径长度类 | 全局变量更新 | 结合高度信息 | 543 |
| 路径和类 | 累加或前缀和 | 可包含负数或任意起点 | 112 / 124 / 437 |
| 公共祖先类 | 返回非空子树 | 逻辑分支清晰 | 236 |

---

## 三、典型题目与详细讲解

### 1️⃣ 104. Maximum Depth of Binary Tree
**题意：**  
给定一棵二叉树，返回其最大深度（根到最远叶子的节点数）。

**思路：**  
每个节点的深度 = `1 + max(左子树深度, 右子树深度)`。  
空节点返回 0，使用后序遍历逐层累加。

```java
int maxDepth(TreeNode root) {
    if (root == null) return 0;
    int left = maxDepth(root.left);
    int right = maxDepth(root.right);
    return Math.max(left, right) + 1;
}
```
**复杂度：** O(n) 时间，O(h) 空间。

---

### 2️⃣ 110. Balanced Binary Tree
**题意：**  
判断一棵树是否为平衡二叉树（任意节点左右子树高度差 ≤ 1）。

**思路：**  
递归同时计算高度和平衡状态。  
若子树不平衡，返回特殊值 `-1` 提前剪枝。

```java
int check(TreeNode root) {
    if (root == null) return 0;
    int left = check(root.left);
    if (left == -1) return -1;
    int right = check(root.right);
    if (right == -1) return -1;
    if (Math.abs(left - right) > 1) return -1;
    return Math.max(left, right) + 1;
}
boolean isBalanced(TreeNode root) {
    return check(root) != -1;
}
```
**复杂度：** O(n) 时间，O(h) 空间。

---

### 3️⃣ 543. Diameter of Binary Tree
**题意：**  
返回任意两节点之间的最长路径长度（边数）。

**思路：**  
路径可经过当前节点，也可在某一子树内。  
直径 = 左右子树高度和。使用全局变量记录最大值。

```java
int diameter = 0;
int depth(TreeNode root) {
    if (root == null) return 0;
    int left = depth(root.left);
    int right = depth(root.right);
    diameter = Math.max(diameter, left + right);
    return Math.max(left, right) + 1;
}
int diameterOfBinaryTree(TreeNode root) {
    depth(root);
    return diameter;
}
```
**复杂度：** O(n) 时间，O(h) 空间。

---

### 4️⃣ 124. Binary Tree Maximum Path Sum
**题意：**  
求任意节点到任意节点的最大路径和。路径必须连续，但可“拐弯”经过根。

**思路：**  
每个节点返回“单侧最大贡献”给父节点；  
路径拐点处更新全局最大值。负贡献舍弃为0。 重点： 全局变量，以及与0比较

```java
int maxSum = Integer.MIN_VALUE;
int gain(TreeNode root) {
    if (root == null) return 0;
    int left = Math.max(0, gain(root.left));
    int right = Math.max(0, gain(root.right));
    maxSum = Math.max(maxSum, root.val + left + right);
    return root.val + Math.max(left, right);
}
int maxPathSum(TreeNode root) {
    gain(root);
    return maxSum;
}
```
**复杂度：** O(n) 时间，O(h) 空间。

---

### 5️⃣ 437. Path Sum III
**题意：**  
给定一棵二叉树和一个整数 k，求树中有多少条从任意节点出发、向下延伸的路径，其节点值之和等于 k。
统计树中路径和等于目标值 `k` 的路径数（可从任意节点开始，必须父→子方向）。

**思路：**  
利用“前缀和思想”：当前和为 `sum`，则之前出现的 `(sum - k)` 即形成一条有效路径。  
在递归中维护哈希表计数。

```java
int pathSum(TreeNode root, int k) {
    //key：前缀和 value：该前缀和出现的次数
    Map<Integer,Integer> prefix = new HashMap<>();
    prefix.put(0,1);
    return dfs(root, 0, k, prefix);
}
int dfs(TreeNode root, int curSum, int k, Map<Integer,Integer> prefix){
    if (root == null) return 0;
    curSum += root.val;
    int res = prefix.getOrDefault(curSum - k, 0);
    
    //当前节点的前缀和加入 map，表示接下来子节点可以使用它来形成路径。
    prefix.put(curSum, prefix.getOrDefault(curSum, 0) + 1);
    res += dfs(root.left, curSum, k, prefix);
    res += dfs(root.right, curSum, k, prefix);
    
    //因为当前路径只对当前分支有效，,离开该节点后不能影响兄弟子树的统计。
    prefix.put(curSum, prefix.get(curSum) - 1);
    return res;
}
```
**复杂度：** O(n) 时间，O(h) 平均空间。

---

### 6️⃣ 236. Lowest Common Ancestor of a Binary Tree
**题意：**  
给定二叉树及两个节点 p 和 q，返回它们的最近公共祖先。

**思路：**  
若当前节点为 null / p / q，直接返回；  
递归左右子树，若两边都非空则当前即 LCA。

```java
TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null || root == p || root == q) return root;
    TreeNode left = lowestCommonAncestor(root.left, p, q);
    TreeNode right = lowestCommonAncestor(root.right, p, q);
    if (left != null && right != null) return root;
    return left != null ? left : right;
}
```
**复杂度：** O(n) 时间，O(h) 空间。

---

## 四、复杂度总结 Complexity Summary

| 题号 | 名称 | 时间复杂度 | 空间复杂度 | 遍历类型 | 关键思想 |
|------|------|-------------|-------------|-----------|-----------|
| 104 | 最大深度 | O(n) | O(h) | 后序 | 高度递推 |
| 110 | 平衡树 | O(n) | O(h) | 后序 | 剪枝提前返回 |
| 543 | 直径 | O(n) | O(h) | 后序 | 高度+全局更新 |
| 124 | 最大路径和 | O(n) | O(h) | 后序 | 单侧贡献+全局最优 |
| 437 | 路径和III | O(n) | O(h) | 前序 | 前缀和统计 |
| 236 | 最近公共祖先 | O(n) | O(h) | 后序 | 返回非空子树 |

---

## 五、总结与注意事项 Key Takeaways

1. **后序是主力**：所有依赖“子树信息”组合的题几乎都用后序。  
2. **全局变量与返回值分离**：全局保存最优值，递归返回局部信息。  
3. **剪枝优化**：提前检测非法状态（如高度返回 -1）。  
4. **时间均为 O(n)**：每节点仅访问一次。  
5. **空间为 O(h)**：递归栈深度。  
6. **思考顺序：**  
   - 我想从子树得到什么？  
   - 如何用左右子树信息合并出当前节点？  
   - 是否需要全局变量保存结果？  

---

_English Summary_

All property problems on trees rely on **postorder traversal**.  
Each node collects information from its children and combines it to produce results.  
Common patterns: compute height, validate balance, update diameter, compute max path, count path sums, and find LCA.

All run in **O(n)** time and **O(h)** space, where *h* is the tree height.
