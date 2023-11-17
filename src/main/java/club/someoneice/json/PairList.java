package club.someoneice.json;

import java.util.*;

/**
 * A list for Pairs. Why not {@link HashMap}? It will orderly the key-value.
 * */
public class PairList<A, B> extends ArrayList<Pair<A, B>>{

    public void put(A key, B value) {
        this.add(new Pair<>(key, value));
    }

    public Pair<A, B> get(A key) {
        Optional<Pair<A, B>> cup = this.stream().filter(it -> it.getKey().equals(key)).findFirst();
        return cup.orElse(null);
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

    public Iterator<Pair<A, B>> getIterator() {
        return this.iterator();
    }
}
