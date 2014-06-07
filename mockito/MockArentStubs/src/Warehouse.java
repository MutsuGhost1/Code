import java.util.HashMap;


public class Warehouse {
    private HashMap<String, Integer> mValues =
            new HashMap<String, Integer>();
    
    public boolean hasInventory(String name, int num){
        int remain = getInventory(name);
        if(num <= remain){
            mValues.put(name, remain-num);
            return true;
        }
        return false;
    }

    public void add(String name, int num){
        mValues.put(name, num);
    }
    
    public int getInventory(String name){
        Integer remain = mValues.get(name);
        return remain == null ? 0 : remain;
    }
}
