package club.someoneice.json.node;

import club.someoneice.json.api.TreeNode;

import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public class ArrayNode extends JsonNode<List> implements Iterable<JsonNode<?>>, TreeNode<JsonNode<?>> {
    public ArrayNode(List<? extends JsonNode<?>> obj) {
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

    public void add(JsonNode<?> any) {
        this.obj.add(any);
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

    public ArrayNode copy(List<JsonNode<?>> list) {
        list.addAll(this.obj);
        return new ArrayNode(list);
    }
}
