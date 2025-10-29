# KMP算法详解（含图解与代码）

## 一、算法目标
在主串 s 中查找模式串 p，避免重复回溯。时间复杂度 O(n + m)。

---

## 二、核心思想
当 s[i] != p[j] 时，不回退 i，而根据 lps[j - 1] 调整 j。  
lps[j - 1] 表示：p[0…j-1] 这一段中最长的相等前后缀长度。

---

## 三、构造 LPS 数组
示例：p = "ababaca"

| 位置 j | 字符 | lps[j] | 解释 |
|--------|------|--------|------|
| 0 | a | 0 | 无前后缀 |
| 1 | b | 0 | 不同 |
| 2 | a | 1 | 前后缀 "a" |
| 3 | b | 2 | 前后缀 "ab" |
| 4 | a | 3 | 前后缀 "aba" |
| 5 | c | 0 | 无匹配 |
| 6 | a | 1 | 前后缀 "a" |

---

## 四、Java代码（含注释）
```java
class Solution {
    public int strStr(String s, String p) {
        if (p.isEmpty()) return 0; // 空模式直接返回0
        int n = s.length(), m = p.length();
        int[] lps = buildLPS(p); // 构造部分匹配表
        int i = 0, j = 0; // i遍历主串s, j遍历模式串p

        while (i < n) {
            if (s.charAt(i) == p.charAt(j)) { 
                // 当前字符匹配
                i++; 
                j++;
                if (j == m) return i - j; // 模式串全部匹配完成
            } else if (j > 0) { 
                // 不匹配时，利用lps表跳转，而不是回退i
                j = lps[j - 1];
            } else {
                // j=0时无法跳转，只能前进i
                i++;
            }
        }
        return -1; // 未找到匹配
    }

    // 构建部分匹配表 lps[i]: p[0..i] 子串中最长的相等前后缀长度
    private int[] buildLPS(String p) {
        int m = p.length();
        int[] lps = new int[m];
        int len = 0; // 当前最长前后缀长度
        int i = 1;   // 从第二个字符开始

        while (i < m) {
            if (p.charAt(i) == p.charAt(len)) {
                // 当前字符与前缀匹配，长度加1
                len++;
                lps[i] = len;
                i++;
            } else if (len > 0) {
                // 若不匹配，回退到上一个可能的前后缀位置
                len = lps[len - 1];
            } else {
                // 无法回退，置0并继续
                lps[i] = 0;
                i++;
            }
        }
        return lps;
    }
}
```

---

## 五、复杂度分析
- 构建 LPS：O(m)  
- 匹配过程：O(n)  
**总复杂度：O(n + m)**  
**空间复杂度：O(m)**  

---

## 六、匹配可视化

### 示例1：完全匹配
主串 s = "aabaaac"  
模式串 p = "aab"

#### LPS
| j | p[j] | lps[j] |
|---|------|--------|
| 0 | a | 0 |
| 1 | a | 1 |
| 2 | b | 0 |

#### 过程
| 步骤 | i | j | s[i] | p[j] | 动作 |
|------|---|---|------|------|------|
| 1 | 0 | 0 | a | a | 匹配 |
| 2 | 1 | 1 | a | a | 匹配 |
| 3 | 2 | 2 | b | b | 匹配成功 → i-j=0 |

图示：
```
s: a a b a a a c
    ↑ ↑ ↑
p:  a a b
```

---

### 示例2：含回退跳转
s = "ababaca"  
p = "aba"  
lps = [0, 0, 1]

| 步骤 | i | j | s[i] | p[j] | 动作 |
|---|---:|---:|---|---|---|
| 1 | 0 | 0 | a | a | 匹配 → (1,1) |
| 2 | 1 | 1 | b | b | 匹配 → (2,2) |
| 3 | 2 | 2 | a | a | 匹配 → 命中0 |
| 4 | 3 | 1 | b | b | 匹配 → (4,2) |
| 5 | 4 | 2 | a | a | 匹配 → 命中2 |
| 6 | 5 | 1 | c | b | 不匹配 → j=lps[0]=0 |
| 7 | 6 | 0 | a | a | 匹配结束 |

图示：
```
s: a b a b a c a
p: a b a
^-----^ 命中 at 0
    ^-----^ 命中 at 2
```

---

## 七、总结
1. i 只前进，不回退。  
2. j 根据 lps 回退。  
3. 匹配完成时返回 i - j。  
4. O(n + m) 时间复杂度。
