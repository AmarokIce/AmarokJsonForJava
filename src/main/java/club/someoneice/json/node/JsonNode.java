package club.someoneice.json.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public class JsonNode<T> {
    protected final T obj;

    /**
     * 导览: <br>
     * {@link JsonNode#getType()} - 获取 JsonNode 持有的参形。 <br>
     * {@link JsonNode#asTypeNode()} - 未知的 JsonNode 转为可知的 JsonNode 分支。 <br>
     * {@link JsonNode#typeOf(NodeType)} - 匹配 {@link JsonNode.NodeType} 相同。 <br>
     * {@link JsonNode#getObj()} - 获取 JsonNode 持有的原始数据。 <br>
     */
    public JsonNode(T obj) {
        this.obj = obj;
    }

    public enum NodeType {
        Null, String, Int, Float, Double, Boolean, Map, Array, Number, Long, Other
    }

    /**
     * 获取 JsonNode 持有的参形。通常这个方法会被对应属性的 JsonNode 覆写，因此 JsonNode 默认的匹配机制只会在类型持有时被执行一次。
     * @return JsonNode 持有的数据类型。
     */
    public NodeType getType() {
        if (obj == null || this instanceof NullNode) return NodeType.Null;
        if (obj instanceof Map || this instanceof MapNode) return NodeType.Map;
        if (obj instanceof List || this instanceof ArrayNode) return NodeType.Array;
        if (obj instanceof String) return NodeType.String;
        if (obj instanceof Boolean) return NodeType.Boolean;
        if (obj instanceof Integer) return NodeType.Int;
        if (obj instanceof Float) return NodeType.Float;
        if (obj instanceof Double) return NodeType.Double;
        if (obj instanceof Long) return NodeType.Long;
        if (obj instanceof Number) return NodeType.Number;

        return NodeType.Other;
    }

    /**
     * @return 将未知的类型重写为可知类型。
     */
    public JsonNode asTypeNode() {
        switch (this.getType()) {
            case String:
                return new StringNode((String) this.obj);
            case Int:
                return new IntegerNode((Integer) this.obj);
            case Float:
                return new FloatNode((Float) this.obj);
            case Double:
                return new DoubleNode((Double) this.obj);
            case Long:
                return new LongNode((Long) this.obj);
            case Boolean:
                return new BooleanNode((Boolean) this.obj);
            case Map:
                return new MapNode(new HashMap<>((Map) this.obj));
            case Array:
                return new ArrayNode(new ArrayList<>((List) this.obj));
        }

        return this;
    }

    /**
     * @return 拷贝数据类型为一个全新对象。返回值不会与原有值存在除内容相同外的交集。
     */
    public JsonNode<?> copy() {
        return new JsonNode<>(this.getObj());
    }

    /**
     * @param type 期望匹配的 NodeType。
     * @return 相同的 NodeType 将会返回 True。
     * @see NodeType
     */
    public boolean typeOf(NodeType type) {
        return this.getType() == type;
    }

    public T getObj() {
        return this.obj;
    }

    @Override
    public String toString() {
        return this.obj.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(this.getClass()) && this.obj.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.obj.hashCode();
    }
}
