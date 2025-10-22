package topinterview;

public class RomanToNumber {
    public int romanToInt(String s) {
        int result = 0;
        for(int i = 0; i < s.length();){
            if(s.indexOf(i) == 'I'){
                if(i + 1 < s.length() - 1){
                    if(s.indexOf(i + 1) == 'V'){
                        result = result + 4;
                        i = i +2;
                    }else if(s.indexOf(i + 1) == 'X'){
                        result = result + 9;
                        i = i +2;
                    }else{
                        result +=1;
                        i ++;
                    }
                }else{
                    result += 1;
                }
            }
        }
        return 0;

    }
}
