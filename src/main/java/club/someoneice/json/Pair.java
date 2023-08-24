package club.someoneice.json;

public class Pair<A, B> {
    private final A key;
    private final B value;

    public Pair(A key, B value) {
        this.key = key;
        this.value = value;
    }

    public A getKey() {
        return this.key;
    }

    public B getValue() {
        return this.value;
    }
}
