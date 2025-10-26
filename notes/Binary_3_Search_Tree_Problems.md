# 🌲 二叉搜索树题型 Binary Search Tree (BST) Problems

---

## 一、定义 Definition

**二叉搜索树（BST）** 满足以下性质：  
对任意节点 root：  
- 左子树所有节点的值 < root.val  
- 右子树所有节点的值 > root.val  
- 左右子树本身也都是 BST

这使得 BST 在搜索、插入、删除等操作中具有 **二分特性**，时间复杂度为 O(log n)（平衡时）。

---

## 二、核心特点 Key Features

| 特点 | 说明 |
|------|------|
| 有序结构 | 左小右大，天然支持范围查询 |
| 剪枝搜索 | 根据大小关系决定进入左或右 |
| 中序遍历升序 | Inorder Traversal 结果是有序数组 |
| 子树独立性 | 每个子树仍是 BST，可递归处理 |

---

## 三、常见题型分类 Common Problems

| 题号 | 题名 | 思路 |
|------|------|------|
| 700 | Search in a BST | 标准二分查找递归或迭代 |
| 701 | Insert into a BST | 沿搜索路径插入新节点 |
| 450 | Delete Node in a BST | 删除节点并保持 BST 性质 |
| 98 | Validate BST | 限定范围验证中序有序 |
| 230 | Kth Smallest Element | 中序第 k 个节点 |
| 235 | LCA in BST | 根据值关系剪枝求最近公共祖先 |

---

## 四、题目详解

### 1️⃣ 700. Search in a Binary Search Tree

**题意：**  
查找 BST 中值为 val 的节点，返回该节点（及其子树）。

**思路：**
- 若 root == null → 不存在；
- 若 root.val == val → 找到；
- 若 val < root.val → 搜左子树；
- 否则 → 搜右子树。

```java
TreeNode searchBST(TreeNode root, int val) {
    if (root == null || root.val == val) return root;
    return val < root.val ? searchBST(root.left, val)
                          : searchBST(root.right, val);
}
```
**复杂度：** O(h)，其中 h 为树高。

---

### 2️⃣ 701. Insert into a BST

**题意：**  
在 BST 中插入新节点，返回根节点。

**思路：**
沿搜索路径递归查找插入位置，遇到 null 时创建节点。

```java
TreeNode insertIntoBST(TreeNode root, int val) {
    if (root == null) return new TreeNode(val);
    if (val < root.val) root.left = insertIntoBST(root.left, val);
    else root.right = insertIntoBST(root.right, val);
    return root;
}
```
**复杂度：** O(h)

---

### 3️⃣ 450. Delete Node in a BST

**题意：**  
删除 BST 中值为 key 的节点。

**思路：**
1. 找到目标节点；  
2. 若无子节点 → 直接删除；  
3. 若只有一侧 → 返回子树；  
4. 若左右都有 → 找右子树最小节点替换。

```java
TreeNode deleteNode(TreeNode root, int key) {
    if (root == null) return null;
    if (key < root.val) root.left = deleteNode(root.left, key);
    else if (key > root.val) root.right = deleteNode(root.right, key);
    else {
        if (root.left == null) return root.right;
        if (root.right == null) return root.left;
        TreeNode min = findMin(root.right);
        root.val = min.val;
        root.right = deleteNode(root.right, min.val);
    }
    return root;
}
TreeNode findMin(TreeNode node) {
    while (node.left != null) node = node.left;
    return node;
}
```
**复杂度：** O(h)

---

### 4️⃣ 98. Validate Binary Search Tree

**题意：**  
判断树是否为合法 BST。

**思路：**
递归时维护当前节点的合法区间 `[min, max)`。  
每个节点必须满足：`min < val < max`。

```java
boolean isValidBST(TreeNode root) {
    return helper(root, Long.MIN_VALUE, Long.MAX_VALUE);
}
boolean helper(TreeNode root, long min, long max) {
    if (root == null) return true;
    if (root.val <= min || root.val >= max) return false;
    return helper(root.left, min, root.val) &&
           helper(root.right, root.val, max);
}
```
**复杂度：** O(n)

---

### 5️⃣ 230. Kth Smallest Element in BST

**题意：**  
返回 BST 中第 k 小的元素。

**思路：**
中序遍历（左→根→右）即升序序列。  
用计数器记录访问顺序，取第 k 个节点。

```java
int count = 0, res = 0;
void inorder(TreeNode root, int k) {
    if (root == null) return;
    inorder(root.left, k);
    count++;
    if (count == k) { res = root.val; return; }
    inorder(root.right, k);
}
int kthSmallest(TreeNode root, int k) {
    inorder(root, k);
    return res;
}
```
**复杂度：** O(h + k)

---

### 6️⃣ 235. Lowest Common Ancestor of a BST

**题意：**  
在 BST 中找到节点 p 和 q 的最近公共祖先。

**思路：**
利用 BST 性质：
- 若 p,q < root → 在左子树；  
- 若 p,q > root → 在右子树；  
- 否则当前节点为 LCA。

```java
TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null) return null;
    if (p.val < root.val && q.val < root.val)
        return lowestCommonAncestor(root.left, p, q);
    if (p.val > root.val && q.val > root.val)
        return lowestCommonAncestor(root.right, p, q);
    return root;
}
```
**复杂度：** O(h)

---

## 五、复杂度总结 Complexity Summary

| 题号 | 名称 | 时间复杂度 | 空间复杂度 | 核心操作 |
|------|------|-------------|-------------|------------|
| 700 | 搜索节点 | O(h) | O(h) | 二分搜索 |
| 701 | 插入节点 | O(h) | O(h) | 递归插入 |
| 450 | 删除节点 | O(h) | O(h) | 替换右子树最小值 |
| 98 | 验证BST | O(n) | O(h) | 区间检查 |
| 230 | 第K小元素 | O(h+k) | O(h) | 中序计数 |
| 235 | 最近公共祖先 | O(h) | O(h) | 值比较剪枝 |

---

## 六、通用规律 Key Takeaways

1. **剪枝思想**：比较大小决定递归方向。  
2. **中序遍历特性**：BST 的中序遍历结果天然有序。  
3. **值域约束法**：验证 BST 时用区间限制而非排序数组。  
4. **节点替换法**：删除节点时用“右子树最小”或“左子树最大”替代。  
5. **复杂度一般为 O(h)**，若平衡则 O(log n)，若退化为链表则 O(n)。

---

_English Summary_

BST problems rely on the ordering property (left < root < right).  
They can be solved with pruning (go left/right), range checking, or inorder traversal.  
All main operations (search, insert, delete, validate, find kth, LCA) run in O(h) time on average.
