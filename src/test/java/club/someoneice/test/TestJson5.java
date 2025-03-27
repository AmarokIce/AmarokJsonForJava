package club.someoneice.test;

import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.StringNode;
import club.someoneice.json.processor.Json5Builder;
import com.google.common.base.Stopwatch;

public final class TestJson5 {
    public static void main(String[] args) {

        Stopwatch watch = Stopwatch.createStarted();

        Json5Builder builder = new Json5Builder();
        Json5Builder.ObjectBean mapBean = builder.createObjectBean();

        mapBean.put("boolean1", new JsonNode<>(true));
        mapBean.put("boolean2", new JsonNode<>(false));
        mapBean.put("String", new JsonNode<>("This is String"));

        mapBean.enterLine();
        mapBean.addNote("This is the comment");
        mapBean.put("Array", builder
                .getArrayBean()
                .add(new JsonNode<>("This is an Array"))
        );

        mapBean.enterLine();
        mapBean.addNote("This is the comment");
        mapBean.put("Map", builder
                .getObjectBean()
                .put("String", new JsonNode<>("This is a Map"))
                .put("Array in Map", builder
                        .getArrayBean()
                        .add(new StringNode("This is an Array in a Map."))
                )
        );

        mapBean.enterLine();
        mapBean.addNote("This is the comment");

        watch.stop();
        System.out.println(builder.build());
        System.out.println(watch);

    }
}
