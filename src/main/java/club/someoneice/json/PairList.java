package club.someoneice.json;

import java.util.*;

public class PairList<A, B> extends ArrayList<Pair<A, B>> {

    public void put(A key, B value) {
        this.add(new Pair<>(key, value));
    }

    public Pair<A, B> get(A key) {
        return this.stream().filter(it -> it.getKey().equals(key)).findFirst().orElse(null);
    }

    public Pair<A, B> getByValue(B value) {
        return this.stream().filter(it -> it.getValue().equals(value)).findFirst().orElse(null);
    }

    public Pair<A, B> get(int i) {
        if (i < this.size())
            return this.get(i);
        else return null;
    }

    public List<Pair<A, B>> asList() {
        return new ArrayList<>(this);
    }

    public void replace(List<Pair<A, B>> list) {
        this.clear();
        this.addAll(list);
    }

    public HashMap<A, B> asMap() {
        HashMap<A, B> map = new HashMap<>();
        this.forEach(it -> {
            map.put(it.getKey(), it.getValue());
        });
        return map;
    }

    public <T extends Map<A, B>> T asMap(T map) {
        this.forEach(it -> {
            map.put(it.getKey(), it.getValue());
        });
        return map;
    }

    public Iterator<Pair<A, B>> getIterator() {
        return this.iterator();
    }
}
