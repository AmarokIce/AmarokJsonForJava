package club.someoneice.json.processor;

import club.someoneice.json.node.ArrayNode;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;

import java.util.Iterator;

public class JsonBuilder {
    private static final String sp = "    ";

    public static String asString(JsonNode<?> node) {
        StringBuilder builder = new StringBuilder();
        if (node.getType() == JsonNode.NodeType.Array) {
            ArrayNode array = (ArrayNode) node;
            builder.append("[");
            Iterator<JsonNode<?>> itor = array.getObj().iterator();
            while (itor.hasNext()) {
                JsonNode<?> it = itor.next();
                if (it.getType() == JsonNode.NodeType.Array || it.getType() == JsonNode.NodeType.Map) {
                    builder.append(asString(it));
                } else if (it.getType() == JsonNode.NodeType.String) {
                    builder.append("\"").append(it).append("\"");
                } else builder.append(it.getObj());

                if (itor.hasNext()) builder.append(",");
            }
            builder.append("]");
        } else if (node.getType() == JsonNode.NodeType.Map) {
            MapNode map = (MapNode) node;
            builder.append("{");
            Iterator<String> itor = map.getObj().keySet().iterator();
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
            builder.append("}");
        } else if (node.getType() == JsonNode.NodeType.String) {
            builder.append("\"").append(node).append("\"");
        } else builder.append(node.getObj().toString());

        return builder.toString();
    }


    public static String prettyPrint(String node) {
        return prettyPrint(node, 0);
    }

    public static String prettyPrint(String node, int ct) {
        StringBuilder builder = new StringBuilder();
        int count = ct;
        char[] charList = node.toCharArray();
        if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
        for (char c : charList) {
            if (c == 44) {
                builder.append(c).append("\r\n");
                if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
            } else if (c == 91) {
                builder.append(c).append("\r\n");
                if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
                ++count;
            } else if (c == 93) {
                --count;
                builder.append("\r\n");
                if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
                builder.append(c);
            } else if (c == 123) {
                builder.append(c).append("\r\n");
                if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
                ++count;
            } else if (c == 125) {
                --count;
                builder.append("\r\n");
                if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
                builder.append(c);
            } else if (c == 58) {
                builder.append(c).append(" ");
            } else builder.append(c);
        }

        return builder.toString();
    }

    public static String prettyPrint(JsonNode<?> node) {
        return prettyPrint(asString(node));
    }
}
