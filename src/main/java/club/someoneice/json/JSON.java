package club.someoneice.json;

import club.someoneice.json.node.ArrayNode;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

// @SuppressWarnings("all")
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
        JsonParser processor = new JsonParser(inputStream, shouldClose);
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

    public <T> T tryPullAsClass(Class<? extends T> clazz, String str) {
        try {
            JsonNode<?> jsonNode = this.parse(str);
            if (jsonNode.getType() != JsonNode.NodeType.Map) return null;
            MapNode jsonMap = (MapNode) jsonNode;
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                field.set(name, jsonMap.get(name));
            }

            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
            return null;
        }
    }

    public <T> T tryPullAsClass(Class<? extends T> clazz, File file) {
        try {
            JsonNode<?> jsonNode = this.parse(file);
            if (jsonNode.getType() != JsonNode.NodeType.Map) return null;
            MapNode jsonMap = (MapNode) jsonNode;

            T t = clazz.newInstance();
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                field.set(t, jsonMap.get(name).getObj());
            }

            return t;
        } catch (InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public <T> T tryPullAsClass(Class<? extends T> clazz, MapNode jsonMap) {
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                field.set(name, jsonMap.get(name));
            }

            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
            return null;
        }
    }

    public <T> List<T> tryPullAsClassList(Class<? extends T> clazz, String str) {
        try {
            JsonNode<?> jsonNode = this.parse(str);
            if (jsonNode.getType() == JsonNode.NodeType.Map) return Collections.singletonList(tryPullAsClass(clazz, (MapNode) jsonNode));
            else if (jsonNode.getType() != JsonNode.NodeType.Array) return null;
            ArrayNode jsonList = (ArrayNode) jsonNode;


            List<T> clazzList = new ArrayList<>();
            for (JsonNode<?> node : jsonList.getObj()) {
                MapNode jsonMap = (MapNode) node;
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String name = field.getName();
                    field.set(name, jsonMap.get(name));
                }

                clazzList.add(clazz.newInstance());
            }

            return clazzList;
        } catch (InstantiationException | IllegalAccessException exception) {
            return null;
        }
    }

    public <T> List<T> tryPullAsClassList(Class<? extends T> clazz, File file) {
        try {
            JsonNode<?> jsonNode = this.parse(file);
            if (jsonNode.getType() == JsonNode.NodeType.Map) return Collections.singletonList(tryPullAsClass(clazz, (MapNode) jsonNode));
            else if (jsonNode.getType() != JsonNode.NodeType.Array) return null;
            ArrayNode jsonList = (ArrayNode) jsonNode;


            List<T> clazzList = new ArrayList<>();
            for (JsonNode<?> node : jsonList.getObj()) {
                MapNode jsonMap = (MapNode) node;
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String name = field.getName();
                    field.set(name, jsonMap.get(name));
                }

                clazzList.add(clazz.newInstance());
            }

            return clazzList;
        } catch (InstantiationException | IllegalAccessException exception) {
            return null;
        }
    }
}
