# 二分查找与 LIS 综合讲解（含 354 俄罗斯套娃信封问题）

## 一、二分查找（Binary Search）详解

### 1. 核心思想
二分查找用于 **有序数组**。每次取中点，将搜索区间缩半，快速定位目标。

**复杂度：**
- 时间：O(log n)
- 空间：O(1)

---

### 2. 基本模板（闭区间）
```java
int binarySearch(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] == target) return mid;
        else if (nums[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}
```

**说明：**
- 搜索区间为 `[left, right]`
- `while (left <= right)` 表示闭区间搜索
- 退出时 `left > right` 表示未找到

---

### 3. 左边界查找（lower bound）
找第一个 **≥ target** 的位置。

```java
int lowerBound(int[] nums, int target) {
    int left = 0, right = nums.length;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] < target) left = mid + 1;
        else right = mid;
    }
    return left;
}
```

---

### 4. 右边界查找（upper bound）
找第一个 **> target** 的位置。

```java
int upperBound(int[] nums, int target) {
    int left = 0, right = nums.length;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] <= target) left = mid + 1;
        else right = mid;
    }
    return left;
}
```

**区间 `[lowerBound, upperBound)` 即所有等于 target 的范围。**

---

### 5. 常见题型

| 类型 | 题目 | 思路 |
|------|------|------|
| 精确查找 | 704. Binary Search | 基础模板 |
| 左右边界 | 34. Find First and Last Position | 分别找 lowerBound 与 upperBound-1 |
| 插入位置 | 35. Search Insert Position | 返回 lowerBound |
| 旋转数组 | 33. Search in Rotated Array | 判断哪半区有序 |
| 平方根 | 69. Sqrt(x) | 找满足 mid² ≤ x 的最大 mid |
| 枚举答案 | 875. Koko Eating Bananas | 二分枚举速率验证可行性 |

---

## 二、354. Russian Doll Envelopes（俄罗斯套娃信封）

### 1. 思路
- 按宽升序、同宽按高降序排序；
- 对高度序列做 **严格递增子序列（LIS）**；
- LIS 长度即最大可嵌套数量。

**排序原因：**
同宽时高降序避免错误嵌套（防止宽相等但高递增被算进 LIS）。

---

### 2. 算法步骤
1. 排序 `(w ↑, h ↓)`
2. 提取高度数组 `hs`
3. 在 `hs` 上做严格递增 LIS（二分优化）

**复杂度**
- 时间：O(n log n)
- 空间：O(n)

---

### 3. Java 实现
```java
import java.util.*;

public class Solution {
    public int maxEnvelopes(int[][] envelopes) {
        if (envelopes == null || envelopes.length == 0) return 0;

        Arrays.sort(envelopes, (a, b) -> {
            if (a[0] != b[0]) return a[0] - b[0];
            return b[1] - a[1];
        });

        int[] tails = new int[envelopes.length];
        int size = 0;
        for (int[] e : envelopes) {
            int x = e[1];
            int i = Arrays.binarySearch(tails, 0, size, x);
            if (i < 0) i = -(i + 1);
            tails[i] = x;
            if (i == size) size++;
        }
        return size;
    }
}
```

---

### 4. 常见错误
- 未对同宽信封按高降序；
- 二分条件写成 “> x” 而不是 “≥ x”；
- 忘记更新 size。

---

## 三、LIS（Longest Increasing Subsequence）讲解

### 1. 定义
LIS = 最长严格递增子序列（元素不必连续）。

---

### 2. 解法一：DP O(n²)
```java
int lengthOfLIS(int[] nums) {
    int n = nums.length;
    int[] dp = new int[n];
    Arrays.fill(dp, 1);
    int res = 1;
    for (int i = 1; i < n; i++) {
        for (int j = 0; j < i; j++) {
            if (nums[j] < nums[i])
                dp[i] = Math.max(dp[i], dp[j] + 1);
        }
        res = Math.max(res, dp[i]);
    }
    return res;
}
```
**时间：** O(n²) **空间：** O(n)

---

### 3. 解法二：二分优化 O(n log n)
利用数组 `tails`：
> `tails[k]` 表示长度为 k+1 的递增子序列的最小结尾。

```java
int lengthOfLIS(int[] nums) {
    int[] tails = new int[nums.length];
    int size = 0;
    for (int x : nums) {
        int i = Arrays.binarySearch(tails, 0, size, x);
        if (i < 0) i = -(i + 1);
        tails[i] = x;
        if (i == size) size++;
    }
    return size;
}
```

---

### 4. 核心逻辑解析
```java
int i = Arrays.binarySearch(tails, 0, size, x);
if (i < 0) i = -(i + 1);
tails[i] = x;
if (i == size) size++;
```
解释：
1. `binarySearch` 找到插入点或相等下标；
2. 若没找到，返回负数 → 转换为插入点；
3. 替换或插入更新最优结尾；
4. 若插入在末尾则序列变长。

---

### 5. 示例
`nums = [10, 9, 2, 5, 3, 7]`

| 步骤 | x | tails | size | 说明 |
|------|---|--------|------|------|
| 1 | 10 | [10] | 1 | 初始化 |
| 2 | 9 | [9] | 1 | 更优结尾 |
| 3 | 2 | [2] | 1 | 更优结尾 |
| 4 | 5 | [2, 5] | 2 | 扩展长度 |
| 5 | 3 | [2, 3] | 2 | 更新结尾 |
| 6 | 7 | [2, 3, 7] | 3 | 新增长度 |

结果：LIS 长度 = **3**

---

### 6. 为什么用二分不用单调栈
| 项目 | LIS | 单调栈 |
|------|------|--------|
| 目标 | 任意子序列递增 | 连续区间单调 |
| 是否跳跃 | 可跳跃 | 不可跳跃 |
| 典型用途 | 300.LIS / 354.Russian Doll | 739.Daily Temperatures |
| 查找方式 | 二分查找全局最优位置 | 局部出栈入栈维护 |

> LIS 是全局最优问题，需要“插入点”搜索；  
> 单调栈是局部连续问题，只能处理相邻元素关系。

---

### 7. `binarySearch` 作用总结
在 LIS 中，`binarySearch` 用来找：
> **第一个 ≥ x 的位置**（lower_bound）

- 若存在：替换该位置 → 结尾更小；
- 若不存在：加到末尾 → LIS 长度 +1。

---

### 8. 示例说明
`tаils = [2, 5, 7]`，来 `x = 3`

| 步骤 | 操作 | 结果 |
|------|------|------|
| 找第一个 ≥ 3 的位置 | 位置 1（元素 5） | 插入点 = 1 |
| 替换 tails[1] = 3 | tails → [2, 3, 7] | 更优尾巴 |

---

### 9. 对比
| 概念 | 含义 | 应用 |
|------|------|------|
| 第一个 ≥ x | lower_bound | LIS（二分法） |
| 第一个 > x | upper_bound | 非严格递增版本 |
| 下一个更大元素 | 单调栈 | 连续区间问题 |

---

## 四、复杂度总结

| 算法 | 时间 | 空间 | 特点 |
|------|------|------|------|
| 二分查找 | O(log n) | O(1) | 查找位置高效 |
| LIS DP | O(n²) | O(n) | 简单直观 |
| LIS 二分 | O(n log n) | O(n) | 面试最常用 |
| 俄罗斯套娃信封 | O(n log n) | O(n) | 二维转一维LIS |

---

## 五、关键结论
1. 二分查找核心是定位插入点（≥ x）。  
2. LIS 二分优化用 lower_bound 替换策略。  
3. 单调栈仅适合连续区间问题。  
4. 354. Russian Doll = 排序 + LIS。  
5. `tails` 并非真实子序列，只记录最优“结尾最小值”状态。

---
