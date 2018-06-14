import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by asd on 4/26/2018.
 */
public class ShuffleData {

    public static void mainShuffleData() throws IOException {

        String line;
        ArrayList<String > data = new ArrayList<String>(100);

        try (FileWriter output = new FileWriter("C:\\Users\\asd\\IdeaProjects\\ML_Offline_1\\src\\Input\\FinalData.txt");
             InputStream fis = new FileInputStream("C:\\Users\\asd\\IdeaProjects\\ML_Offline_1\\src\\Input\\processedData.txt");
             InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
             BufferedReader br = new BufferedReader(isr)
        ) {

            while ((line = br.readLine()) != null) {
                data.add(line);
            }

            Collections.shuffle(data);

            for(String s : data) {
                output.write(s+"\n");
                //System.out.println(s);
            }

        }


    }
}
