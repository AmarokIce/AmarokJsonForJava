package club.someoneice.json.node;

import club.someoneice.json.api.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ArrayNode extends JsonNode<List<JsonNode<?>>> implements Iterable<JsonNode<?>>, TreeNode<JsonNode<?>> {
    public ArrayNode(List<JsonNode<?>> obj) {
        super(obj);
    }

    public ArrayNode() {
        super(new ArrayList());
    }

    @Override
    public List<JsonNode<?>> getObj() {
        return (List<JsonNode<?>>) super.getObj();
    }

    @Override
    public NodeType getType() {
        return NodeType.Array;
    }

    @Override
    public void addChild(JsonNode<?>... child) {
        Arrays.stream(child).forEach(this::add);
    }

    public JsonNode<?> add(JsonNode<?> obj) {
        this.obj.add(obj);
        return obj;
    }

    public void addAll(List<JsonNode<?>> list) {
        this.obj.addAll(list);
    }

    public JsonNode<?> get(int i) {
        return (JsonNode<?>) this.obj.get(i);
    }

    public void clear() {
        this.obj.clear();
    }

    public boolean isEmpty() {
        return this.obj.isEmpty();
    }

    public boolean isNotEmpty() {
        return !this.obj.isEmpty();
    }

    public Stream<JsonNode<?>> stream() {
        return this.obj.stream();
    }

    @Override
    public Iterator<JsonNode<?>> iterator() {
        return this.obj.iterator();
    }

    @Override
    public ArrayNode asTypeNode() {
        return this;
    }

    @Override
    public ArrayNode copy() {
        return new ArrayNode(new ArrayList<>(this.obj));
    }

    public ArrayNode copy(ArrayNode node) {
        node.addAll(this.getObj());
        return node;
    }

    public ArrayNode copy(List<JsonNode<?>> list) {
        list.addAll(this.obj);
        return new ArrayNode(list);
    }

    public ArrayNode add(String str) {
        this.add(new StringNode(str));
        return this;
    }

    public ArrayNode add(int i) {
        this.add(new IntegerNode(i));
        return this;
    }

    public ArrayNode add(long l) {
        this.add(new LongNode(l));
        return this;
    }

    public ArrayNode add(double d) {
        this.add(new DoubleNode(d));
        return this;
    }

    public ArrayNode add(float f) {
        this.add(new FloatNode(f));
        return this;
    }

    public ArrayNode add(boolean b) {
        this.add(new BooleanNode(b));
        return this;
    }
}
