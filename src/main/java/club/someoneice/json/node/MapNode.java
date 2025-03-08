package club.someoneice.json.node;

import club.someoneice.json.Pair;
import club.someoneice.json.PairList;
import club.someoneice.json.api.TreeNode;

import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MapNode extends JsonNode<Map<String, JsonNode<?>>> implements Iterable<Pair<String, JsonNode<?>>>, TreeNode<Pair<String, JsonNode<?>>> {
    public MapNode(Map<String, JsonNode<?>> obj) {
        super(obj);
    }

    public MapNode() {
        super(new LinkedHashMap<>());
    }

    @Override
    public NodeType getType() {
        return NodeType.Map;
    }

    @Override
    public Map<String, JsonNode<?>> getObj() {
        return (Map<String, JsonNode<?>>) super.getObj();
    }

    @Override
    public void addChild(Pair<String, JsonNode<?>>... child) {
        Arrays.stream(child).forEach(it -> this.put(it.getKey(), it.getValue()));
    }

    public void addAll(MapNode mapNode) {
        this.obj.putAll(mapNode.getObj());
    }

    public JsonNode<?> get(String key) {
        return (JsonNode<?>) this.obj.get(key);
    }

    public JsonNode<?> put(String key, JsonNode<?> value) {
        return this.obj.put(key, value);
    }

    public boolean has(String key) {
        return this.obj.containsKey(key);
    }

    public JsonNode<?> put(Pair<String, JsonNode<?>> pair) {
        return this.put(pair.getKey(), pair.getValue());
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

    public PairList<String, JsonNode<?>> asPairList() {
        PairList<String, JsonNode<?>> pairList = new PairList<>();
        this.getObj().forEach(pairList::put);
        return pairList;
    }

    public Stream<Pair<String, JsonNode<?>>> stream() {
        return this.asPairList().stream();
    }

    @Override
    public Iterator<Pair<String, JsonNode<?>>> iterator() {
        return this.asPairList().iterator();
    }

    @Override
    public MapNode asTypeNode() {
        return this;
    }

    @Override
    public MapNode copy() {
        return new MapNode(new HashMap(this.obj));
    }

    public MapNode copy(Map<String, JsonNode<?>> map) {
        map.putAll(this.obj);
        return new MapNode(map);
    }

    public static MapNode fromNodeMap(Map<String, JsonNode<?>> map) {
        return new MapNode(map);
    }

    public static MapNode fromMapNonnull(Map<String, Object> map) {
        MapNode node = new MapNode();
        map.entrySet().stream()
                .map(it -> new Pair<>(it.getKey(), JsonNode.asJsonNodeOrEmpty(it.getValue())))
                .filter(it -> it.getValue().nonNull())
                .forEach(it -> node.put(it.getKey(), it.getValue()));
        return node;
    }

    public static MapNode fromMap(Map<String, Object> map) {
        MapNode node = new MapNode();
        map.entrySet().stream()
                .map(it -> new Pair<>(it.getKey(), JsonNode.asJsonNodeOrEmpty(it.getValue())))
                .forEach(it -> node.put(it.getKey(), it.getValue()));
        return node;
    }

    public MapNode put(String name, String str) {
        this.put(name, new StringNode(str));
        return this;
    }

    public MapNode put(String name, int i) {
        this.put(name, new IntegerNode(i));
        return this;
    }

    public MapNode put(String name, long l) {
        this.put(name, new LongNode(l));
        return this;
    }

    public MapNode put(String name, double d) {
        this.put(name, new DoubleNode(d));
        return this;
    }

    public MapNode put(String name, float f) {
        this.put(name, new FloatNode(f));
        return this;
    }

    public MapNode put(String name, boolean b) {
        this.put(name, new BooleanNode(b));
        return this;
    }
}
