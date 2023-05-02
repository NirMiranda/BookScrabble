package Model.Logic;
import java.io.*;
import java.security.NoSuchAlgorithmException;

public class Dictionary {

    String [] f;
    CacheManager existWord;
    CacheManager notExistWord;
    BloomFilter b;


    public Dictionary(String...fileNames){
        f=fileNames.clone();
        existWord=new CacheManager(400,new LRU());
        notExistWord=new CacheManager(100,new LFU());
        b = new BloomFilter(256, "SHA1", "MD5");
           for(int i=0;i<fileNames.length;i++) {
                try {
                    BufferedReader in=new BufferedReader(new FileReader(fileNames[i]));
                    String line;
                    while ((line = in.readLine()) != null) {
                        String [] words=line.split("\\s+");
                                for(String word: words) {
                                    b.add(word);
                                }
                    }
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


    }
    public boolean query(String word){
        if(existWord.query(word)) return true;
        else if(notExistWord.query(word)) return false;
        else if (b.contains(word)){
            existWord.add(word);
            return true;
        }
        else{
            notExistWord.add(word);
            return false;
        }
    }


    public boolean challenge(String word) {
        IOSearcher io = new IOSearcher();
        try {
            if (io.search(word,this.f) == true) {
                existWord.add(word);
                return true;
            } else {
                notExistWord.add(word);
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

