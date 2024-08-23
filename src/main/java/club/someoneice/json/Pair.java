package club.someoneice.json;

public class Pair<A, B> {
    protected A key;
    protected B value;

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

    public void setKey(A key) {
        this.key = key;
    }

    public void setValue(B value) {
        this.value = value;
    }
}
