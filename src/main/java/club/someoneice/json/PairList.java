package club.someoneice.json;

import java.util.*;
import java.util.function.Predicate;

/**
 * 持有 <code>Pair</code> 的 <code>ArrayList</code>。 <br>
 * 读写速度无法与 {@link LinkedHashMap} 相提并论，仅作为 <code>Pair</code> 的列表实现。
 * @param <A>
 * @param <B>
 */
public class PairList<A, B> extends ArrayList<Pair<A, B>> {
	private static final long serialVersionUID = 2;
	
	public final Set<Pair<A, B>> cache = new HashSet<>();

	public Pair<A, B> put(A key, B value) {
		Pair<A, B> pair = new Pair<>(key, value);
        this.add(pair);
        return pair;
    }
	
	public Pair<A, B> put(Pair<A, B> pair) {
		this.add(pair);
		return pair;
	}

    /**
     * @deprecated - Use {@link PairList#getByKey}
     */
    @Deprecated
    public Pair<A, B> get(A key) {
        return getByKey(key);
    }

    public Pair<A, B> getByKey(A key) {
    	this.checkUpdates();
    	return this.cache.stream().filter(it -> it.getKey() == key).findFirst().orElse(null);
    }

    public Pair<A, B> getByValue(B value) {
        this.checkUpdates();
        return this.stream().filter(it -> it.getValue() == value).findFirst().orElse(null);
    }

    public Pair<A, B> at(int i) {
    	return i < this.size() ? this.get(i) : null;
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
    
    private void checkUpdates() {
    	if (this.cache.size() == this.size()) {
    		return;
    	}
    	
    	this.cache.clear();
    	this.cache.addAll(this);
    }
}
