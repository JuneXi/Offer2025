package topinterview;

import java.util.Stack;

public class BasicCalculatorII {
    /*
    public int calculate(String s) {
        //String[] a = s.split("[+\\-*]");
        Stack<Integer> stack = new Stack<>();
        String pre = a[0];
        stack.push(Integer.valueOf(pre));
        for (int i = 1; i < a.length; i++) {
            if (Integer.parseInt(a[i] )) {
                if (pre.equals("+")) {
                    stack.push(Integer.valueOf(a[i]));
                    continue;
                }
                if (pre.equals("-")) {
                    stack.push(0 - Integer.valueOf(a[i]));
                    continue;
                }
                if (pre.equals("*")) {
                    stack.push(stack.pop() * Integer.valueOf(a[i]));
                    continue;
                }
                stack.push(stack.pop() / Integer.valueOf(a[i]));
            } else {
                pre = a[i];
            }
        }
        int res = 0;
        while (!stack.isEmpty()) {
            res += stack.pop();
        }
        return res;

    }
    */
}
