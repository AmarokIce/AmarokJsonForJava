package club.someoneice.test;

import club.someoneice.json.JSON;
import club.someoneice.json.node.ArrayNode;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;
import club.someoneice.json.node.StringNode;
import club.someoneice.json.processor.Json5Builder;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TestJson {
    static JSON json = JSON.json5;

    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
        File file = new File("./Json.json5");

        if (!file.exists() || !file.isFile()) file.createNewFile();
        Json5Builder builder = new Json5Builder();
        Json5Builder.ObjectBean mapBean = builder.getObjectBean();

        mapBean.put("test", new JsonNode<>(true));
        builder.put(mapBean);
        Files.write(builder.build().getBytes(), file);
    }
}
