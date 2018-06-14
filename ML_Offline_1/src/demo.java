/**
 * Created by asd on 4/26/2018.
 */
public class demo {
    public static void main(String[] args) {
        int[] x = {1, 3, 4};
        int[] y = x;
        x[1] = 10000;

        for(int i: y){
            System.out.println(i);
        }
    }
}
