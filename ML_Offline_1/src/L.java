import org.jcp.xml.dsig.internal.dom.DOMUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.util.Random;

/**
 * Created by asd on 4/25/2018.
 */
public class L {

    double[] weightL;
    ArrayList<String[]> examplesL;
    int numberofC  = 0;



    public Hypothesis selectAttribute(ArrayList<String[]> examplesL) {

        /**
         * These attribute are to be returned
         */

        ArrayList<String> attributeTypeName = new ArrayList<>();
        ArrayList<String > attributeDecision = new ArrayList<>();
        String attributeName;
        int attributeIndex;
        double splitPoint=0;

        int nAttribute = examplesL.get(0).length;
        int nData = examplesL.size();
        int nClass = 0;
        /**
         * individual list of each attribute
         */
        ArrayList[] attribute = new ArrayList[nAttribute];

        /**
         * attribute set holds unique values of each attribute
         */
        ArrayList[] attributeSet = new ArrayList[nAttribute];

        for(int i=0; i<nAttribute; i++) {
            attribute[i] = new ArrayList<String>();
            attributeSet[i] = new ArrayList<String >();
        }

        /**
         * Extract data from data set and make them individual attribute list
         */
        String[] values;

        for(int i=0; i<nData; i++) {
            values = examplesL.get(i);
            for(int j=0; j<nAttribute; j++) {
                attribute[j].add(values[j]);
            }
        }


        /**
         * Generate attribute set
         */
        for(int i=0; i<nAttribute; i++) {
            attributeSet[i] = uniqueValues(attribute[i]);
        }

        /**
         * Last row is the class
         * So we can get the number of different classes
         */
        nClass = attributeSet[nAttribute-1].size();
        numberofC = nClass;
        //System.out.println(nClass);


        /**
         * Determine continuity of attributes
         */
        boolean[] continuous = new boolean[nAttribute];
        for(int i=0; i<nAttribute; i++) {
            if(tryParse(attributeSet[i].get(0).toString()) != null) {
                continuous[i] = true;
                //System.out.println(i);
            }
            else {
                continuous[i] = false;
            }
        }

        /**
         * To calculate entropy we need to sum
         * the weights of each attribute by their class
         * class1 : attr1 : type1, type2, .........
         * class2 : attr1 : type1, type2, .........
         * .......................................
         */
        double[][] attributeWeight = new double[nClass][];

        /**
         * We are going to find an attribute
         * for which we can get minimum entropy
         */
        double minEntropy = Double.MAX_VALUE;
        int minEntropyAttribute = 0;
        double savedPoint = 0;
        double savedPointAll[] = new double[nAttribute];
        double allEntropy[] = new double[nAttribute];

        /**
         * Select one attribute
         */
        for(int attr=0; attr<nAttribute-1; attr++) {

            int attributeType = attributeSet[attr].size();

            // if(attr == 10) continue;



            if(continuous[attr] == true) {

                attributeType = 2;

                double max = findMax(attribute[attr]);
                double min = findMin(attribute[attr]);
                double DELX = (max - min)/50;
                int less=0;
                int greater=1;


                double point = min;
                double minEntropyCont = Double.MAX_VALUE;

                savedPoint = 0;


                while (point <= max ) {
                    /**
                    * Select one class
                    */
                    for(int cls=0; cls<nClass; cls++) {
                        attributeWeight[cls] = new double[2];
                    }
                        //String nameOfClass = attributeSet[nAttribute-1].get(cls).toString();

                    for(int i=0; i<nData; i++) {

                        double val = Double.parseDouble( attribute[attr].get(i).toString() );
                        String className = attribute[nAttribute-1].get(i).toString();
                        int classIdx = attributeSet[nAttribute-1].indexOf(className);
                        if(val < point) {
                            attributeWeight[classIdx][less] += 1;
                        }
                        else {
                            attributeWeight[classIdx][greater] += 1;

                        }
                    }

                   // printAttributeWeight(attributeWeight);

                    double sum=0;
                    double typeSum=0;
                    double totalSum = 0;
                    for(int type=0; type<attributeType; type++) {

                        typeSum=0;

                        for(int cls=0; cls<nClass; cls++) {
                            typeSum+=attributeWeight[cls][type];
                        }

                       //System.out.println("typeSum " + typeSum);


                        for(int cls=0; cls<nClass; cls++) {
                            sum += entropyDis( attributeWeight[cls][type] , typeSum );
                        }


                        sum = sum * (typeSum);
                        System.out.print("");
                        //System.out.println("sum " + sum);
                        totalSum += sum;


                        //totalSum /= nData;


                        sum = 0;
                    }

                    totalSum /= nData;

                    for(int cls=0; cls<nClass; cls++) {
                        for(int type = 0; type<attributeType; type++) {
                            //System.out.println(cls + " " + type + " " + attributeWeight[cls][type]);
                        }
                    }

                   // System.out.println("Entropy of ++ " + attr +" : "  + totalSum);

                    if(minEntropyCont > totalSum) {
                        minEntropyCont = totalSum;
                        //minEntropyAttribute = attr;
                        savedPoint = point;
                        //saved = attributeWeight;
                       // System.out.println(attr + " Min found at point : " + point);
                    }

                    point += DELX;

                }

                savedPointAll[attr] = savedPoint;
                allEntropy[attr] = minEntropyCont;
               // System.out.println(attr + " Saved point : " + savedPoint);

                for(int cls=0; cls<nClass; cls++) {
                    attributeWeight[cls] = new double[2];
                }

                for(int i=0; i<nData; i++) {
                    double val = Double.parseDouble(attribute[attr].get(i).toString());
                    System.out.print("");
                    String className = attribute[nAttribute-1].get(i).toString();
                    //System.out.println(className);
                    int classIdx = attributeSet[nAttribute-1].indexOf(className);

                    if(val < savedPoint) {
                        attributeWeight[classIdx][less] += 1;
                    }
                    else {
                        attributeWeight[classIdx][greater] += 1;
                    }
                }

                for(int cls=0; cls<nClass; cls++) {
                    for(int type = 0; type<attributeType; type++) {
                       // System.out.println(cls + " " + type + " " + attributeWeight[cls][type] + " **************** ");
                    }
                }

            }
            else {

                /**
                 * Select one class
                 */
                for (int cls = 0; cls < nClass; cls++) {

                    attributeWeight[cls] = new double[attributeType];
                    String nameOfClass = attributeSet[nAttribute - 1].get(cls).toString();

                    /**
                     * For each different attribute type
                     */
                    for (int i = 0; i < attributeSet[attr].size(); i++) {

                        String s1 = attributeSet[attr].get(i).toString();
                        /**
                         * Data set iteration
                         */
                        for (int j = 0; j < nData; j++) {
                            String s2 = attribute[attr].get(j).toString();
                            String dataClass = attribute[nAttribute - 1].get(j).toString();

                            if (s1.equals(s2) && nameOfClass.equals(dataClass)) {
                                attributeWeight[cls][i] += 1;
                            }
                        }
                        //System.out.print(nameOfClass + " " + attributeWeight[cls][i] + " ");
                    }
                    //System.out.println();

                }

               // System.out.println("Attr " + attr);
                for(int cls=0; cls<nClass; cls++) {
                    for(int type = 0; type<attributeType; type++) {
                      //  System.out.println(cls + " " + type + " " + attributeWeight[cls][type]);
                    }
                }


            }

            double totalSum = 0;
            double sum=0;
            double typeSum=0;



            for(int type=0; type<attributeType; type++) {

                typeSum=0;

                for(int cls=0; cls<nClass; cls++) {
                    typeSum+=attributeWeight[cls][type];
                }

                for(int cls=0; cls<nClass; cls++) {
                    sum += entropyDis( attributeWeight[cls][type] , typeSum );
                }

                sum = sum * (typeSum);

                totalSum += sum;
                sum=0;

                //totalSum /= nData;
            }

            //sum = sum * ()

            totalSum /= nData;


            //System.out.println("Entropy of " + attr +" : "  + totalSum);
            allEntropy[attr] = totalSum;

            if(minEntropy > totalSum) {
                minEntropy = totalSum;
                minEntropyAttribute = attr;
                //System.out.println(minEntropyAttribute + "---------------" + totalSum + "-----------" );
            }
            //System.out.println(minEntropyAttribute+" ***********************************");
        }

        //System.out.println(minEntropyAttribute  + " " + minEntropy);


        /**
         * Map with selected attribute type
         * index : minEntropyAttribute
         */

        ArrayList<String> attributeClassMap = new ArrayList<>();
        ArrayList<String> attributeTypeMap = new ArrayList<>();


        int attributeType = attributeSet[minEntropyAttribute].size();

        if(continuous[minEntropyAttribute]) {

            attributeType = 2;

            int less  = 0 ;
            int greater = 1;

            for(int cls=0; cls<nClass; cls++) {
                attributeWeight[cls] = new double[2];
            }

            for(int i=0; i<nData; i++) {
                double val = Double.parseDouble(attribute[minEntropyAttribute].get(i).toString());
                String className = attribute[nAttribute-1].get(i).toString();
                //System.out.println(className);
                int classIdx = attributeSet[nAttribute-1].indexOf(className);
                if(val < savedPointAll[minEntropyAttribute]) {
                    attributeWeight[classIdx][less] += 1;
                    System.out.print("");
                }
                else {
                    attributeWeight[classIdx][greater] += 1;
                }
            }


            for(int cls=0; cls<nClass; cls++) {
                for(int type = 0; type<attributeType; type++) {
                   // System.out.println(cls + " " + type + " " + attributeWeight[cls][type] + " ################### ");
                }
            }
            //System.out.println("minEntropyAttribute " +minEntropyAttribute);
           // System.out.println("savedPointAll[minEntropyAttribute] " + savedPointAll[minEntropyAttribute]);




            double max = Double.MIN_VALUE;
            int classIdx=0;

            for(int i=0; i<attributeType; i++) {
                max = Double.MIN_VALUE;

                for(int cls=0; cls<nClass; cls++) {
                   // System.out.println(cls + " " + i + " " +attributeWeight[cls][i] + " " + attributeSet[nAttribute-1].get(cls).toString());
                    if(max < attributeWeight[cls][i]) {
                        max = attributeWeight[cls][i];
                        classIdx = cls;
                    }
                }

                attributeClassMap.add(attributeSet[nAttribute-1].get(classIdx).toString());
                attributeTypeMap.add(""+i);

            }



        }

        else {

            /**
             * Select one class
             */
            for (int cls = 0; cls < nClass; cls++) {

                attributeWeight[cls] = new double[attributeType];
                String nameOfClass = attributeSet[nAttribute - 1].get(cls).toString();

                /**
                 * For each different attribute type
                 */
                for (int i = 0; i < attributeSet[minEntropyAttribute].size(); i++) {

                    String s1 = attributeSet[minEntropyAttribute].get(i).toString();
                    /**
                     * Data set iteration
                     */
                    for (int j = 0; j < nData; j++) {
                        String s2 = attribute[minEntropyAttribute].get(j).toString();
                        String dataClass = attribute[nAttribute - 1].get(j).toString();

                        if (s1.equals(s2) && nameOfClass.equals(dataClass)) {
                            attributeWeight[cls][i] += 1;
                            System.out.print("");
                        }
                    }
                }
            }

           // System.out.println("Attr " + minEntropyAttribute);
            for(int cls=0; cls<nClass; cls++) {
                for(int type = 0; type<attributeType; type++) {
                   // System.out.println(cls + " " + type + " " + attributeWeight[cls][type]);
                }
            }


            double max = 0;
            int classIdx = 0;

            for (int i = 0; i < attributeType; i++) {
                max = 0;
                for (int cls = 0; cls < nClass; cls++) {
                    if (max < attributeWeight[cls][i]) {
                        max = attributeWeight[cls][i];
                        classIdx = cls;
                    }
                }
                attributeClassMap.add(attributeSet[nAttribute - 1].get(classIdx).toString());
                attributeTypeMap.add(attributeSet[minEntropyAttribute].get(i).toString());
            }
        }


        //System.out.println("=======================================");

        //System.out.println(minEntropyAttribute);

        for(int i=0;  i<attributeClassMap.size();i++) {
            //System.out.println(attributeTypeMap.get(i) + " " + attributeClassMap.get(i));
        }
        if(continuous[minEntropyAttribute]) {
           // System.out.println(savedPointAll[minEntropyAttribute]);
        }

        for(int i=0; i<nAttribute; i++) {
            //System.out.println(i + " " + allEntropy[i] + " " + savedPointAll[i] + " " + continuous[i]);
        }


        attributeDecision = attributeClassMap;
        attributeTypeName = attributeTypeMap;
        attributeIndex = minEntropyAttribute;


        Hypothesis hypothesis = new Hypothesis(attributeTypeName, attributeDecision, attributeIndex, savedPointAll[attributeIndex], continuous[attributeIndex]);
        //System.out.println("return");
        return hypothesis;
        //return null;
    }


    public void printArray(double[] x) {
        for(int i=0; i<x.length; i++) {
            System.out.println(x);
        }
    }





    public double findMax(ArrayList x) {
        double max = Double.MIN_VALUE;
        for(int i=0; i<x.size(); i++) {
            double temp = Double.parseDouble(x.get(i).toString());

            if(max < temp) {
                max = temp;
            }
        }
        return max;
    }


    public double findMin(ArrayList x) {
        double min = Double.MAX_VALUE;
        for(int i=0; i<x.size(); i++) {
            double temp = Double.parseDouble(x.get(i).toString());

            if(min > temp) {
                min = temp;
            }
        }
        return min;
    }




    public Double tryParse(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public ArrayList<String>  uniqueValues(ArrayList<String> list) {
        ArrayList<String> newList = new ArrayList<>(new HashSet<>(list));
        return newList;
    }

    public double entropyDis(double n, double N) {

        if(n==0 || N ==0 ) return 0;

        double result = (-1) * (n/N) * log2((n/N));
        //System.out.println(result);
        return result;
    }

    public double log2(double x) {

        return (Math.log(x) / Math.log(2));
    }

    public void printAttributeWeight(double[][] attributeWeight) {
        for(int i=0; i<attributeWeight.length; i++) {
            for(int j=0; j<attributeWeight[0].length; j++) {
                System.out.print(attributeWeight[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int numberOfClass() {
        return numberofC;
    }


}
