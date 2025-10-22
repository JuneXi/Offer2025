package topinterview;

import java.util.Stack;

public class BasicCalculator {
    public static void main(String[] args){
        BasicCalculator app = new BasicCalculator();
        String s = "";
        app.calculate(s);
    }
    public int calculate(String s) {
        Stack<Integer> stack = new Stack<>();
        Stack<Integer> sign = new Stack<>();
        if (s.charAt(0) == '-') {
            stack.push(0);
        }
        sign.push(1);
        char ss = '+';
        int num = 0;


        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
                continue;
            }
            if(c == '(') {
                if(i > 0 && s.charAt(i - 1) == '-'){
                    sign.push(-sign.peek());
                } else {
                    sign.push(sign.peek());
                }
                continue;
            }
            if(c == ')'){
                stack.push(null);
            }

            else if (ss == '+') {
                stack.push(sign.peek() * num);
                num = 0;
            } else if(c =='-'){
                stack.push(ss*sign.peek() * num);
               // ss = -ss;
            } else if(c == '('){

            } else if(c == ')'){
                sign.pop();
            }
        }
        int res = 0;
        while (!stack.isEmpty()){
            res += stack.pop();
        }
        return res;
    }

}
