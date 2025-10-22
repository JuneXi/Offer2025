package topinterview;

public class SumOfSquareNumbers {
    public boolean judgeSquareSum(int c) {
        int n = (int)Math.ceil(Math.sqrt(c));
        int end = n/2;
        int start = 1;
        while(start < end){
            if(start * start + end * end == c){
                return true;
            }
            if(start* start + end *end > c) {
                end--;
            }else{
                start ++;
            }
        }

        return start * start + end * end == c;

    }
}
