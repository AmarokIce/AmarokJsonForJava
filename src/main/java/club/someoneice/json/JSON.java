package club.someoneice.json;

import club.someoneice.json.node.ArrayNode;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public final class JSON {
    public static JSON json = new JSON(false);
    public static JSON json5 = new JSON(true);

    private final boolean isJson5;

    /**
     * 导览: <br>
     * {@link JSON#parse(File)} 从文件读取并解析为 JsonNode。 <br>
     * {@link JSON#parse(InputStream, boolean)} 从输入流读取并解析为 JsonNode。内建关闭流，因此请内联创建。 <br>
     * {@link JSON#parse(String)}  从字符串解析为 JsonNode。 <br>
     * {@link JSON#tryPullArrayOrEmpty(String)} 从字符串解析为 ArrayNode，或返回空的 ArrayNode。 <br>
     * {@link JSON#tryPullArrayOrEmpty(JsonNode)} 从未知类型解析为 ArrayNode，或返回空的 ArrayNode。 <br>
     * {@link JSON#tryPullObjectOrEmpty(String)}}} 从字符串解析为 MapNode，或返回空的 MapNode。 <br>
     * {@link JSON#tryPullObjectOrEmpty(JsonNode)} 从未知类型解析为 MapNode，或返回空的 MapNode。 <br>
     */
    private JSON(boolean isJson5) {
        this.isJson5 = isJson5;
    }

    public JsonNode<?> parse(File file) {
        JsonParser processor = new JsonParser(file, this.isJson5);
        return processor.getNodeWithTypeUnknown();
    }

    public JsonNode<?> parse(InputStream inputStream, boolean shouldClose) {
        JsonParser processor = new JsonParser(inputStream, shouldClose, this.isJson5);
        return processor.getNodeWithTypeUnknown();
    }

    public JsonNode<?> parse(String str) {
        JsonParser processor = new JsonParser(str);
        return processor.getNodeWithTypeUnknown();
    }

    public ArrayNode tryPullArrayOrEmpty(JsonNode<?> jsonNode) {
        if (jsonNode.getType() == JsonNode.NodeType.Array) return (ArrayNode) jsonNode;
        else return new ArrayNode(new ArrayList<>());
    }

    public ArrayNode tryPullArrayOrEmpty(String raw) {
        JsonNode<?> jsonNode = this.parse(raw);
        return this.tryPullArrayOrEmpty(jsonNode);
    }

    public MapNode tryPullObjectOrEmpty(JsonNode<?> jsonNode) {
        if (jsonNode.getType() == JsonNode.NodeType.Map) return (MapNode) jsonNode;
        else return new MapNode(new HashMap<>());
    }

    public MapNode tryPullObjectOrEmpty(String raw) {
        JsonNode<?> jsonNode = this.parse(raw);
        return this.tryPullObjectOrEmpty(jsonNode);
    }

    /* Damage Zoom : Don't use these on Java 12+ */

    public <T> T tryPullAsClass(Class<? extends T> clazz, String str) throws InstantiationException, IllegalAccessException {
        MapNode jsonMap = tryPullObjectOrEmpty(this.parse(str));
        return tryPullAsClass(clazz, jsonMap);
    }

    public <T> T tryPullAsClass(Class<? extends T> clazz, File file) throws InstantiationException, IllegalAccessException {
        MapNode jsonMap = tryPullObjectOrEmpty(this.parse(file));
        return tryPullAsClass(clazz, jsonMap);
    }

    public <T> T tryPullAsClass(Class<? extends T> clazz, MapNode jsonMap) throws InstantiationException, IllegalAccessException {
        T targetClass = clazz.newInstance();
        Field[] fields = targetClass.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            field.set(targetClass, jsonMap.get(name).getObj());
        }

        return targetClass;
    }

    public <T> List<T> tryPullAsClassList(Class<? extends T> clazz, String str) throws InstantiationException, IllegalAccessException {
        JsonNode<?> jsonNode = this.parse(str);
        return createClassList(clazz, jsonNode);
    }

    public <T> List<T> tryPullAsClassList(Class<? extends T> clazz, File file) throws InstantiationException, IllegalAccessException {
        JsonNode<?> jsonNode = this.parse(file);
        return createClassList(clazz, jsonNode);
    }

    private <T> List<T> createClassList(Class<? extends T> clazz, JsonNode<?> jsonNode) throws InstantiationException, IllegalAccessException {
        if (jsonNode.getType() == JsonNode.NodeType.Map)
            return Collections.singletonList(tryPullAsClass(clazz, (MapNode) jsonNode));
        else if (jsonNode.getType() != JsonNode.NodeType.Array)
            return null;

        ArrayNode jsonList = tryPullArrayOrEmpty(jsonNode);

        List<T> clazzList = new ArrayList<>();
        for (JsonNode<?> node : jsonList.getObj()) {
            clazzList.add(this.tryPullAsClass(clazz, tryPullObjectOrEmpty(node)));
        }

        return clazzList;
    }
}
