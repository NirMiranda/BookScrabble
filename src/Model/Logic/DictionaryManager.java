package Model.Logic;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class DictionaryManager {

    private static DictionaryManager d = null;
    private Map<String, Dictionary> map;

    DictionaryManager() {
        map = new HashMap<>();

    }

    public boolean query(String... args) {
        boolean flag=false;
        for (int i = 0; i < args.length - 1; i++) {
            if (!map.containsKey(args[i]))
                map.put(args[i], new Dictionary(args[i].toString()));
            if (map.get(args[i]).query(args[args.length - 1]) == true) {
                flag=true;
            }
            map.get(args[i]).query(args[i]);
        }
        return flag;
    }


    public boolean challenge (String word, String...args) {
        boolean flag=false;
        for (int i = 0; i < args.length - 1; i++) {
            if (!map.containsKey(args[i]))
                map.put(args[i], new Dictionary(args[i].toString()));
            if (map.get(args[i]).challenge(args[args.length - 1]) == true) {
                flag=true;
            }
            map.get(args[i]).challenge(args[i]);
        }
        return flag;
    }

    public int getSize(){
        return this.map.size();
    }

    public static DictionaryManager get() {
        if (d == null)
            d = new DictionaryManager();
        return d;
    }
}


