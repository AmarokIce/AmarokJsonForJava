package club.someoneice.test;

import club.someoneice.json.JSON;
import club.someoneice.json.node.ArrayNode;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;
import club.someoneice.json.node.StringNode;
import club.someoneice.json.processor.Json5Builder;
import com.google.common.base.Stopwatch;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TestJson {
    static JSON json = JSON.json5;

    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
        File file = new File("./Json.json5");

        Stopwatch watch = Stopwatch.createStarted();

        if (!file.exists() || !file.isFile()) file.createNewFile();
        Json5Builder builder = new Json5Builder();
        Json5Builder.ObjectBean mapBean = builder.getObjectBean();
        Json5Builder.ArrayBean arrayBeanInMap = builder.getArrayBean();
        Json5Builder.ObjectBean mapBeanInMap = builder.getObjectBean();

        mapBean.put("boolean1", new JsonNode<>(true));
        mapBean.put("boolean2", new JsonNode<>(false));
        mapBean.put("String", new JsonNode<>("This is String"));

        arrayBeanInMap.add(new JsonNode<>("This is an Array"));
        mapBeanInMap.put("String", new JsonNode<>("This is a Map"));

        mapBean.enterLine();

        mapBean.addBean("Array", arrayBeanInMap);
        mapBean.addBean("Map", mapBeanInMap);

        mapBean.enterLine();

        mapBean.addNote("This is the comment");
        builder.put(mapBean);
        Files.write(builder.build().getBytes(), file);
        System.out.println(watch);
        watch.stop();
    }
}
