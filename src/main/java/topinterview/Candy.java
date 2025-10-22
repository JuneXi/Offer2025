package topinterview;

public class Candy {
    public int candy(int[] ratings) {
        int[] candy = new int[ratings.length];

        for(int i = 0; i < ratings.length; i++){
            if(i == 0){
                int j = i + 1;
                while(j < ratings.length && ratings[j] > ratings[j + 1]){
                    i++;
                }
            }

        }

        return 0;
    }
}
