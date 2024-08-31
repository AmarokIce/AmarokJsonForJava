package club.someoneice.test;

import club.someoneice.json.JSON;
import club.someoneice.json.node.MapNode;
import club.someoneice.json.node.StringNode;
import club.someoneice.json.processor.JsonBuilder;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;

public class SpeedTest {
    public static final HashMap<String, String> map = new HashMap<>();
    public static void main(String[] args) throws IOException {
        map.put("Test1", "Test1");
        map.put("Test2", "Test2");
        map.put("Test3", "Test3");
        map.put("Test4", "Test4");
        map.put("Test5", "Test5");
        map.put("Test6", "Test6");
        map.put("Test7", "Test7");
        map.put("Test8", "Test8");
        map.put("Test9", "Test9");
        map.put("Test10", "Test10");

        Gson gson = new Gson();
        Gson prttyGson = new GsonBuilder().setPrettyPrinting().create();

        MapNode mapNode = new MapNode();
        map.forEach((key, value) -> mapNode.put(key, new StringNode(value)));

        System.out.println("Map to String: ");
        Stopwatch watch = Stopwatch.createStarted();
        gson.toJson(map);
        watch.stop();
        System.out.println("Gson: " + watch);

        watch.reset().start();
        prttyGson.toJson(map);
        watch.stop();
        System.out.println("Gson (Pretty): " + watch);

        watch.reset().start();
        JsonBuilder.asString(mapNode);
        watch.stop();
        System.out.println("AJ4J: " + watch);

        watch.reset().start();
        JsonBuilder.prettyPrint(mapNode);
        watch.stop();
        System.out.println("AJ4J (Pretty): " + watch);

        String str = JsonBuilder.prettyPrint(mapNode);
        System.out.println("String to Map");
        watch.reset().start();
        gson.fromJson(str, new TypeToken<HashMap<String, String>>() {}.getType());
        watch.stop();
        System.out.println("Gson: " + watch);

        watch.reset().start();
        JSON.json.parse(str);
        watch.stop();
        System.out.println("AJ4J: " + watch);
    }
}
