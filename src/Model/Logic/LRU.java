package Model.Logic;
import java.util.LinkedHashSet;


public class LRU implements CacheReplacementPolicy {
    LinkedHashSet<String> set = new LinkedHashSet<>();
    public LRU() {
    }
    @Override
    public void add(String word) {
        {
            if(set.contains(word)){
                   set.remove(word);
                   set.add(word);
               }
               else{
                   set.add(word);
               }
        }
    }
    @Override
    public String remove() {
        String temp= set.iterator().next();
        set.remove(set.iterator().next());
         return temp;
    }
}
