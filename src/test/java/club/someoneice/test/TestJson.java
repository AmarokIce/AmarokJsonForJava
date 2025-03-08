package club.someoneice.test;

import club.someoneice.json.node.ArrayNode;
import club.someoneice.json.node.MapNode;
import club.someoneice.json.node.StringNode;
import club.someoneice.json.processor.JsonBuilder;

public class TestJson {
    public static void main(String[] args) {
        MapNode map = new MapNode();

        ArrayNode node = new ArrayNode();
        node.add(new StringNode("\"test:test\""));

        map.put("array", node);
        map.put("test", new StringNode("test:test"));

        String out = JsonBuilder.prettyPrint(map);
        System.out.println(out);
    }
}
