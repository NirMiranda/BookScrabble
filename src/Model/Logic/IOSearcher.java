package Model.Logic;

import java.io.*;

public class IOSearcher {

    public IOSearcher() {
    }

    public static boolean search(String word, String...filesNames) throws FileNotFoundException {
        for (int i = 0; i < filesNames.length; i++) {
            try{
            File file = new File(filesNames[i]);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(word)) {
                    return true;
                }
            }
                reader.close();
            } catch (IOException e){}

        }
        return false;
    }
}

