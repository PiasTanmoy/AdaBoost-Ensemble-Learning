import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by asd on 4/26/2018.
 */
public class AdaBoost {


    public  ArrayList<Hypothesis> h;
    public  ArrayList<Double> z;

    public  int K = 10;


    public  String inputFile;
    public  int N = 0;
    public  double w[];
    public int nClass=0;
    int nAttribute;

    public  void mainAdaBoost(String inputFile, int K) throws IOException, InterruptedException {
        this.K = K;

        //DataSetManagement dataSetManagement = new DataSetManagement();
        //dataSetManagement.mainDataSetManagement();
        this.inputFile = inputFile;

        /**
         *  Insert the selected data in to array list
         */
        MakeExampleSet makeExampleSet = new MakeExampleSet();
        ArrayList<String[]> examples = makeExampleSet.mainMakeExampleSet(inputFile);

        if(examples.size() == 0) {
            System.out.println("no data");
            return;
        }

        int nSample=examples.size();
        N = nSample;
        nAttribute=examples.get(0).length;
        w = new double[nSample];
        //System.out.println(nSample);


        /**
         * Initialize weight vector
         */
        for(int i=0; i<nSample; i++) {
            w[i] = 1.0/nSample;
        }

        /**
         * individual list of each attribute
         */
        ArrayList[] attribute = new ArrayList[nAttribute];
        for(int i=0; i<nAttribute; i++) {
            attribute[i] = new ArrayList<String>();
        }
        /**
         * Extract data from data set and make them individual attribute list
         */
        String[] values;

        for(int i=0; i<nSample; i++) {
            values = examples.get(i);
            for(int j=0; j<nAttribute; j++) {
                attribute[j].add(values[j]);
            }
        }


        /**
         * AdaBoost L
         */
        //L learner = new L();
        //ArrayList<String[]> examplesL = learner.sampling(examples, w);


        /**
         *
         */
        h = new ArrayList<>(100);
        z = new ArrayList<>(100);

        /**
         * Get week learners or hypotheses
         */


        double error = 0;

        for(int k=0; k<K; k++) {

            /**
             *
             */
            RandomSampling randomSampling = new RandomSampling();
            ArrayList<String[]> examplesL = randomSampling.sampling(examples, w);

            /**
             *
             */
            L learner = new L();
            Hypothesis hypothesis = learner.selectAttribute(examplesL);
            nClass = learner.numberOfClass();
            //Hypothesis hypothesis = learner.selectAttribute(examples);

           // System.out.println(k + " ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");


           // h.add(k, hypothesis);

            error = 0;

//            for(int i=0; i<nSample; i++) {
//                System.out.println(w[i]);
//            }

            /**
             * Continuous
             */

            if( hypothesis.isContinuous() ) {

                /**
                 * Get the hypothesis information
                 */
                int attrIdx = hypothesis.getAttributeIndex();
                double splitPoint = hypothesis.getSplitPoint();
                ArrayList<String> decision = hypothesis.getAttributeDecision();
                String cls1 = decision.get(0);
                String cls2 = decision.get(1);

               // System.out.println(cls1 + " " + cls2 + " " + splitPoint);

                for(int j=0; j<N; j++) {
                    /**
                     * Get the information of the current data point
                     */
                    double val = Double.parseDouble(attribute[attrIdx].get(j).toString());
                    String cls = attribute[nAttribute-1].get(j).toString();

                    //System.out.println(val + " " + cls);

                    if(val < splitPoint) {
                        if(!cls.equals(cls1)) {
                            error += w[j];
                        }
                    }
                    else {
                        if(!cls.equals(cls2)) {
                            error += w[j];
                        }
                    }
                    //System.out.println(error);
                }

                if(error >= 0.5) {

                    //K++;
                    k--;

                    continue;
                }

                double sum =0;

                for(int j=0; j<N; j++) {
                    double val = Double.parseDouble(attribute[attrIdx].get(j).toString());
                    String cls = attribute[nAttribute-1].get(j).toString();

                    if(val < splitPoint) {
                        if(cls.equals(cls1)) {
                            w[j] = w[j] * (error / (1 - error));
                        }
                    }
                    else {
                        if(cls.equals(cls2)) {
                            w[j] = w[j] * (error / (1 - error));
                        }
                    }
                    sum+=w[j];
                }

               // System.out.println("---------------------"+sum);
                /**
                 * Normalize the weight vector
                 */

                normalize(w);

                /**
                 * Get the weight of the hypothesis
                 */
                z.add(k, log2((1-error)/error)) ;
                //System.out.println("error " +  error);
               // System.out.println("z : " + log2((1-error)/error));

            }
            else {
                /**
                 * Get the hypothesis information
                 */
                int attrIdx = hypothesis.getAttributeIndex();
                double splitPoint = hypothesis.getSplitPoint();
                ArrayList<String> decision = hypothesis.getAttributeDecision();
                ArrayList<String> attributeType = hypothesis.getAttributeTypeName();

                for(int j=0; j<N; j++) {
                    /**
                     * Get the information of the current data point
                     */
                    String val = attribute[attrIdx].get(j).toString();
                    String cls = attribute[nAttribute - 1].get(j).toString();

//                    System.out.println("attributeType.indexOf(val) "+attributeType.indexOf(val));
//                    System.out.println(val);

                    int idx;
                    String clsLearned;


                    if(attributeType.indexOf(val) != -1) {
                        idx = attributeType.indexOf(val);
                        clsLearned = decision.get(idx);
                    }
                    else {
                        clsLearned = "NOCLASS";
                    }



                    if(cls.equals(clsLearned)) {
                        //wait
                    }
                    else {
                        error += w[j];

                    }
                    //System.out.println(error);

                    if(error >= 0.5) {

                        //K++;
                        k--;

                       // System.out.println("------------Continue--------------- " + k);
                        continue;
                    }

                    if(cls.equals(clsLearned)) {
                        w[j] = w[j] * (error / (1 - error));
                    }
                    else {
                        //error += w[j];
                    }

                }

                /**
                 * Normalize the weight vector
                 */

                w = normalize(w);

                /**
                 * Get the weight of the hypothesis
                 */
                z.add(k, log2((1-error)/error)) ;
                //System.out.println("error " +  error);
                //System.out.println(log2((1-error)/error));

            }

            h.add(k, hypothesis);

        }

        //printHypotheses();


    }


    public ArrayList<Double> normalizeZ() {
        double sum = 0;
        ArrayList<Double> zNormalized = new ArrayList<>();

        for(int i=0; i<z.size(); i++) {
            sum += z.get(i);
        }

        for(int i=0; i<z.size(); i++) {
            zNormalized.add( (z.get(i)/sum ));
        }

        return zNormalized;
    }

    int truePositive = 0;
    int predictedConditionPositive = 0;
    int conditionPositive = 0;

    public int getPredictedConditionPositive() {
        return predictedConditionPositive;
    }

    public int getTruePositive() {
        return truePositive;
    }

    public int getConditionPositive() {
        return conditionPositive;
    }


    public boolean adaBoostTest(String[] sample) {

        double[] classWeights = new double[nClass];

        ArrayList<Double> zNormalized = normalizeZ();

        int attributeIndexH = 0;
        Hypothesis hypothesis;
        String sVal;
        double dVal;
        String sampleClass = sample[nAttribute - 1];
        ArrayList<String> attributeTypeName;
        ArrayList<String > attributeDecision;
        double splitPoint;
        String hypothesisClass;



        for(int i=0; i<h.size(); i++) {
            hypothesis = h.get(i);

            attributeIndexH = hypothesis.getAttributeIndex();
            attributeDecision = hypothesis.getAttributeDecision();
            attributeTypeName = hypothesis.getAttributeTypeName();

            if(hypothesis.isContinuous()){
                splitPoint = hypothesis.getSplitPoint();
                dVal = Double.parseDouble(sample[attributeIndexH]);

                if(dVal < splitPoint) {
                    hypothesisClass = attributeDecision.get(0);

//                    if(hypothesisClass.equals("yes")) {
//                        predictedConditionPositive++;
//                    }

                    if(hypothesisClass.equals(sampleClass)) {
                        classWeights[0] += zNormalized.get(i);
                    }
                    else {
                        classWeights[1] += zNormalized.get(i);
                    }
                }
                else {
                    hypothesisClass = attributeDecision.get(1);

//                    if(hypothesisClass.equals("yes")) {
//                        predictedConditionPositive++;
//                    }

                    if(hypothesisClass.equals(sampleClass)) {
                        classWeights[0] += zNormalized.get(i);
                    }
                    else {
                        classWeights[1] += zNormalized.get(i);
                    }
                }
            }
            else {
                sVal = sample[attributeIndexH];

                int id = attributeTypeName.indexOf(sVal);
                if(id == -1) {
                    classWeights[0] += zNormalized.get(i);
                    continue;
                }


                hypothesisClass = attributeDecision.get(id);

//                if(hypothesisClass.equals("yes")) {
//                    predictedConditionPositive++;
//                }


                if(hypothesisClass.equals(sampleClass)) {
                    classWeights[0] += zNormalized.get(i);
                }
                else {
                    classWeights[1] += zNormalized.get(i);
                }
            }

            //System.out.println(hypothesisClass + " "  + sampleClass);

            if(hypothesisClass.equals("yes") && sampleClass.equals("yes")) {
                truePositive++;
            }

            if(hypothesisClass.equals("yes")) {
                predictedConditionPositive++;
            }

        }

       // System.out.println(classWeights[0] + " " +  classWeights[1]);

        if(classWeights[0] > classWeights[1]) {
            return true;
        }

        if(classWeights[0] >= 0.5) {
            return true;
        }

        return false;
    }


    public  ArrayList<Hypothesis> getH() {
        return h;
    }

    public  ArrayList<Double> getZ() {
        return z;
    }

    public  void printHypotheses() {
        for(int i=0; i<h.size(); i++) {
            System.out.println("Hypothesis ID : " + i);

            h.get(i).print();
            System.out.println("Weight " + z.get(i));
        }
        System.out.println("\n\n");
    }


    public  double[] normalize(double[] x) {
        double sum = 0;
        for(int i=0; i<x.length; i++) {
            sum += x[i];
        }

        //System.out.println("SUM                                          " + sum);
        //System.out.println(x.length);

        for(int i=0; i<x.length; i++) {
            x[i] = x[i] / sum;
        }

        for(int i=0; i<x.length; i++) {
            w[i] = x[i];
        }

        sum = 0;
        for(int i=0; i<x.length; i++) {
            sum += x[i];
        }

        //System.out.println("SUM                                          " + sum);
        //System.out.println(x.length);

        return x;
    }


    public  double log2(double x) {

        return (Math.log(x) / Math.log(2));
    }


    public  void printList(ArrayList<String[]> examples) {
        int nSample = examples.size();
        for(int i=0; i<nSample; i++) {
            for(String s : examples.get(i)) {
                System.out.print(s + " ");
            }
            System.out.println();
        }

    }
}
