# 哈希表总结 Hash Table Overview

## 一、核心概念 Core Concepts

* **哈希函数 (Hash Function)**: `index = hash(key) % capacity` 需要均匀分布和低计算成本。
* **冲突 (Collision)**: 两个 key 映射到同一位置，常见解决方法：

    * 拉链法 (Chaining)
    * 开放定址法 (Open Addressing)
* **负载因子 (Load Factor)**: `size / buckets` 超过阈值触发扩容和 rehash。

## 二、复杂度 Complexity

| 操作 | 平均   | 最坏   |
| -- | ---- | ---- |
| 插入 | O(1) | O(n) |
| 查找 | O(1) | O(n) |
| 删除 | O(1) | O(n) |

## 三、语言实现 Key Implementations

* **Java `HashMap`**: 拉链法 + 树化（JDK8），链长 > 8 转红黑树。

## 四、常见应用 Common Applications

* 计数、去重、符合条件过滤
* 双端匹配 (Two Sum)
* 前缀和计数表

---

# 560. Subarray Sum Equals K

## 思路 (Prefix Sum + Hash Counting)

前缀和 `pre[j] = nums[0..j]`。想找和为 k 的区间，等价于找 i、j 使 `pre[j] - pre[i-1] = k`。
每一步累加前缀和，用 `Map<sum, freq>` 记录已出现的前缀和次数，将 `freq[sum - k]` 加入答案。初始化 `freq[0]=1`表示空前缀。

* **Time**: O(n)
* **Space**: O(n)

## Java

```java
import java.util.*;

public class Solution {
    // Count subarrays whose sum equals k
    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        freq.put(0, 1);                  // empty prefix sum occurs once
        int sum = 0, ans = 0;
        for (int x : nums) {
            sum += x;                    // current prefix sum
            ans += freq.getOrDefault(sum - k, 0); // number of valid starts
            freq.put(sum, freq.getOrDefault(sum, 0) + 1); // record prefix
        }
        return ans;
    }
}
```

> 若数组和可能溢出 int，改为 long。

## 易错点 Pitfalls

* 忘记初始化 `freq[0]=1`
* 含负数时不能用滑窗
* 注意前缀和溢出

## 相关变体 Variants

* 最长/最短和为 k 的子数组：记录首次出现位置
* 区间和在 [L,R]：有序表或树状数组
* 二维版：按行压缩，套用本题模板

---

**总结 Summary**

| 特性   | 描述          |
| ---- | ----------- |
| 数据结构 | 数组 + 哈希函数   |
| 优点   | 查找/插入/删除高效  |
| 缺点   | 不支持排序，占用空间大 |
| 常见应用 | 映射、计数、去重、缓存 |
