package club.someoneice.json.node;

import club.someoneice.json.Pair;
import club.someoneice.json.PairList;
import club.someoneice.json.api.TreeNode;

import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public class MapNode extends JsonNode<Map> implements Iterable<Pair<String, JsonNode<?>>>, TreeNode<Pair<String, JsonNode<?>>> {
    public MapNode(Map<String, ? extends JsonNode<?>> obj) {
        super(obj);
    }

    public MapNode() {
        super(new HashMap());
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

    public void put(String key, JsonNode<?> any) {
        this.obj.put(key, any);
    }

    public boolean has(String key) {
        return this.obj.containsKey(key);
    }

    public void put(Pair<String, JsonNode<?>> pair) {
        this.put(pair.getKey(), pair.getValue());
    }

    public void clear() {
        this.obj.clear();
    }

    public boolean isEmpty() {
        return this.obj.isEmpty();
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
}
