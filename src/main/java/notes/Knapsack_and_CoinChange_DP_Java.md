# Knapsack Problems and Related Dynamic Programming (Including Coin Change and Permutations)

> Language: Java  
> Description: A complete summary of 0-1 Knapsack, Unbounded Knapsack, Multiple Knapsack, Group Knapsack, and Coin Change problems.  
> Ready to be saved as `.md`.

---

## Table of Contents
1. 0-1 Knapsack  
2. Unbounded Knapsack  
3. Multiple Knapsack  
4. Group Knapsack  
5. Coin Change  
6. Permutations vs Combinations  
7. Iteration Order Summary  
8. Initialization and Complexity

---

## 1. 0-1 Knapsack

**Definition**: Each item can be chosen at most once.  
**DP Meaning**: `dp[i][j]` = max value using first `i` items within capacity `j`.

### 2D DP
```java
for (int i = 1; i <= n; i++) {
    for (int j = 0; j <= W; j++) {
        dp[i][j] = dp[i-1][j];
        if (j >= w[i]) dp[i][j] = Math.max(dp[i][j], dp[i-1][j-w[i]] + v[i]);
    }
}
```

### 1D DP (reverse order)
```java
for (int i = 1; i <= n; i++)
    for (int j = W; j >= w[i]; j--) // reverse
        dp[j] = Math.max(dp[j], dp[j-w[i]] + v[i]);
```

---

## 2. Unbounded Knapsack

**Definition**: Each item can be used unlimited times.  
**Relation**: The Coin Change problem is a special case.

### 2D DP
```java
for (int i = 1; i <= n; i++) {
    for (int j = 0; j <= W; j++) {
        dp[i][j] = dp[i-1][j];
        if (j >= w[i]) dp[i][j] = Math.max(dp[i][j], dp[i][j-w[i]] + v[i]);
    }
}
```

### 1D DP (forward order)
```java
for (int i = 1; i <= n; i++)
    for (int j = w[i]; j <= W; j++) // forward
        dp[j] = Math.max(dp[j], dp[j-w[i]] + v[i]);
```

---

## 3. Multiple Knapsack

**Definition**: Each item `i` can be chosen up to `c[i]` times.  
**Optimization**: Use binary decomposition to convert to several 0-1 items.

### After binary splitting (reverse order)
```java
for (Item it : itemsAfterSplit)
    for (int j = W; j >= it.w; j--)
        dp[j] = Math.max(dp[j], dp[j-it.w] + it.v);
```

---

## 4. Group Knapsack

**Definition**: Items are divided into groups. At most one item can be chosen from each group.

### 1D Implementation (group outer, reverse order)
```java
for (List<Item> group : groups) {
    for (int j = W; j >= 0; j--) {
        long best = dp[j];
        for (Item it : group)
            if (j >= it.w) best = Math.max(best, dp[j-it.w] + it.v);
        dp[j] = best;
    }
}
```

---

## 5. Coin Change Problem

> Belongs to **Unbounded Knapsack**.  
> Two major forms:  
> - **Counting DP** (number of ways)  
> - **Optimization DP** (minimum number of coins)

### (1) Counting Type — Number of Combinations
**Goal**: Find how many ways to make up the amount.  
**Transition**: `dp[j] += dp[j - coin]`  
**Order**: Outer loop = coins, inner loop = amount (**forward**).

```java
int[] coins = {1,2,5};
int amount = 11;
long[] dp = new long[amount+1];
dp[0] = 1;
for (int coin : coins)
    for (int j = coin; j <= amount; j++)
        dp[j] += dp[j - coin];
System.out.println(dp[amount]);
```

### (2) Optimization Type — Minimum Number of Coins
**Goal**: Minimum coins needed to reach amount.  
**Transition**: `dp[j] = min(dp[j], dp[j - coin] + 1)`  
**Order**: Outer loop = coins, inner loop = amount (**forward**).

```java
int[] coins = {1,2,5};
int amount = 11;
int INF = 1_000_000_000;
int[] dp = new int[amount+1];
Arrays.fill(dp, INF);
dp[0] = 0;
for (int coin : coins)
    for (int j = coin; j <= amount; j++)
        dp[j] = Math.min(dp[j], dp[j - coin] + 1);
System.out.println(dp[amount] >= INF ? -1 : dp[amount]);
```

---

## 6. Permutations vs Combinations

| Item | Combination | Permutation |
|------|--------------|--------------|
| Order matters? | No `{1,2}` = `{2,1}` | Yes `{1,2}` ≠ `{2,1}` |
| Outer loop | Coins | Amount |
| Common usage | Coin Change II | Coin Change Permutations |
| Formula | `dp[j] += dp[j - coin]` | `dp[j] += dp[j - coin]` (different order) |

Permutation example:
```java
int[] coins = {1,2,5};
int amount = 5;
long[] dp = new long[amount+1];
dp[0] = 1;
for (int j = 1; j <= amount; j++) // outer: amount
    for (int coin : coins)
        if (j >= coin) dp[j] += dp[j - coin];
System.out.println(dp[amount]);
```

---

## 7. Iteration Order Summary

| Type | j Loop Direction | Note |
|------|-------------------|------|
| 0-1 Knapsack | Reverse | Avoid reuse |
| Unbounded Knapsack | Forward | Allow reuse |
| Multiple Knapsack (binary split) | Reverse | Same as 0-1 |
| Group Knapsack | Reverse | One per group |
| Coin Change (combination) | Forward | Coin outer |
| Coin Change (min coins) | Forward | Coin outer |
| Coin Change (permutation) | Forward | Amount outer |

---

## 8. Initialization and Complexity

| Type | Initialization | Time | Space |
|------|----------------|------|--------|
| Max value | `dp[0]=0`, others 0 | O(nW) | O(W) |
| Counting | `dp[0]=1` | O(nW) | O(W) |
| Min value | `dp[0]=0`, others INF | O(nW) | O(W) |

---

✅ **Summary**:  
> The Coin Change problem is essentially an **Unbounded Knapsack** with two DP goals:  
> - **Counting DP**: number of ways to reach target.  
> - **Optimization DP**: minimum number of coins required.
