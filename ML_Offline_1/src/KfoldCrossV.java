import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by asd on 4/27/2018.
 */
public class KfoldCrossV {

    static String inputFileName;
    static String adaBoostInputFile = "train";
    static int nHypothesis = 20;
    static int kfold = 5;
    static int round = 1;
    static double F1Avg = 0;

    public static void main(String[] args) throws IOException, InterruptedException {


        for(int r=0; r<kfold;r++) {

            /**
             * This takes raw .csv data set
             * and returns a working data set
             * named FinalData.txt
             */
            //DataSetManagement dataSetManagement = new DataSetManagement();
            //dataSetManagement.mainDataSetManagement();

            /**
             * This one splits the FinalData.txt
             * in to 2 parts
             * train.txt contains (k-1)/k of total data points
             * test.txt contains (1/k) of total data points
             * test and train set are disjoint
             */
            SplitDataSet splitDataSet = new SplitDataSet();
            splitDataSet.mainSplitDataSet(kfold, r);

            Thread.sleep(1000);


            /**
             * AdaBoost takes train.txt and trains the weeks learners
             */
            AdaBoost adaBoost = new AdaBoost();
            adaBoost.mainAdaBoost(adaBoostInputFile, nHypothesis);


            /**
             * Now test the performance
             */
            MakeExampleSet makeExampleSet = new MakeExampleSet();
            ArrayList<String[]> testSet = makeExampleSet.mainMakeExampleSet("test");

            if (testSet.size() == 0) System.out.println("Empty test set!!!!!");

            int nAttribute = testSet.get(0).length;
            int nSample = testSet.size();

            /**
             * individual list of each attribute
             */
            ArrayList[] attribute = new ArrayList[nAttribute];
            for (int i = 0; i < nAttribute; i++) {
                attribute[i] = new ArrayList<String>();
            }
            /**
             * Extract data from data set and make them individual attribute list
             */
            String[] values;

            for (int i = 0; i < nSample; i++) {
                values = testSet.get(i);
                for (int j = 0; j < nAttribute; j++) {
                    attribute[j].add(values[j]);
                }
            }


            /**
             *
             */

            int trueCount = 0;
            int falseCount = 0;
            boolean res;
            double conditionPositive = 0;
            double truePositive = 0;
            double predictedConditionPositive = 0;


            for (int i = 0; i < testSet.size(); i++) {
                if (testSet.get(i)[nAttribute - 1].equals("yes")) {
                    conditionPositive += 1.0;
                }
            }

            for (int i = 0; i < testSet.size(); i++) {
                res = adaBoost.adaBoostTest(testSet.get(i));
                if (res) trueCount++;
                else falseCount++;

                if (res == true && testSet.get(i)[nAttribute - 1].equals("yes")) {
                    predictedConditionPositive++;
                    truePositive++;
                }
                if (res == false && testSet.get(i)[nAttribute - 1].equals("no")) {
                    predictedConditionPositive++;
                }

                //System.out.println(res);
            }

            //truePositive = adaBoost.getTruePositive() * 1.0;
            //predictedConditionPositive = adaBoost.getPredictedConditionPositive() * 1.0;

            // System.out.println("True pos : " + truePositive);
            // System.out.println("Cond pos : "  + conditionPositive);
            // System.out.println("predictedConditionPositive : " + predictedConditionPositive);

            double recall = (truePositive / conditionPositive) * 100;
            double precision = (truePositive / predictedConditionPositive) * 100;


           // System.out.println("Recall = " + (truePositive / conditionPositive) * 100 + " %");
           // System.out.println("precision = " + (truePositive / predictedConditionPositive) * 100 + " %");

            double F1 = 2 / ((1 / recall) + (1 / precision));
            System.out.println(F1);

            F1Avg += F1;

            //  System.out.println(trueCount);
            //  System.out.println(falseCount);

            double accuracy = (trueCount * 1.0 / (trueCount * 1.0 + falseCount * 1.0) * 1.0) * 100.0;
           // System.out.println("Accuracy : " + accuracy + " %");
        }

        System.out.println("Avg : " + F1Avg/kfold);




    }
}
