package club.someoneice.json.processor;

import club.someoneice.json.JsonParser;
import club.someoneice.json.node.ArrayNode;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;

import java.util.Iterator;

public class JsonBuilder {
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
        if (count > 0) for (int i = 0; i < count; i++) builder.append(JsonParser.SP);
        for (char c : charList) {
            count = checkAndPut(builder, count, c);
        }

        return builder.toString();
    }

    static int checkAndPut(StringBuilder builder, int count, char c) {
        if (c == JsonParser.KEY_NEXT) {
            builder.append(c).append("\r\n");
            if (count > 0) for (int i = 0; i < count; i++) builder.append(JsonParser.SP);
        } else if (c == JsonParser.KEY_ARRAY_START) {
            builder.append(c).append("\r\n");
            if (count > 0) for (int i = 0; i < count; i++) builder.append(JsonParser.SP);
            ++count;
        } else if (c == JsonParser.KEY_ARRAY_END) {
            --count;
            builder.append("\r\n");
            if (count > 0) for (int i = 0; i < count; i++) builder.append(JsonParser.SP);
            builder.append(c);
        } else if (c == JsonParser.KEY_MAP_START) {
            builder.append(c).append("\r\n");
            if (count > 0) for (int i = 0; i < count; i++) builder.append(JsonParser.SP);
            ++count;
        } else if (c == JsonParser.KEY_MAP_END) {
            --count;
            builder.append("\r\n");
            if (count > 0) for (int i = 0; i < count; i++) builder.append(JsonParser.SP);
            builder.append(c);
        } else if (c == JsonParser.KEY_VALUE) {
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
