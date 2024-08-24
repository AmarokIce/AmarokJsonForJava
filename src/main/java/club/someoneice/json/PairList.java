package club.someoneice.json;

import java.util.*;
import java.util.function.Predicate;

public class PairList<A, B> extends ArrayList<Pair<A, B>> {

    public void put(A key, B value) {
        this.add(new Pair<>(key, value));
    }

    /**
     * @deprecated - Use {@link PairList#getByKey}
     */
    @Deprecated
    public Pair<A, B> get(A key) {
        return getByKey(key);
    }

    public Pair<A, B> getByKey(A key) {
        return this.stream().filter(it -> it.getKey().equals(key)).findFirst().orElse(null);
    }

    public Pair<A, B> getByValue(B value) {
        return this.stream().filter(it -> it.getValue().equals(value)).findFirst().orElse(null);
    }

    public Pair<A, B> at(int i) {
        if (i < this.size())
            return this.get(i);
        else return null;
    }

    public void replaceIf(Pair<A, B> pair, Predicate<? super Pair<A, B>> filter) {
        this.replaceIf(pair.getKey(), pair.getValue(), filter);
    }

    public void replaceIf(A key, B value, Predicate<? super Pair<A, B>> filter) {
        Objects.requireNonNull(filter);
        this.stream().filter(filter).forEach(it -> {
            it.setKey(key);
            it.setValue(value);
        });
    }

    public void setKeyIf(A key, Predicate<? super Pair<A, B>> filter) {
        Objects.requireNonNull(filter);
        this.stream().filter(filter).forEach(it -> it.setKey(key));
    }

    public void setValueIf(B value, Predicate<? super Pair<A, B>> filter) {
        Objects.requireNonNull(filter);
        this.stream().filter(filter).forEach(it -> it.setValue(value));
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
        this.forEach(it -> map.put(it.getKey(), it.getValue()));
        return map;
    }

    public <T extends Map<A, B>> T asMap(T map) {
        this.forEach(it -> map.put(it.getKey(), it.getValue()));
        return map;
    }

    public Iterator<Pair<A, B>> getIterator() {
        return this.iterator();
    }
}
