package club.someoneice.test;

import club.someoneice.json.JSON;
import club.someoneice.json.node.ArrayNode;
import club.someoneice.json.node.MapNode;
import club.someoneice.json.node.StringNode;
import club.someoneice.json.processor.Json5Builder;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class TestJson {
    static JSON json = JSON.json5;

    public static void main(String[] args) throws IOException {
        File file = new File("./Json.json5");
        MapNode node = (MapNode) json.parse(file);
        System.out.println(node.get("test").getObj());
        System.out.println(((MapNode) ((ArrayNode) node.get("test2")).getObj().get(2)).get("TestInList"));

        File file1 = new File("./ClassTest.json");
        TestClass test = json.tryPullAsClass(TestClass.class, file1);
        if (test == null) System.out.println("False! The class is null!");
        else {
            System.out.println(test.test);
            System.out.println(test.test2);
        }

        File file2 = new File("./JsonWrite.json5");
        if (!file2.exists() || !file2.isFile()) file2.createNewFile();
        Json5Builder builder = new Json5Builder();
        Json5Builder.ArrayBean arrayBean = builder.getArrayBean();
        Json5Builder.ObjectBean mapBean = builder.getObjectBean();

        arrayBean.add(new StringNode("Test"));
        arrayBean.enterLine();
        arrayBean.add(new StringNode("This is another Test"));

        mapBean.put("test", new StringNode("Test"));
        mapBean.addNote("This is a test note");
        mapBean.put("newTest", new StringNode("This is another Test"));

        MapNode map = new MapNode();
        map.put("testInMap", new StringNode("mapTest"));
        mapBean.put("nodeMap", map);

        arrayBean.addBean(mapBean);

        builder.put(arrayBean);
        String data = builder.build();
        System.out.println(data);

        OutputStream outputStream = Files.newOutputStream(file2.toPath());
        outputStream.write(data.getBytes());
        outputStream.close();

    }

    public static class TestClass {
        public int test;
        public int test2;

        public TestClass() {}
    }

}
