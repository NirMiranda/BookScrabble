package Model.Logic;
import java.io.*;


public class BookScrabbleHandler implements ClientHandler {

    BufferedReader in;
    PrintWriter out;
    DictionaryManager d = new DictionaryManager();

    public BookScrabbleHandler() {
    }

    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        in = new BufferedReader(new InputStreamReader(inFromclient));
        out = new PrintWriter(outToClient, true);

        String line;
        try {
                line = in.readLine();
                String[] arr = line.split(","); //הפרדת המחרוזת עי פסיקים
                String[] arrCopy = new String[arr.length - 1]; //מערך להעתיק אליו
                System.arraycopy(arr, 1, arrCopy, 0, arr.length - 1);
                String response;
                if (arr[0].equals("Q")) {
                    if (d.query(arrCopy)) {
                        out.println("true\n");
                    } else
                        out.println("false\n");
                }
                if (arr[0].equals("C")) {
                    if (d.challenge(arr[arr.length-1],arrCopy)) {
                        out.println("true\n");
                    } else
                        out.println("false\n");
                }
                close();
            } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() {
        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        out.close();
    }

}


