package Model.Logic;
import java.util.*;

public class LFU implements CacheReplacementPolicy{
    public class Temp implements Comparable<Temp>{

        private String word;
        private int value;

        public Temp(String w, int count){
            this.word=w;
            this.value=count;
        }
        public Temp(String w){
            this.word=w;
            this.value=1;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public int compareTo(Temp o) {
            return this.value-o.value;
        }
    }

    HashMap<String, Temp> stringMap;
    PriorityQueue<Temp>q;

    public LFU() {
        stringMap= new HashMap<>();
        q=new PriorityQueue<>();
   }

    @Override
    public void add(String word) {
        if(stringMap.containsKey(word)==true) {
            q.remove(stringMap.get(word));
            Temp t=stringMap.get(word);
            t.value++;
            stringMap.get(word).value=t.value;
            q.add(t);
        }
        else{
            Temp t=new Temp(word);
            stringMap.put(word,t);
            q.add(t);
        }
    }

    @Override
    public String remove() {
        String s;
        s=q.poll().word;
        q.poll();
        return s;
    }
}
