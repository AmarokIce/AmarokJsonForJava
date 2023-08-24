package club.someoneice.json.node;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public class ArrayNode extends JsonNode<List> {
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
}
