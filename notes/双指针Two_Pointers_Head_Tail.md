# 🧩 头尾指针（Two Pointers, Opposite Direction）

头尾指针（Head-Tail pointers）方法在算法中是高效处理有序、单调或区间类问题的常用技巧。
Two pointers move from both ends of an array toward the center based on logical conditions to find optimal or valid results.

---

## 模板1：两数之和 / Template 1: Two Sum (Sorted Array)

### 题型 / Problem Type
查找排序数组中两个数，使其和等于目标值。
Find two numbers in a sorted array whose sum equals a target value.

### 思路 / Idea
- 数组已排序。  
- 若当前和 < 目标 → 左指针右移以增大和；  
- 若当前和 > 目标 → 右指针左移以减小和；  
- 相等则找到答案。  
Because the array is sorted, we can move pointers deterministically without missing any valid pair.

### 代码 / Code
```java
int[] twoSumSorted(int[] a, int target) {
    int l = 0, r = a.length - 1;
    while (l < r) {
        int sum = a[l] + a[r];
        if (sum == target) return new int[]{l, r}; // found
        if (sum < target) l++; // move left pointer to increase sum
        else r--; // move right pointer to decrease sum
    }
    return new int[]{-1, -1}; // not found
}
```

### 复杂度 / Complexity
时间 O(n)，空间 O(1)。  
Time O(n), Space O(1).

---

## 模板2：三数之和 / Template 2: Three Sum

### 题型 / Problem Type
找出所有三元组使得和为 0。  
Find all triplets whose sum is zero.

### 思路 / Idea
1. 先排序。  
2. 固定第一个数 nums[i]，然后在右侧区间 [i+1, n-1] 内用双指针寻找两数之和为 -nums[i]。  
3. 若和 < 0 → 左移；若和 > 0 → 右移；相等则记录结果并去重。  
Sort the array, fix one element, and use two pointers to find pairs whose sum equals the negative of the fixed number.

### 代码 / Code
```java
List<List<Integer>> threeSum(int[] nums) {
    Arrays.sort(nums);
    List<List<Integer>> res = new ArrayList<>();
    int n = nums.length;
    for (int i = 0; i < n; i++) {
        if (i > 0 && nums[i] == nums[i-1]) continue; // skip duplicates
        int l = i + 1, r = n - 1;
        while (l < r) {
            int s = nums[i] + nums[l] + nums[r];
            if (s == 0) {
                res.add(Arrays.asList(nums[i], nums[l], nums[r]));
                l++; r--;
                while (l < r && nums[l] == nums[l-1]) l++; // skip left duplicates
                while (l < r && nums[r] == nums[r+1]) r--; // skip right duplicates
            } else if (s < 0) l++; // increase sum
            else r--; // decrease sum
        }
    }
    return res;
}
```

### 复杂度 / Complexity
时间 O(n²)，空间 O(1)。  
Time O(n²), Space O(1).

---

## 模板3：回文串判定 / Template 3: Palindrome Check

### 题型 / Problem Type
判断字符串是否为回文，只考虑字母和数字。  
Determine whether a string is a palindrome, ignoring non-alphanumeric characters.

### 思路 / Idea
- 左右指针从两端向中间扫描。  
- 跳过非字母数字字符。  
- 比较忽略大小写的字符是否相同。  
Use two pointers to scan inward, skipping invalid characters and comparing lowercase versions.

### 代码 / Code
```java
boolean isPalindrome(String s) {
    int l = 0, r = s.length() - 1;
    while (l < r) {
        while (l < r && !Character.isLetterOrDigit(s.charAt(l))) l++; // skip left non-alphanumeric
        while (l < r && !Character.isLetterOrDigit(s.charAt(r))) r--; // skip right non-alphanumeric
        if (Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r))) return false;
        l++; r--;
    }
    return true;
}
```

### 复杂度 / Complexity
时间 O(n)，空间 O(1)。  
Time O(n), Space O(1).

---

## 模板4：盛最多水的容器 / Template 4: Container With Most Water

### 题型 / Problem Type
找出两条竖线与 x 轴形成的容器能盛最多的水。  
Find two lines forming a container that holds the most water.

### 思路 / Idea
- 面积 = min(h[l], h[r]) * (r - l)。  
- 面积受短板限制，移动短板一侧可能增大面积。  
Move the shorter line inward to possibly increase the area because it limits the height.

### 代码 / Code
```java
int maxArea(int[] h) {
    int l = 0, r = h.length - 1, ans = 0;
    while (l < r) {
        ans = Math.max(ans, Math.min(h[l], h[r]) * (r - l)); // compute area
        if (h[l] < h[r]) l++; // move shorter side
        else r--;
    }
    return ans;
}
```

### 复杂度 / Complexity
时间 O(n)，空间 O(1)。  
Time O(n), Space O(1).

---

## 模板5：接雨水 / Template 5: Trapping Rain Water

### 题型 / Problem Type
给定高度数组，计算能接的雨水总量。  
Given elevation heights, compute total trapped rainwater.

### 思路 / Idea
- 用两个指针和两个变量记录左右最高高度 leftMax、rightMax。  
- 较低侧决定当前积水量。  
- 若 h[l] < h[r] → 由左侧确定水量。  
Maintain leftMax and rightMax, the lower side determines trapped water.

### 代码 / Code
```java
int trap(int[] h) {
    int l = 0, r = h.length - 1;
    int lm = 0, rm = 0, water = 0;
    while (l < r) {
        if (h[l] < h[r]) {
            lm = Math.max(lm, h[l]); // update left max
            water += lm - h[l]; // water on left side
            l++;
        } else {
            rm = Math.max(rm, h[r]); // update right max
            water += rm - h[r]; // water on right side
            r--;
        }
    }
    return water;
}
```

### 复杂度 / Complexity
时间 O(n)，空间 O(1)。  
Time O(n), Space O(1).

---

## 📊 类型总结 / Summary

| 类型 / Type | 代表题 / Example | 思维要点 / Key Idea |
|--------------|------------------|----------------------|
| 查找型 / Search | Two Sum, Three Sum | 有序数组中通过移动指针找和、差。 |
| 判定型 / Validation | Palindrome | 比较两端字符是否对称。 |
| 最优化 / Optimization | Container, Trap Water | 由两端状态决定全局最优。 |
| 计数型 / Counting | Pair counting problems | 收缩指针线性计数。 |

---

## 📘 复杂度汇总 / Complexity Summary

| 模板 / Template | 时间 / Time | 空间 / Space |
|-----------------|-------------|--------------|
| 两数之和 / Two Sum | O(n) | O(1) |
| 三数之和 / Three Sum | O(n²) | O(1) |
| 回文串 / Palindrome | O(n) | O(1) |
| 盛水容器 / Container | O(n) | O(1) |
| 接雨水 / Trapping Rain Water | O(n) | O(1) |
