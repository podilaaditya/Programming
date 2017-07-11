import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int s = in.nextInt(); //7 , 
        int t = in.nextInt(); // 11
        int a = in.nextInt(); // 5
        int b = in.nextInt(); // 15
        int m = in.nextInt(); // -2 2 1
        int n = in.nextInt(); // 5 -6
        int apple_result =0 , orange_result=0;
        int[] apple = new int[m];
        for(int apple_i=0; apple_i < m; apple_i++){

            apple[apple_i] = in.nextInt();
            //System.out.println("apple[apple_i] ="+apple[apple_i]);
        }
        int[] orange = new int[n];
        for(int orange_i=0; orange_i < n; orange_i++){
            orange[orange_i] = in.nextInt();
            //System.out.println("orange[orange_i] ="+orange[orange_i]);
        }
        
        for(int apple_cnt = 0; apple_cnt < m;apple_cnt++ ) {
            int apple_pos = a+(apple[apple_cnt]); //(7 + (-2))
            //System.out.println("apple pos ="+apple_pos+ " s ="+s + " t="+t);
            if(apple_pos >= s && apple_pos<= t){
                 
                apple_result++;
            }
        }
        for(int orange_cnt = 0; orange_cnt < n;orange_cnt++ ) {
             int orange_pos = b+(orange[orange_cnt]);
             //System.out.println("orange pos ="+orange_pos+ " s ="+s + " t="+t);
             if(orange_pos >= s && orange_pos<= t){
                
                orange_result++;
            }
        }
        System.out.println(""+apple_result);
        System.out.println(""+orange_result);        
    }
}