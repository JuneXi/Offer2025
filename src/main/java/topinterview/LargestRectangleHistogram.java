package topinterview;

import java.util.Stack;

public class LargestRectangleHistogram {
    public int largestRectangleArea(int[] heights) {
        // we should only keep the increasing value
        // store the index of the value
        Stack<Integer> stack = new Stack();
        int res = 0;
        for (int i = 0; i < heights.length; i++) {
            while (!stack.isEmpty() && heights[i] <= heights[stack.peek()]) {
                stack.pop();
            }
            stack.push(i);
            for (int j = 0; j < stack.size(); j++) {
                int index = stack.get(j);
                int height = heights[index];
                if (j == 0) {
                    res = Math.max(res, height * (i + 1));
                } else {
                    res = Math.max(res, height * (i - stack.get(j - 1)));
                }
            }
        }
        return res;

    }
}
