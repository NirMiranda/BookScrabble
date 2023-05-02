package Model.Logic;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class CacheManager {
    int capacity;
    CacheReplacementPolicy crp;

    Set<String>cache;
    public CacheManager(int capacity, CacheReplacementPolicy crp){
        this.capacity=capacity;
        this.crp = crp;
        this.cache = new HashSet<String>(capacity);
    }

    public boolean query(String word){
        if(cache.contains(word))
            return true;
        return false;
    }
    public void add(String word){
        if(cache.size()==capacity) {
            cache.remove(crp.remove());
            cache.add(word);
        }
        else {
            cache.add(word);
        }
        crp.add(word);
        }
    }


