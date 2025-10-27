# 滑动窗口与同向双指针全解（中英对照）
**Sliding Window & Same-Direction Two Pointers — Full Guide**

---

## 一、核心概念｜Core Concepts

**同向双指针 (Two pointers in same direction)**  
- 两个指针从左向右移动，定义区间 `[l, r)`。  
- 用于动态维护一段满足条件的区间。

**滑动窗口 (Sliding window)**  
- 双指针的特例：窗口可以扩张或收缩。  
- 常用于求“最长 / 最短 / 计数 / 覆盖”类型问题。

---

## 二、适用场景｜When to Use
| 中文说明 | English Explanation |
|---|---|
| 数组或字符串线性遍历，不回退 | Linear scan over array/string |
| 目标与区间属性相关：和、积、计数、去重、覆盖 | Problem defined over a range: sum, product, count, distinct, coverage |
| 元素非负或能通过计数表维护约束 | Non-negative values or manageable via frequency table |

---

## 三、复杂度分析｜Complexity
| 项目 | 时间复杂度 | 空间复杂度 |
|---|---|---|
| 指针各最多移动 n 次 | O(n) | O(Σ)（字符集或整数范围） |
| With two pointers | O(n) | O(alphabet or range) |

---

## 四、字符集选择｜Alphabet Choice

### 快速结论
- 纯英文数字与常见 ASCII 符号：`int[128]`
- 可能含扩展 8-bit 字符（如 `é ç` 等）：`int[256]`
- 含中文、表情或任意 Unicode：`HashMap<Character,Integer>` 或 `Map<Integer,Integer>`

### 使用场景
| 场景 | 推荐结构 | 说明 |
|---|---|---|
| 标准 ASCII（0–127） | `int[128]` | 英文字母、数字、常见符号 |
| 扩展 ASCII（0–255） | `int[256]` | 含欧洲语符、特殊符号 |
| 仅大写 A–Z | `int[26]` | 常数最小 |
| Unicode（中文、emoji） | `Map<Character,Integer>` | Java `char` 为 16-bit UTF-16 |
| 严格按码点统计 | `Map<Integer,Integer>` | 处理 surrogate pairs（emoji） |

### 复杂度
- 数组计数（128/256/26）：O(n) 时间，O(1) 空间，常数小。  
- Map 计数：O(n) 摊还，O(Σ) 空间，常数大。

---

## 五、模板与示例｜Templates & Examples

### 1️⃣ 固定长度窗口｜Fixed-size Window
```java
int maxSumFixed(int[] nums, int k) {
    int n = nums.length;
    int sum = 0;
    for (int i = 0; i < k; i++) sum += nums[i];
    int best = sum;
    for (int r = k; r < n; r++) {
        sum += nums[r] - nums[r - k];
        best = Math.max(best, sum);
    }
    return best;
}
```
**O(n)** time, **O(1)** space.

---

### 2️⃣ 至多 K 种不同字符｜At most K distinct
```java
int lengthOfLongestSubstringKDistinct(String s, int K) {
    int[] freq = new int[256];
    int kinds = 0, best = 0;
    for (int l = 0, r = 0; r < s.length(); r++) {
        if (freq[s.charAt(r)]++ == 0) kinds++;
        while (kinds > K) if (--freq[s.charAt(l++)] == 0) kinds--;
        best = Math.max(best, r - l + 1);
    }
    return best;
}
```
**O(n)** time, **O(1)** space.

---

### 3️⃣ 最小覆盖子串｜Minimum Window Substring (LC 76)
```java
String minWindow(String s, String t) {
    int[] need = new int[128];
    for (char c : t.toCharArray()) need[c]++;
    int needCount = t.length();
    int bestLen = Integer.MAX_VALUE, bestL = 0;
    for (int l = 0, r = 0; r < s.length(); r++) {
        if (need[s.charAt(r)]-- > 0) needCount--;
        while (needCount == 0) {
            if (r - l + 1 < bestLen) { bestLen = r - l + 1; bestL = l; }
            if (++need[s.charAt(l++)] > 0) needCount++;
        }
    }
    return bestLen == Integer.MAX_VALUE ? "" : s.substring(bestL, bestL + bestLen);
}
```
**O(n)** time, **O(1)** space.

---

### 4️⃣ 非负数组：子数组和 ≤ S｜Non-negative sum ≤ S
```java
long countSubarraysAtMostSum(int[] nums, int S) {
    long ans = 0;
    int sum = 0;
    for (int l = 0, r = 0; r < nums.length; r++) {
        sum += nums[r];
        while (sum > S) sum -= nums[l++];
        ans += r - l + 1;
    }
    return ans;
}
```
**O(n)** time, **O(1)** space.

---

### 5️⃣ 乘积 < k｜Product < k
```java
long numSubarrayProductLessThanK(int[] nums, int k) {
    if (k <= 1) return 0;
    long ans = 0, prod = 1;
    for (int l = 0, r = 0; r < nums.length; r++) {
        prod *= nums[r];
        while (prod >= k) prod /= nums[l++];
        ans += r - l + 1;
    }
    return ans;
}
```
**O(n)** time, **O(1)** space.

---

### 6️⃣ 恰好 K 个不同整数｜Exactly K distinct
```java
long atMostKDistinct(int[] a, int K) {
    int[] cnt = new int[200001];
    int kinds = 0;
    long ans = 0;
    for (int l = 0, r = 0; r < a.length; r++) {
        if (cnt[a[r]]++ == 0) kinds++;
        while (kinds > K) if (--cnt[a[l++]] == 0) kinds--;
        ans += r - l + 1;
    }
    return ans;
}

long subarraysWithKDistinct(int[] a, int K) {
    return atMostKDistinct(a, K) - atMostKDistinct(a, K - 1);
}
```
**O(n)** time, **O(K)** space.

---

### 7️⃣ 无重复子串｜Longest Substring Without Repeating
```java
int lengthOfLongestSubstring(String s) {
    int[] last = new int[256];
    Arrays.fill(last, -1);
    int best = 0, l = 0;
    for (int r = 0; r < s.length(); r++) {
        int c = s.charAt(r);
        l = Math.max(l, last[c] + 1);
        best = Math.max(best, r - l + 1);
        last[c] = r;
    }
    return best;
}
```
**O(n)** time, **O(1)** space.

---

### 8️⃣ 原地去重｜In-place Dedup (sorted)
```java
int removeDuplicates(int[] nums) {
    int w = 0;
    for (int r = 0; r < nums.length; r++) {
        if (r == 0 || nums[r] != nums[r - 1]) nums[w++] = nums[r];
    }
    return w;
}
```
**O(n)** time, **O(1)** space.

---

### 9️⃣ 最长连续 1（允许翻转 K 次 0）｜Max Consecutive Ones III
```java
int longestOnes(int[] A, int K) {
    int zero = 0, best = 0;
    for (int l = 0, r = 0; r < A.length; r++) {
        if (A[r] == 0) zero++;
        while (zero > K) if (A[l++] == 0) zero--;
        best = Math.max(best, r - l + 1);
    }
    return best;
}
```
**O(n)** time, **O(1)** space.

---

## 六、常见陷阱｜Common Pitfalls
| 问题 | 原因 | 解决方案 |
|---|---|---|
| 元素含负数时“和 ≤ S”失效 | 窗口不可单调 | 用前缀和 + 有序表或哈希 |
| 计数恢复错误 | 收缩时未正确回退 | 收缩时更新计数并检测 0↔1 转换 |
| 整型溢出 | 乘积或和过大 | 使用 long |
| 字符范围未知 | 数组越界或浪费 | 改用 Map |

---

## 七、题型速查｜Quick Reference
| 题型 | 模板 | 核心逻辑 |
|---|---|---|
| 最长/最多 | atMost 模板 | 扩右缩左保持合法 |
| 恰好 K | 差分 atMost | atMost(K)−atMost(K−1) |
| 最小覆盖 | minWindow | 先扩再收 |
| 非负和/积 | sum≤S 或 product<k | 单调窗口 |
| 原地压缩 | 读写指针 | r 扫，w 写 |
| 固定窗口 | 固定长度 | 加入减去 |

---

## 八、推荐练习｜Recommended LeetCode Set
| LC | English | 中文 |
|---|---|---|
| 3 | Longest Substring Without Repeating Characters | 无重复字符的最长子串 |
| 76 | Minimum Window Substring | 最小覆盖子串 |
| 209 | Minimum Size Subarray Sum | 长度最小的子数组 |
| 340 / 159 | Longest Substring with At Most K Distinct Characters | 至多 K 种字符的最长子串 |
| 424 | Longest Repeating Character Replacement | 替换后的最长重复子串 |
| 713 | Subarray Product Less Than K | 乘积小于 K 的子数组 |
| 904 | Fruit Into Baskets | 水果成篮 |
| 1004 | Max Consecutive Ones III | 最长连续 1 |
| 992 | Subarrays with K Different Integers | 恰好 K 个不同整数的子数组 |
