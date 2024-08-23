package club.someoneice.json;

import club.someoneice.json.node.*;

import java.util.List;
import java.util.Map;

/**
 * 语法糖集，拒绝糖浆，从你做起。
 */
@SuppressWarnings("unused")
public class Nodes {
    private Nodes() {}

    public static ArrayNode createArrayNode() {
        return new ArrayNode();
    }

    public static MapNode createMapNode() {
        return new MapNode();
    }

    /* Known type */
    public static StringNode asString(String str) {
        return new StringNode(str);
    }

    public static IntegerNode asInt(Integer i) {
        return new IntegerNode(i);
    }

    public static FloatNode asFloat(float f) {
        return new FloatNode(f);
    }

    public static DoubleNode asDouble(double d) {
        return new DoubleNode(d);
    }

    public static BooleanNode asBoolean(boolean b) {
        return new BooleanNode(b);
    }

    public static ArrayNode asArray(List<JsonNode<?>> a) {
        return new ArrayNode(a);
    }

    public static MapNode asMap(Map<String, JsonNode<?>> o) {
        return new MapNode(o);
    }

    /* Auto sugar */
    public static StringNode as(String str) {
        return new StringNode(str);
    }

    public static IntegerNode as(Integer i) {
        return new IntegerNode(i);
    }

    public static FloatNode as(float f) {
        return new FloatNode(f);
    }

    public static DoubleNode as(double d) {
        return new DoubleNode(d);
    }

    public static BooleanNode as(boolean b) {
        return new BooleanNode(b);
    }

    public static ArrayNode as(List<JsonNode<?>> a) {
        return new ArrayNode(a);
    }

    public static MapNode as(Map<String, JsonNode<?>> o) {
        return new MapNode(o);
    }
}
