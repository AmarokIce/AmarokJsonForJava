package club.someoneice.json;

import club.someoneice.json.node.ArrayNode;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

@SuppressWarnings("unused")
public final class JSON {
    private final boolean isJson5;

    public static JSON json  = new JSON(false);
    public static JSON json5 = new JSON(true);

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

    public <T> T tryPullAsClass(Class<? extends T> clazz, String str) throws InstantiationException, IllegalAccessException {
        MapNode jsonMap = tryPullObjectOrEmpty(this.parse(str));
        return tryPullAsClass(clazz, jsonMap);
    }

    public <T> T tryPullAsClass(Class<? extends T> clazz, File file) throws InstantiationException, IllegalAccessException {
        MapNode jsonMap = tryPullObjectOrEmpty(this.parse(file));
        return tryPullAsClass(clazz, jsonMap);
    }

    public <T> T tryPullAsClass(Class<? extends T> clazz, MapNode jsonMap) throws InstantiationException, IllegalAccessException{
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

    public <T> List<T> tryPullAsClassList(Class<? extends T> clazz, File file) throws InstantiationException, IllegalAccessException {
        JsonNode<?> jsonNode = this.parse(file);
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
