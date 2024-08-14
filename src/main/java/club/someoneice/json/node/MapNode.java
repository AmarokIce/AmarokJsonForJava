package club.someoneice.json.node;

import club.someoneice.json.Pair;
import club.someoneice.json.api.TreeNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public class MapNode extends JsonNode<Map> implements TreeNode<Pair<String, JsonNode<?>>> {
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
        Arrays.stream(child).forEach(it -> {
            this.put(it.getKey(), it.getValue());
        });
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
}
