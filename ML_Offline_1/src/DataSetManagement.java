import java.io.IOException;

/**
 * Created by asd on 4/27/2018.
 */
public class DataSetManagement {

    String inputFile;

    public void mainDataSetManagement(String inputFile) throws InterruptedException, IOException {
        /**
         * Shuffle the entire data set
         */
        ShuffleAllData shuffleAllData = new ShuffleAllData();
        shuffleAllData.mainShuffleAllData(inputFile);
        //shuffleAllData.mainShuffleAllData("bank-additional-full");

        Thread.sleep(1000);

        /**
         * Get equal number of class
         */
        ReadInputFile readInputFile = new ReadInputFile();
        readInputFile.mainReadInputFile();

        Thread.sleep(1000);

        /**
         *  Shuffle the new data set
         */
        ShuffleData shuffleData = new ShuffleData();
        shuffleData.mainShuffleData();

        Thread.sleep(1000);
    }

}
