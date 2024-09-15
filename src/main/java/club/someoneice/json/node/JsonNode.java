package club.someoneice.json.node;

import club.someoneice.json.api.NodeLike;
import club.someoneice.json.api.exception.NodeCastException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public class JsonNode<T> implements NodeLike {
    protected final T obj;

    public JsonNode(T obj) {
        this.obj = obj;
    }

    public enum NodeType {
        Null, String, Int, Float, Double, Boolean, Map, Array, Number, Long, Other
    }

    /**
     * 获取 JsonNode 持有的参形。通常这个方法会被对应属性的 <code>JsonNode</code> 覆写，因此 <code>JsonNode</code> 默认的匹配机制只会在类型持有时被执行一次。
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
            case Null:
            	return NullNode.INSTANCE;
            case Number:
            case Other:
            	return this;
        }

        return this;
    }

    /**
     * 将未知的类型重写为目标类型。当类型不符时会抛出 {@link NodeCastException} 错误以警告。
     * @param type 将要检查的目标类型。
     * @param message 无法匹配时的错误类型。
     * @return 持有目标类型的 <code>JsonNode</code>。
     * @throws NodeCastException 当持有类型与目标类型不符合时，抛出错误。
     */
    public JsonNode asTypeNodeOrThrow(NodeType type, String message) {
        JsonNode node = this.asTypeNode();
        if (!node.typeOf(type)) {
            throw new NodeCastException(message);
        }
        return node;
    }

    /**
     * 将未知的类型重写为目标类型。当类型不符时会抛出 <code>NodeCastException</code> 错误以警告。
     * @param type 将要检查的目标类型。
     * @return 持有目标类型的 <code>JsonNode</code>。
     * @throws NodeCastException 当持有类型与目标类型不符合时，抛出错误。
     */
    public JsonNode asTypeNodeOrThrow(NodeType type) {
        return this.asTypeNodeOrThrow(type, "");
    }

    /**
     * 将未知的类型重写为目标类型。当类型不符时会尝试执行 <code>Supplier</code> 取得补偿。
     * @param type 将要检查的目标类型。
     * @param function 在目标不符时，将会调用的补偿器。
     * @return 转型后或补偿后的数据。
     */
    public <N extends JsonNode> N asTypeOrElse(NodeType type, Supplier<N> function) {
        JsonNode node = this.asTypeNode();
        return this.typeOf(type) ? (N) node : function.get();
    }

    /**
     * 将未知的类型重写为目标类型。当类型不符时会提供 <code>NullNode</code>。
     * @param type 将要检查的目标类型。
     * @return 转型后的 <code>JsonNode</code>，亦或是 <code>NullNode</code>。
     */
    public JsonNode asTypeOrNull(NodeType type) {
        JsonNode node = this.asTypeNode();
        return this.typeOf(type) ? node : NullNode.INSTANCE;
    }

    /**
     * 将 <code>JsonNode</code> 转为 <code>MapNode</code>，或提供空的 <code>MapNode</code>。
     * @return 转型后的，亦或是空的 <code>MapNode</code>。
     */
    public MapNode asMapNodeOrEmpty() {
        JsonNode node = this.asTypeNode();
        return node.typeOf(NodeType.Map) ? (MapNode) node : new MapNode();
    }

    /**
     * 将 <code>JsonNode</code> 转为 <code>ArrayNode</code>，或提供空的 <code>ArrayNode</code>。
     * @return 转型后的，亦或是空的 <code>ArrayNode</code>。
     */
    public ArrayNode asArrayNodeOrEmpty() {
        JsonNode node = this.asTypeNode();
        return node.typeOf(NodeType.Array) ? (ArrayNode) node : new ArrayNode();
    }

    /**
     * @return 拷贝数据类型为一个全新对象。返回值不会与原有值存在除内容相同外的交集。
     */
    public JsonNode<?> copy() {
        return new JsonNode<>(this.getObj());
    }

    /**
     * @param type 期望匹配的 <code>NodeType</code>。
     * @return 相同的 <code>NodeType</code> 将会返回 <code>True</code>。
     * @see NodeType
     */
    public boolean typeOf(NodeType type) {
        return this.getType() == type;
    }

    public boolean isNull() {
        return this.typeOf(NodeType.Null) || this == NullNode.INSTANCE;
    }

    public boolean nonNull() {
        return !isNull();
    }

    /**
     * @return JsonNode 持有的真实数据。
     */
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

    @Override
    public JsonNode asJsonNode() {
        return this.asTypeNode();
    }

    public static JsonNode asJsonNodeOrEmpty(Object obj) {
        if (obj instanceof NodeLike) {
            return ((NodeLike) obj).asJsonNode();
        }

        JsonNode node = new JsonNode(obj);
        return node.getType() == NodeType.Other ? NullNode.INSTANCE : node;
    }
}
