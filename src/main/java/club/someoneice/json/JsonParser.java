package club.someoneice.json;

import club.someoneice.json.node.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class JsonParser {
    private static final char KEY_SPACE = 32;
    private static final char KEY_STRING = 34;
    private static final char KEY_NEXT = 44;
    private static final char KEY_VALUE = 58;
    private static final char KEY_ARRAY_START = 91;
    private static final char KEY_ARRAY_END = 93;
    private static final char KEY_MAP_START = 123;
    private static final char KEY_MAP_END = 125;
    private final char KEY_COUNT = 46;
    String raw;

    JsonParser(String str) {
        this.raw = str;
    }

    JsonParser(File file, boolean json5) {
        try {
            if (!json5) this.raw = fileReader(file);
            else this.json5processor(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    JsonParser(InputStream stream, boolean shouldClose, boolean isJson5) {
        try {
            this.raw = !isJson5 ? (shouldClose ? streamReader(stream) : streamReaderWithoutClose(stream))
                    : (shouldClose ? json5ProcessorStreamWithClose(stream) : json5processor(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String json5ProcessorStreamWithClose(InputStream stream) throws IOException {
        String rawS = json5processor(stream);
        stream.close();

        return rawS;
    }

    String json5processor(InputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStreamReader in = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(in);
        while (reader.ready()) {
            String text = reader.readLine();

            if (text.contains("//")) {
                builder.append(text, 0, text.indexOf("//"));
            } else builder.append(text);
        }

        in.close();
        reader.close();

        return builder.toString();
    }

    private void json5processor(File file) throws IOException {
        if (file.exists() && file.isFile()) {
            StringBuilder builder = new StringBuilder();
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);

            read(builder, reader);

            fr.close();
            reader.close();

            this.raw = builder.toString();
            return;
        }

        raw = "";
    }

    private void read(StringBuilder builder, BufferedReader reader) throws IOException {
        while (reader.ready()) {
            String text = reader.readLine();
            if (text.contains("//")) {
                builder.append(text, 0, text.indexOf("//"));
            } else builder.append(text);
        }
    }

    JsonNode<?> getNodeWithTypeUnknown() {
        if (raw.isEmpty()) return NullNode.INSTANCE;
        return this.getNodeWithTypeUnknown(raw);
    }

    private JsonNode<?> getNodeWithTypeUnknown(String rawStr) {
        rawStr = rawStr.replaceAll(" ", "").replaceAll("\n", "").replaceAll("\r", "");
        final char[] charList = rawStr.toCharArray();
        final char c = charList[0];

        return c == KEY_ARRAY_START ? arrayNodeProcessor(charList)
                : c == KEY_MAP_START ? mapNodeProcessor(charList)
                : tryGetNode(new StringBuilder(rawStr));
    }

    private ArrayNode arrayNodeProcessor(char[] charList) {
        ArrayNode node = new ArrayNode(new ArrayList<>());
        StringBuilder builder = new StringBuilder();
        boolean stringStart = false;
        for (int i = 1; i < charList.length; i++) {
            char c = charList[i];

            if (stringStart) {
                if (c == KEY_STRING) {
                    builder.append(c);
                    continue;
                }

                stringStart = false;
                node.add(tryGetNode(builder));
                builder.delete(0, builder.length());

                continue;
            }

            if (c == KEY_SPACE) continue;
            if (c == KEY_MAP_END) throw new NullPointerException("Here are no map! But its a map 's end!");

            if (c == KEY_NEXT) {
                if (!builder.toString().isEmpty())
                    node.add(tryGetNode(builder));
                builder.delete(0, builder.length());

                continue;
            } else if (c == KEY_ARRAY_END) {
                if (builder.toString().isEmpty()) return node;

                node.add(tryGetNode(builder));
                return node;
            }

            if (c == KEY_STRING) {
                stringStart = true;
                continue;
            } else if (c == KEY_ARRAY_START) {
                int end = findArrayEnd(charList, i);
                if (end == -1) throw new NullPointerException("Here are no end for this array!");

                StringBuilder array = getArrayFromObject(charList, i, end);
                node.add(arrayNodeProcessor(array.toString().toCharArray()));
                i = end;
                continue;
            } else if (c == KEY_MAP_START) {
                int end = findMapEnd(charList, i);
                if (end == -1) throw new NullPointerException("Here are no end for this map!");
                StringBuilder array = getMapFromObj(charList, i, end);

                node.add(mapNodeProcessor(array.toString().toCharArray()));
                i = end;
                continue;
            }
            builder.append(c);
        }

        return null;
    }

    private int findArrayEnd(char[] charList, int start) {
        int count = 0;
        for (int i = start; i < charList.length; i++) {
            if (charList[i] == KEY_ARRAY_START) count++;
            if (charList[i] == KEY_ARRAY_END) {
                if (count == 1) return i;
                count--;
            }
        }

        return -1;
    }

    private StringBuilder getArrayFromObject(char[] charList, int start, int end) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end + 1; i++) {
            builder.append(charList[i]);
        }
        return builder;
    }

    private MapNode mapNodeProcessor(char[] charList) {
        MapNode node = new MapNode(new HashMap<>());

        StringBuilder builder = new StringBuilder();

        StringBuilder key = new StringBuilder();
        JsonNode<?> valueNode = NullNode.INSTANCE;

        boolean stringStart = false;
        boolean keyEnd = false;

        for (int i = 1; i < charList.length; i++) {
            char c = charList[i];

            if (stringStart) {
                if (c == KEY_STRING) {
                    stringStart = false;
                    if (keyEnd) {
                        valueNode = new StringNode(builder.toString());
                        builder.delete(0, builder.length());
                    }
                    continue;
                }

                builder.append(c);
                continue;
            }

            if (c == KEY_VALUE) {
                if (keyEnd) throw new RuntimeException("What up here? Its had two value?");

                key.append(builder);
                builder.delete(0, builder.length());

                keyEnd = true;
                continue;
            } else if (c == KEY_NEXT) {
                if (!keyEnd) throw new RuntimeException("Its a array bro.");
                keyEnd = false;

                if (valueNode == NullNode.INSTANCE) valueNode = tryGetNode(builder);
                node.put(key.toString(), valueNode);

                builder.delete(0, builder.length());
                key.delete(0, key.length());
                valueNode = NullNode.INSTANCE;

                continue;
            } else if (c == KEY_MAP_END) {
                if (!keyEnd) throw new RuntimeException("Its a array bro.");

                if (valueNode == NullNode.INSTANCE) valueNode = tryGetNode(builder);
                node.put(key.toString(), valueNode);

                return node;
            }

            if (c == KEY_STRING) {
                stringStart = true;
                continue;
            } else if (c == KEY_ARRAY_START) {
                int end = findArrayEnd(charList, i);
                if (end == -1) throw new NullPointerException("Here are no end for this array!");
                StringBuilder array = getArrayFromObject(charList, i, end);
                valueNode = arrayNodeProcessor(array.toString().toCharArray());
                i = end;
                continue;
            } else if (c == KEY_MAP_START) {
                int end = findMapEnd(charList, i);
                if (end == -1) throw new NullPointerException("Here are no end for this map!");
                StringBuilder array = getMapFromObj(charList, i, end);

                valueNode = mapNodeProcessor(array.toString().toCharArray());
                i = end;
                continue;
            }

            builder.append(c);
        }

        return null;
    }

    private int findMapEnd(char[] charList, int start) {
        int count = 0;
        for (int i = start; i < charList.length; i++) {
            if (charList[i] == KEY_MAP_START) count++;
            if (charList[i] == KEY_MAP_END) {
                if (count == 1) return i;
                count--;
            }
        }

        return -1;
    }

    private StringBuilder getMapFromObj(char[] charList, int start, int end) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end + 1; i++) builder.append(charList[i]);
        return builder;
    }

    JsonNode<?> tryGetNode(StringBuilder builder) {
        String str = builder.toString();
        if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false"))
            return new BooleanNode(str.equalsIgnoreCase("true"));
        NumberType number = getNumber(str);
        if (number != null)
            switch (number) {
                case Int:
                    return new IntegerNode(Integer.parseInt(str));
                case Float:
                    return new FloatNode(Float.parseFloat(str));
                case Double:
                    return new DoubleNode(Double.parseDouble(str));
            }
        return new StringNode(str);
    }

    private NumberType getNumber(String str) {
        if (str.isEmpty()) return null;
        boolean has = false;
        boolean E = false;

        if (str.startsWith("-")) str = str.replace("-", "");

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 48 && c <= 57) continue;

            if (c == KEY_COUNT) {
                if (has) return null;
                else has = true;

                continue;
            }

            // E, e
            if (c == 69 || c == 101) {
                if (E) return null;
                else E = true;
                continue;
            }

            // D, d
            if (c == 100 || c == 68) {
                if (i == str.length() - 1)
                    return NumberType.Double;
                else return null;
            }

            // F, f
            if (c == 102 || c == 70) {
                if (i == str.length() - 1)
                    return NumberType.Float;
                else return null;
            }

            return null;
        }
        if (has) return NumberType.Double;
        else return NumberType.Int;
    }

    String fileReader(File file) {
        if (file.exists() && file.isFile()) {
            try {
                return new String(Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @SuppressWarnings("all")
    String streamReaderWithoutClose(InputStream stream) throws IOException {
        byte[] bytes = new byte[stream.available()];
        stream.read(bytes);

        return new String(bytes);
    }

    @SuppressWarnings("all")
    String streamReader(InputStream stream) throws IOException {
        byte[] bytes = new byte[stream.available()];
        stream.read(bytes);
        stream.close();

        return new String(bytes);
    }

    private enum NumberType {
        Int, Float, Double
    }
}
