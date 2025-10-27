
# 回溯法（Backtracking）、剪枝（Pruning）与状态缓存（Memoization）详解

---

## 一、回溯法核心思想

回溯法 = 深度优先搜索 + 剪枝。  
它通过递归构建解空间树（State Space Tree），逐步尝试所有可能解，并在发现不满足条件时“回退”（Backtrack）到上一步。

### 通用模板
```java
void backtrack(List<Integer> path, int[] nums) {
    if (path 满足结束条件) {
        保存结果;
        return;
    }
    for (int i = 起点; i < nums.length; i++) {
        if (剪枝条件) continue;
        path.add(nums[i]);
        backtrack(path, nums);
        path.remove(path.size()-1);
    }
}
```

**核心步骤**  
1. 选择（Try）  
2. 递归（Explore）  
3. 撤销（Undo）

---

## 二、典型类型

| 类型 | 示例 | 状态树结构 | 特点 |
|------|------|-------------|------|
| 子集 Subset | LC78 | 二叉树 | 每元素选/不选 |
| 排列 Permutation | LC46 | n! 分支 | 用 used[] |
| 组合 Combination | LC77 | 剪枝 | 固定长度 |
| 括号 / 分割类 | LC22, 131 | 树深受约束 | 条件复杂 |
| N皇后 / 数独 | LC51, 37 | 多维剪枝 | 高级约束 |

---

## 三、N 皇后问题

### 思路
- 每行放一个皇后。
- 列、两条对角线禁止重复。

### 实现
```java
class Solution {
    List<List<String>> res = new ArrayList<>();
    public List<List<String>> solveNQueens(int n) {
        char[][] board = new char[n][n];
        for (char[] row : board) Arrays.fill(row, '.');
        boolean[] col = new boolean[n];
        boolean[] diag1 = new boolean[2 * n];
        boolean[] diag2 = new boolean[2 * n];
        backtrack(board, 0, n, col, diag1, diag2);
        return res;
    }

    void backtrack(char[][] board, int row, int n,
                   boolean[] col, boolean[] diag1, boolean[] diag2) {
        if (row == n) {
            List<String> cur = new ArrayList<>();
            for (char[] r : board) cur.add(new String(r));
            res.add(cur);
            return;
        }
        for (int c = 0; c < n; c++) {
            int d1 = row + c;
            int d2 = row - c + n;
            if (col[c] || diag1[d1] || diag2[d2]) continue;

            board[row][c] = 'Q';
            col[c] = diag1[d1] = diag2[d2] = true;

            backtrack(board, row + 1, n, col, diag1, diag2);

            board[row][c] = '.';
            col[c] = diag1[d1] = diag2[d2] = false;
        }
    }
}
```

**复杂度**
- 时间：O(N!)  
- 空间：O(N²)

---

## 四、数独求解

题意

给定一个 9×9 的部分填充数独，填充空格（'.'）使其满足：

每行、每列、每个 3×3 小方块数字 1~9 不重复。

### 思路解析

每个空格是一个“待决策点”。

依次尝试数字 1~9，检测合法性。

若某步无解，则回溯到上一步重新尝试。

### 关键数据结构

rows[9][10], cols[9][10], boxes[9][10]

分别记录数字是否在对应行、列、小方块出现过。

位置到 box 的映射：
int box = (row / 3) * 3 + col / 3;

### 实现
```java
class Solution {
    boolean[][] rows = new boolean[9][10];
    boolean[][] cols = new boolean[9][10];
    boolean[][] boxes = new boolean[9][10];
    char[][] board;

    public void solveSudoku(char[][] board) {
        this.board = board;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    int num = board[i][j] - '0';
                    rows[i][num] = cols[j][num] = boxes[(i / 3) * 3 + j / 3][num] = true;
                }
            }
        }
        backtrack(0, 0);
    }

    boolean backtrack(int r, int c) {
        if (r == 9) return true;
        if (c == 9) return backtrack(r + 1, 0);
        if (board[r][c] != '.') return backtrack(r, c + 1);

        int box = (r / 3) * 3 + c / 3;
        for (int num = 1; num <= 9; num++) {
            if (rows[r][num] || cols[c][num] || boxes[box][num]) continue;
            board[r][c] = (char)(num + '0');
            rows[r][num] = cols[c][num] = boxes[box][num] = true;
            if (backtrack(r, c + 1)) return true;
            board[r][c] = '.';
            rows[r][num] = cols[c][num] = boxes[box][num] = false;
        }
        return false;
    }
}
```

---

## 五、剪枝分类与技巧
剪枝（Pruning）是回溯法的核心优化手段。
目标：提前终止无效或不可能产生解的搜索分支，减少状态树节点数量，从指数级下降到可运行范围。

| 类型 | 检测条件 | 示例 |
|------|-----------|------|
| 可行性剪枝 | 当前非法 | N皇后冲突 |
| 约束剪枝 | 超出目标 | 组合求和 sum>target |
| 重复剪枝 | 重复状态 | 排列去重 |
| 启发式剪枝 | 候选数最少优先 | 数独 |
| 上下界剪枝 | bound >= best | 最优化搜索 |

### 通用框架
```java
void backtrack(State s) {
    if (违反约束(s)) return;
    if (满足结束条件(s)) {记录; return;}
    for (choice in choices) {
        if (无效(choice)) continue;
        apply(choice);
        backtrack(next(s, choice));
        undo(choice);
    }
}
```

四、剪枝的通用策略

1. 排序提前

对输入排序，使剪枝条件单调（如递增数组 + sum > target 可直接 break）。

减少无效递归。

2. 提前判断合法性

每层选前先判断是否冲突。

减少进入深层再失败的浪费。

3. 使用布尔表或位掩码

常用于行列标记、状态缓存（如数独/N皇后）。

剪枝判断 O(1)。

4. 状态缓存 (Memoization)

对重复状态直接返回缓存结果。

典型于带 bitmask 的搜索类题。
---

## 六、状态缓存（Memoization）

### 定义
在搜索中缓存已访问状态结果，避免重复计算。  
动态规划可视为带缓存的搜索。
### 本质
动态规划（DP） = 带状态缓存的搜索。

回溯 + 缓存 = 记忆化搜索（Memoized DFS）。

### 常见应用

| 问题 | 状态 | 缓存键 | 示例 |
|------|------|--------|------|
| Can I Win | mask+sum | int mask | LC464 |
| Partition to K Subsets | mask+sum | int mask | LC698 |
| Word Break II | start index | int start | LC140 |
| 路径类 | 坐标+目标 | string key | LC1306 |

### 核心模板
```java
Map<Integer, Boolean> memo;

boolean dfs(int mask, int sum) {
    if (memo.containsKey(mask)) return memo.get(mask);
    ...
    memo.put(mask, result);
    return result;
}
```

---

## 七、代表题目摘要

### 464. Can I Win
- 状态：mask + sum
- 剪枝：若 sum+i≥target → 赢
- 缓存：mask
- 复杂度：O(2ⁿ × n)

### 698. Partition to K Equal Sum Subsets
- 状态：mask + curSum
- 剪枝：curSum > target
- 缓存：mask
- 复杂度：O(2ⁿ × n)

### 140. Word Break II
- 状态：字符串索引
- 剪枝：不在词典中
- 缓存：start
- 复杂度：O(n³)

---

## 八、三者关系总结

| 比较 | 回溯 | 回溯+缓存 | 动态规划 |
|------|------|-----------|-----------|
| 搜索方向 | 自顶向下 | 自顶向下 | 自底向上 |
| 重复状态 | 有 | 消除 | 消除 |
| 内存开销 | 小 | 中 | 大 |
| 应用场景 | 组合构造 | 子集划分/博弈 | 优化/路径最短 |

---

## 九、核心思维模型总结

1. 回溯：系统探索所有可能路径。  
2. 剪枝：提前排除不可能路径。  
3. 状态缓存：避免重复探索同一状态。  
4. 三者结合是高效搜索算法的关键。

---

**完**
