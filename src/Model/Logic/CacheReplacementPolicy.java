package Model.Logic;
import java.util.LinkedHashMap;
import java.util.Map;
public interface CacheReplacementPolicy{
	void add(String word);
	String remove();
}
