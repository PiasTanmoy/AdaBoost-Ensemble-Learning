import java.util.ArrayList;
import java.util.Random;

/**
 * Created by asd on 4/27/2018.
 */
public class RandomSampling {
    //double[] weightL;
    ArrayList<String[]> examplesL;

    public ArrayList<String[]> sampling(ArrayList<String[]> examples, double[] w) {
        /**
         * Define new weights and examples for this Learner
         */
        //weightL = new double[examples.size()];
        examplesL = new ArrayList<>(100);

        int N = examples.size();
        Random random;

        double sample;
        double sum = 0;
        int idx = 0;

        /**
         * Random weighted sampling
         */
        random = new Random(1305055);
        for(int n = 0; n<N; n++) {


            sample = random.nextDouble();

            sum = 0;
            idx = 0;

            for (int i = 0; i < w.length; i++) {
                if (sum > sample) {
                    break;
                }
                idx = i;
                sum += w[i];
            }
            examplesL.add(examples.get(idx));
        }

        /**
         * Now we got the example set for this L
         * exampleL
         */

        return examplesL;
    }
}
