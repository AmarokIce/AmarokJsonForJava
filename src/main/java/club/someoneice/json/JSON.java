package club.someoneice.json;

import club.someoneice.json.api.JsonVar;
import club.someoneice.json.node.*;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public final class JSON {
    public static JSON json = new JSON(false);
    public static JSON json5 = new JSON(true);

    private final boolean isJson5;

    private JSON(boolean isJson5) {
        this.isJson5 = isJson5;
    }

    /**
     * 从文件读取并解析为 JsonNode。
     * @param file 将要解析的文件。
     * @return 处理后的未知类型的 JsonNode。
     */
    public JsonNode<?> parse(File file) {
        JsonParser processor = new JsonParser(file, this.isJson5);
        return processor.getNodeWithTypeUnknown();
    }

    /**
     * 从输入流读取并解析为 JsonNode。内建关闭流。如 <code>shouldClose</code> 为 <code>true</code> ,请内联创建。
     * @param inputStream 将要解析的输入流。
     * @param shouldClose 决定流是否需要内部自动关闭。
     * @return 处理后的未知类型的 JsonNode。
     */
    public JsonNode<?> parse(InputStream inputStream, boolean shouldClose) {
        JsonParser processor = new JsonParser(inputStream, shouldClose, this.isJson5);
        return processor.getNodeWithTypeUnknown();
    }

    /**
     * 从字符串解析为 JsonNode。
     * @param str 将要解析的字符串。
     * @return 处理后的未知类型的 JsonNode。
     */
    public JsonNode<?> parse(String str) {
        JsonParser processor = new JsonParser(str);
        return processor.getNodeWithTypeUnknown();
    }

    /**
     * @deprecated Use {@link JsonNode#asArrayNodeOrEmpty()}
     * @param jsonNode 需要转型为 ArrayNode 的 JsonNode。
     * @return 符合时提供确定的 ArrayNode，否则提供空的 ArrayNode。
     */
    @Deprecated
    public ArrayNode tryPullArrayOrEmpty(JsonNode<?> jsonNode) {
        if (jsonNode.getType() == JsonNode.NodeType.Array) return (ArrayNode) jsonNode;
        else return new ArrayNode(new ArrayList<>());
    }

    /**
     * @deprecated Use {@link JsonNode#asArrayNodeOrEmpty()}
     * @param raw 需要转型为 ArrayNode 的字符串，自动解析后转义。
     * @return 符合时提供确定的 ArrayNode，否则提供空的 ArrayNode。
     */
    @Deprecated
    public ArrayNode tryPullArrayOrEmpty(String raw) {
        JsonNode<?> jsonNode = this.parse(raw);
        return this.tryPullArrayOrEmpty(jsonNode);
    }

    /**
     * @deprecated Use {@link JsonNode#asMapNodeOrEmpty()}
     * @param jsonNode 需要转型为 MapNode 的 JsonNode。
     * @return 符合时提供确定的 MapNode，否则提供空的 MapNode。
     */
    @Deprecated
    public MapNode tryPullObjectOrEmpty(JsonNode<?> jsonNode) {
        if (jsonNode.getType() == JsonNode.NodeType.Map) return (MapNode) jsonNode;
        else return new MapNode(new HashMap<>());
    }

    /**
     * @deprecated Use {@link JsonNode#asMapNodeOrEmpty()}
     * @param raw 需要转型为 MapNode 的字符串，自动解析后转义。
     * @return 符合时提供确定的 MapNode，否则提供空的 MapNode。
     */
    @Deprecated
    public MapNode tryPullObjectOrEmpty(String raw) {
        JsonNode<?> jsonNode = this.parse(raw);
        return this.tryPullObjectOrEmpty(jsonNode);
    }

    /* Damage Zoom : Don't use these on Java 12+ */
    
    // TODO : Fix the instances.

    public <T> T tryPullAsClass(Class<? extends T> clazz, String str) throws InstantiationException, IllegalAccessException {
        MapNode jsonMap = tryPullObjectOrEmpty(this.parse(str));
        return tryPullAsClass(clazz, jsonMap);
    }

    public <T> T tryPullAsClass(Class<? extends T> clazz, File file) throws InstantiationException, IllegalAccessException {
        MapNode jsonMap = tryPullObjectOrEmpty(this.parse(file));
        return tryPullAsClass(clazz, jsonMap);
    }

    public <T> T tryPullAsClass(Class<T> clazz, MapNode jsonMap) throws InstantiationException, IllegalAccessException {
        T targetClass = clazz.newInstance();
        Field[] fields = targetClass.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            field.set(targetClass, jsonMap.get(name).getObj());
        }

        return targetClass;
    }

    public <T> MapNode tryPushFromClass(T clazz) throws IllegalAccessException {
        final MapNode root = new MapNode();
        final Field[] fields = clazz.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            boolean flag = field.isAnnotationPresent(JsonVar.class);
            if (!flag && Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            final String name = flag ? field.getAnnotation(JsonVar.class).value() : field.getName();
            final Object obj = field.get(clazz);
            final String clazzName = obj.getClass().getSimpleName();
            final JsonNode<?> node;
            if (obj instanceof String) {
                node = new StringNode((String) obj);
            } else if (obj instanceof Integer) {
                node = new IntegerNode((int) obj);
            } else if (obj instanceof Number) {
                node = new DoubleNode((double) obj);
            } else if (obj instanceof Boolean) {
                node = new BooleanNode((boolean) obj);
            } else continue;

            root.put(name, node);
        }

        return root;
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
