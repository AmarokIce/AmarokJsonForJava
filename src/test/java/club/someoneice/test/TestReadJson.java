package club.someoneice.test;

import club.someoneice.json.JSON;
import club.someoneice.json.node.MapNode;

import java.io.File;

public final class TestReadJson {
    public static void main(String[] args) {
        File file = new File("./Json.json5");
        MapNode node = JSON.json5.parse(file).asMapNodeOrEmpty();
        String key = node.get("Map").asMapNodeOrEmpty().get("String").toString();
        System.out.println(key);
    }
}
