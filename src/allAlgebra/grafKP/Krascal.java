package allAlgebra.grafKP;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Krascal {
    private Map<Integer,Integer> map =new HashMap<>();
    public Krascal(Map<Integer,Integer> map){
        this.map.putAll(map);
    }
    public void addProperti(int key,int newLocate) {
        map.put(key,newLocate);
    }

    public Integer getInstance(int key) {
        return map.get(key);
    }
    public Collection<Integer> getAllValues() {
        return map.values();
    }
    public int find(int x) {
        if (!map.containsKey(x)) {
            map.put(x, x);
            return x;
        }

        int parent = map.get(x);

        if (parent != x) {
            parent = find(parent);
            map.put(x, parent);
        }
        return parent;
    }

    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false;
        }

        map.put(rootX, rootY);
        return true;
    }
}
