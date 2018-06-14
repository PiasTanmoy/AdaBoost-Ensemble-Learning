import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by asd on 4/27/2018.
 */
public class SplitDataSet {

    int k = 5;
    String name = "file_";
    int nData = 0;
    int n=0;

    public void mainSplitDataSet(int division, int round) throws IOException {
        String line;
        ArrayList<String > data = new ArrayList<String>(100);

        try (
             InputStream fis = new FileInputStream("C:\\Users\\asd\\IdeaProjects\\ML_Offline_1\\src\\Input\\FinalData.txt");
             InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
             BufferedReader br = new BufferedReader(isr)
        ) {

            while ((line = br.readLine()) != null) {
                data.add(line);
                nData++;
            }

           // Collections.shuffle(data);
            //Collections.shuffle(data);


        }

        //System.out.println(data.size());

        //FileWriter output;
        int startPoint = 0;
        n = round;
        k = division;
        int d = data.size()/k;

        try (FileWriter output = new FileWriter("C:\\Users\\asd\\IdeaProjects\\ML_Offline_1\\src\\Input\\test.txt");
        FileWriter output2 = new FileWriter("C:\\Users\\asd\\IdeaProjects\\ML_Offline_1\\src\\Input\\train.txt");)
        {
            for(int j=startPoint; j<n*d; j++) {
                output2.write(data.get(j));
                output2.write("\n");
            }

            startPoint += n*d;

            for(int j=startPoint; j< (n+1)*d; j++) {
                output.write(data.get(j));
                output.write("\n");
            }

            if(n==0) startPoint += (n+1)*d;
            else startPoint += n*d;

            for(int j=startPoint; j<data.size(); j++) {
                output2.write(data.get(j));
                output2.write("\n");
            }

        }

    }
}
