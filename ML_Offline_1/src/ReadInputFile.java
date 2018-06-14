import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by asd on 4/25/2018.
 */
public class ReadInputFile {

    public static void mainReadInputFile() throws IOException {

        String line = null;
        int numberOfRows = 5000;
        int numberOfCol = 17;
        int N=0;
        int row = 0;


        int nData =0;
        int nAttribute=0;
        try (FileWriter output = new FileWriter("C:\\Users\\asd\\IdeaProjects\\ML_Offline_1\\src\\Input\\processedData.txt");
             InputStream fis = new FileInputStream("C:\\Users\\asd\\IdeaProjects\\ML_Offline_1\\src\\Input\\InputFile.txt");
             InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
             BufferedReader br = new BufferedReader(isr)
        ) {
            while ((line = br.readLine()) != null) {
                // Do your thing with line
                String[] attributes = line.split(";");
                nAttribute = attributes.length;
                nData++;
            }
        }

        numberOfRows = nData;
        numberOfCol = nAttribute;

        System.out.println("numberOfRows " + numberOfRows);
        System.out.println("numberOfCol " + numberOfCol);


        String[][] inputData = new String[numberOfRows][numberOfCol];
        String[][] examplesFinal = new String[numberOfRows][numberOfCol];

        try (FileWriter output = new FileWriter("C:\\Users\\asd\\IdeaProjects\\ML_Offline_1\\src\\Input\\processedData.txt");
             InputStream fis = new FileInputStream("C:\\Users\\asd\\IdeaProjects\\ML_Offline_1\\src\\Input\\InputFile.txt");
             InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
             BufferedReader br = new BufferedReader(isr)
        ) {

            row = 0;

            while ((line = br.readLine()) != null) {
                // Do your thing with line
                String[] attributes = line.split(";");

                for(int i=0; i<attributes.length; i++) {
                    inputData[row][i] = attributes[i];
                }
                row++;
            }
        }

        System.out.println(row);

        boolean flag = true;
        for(int i=0; i<row && flag; i++) {
            for(int j=0; j<numberOfCol; j++) {
                if(inputData[i][j] == null) flag = false;
            }
            N++;
        }
        System.out.println(N);

        int countYes=0;
        int rowFinal=0;
        for(int i=0; i<N; i++) {
            if(inputData[i][inputData[0].length - 1].equals("\"yes\"")) {
                for(int j=0; j<inputData[0].length; j++) {
                    examplesFinal[rowFinal][j] = inputData[i][j];
                }
                countYes++;
                rowFinal++;
            }
        }

        System.out.println(countYes);

        int k=countYes;

        for(int i=0; i<N && countYes!=0 ; i++) {
            if(inputData[i][inputData[0].length - 1].equals("\"no\"")) {
                for(int j=0; j<inputData[0].length && countYes!=0; j++) {
                    examplesFinal[rowFinal][j] = inputData[i][j];
                }
                rowFinal++;
                countYes--;
            }
        }


        try (FileWriter output = new FileWriter("C:\\Users\\asd\\IdeaProjects\\ML_Offline_1\\src\\Input\\processedData.txt")
        ) {
            for (int i = 0; i < rowFinal; i++) {
                for (int j = 0; j < examplesFinal[0].length; j++) {
                    output.write(examplesFinal[i][j].replace("\"", "") + " ");
                    //System.out.print(examplesFinal[i][j]);
                }
                output.write("\n");
                //System.out.println();
            }
        }

    }
}
