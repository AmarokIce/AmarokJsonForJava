package club.someoneice.json.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public class JsonNode<T> {
    public static final AbstractNode NULL = new AbstractNode();
    protected final T obj;

    public JsonNode(T obj) {
        this.obj = obj;
    }

    public enum NodeType {
        Null, String, Int, Float, Double, Boolean, Map, Array, Number, Long, Other
    }

    public NodeType getType() {
        if (obj == null || this instanceof AbstractNode)        return NodeType.Null;
        if (obj instanceof Map || this instanceof MapNode)      return NodeType.Map;
        if (obj instanceof List || this instanceof ArrayNode)   return NodeType.Array;
        if (obj instanceof String)                              return NodeType.String;
        if (obj instanceof Boolean)                             return NodeType.Boolean;
        if (obj instanceof Integer)                             return NodeType.Int;
        if (obj instanceof Float)                               return NodeType.Float;
        if (obj instanceof Double)                              return NodeType.Double;
        if (obj instanceof Long)                                return NodeType.Long;
        if (obj instanceof Number)                              return NodeType.Number;

        return NodeType.Other;
    }

    public T getObj() {
        return this.obj;
    }

    public JsonNode asTypeNode() {
        switch (this.getType()) {
            case String:    return new StringNode((String) this.obj);
            case Int:       return new IntegerNode((Integer) this.obj);
            case Float:     return new FloatNode((Float) this.obj);
            case Double:    return new DoubleNode((Double) this.obj);
            case Long:      return new LongNode((Long) this.obj);
            case Boolean:   return new BooleanNode((Boolean) this.obj);
            case Map:       return new MapNode(new HashMap<>((Map) this.obj));
            case Array:     return new ArrayNode(new ArrayList<>((List) this.obj));
        }

        return this;
    }

    @Override
    public String toString() {
        return this.obj.toString();
    }
}
