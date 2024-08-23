package club.someoneice.json.processor;

import club.someoneice.json.node.ArrayNode;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;

import java.util.Iterator;

public class JsonBuilder {
    private static final String sp = "    ";
    private static final char KEY_NEXT = 44;
    private static final char KEY_VALUE = 58;
    private static final char KEY_ARRAY_START = 91;
    private static final char KEY_ARRAY_END = 93;
    private static final char KEY_MAP_START = 123;
    private static final char KEY_MAP_END = 125;

    private JsonBuilder() {}

    public static String prettyPrint(JsonNode<?> node) {
        return prettyPrint(asString(node));
    }

    public static String prettyPrint(String node) {
        return prettyPrint(node, 0);
    }

    public static String asString(JsonNode<?> node) {
        StringBuilder builder = new StringBuilder();
        if (node.getType() == JsonNode.NodeType.Array) {
            ArrayNode array = (ArrayNode) node;

            builder.append("[");
            Iterator<JsonNode<?>> itor = array.getObj().iterator();

            whileToBuildNode(builder, itor);

            builder.append("]");
        } else if (node.getType() == JsonNode.NodeType.Map) {
            MapNode map = (MapNode) node;
            builder.append("{");
            Iterator<String> itor = map.getObj().keySet().iterator();

            whileToBuildString(builder, itor, map);

            builder.append("}");
        } else if (node.getType() == JsonNode.NodeType.String) {
            builder.append("\"").append(node).append("\"");
        } else builder.append(node.getObj().toString());

        return builder.toString();
    }

    public static String prettyPrint(String node, int ct) {
        StringBuilder builder = new StringBuilder();
        int count = ct;
        char[] charList = node.toCharArray();
        if (count > 0) for (int i = 0; i < count; i++) builder.append(sp);
        for (char c : charList) {
            count = checkAndPut(builder, count, c, KEY_NEXT, sp, KEY_ARRAY_START, KEY_ARRAY_END, KEY_MAP_START, KEY_MAP_END, KEY_VALUE);
        }

        return builder.toString();
    }

    static int checkAndPut(StringBuilder builder, int count, char c, char keyNext, String sp, char keyArrayStart, char keyArrayEnd, char keyMapStart, char keyMapEnd, char keyValue) {
        if (c == keyNext) {
            builder.append(c).append("\r\n");
            if (count > 0) for (int i = 0; i < count; i++) builder.append(sp);
        } else if (c == keyArrayStart) {
            builder.append(c).append("\r\n");
            if (count > 0) for (int i = 0; i < count; i++) builder.append(sp);
            ++count;
        } else if (c == keyArrayEnd) {
            --count;
            builder.append("\r\n");
            if (count > 0) for (int i = 0; i < count; i++) builder.append(sp);
            builder.append(c);
        } else if (c == keyMapStart) {
            builder.append(c).append("\r\n");
            if (count > 0) for (int i = 0; i < count; i++) builder.append(sp);
            ++count;
        } else if (c == keyMapEnd) {
            --count;
            builder.append("\r\n");
            if (count > 0) for (int i = 0; i < count; i++) builder.append(sp);
            builder.append(c);
        } else if (c == keyValue) {
            builder.append(c).append(" ");
        } else builder.append(c);
        return count;
    }

    private static void whileToBuildNode(StringBuilder builder, Iterator<JsonNode<?>> itor) {
        while (itor.hasNext()) {
            JsonNode<?> it = itor.next();
            createAndAppend(builder, it, itor.hasNext());
        }
    }

    private static void whileToBuildString(StringBuilder builder, Iterator<String> itor, MapNode map) {
        while (itor.hasNext()) {
            String key = itor.next();
            JsonNode<?> value = map.get(key);
            builder.append(key).append(":");
            createAndAppend(builder, value, itor.hasNext());
        }
    }

    private static void createAndAppend(StringBuilder builder, JsonNode<?> it, boolean b) {
        if (it.getType() == JsonNode.NodeType.Array || it.getType() == JsonNode.NodeType.Map) {
            builder.append(asString(it));
        } else if (it.getType() == JsonNode.NodeType.String) {
            builder.append("\"").append(it).append("\"");
        } else builder.append(it.getObj());

        if (b) builder.append(",");
    }
}
