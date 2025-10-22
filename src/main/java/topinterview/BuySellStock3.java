package topinterview;

public class BuySellStock3 {
    public static void main(String[] args) {
        BuySellStock3 app = new BuySellStock3();
        int[] test = {6,1,3,2,4,7};
        app.maxProfit(test);
    }
    public int maxProfit(int[] prices) {
        //let assume we divide prices into 2 parts, we have to do on transaction in first then 2nd transaction on last.
        // or no transaction => compare with 0
        //then profit[i] = profit[0 -i]max + profit [i - l] max
        //then profile[i + 1] = profit[0 - i+1]max = profit[i+1 =>l]max
        // profit[0 - i+1] = [profit0 -i]max, profit[0-i]

        int l = prices.length;
        // from 0 - i index, the max, including i
        int[] front = new int[prices.length];
        // from i - l, the max, including i
        int[] end = new int[prices.length];

        buildFront(front, prices);

        buildEnd(end, prices);

        int res = Math.max(front[prices.length - 1], end[0]);
        for(int i = 0 ; i <prices.length - 1;i++){
            res = Math.max(front[i] + end[i + 1], res);
        }
        return res;
    }

    private void buildFront(int[] front, int[] prices){
        // build front
        front[0] = 0;
        int max = prices[0];
        int min = prices[0];
        for(int i = 1; i < prices.length; i++) {
            if(prices[i] >= max){
                max = prices[i];
                front[i] = Math.max(max - min, front[i - 1]);
            }else if(prices[i] >= min){
                front[i] = Math.max(front[i - 1], prices[i] - min);
            } else if(prices[i] < min){
                min = prices[i];
                front[i] = front[i - 1];

            }else{
            front[i] = front[i - 1];}

        }
    }

    private void buildEnd(int[] end, int[] prices){
        int l = prices.length;
        int min = prices[l - 1];
        int max = prices[l - 1];
        end[l - 1] = 0;
        for(int i = l -2; i>=0;i--){
            if(prices[i] <= min) {
                min = prices[i];
                end[i] = Math.max(max - min, end[i + 1]);
            } else if(prices[i]<= max){
                end[i] = Math.max(max - prices[i], end[i+1]);
            }
            else if(prices[i] > max) {
                max = prices[i];
                end[i] = end[i + 1];
            } else{
            end[i] = end[i+1];}
        }
    }
}
