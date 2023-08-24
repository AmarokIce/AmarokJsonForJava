package club.someoneice.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class PairList<A, B> {
    private final List<Pair<A, B>> pairs = new ArrayList<>();

    public void put(A key, B value) {
        this.pairs.add(new Pair<>(key, value));
    }

    public void remove(int i) {
        if (i < this.pairs.size())
            this.pairs.remove(i);
    }

    public Pair<A, B> get(A key) {
        Optional<Pair<A, B>> cup = this.pairs.stream().filter(it -> it.getKey().equals(key)).findFirst();
        return cup.orElse(null);
    }

    public Pair<A, B> get(int i) {
        if (i < this.pairs.size())
            return this.pairs.get(i);
        else return null;
    }

    public List<Pair<A, B>> asList() {
        return new ArrayList<>(this.pairs);
    }

    public void addAll(List<Pair<A, B>> list) {
        this.pairs.addAll(list);
    }

    public void replace(List<Pair<A, B>> list) {
        this.pairs.clear();
        this.addAll(list);
    }

    public Iterator<Pair<A, B>> getIterator() {
        return this.pairs.iterator();
    }
}
