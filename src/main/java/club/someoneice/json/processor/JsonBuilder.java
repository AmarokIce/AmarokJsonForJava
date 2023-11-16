package club.someoneice.json.processor;

import club.someoneice.json.node.ArrayNode;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;

import java.util.Iterator;

public class JsonBuilder {
    private static final String sp = "    ";

    private static void whileToBuildNode(StringBuilder builder, Iterator<JsonNode<?>> itor) {
        while (itor.hasNext()) {
            JsonNode<?> it = itor.next();
            if (it.getType() == JsonNode.NodeType.Array || it.getType() == JsonNode.NodeType.Map) {
                builder.append(asString(it));
            } else if (it.getType() == JsonNode.NodeType.String) {
                builder.append("\"").append(it).append("\"");
            } else builder.append(it.getObj());

            if (itor.hasNext()) builder.append(",");
        }
    }

    private static void whileToBuildString(StringBuilder builder, Iterator<String> itor, MapNode map) {
        while (itor.hasNext()) {
            String key = itor.next();
            JsonNode<?> value = map.get(key);
            builder.append(key).append(":");
            if (value.getType() == JsonNode.NodeType.Array || value.getType() == JsonNode.NodeType.Map) {
                builder.append(asString(value));
            } else if (value.getType() == JsonNode.NodeType.String) {
                builder.append("\"").append(value).append("\"");
            } else builder.append(value.getObj());

            if (itor.hasNext()) builder.append(",");
        }
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


    public static String prettyPrint(String node) {
        return prettyPrint(node, 0);
    }

    private static final char KEY_NEXT = 44;
    private static final char KEY_VALUE = 58;
    private static final char KEY_ARRAY_START = 91;
    private static final char KEY_ARRAY_END = 93;
    private static final char KEY_MAP_START = 123;
    private static final char KEY_MAP_END = 125;

    public static String prettyPrint(String node, int ct) {
        StringBuilder builder = new StringBuilder();
        int count = ct;
        char[] charList = node.toCharArray();
        if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
        for (char c : charList) {
            if (c == KEY_NEXT) {
                builder.append(c).append("\r\n");
                if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
            } else if (c == KEY_ARRAY_START) {
                builder.append(c).append("\r\n");
                if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
                ++count;
            } else if (c == KEY_ARRAY_END) {
                --count;
                builder.append("\r\n");
                if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
                builder.append(c);
            } else if (c == KEY_MAP_START) {
                builder.append(c).append("\r\n");
                if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
                ++count;
            } else if (c == KEY_MAP_END) {
                --count;
                builder.append("\r\n");
                if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
                builder.append(c);
            } else if (c == KEY_VALUE) {
                builder.append(c).append(" ");
            } else builder.append(c);
        }

        return builder.toString();
    }

    public static String prettyPrint(JsonNode<?> node) {
        return prettyPrint(asString(node));
    }
}
