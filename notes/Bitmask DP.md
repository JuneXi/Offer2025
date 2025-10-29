# LeetCode 698: Partition to K Equal Sum Subsets — 双语版 (Bilingual Version)

## 一、题目简介 | Problem Description
给定一个整数数组 `nums` 和整数 `k`，判断是否可以将 `nums` 分成 `k` 个非空子集，使每个子集的和相等。  
Given an integer array `nums` and an integer `k`, determine if you can partition `nums` into `k` non-empty subsets such that each subset has an equal sum.

---

## 二、核心思想 | Core Idea
每个数字只能使用一次，总和必须能被 `k` 整除。目标和为 `target = sum(nums)/k`。  
Each number can be used only once, and the total sum must be divisible by `k`. The goal is `target = sum(nums)/k`.

---

## 三、解法一：回溯 + 剪枝（装桶法） | Solution 1: Backtracking with Pruning (Bucket Filling)

### 思路 | Idea
使用 `k` 个桶表示 `k` 个子集，从大到小将数字放入桶中。当某个桶超过目标和时立即回溯。  
Use `k` buckets to represent `k` subsets. Place numbers from large to small. Backtrack immediately when a bucket exceeds the target sum.

### 剪枝策略 | Pruning Rules
1. 降序排序以提前剪枝。  
   Sort in descending order to prune earlier.  
2. 当当前桶和超过目标时跳过。  
   Skip when the bucket sum exceeds target.  
3. 空桶失败后不再尝试下一个空桶。  
   Stop trying new empty buckets once one fails.  
4. 同层等价桶去重。  
   Avoid duplicate states caused by equivalent buckets in the same layer.

### Java 代码 | Java Code
```java
class Solution {
    public boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = 0;
        for (int x : nums) sum += x;
        if (sum % k != 0) return false;
        int target = sum / k;

        Arrays.sort(nums);
        for (int i = 0, j = nums.length - 1; i < j; i++, j--) {
            int t = nums[i]; nums[i] = nums[j]; nums[j] = t; // descending
        }
        if (nums[0] > target) return false;

        int[] buckets = new int[k];
        return dfs(nums, 0, buckets, target);
    }

    private boolean dfs(int[] nums, int idx, int[] buckets, int target) {
        if (idx == nums.length) return true;
        int val = nums[idx];
        HashSet<Integer> tried = new HashSet<>();
        for (int b = 0; b < buckets.length; b++) {
            if (tried.contains(buckets[b])) continue;
            if (buckets[b] + val > target) continue;

            tried.add(buckets[b]);
            buckets[b] += val;
            if (dfs(nums, idx + 1, buckets, target)) return true;
            buckets[b] -= val;

            if (buckets[b] == 0) break;
        }
        return false;
    }
}
```

### 复杂度 | Complexity
时间复杂度：剪枝后可通过 `n ≤ 16` 的测试。  
Time complexity: Passes within limits after pruning (`n ≤ 16`).  

空间复杂度：`O(k)`。  
Space complexity: `O(k)`.

---

## 四、解法二：位压 DP（Mask + 记忆化） | Solution 2: Bitmask DP with Memoization

### 思路 | Idea
用整数 `mask` 表示哪些元素被使用。当前桶和用 `cur` 表示。当 `cur == target` 时，开启下一个桶。  
Use an integer `mask` to represent which elements are used. The current bucket sum is `cur`. When `cur == target`, move to the next bucket.

### 核心位操作 | Key Bit Operations
| 操作 | 代码 | 说明 |
|------|------|------|
| 取第 i 位 | `int bit = 1 << i;` | Bit for `nums[i]` |
| 判断是否选中 | `(mask & bit) != 0` | Check if chosen |
| 选中元素 | `mask | bit` | Mark as used |

1. mask |= (1 << i); // 设置第 i 位为 1：
2. mask &= ~(1 << i);   // 取消第i位 → 设置为0
3. boolean on = ((mask >> i) & 1) == 1; // 判断是否为1



### Java 代码 | Java Code
```java
class Solution {
    int[] nums;
    int n, target;
    Boolean[] memo;

    public boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = 0; for (int x : nums) sum += x;
        if (sum % k != 0) return false;
        target = sum / k;

        Arrays.sort(nums);
        for (int i = 0, j = nums.length - 1; i < j; i++, j--) {
            int t = nums[i]; nums[i] = nums[j]; nums[j] = t;
        }
        if (nums[0] > target) return false;

        this.nums = nums;
        this.n = nums.length;
        memo = new Boolean[1 << n];
        return dfs(0, 0, k);
    }

    boolean dfs(int mask, int cur, int kLeft) {
        if (kLeft == 1) return true;
        if (cur == target) return dfs(mask, 0, kLeft - 1);
        if (memo[mask] != null) return memo[mask];

        int prev = -1;
        for (int i = 0; i < n; i++) {
            if (((mask >> i) & 1) == 1) continue;
            int v = nums[i];
            if (v == prev) continue;
            if (cur + v > target) continue;
            if (dfs(mask | (1 << i), cur + v, kLeft)) return memo[mask] = true;
            prev = v;
            if (cur == 0) break;
        }
        return memo[mask] = false;
    }
}
```

### 复杂度 | Complexity
时间复杂度：`O(n * 2^n)`  
Time complexity: `O(n * 2^n)`  
空间复杂度：`O(2^n)`  
Space complexity: `O(2^n)`

---

## 五、Mask（位掩码）详解 | Understanding Bitmask

Mask（位掩码） 就是一种用整数的二进制位来表示一组状态的方法。

在「子集、选择、使用情况」类问题里，它能把 一串布尔数组 boolean[] used 压缩成 一个整数 mask。每一位代表一个元素是否被选中。

| 状态 | 示例 (n=5) | 二进制 | 十进制 |
|------|-------------|--------|--------|
| 无选中 | [] | 00000 | 0 |
| 选1 | [1] | 00001 | 1 |
| 选1,3 | [1,3] | 00101 | 5 |
| 全选 | [1,2,3,4,5] | 11111 | 31 |

### 操作示例 | Operations
```java
int bit = 1 << (i - 1);
if ((mask & bit) != 0) continue; // already used
int next = mask | bit; // mark as used


        for (int i = 0; i < n; i++) {
        if ((mask >> i & 1) == 1) continue; // 已用
        int next = mask | (1 << i);         // 选 i
        if (dfs(next, ...)) return true;
        }
```

### 优点 | Advantages
- 状态唯一，可记忆化。  
  Unique and memoizable state.  
- 快速判断元素是否已使用。  
  Quick check for used elements.  
- 占用空间少，易实现。  
  Compact and efficient.

---

## 六、为什么要降序排序 | Why Sort in Descending Order
降序可以大幅提高剪枝效率：大数先放，若放不下立即剪枝。  
Descending order improves pruning: large numbers fill quickly, prune earlier.

**示例 | Example**  
```
target = 10
升序: [1,2,3,9] → many recursive calls
降序: [9,3,2,1] → prune early
```

---

## 七、相似题目 | Related Problems

| 题号 | 名称 | 类似点 |
|------|------|--------|
| 416 | Partition Equal Subset Sum | Subset-sum DP |
| 473 | Matchsticks to Square | k=4 special case |
| 464 | Can I Win | Same bitmask logic |
| 1723 | Find Minimum Time to Finish All Jobs | Multi-bucket allocation |
| 1986 | Minimum Number of Work Sessions | Same DP pattern |

---

## 八、总结 | Summary

| 方法 | 思路 | 优点 | 缺点 |
|------|------|------|------|
| 回溯 + 剪枝 | 尝试装桶 | Simple & intuitive | Needs manual pruning |
| 位压 DP | 记忆化搜索 | Reuses states efficiently | Requires bitmask understanding |

---

本题核心：合理剪枝 + 降序排序 + mask 状态记忆。  
Key takeaway: Prune effectively, sort descending, and memoize states.
