package topinterview;

public class IntegerReplacement {

    public static void main (String[] args){
        IntegerReplacement app = new IntegerReplacement();
        int res = app.integerReplacement(2147483647);
        System.out.println(res);
    }
    public int integerReplacement(int n) {
        int count = 0;
        double tem = n;
        while(tem != 1){
            System.out.println(tem);
            if(tem == 3){
                count += 2;
                break;
            }
            if( tem %2 == 0){
                tem = tem/2;
                count++;
            } else {
                if((tem+1)%4 == 0){
                    tem = tem+1;
                    count++;
                } else {
                    tem = tem -1;
                    count++;
                }
            }

        }
        return count;

    }
}
