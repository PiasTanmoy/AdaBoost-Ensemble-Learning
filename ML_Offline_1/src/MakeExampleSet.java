import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by asd on 4/26/2018.
 */
public class MakeExampleSet {

    public ArrayList  mainMakeExampleSet(String fileName) throws IOException {


        String line;
        ArrayList<String[]> examples = new ArrayList<>(100);

        try (
             //InputStream fis = new FileInputStream("C:\\Users\\asd\\IdeaProjects\\ML_Offline_1\\src\\Input\\FinalData.txt");
             InputStream fis = new FileInputStream("C:\\Users\\asd\\IdeaProjects\\ML_Offline_1\\src\\Input\\"+fileName+".txt");
             InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
             BufferedReader br = new BufferedReader(isr)
        ) {

            while ((line = br.readLine()) != null) {
                String[] attributes = line.split(" ");
                examples.add(attributes);
            }
        }

        return examples;
    }

}
