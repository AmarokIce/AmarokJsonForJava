package club.someoneice.json.processor;

import club.someoneice.json.JsonParser;
import club.someoneice.json.Pair;
import club.someoneice.json.PairList;
import club.someoneice.json.api.IJson5Bean;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.NullNode;
import club.someoneice.json.node.StringNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
public class Json5Builder {
    private final List<IJson5Bean> taskTable = new ArrayList<>();

    public Json5Builder create() {
        return new Json5Builder();
    }

    public <T extends IJson5Bean> T put(T bean) {
        this.taskTable.add(bean);
        return bean;
    }

    public void clearTask() {
        this.taskTable.clear();
    }

    public boolean noTask() {
        return this.taskTable.isEmpty();
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        taskTable.forEach(it -> builder.append(build(it, 0)));
        return builder.toString();
    }

    private String build(IJson5Bean bean, int ct) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        if (!bean.isMap()) {
            arrayBuilder(builder, ct, bean);
        } else {
            mapBuilder(builder, ct, bean);
        }

        return builder.toString();
    }

    private void arrayBuilder(StringBuilder builder, int ct, IJson5Bean bean) {
        Iterator<Pair<IJson5Bean.COMMAND, JsonNode<?>>> iterator = ((PairList<IJson5Bean.COMMAND, JsonNode<?>>) bean.getTask()).getIterator();
        int count = 1 + ct;
        for (int i = 0; i < count - 1; i++) builder.append(JsonParser.SP);
        builder.append("[");

        while (iterator.hasNext()) {
            Pair<IJson5Bean.COMMAND, JsonNode<?>> pair = iterator.next();
            for (int i = 0; i < count; i++) builder.append(JsonParser.SP);

            arrayCommand(iterator.hasNext(), builder, pair, count);

            for (int i = 0; i < count - 1; i++) builder.append(JsonParser.SP);
        }
        builder.append("\n");
        for (int i = 0; i < count - 1; i++) builder.append(JsonParser.SP);
        builder.append("]");
    }

    private void arrayCommand(boolean hasNext, StringBuilder builder, Pair<IJson5Bean.COMMAND, JsonNode<?>> pair, int count) {
        builder.append("\n");
        switch (pair.getKey()) {
            case NODE: {
                if (pair.getValue() instanceof IJson5Bean) {
                    builder.append(build((IJson5Bean) pair.getValue(), count));
                } else {
                    builder.append(JsonBuilder.prettyPrint(JsonBuilder.asString(pair.getValue()), count));
                }

                if (hasNext) builder.append(",");
                break;
            }

            case COMMIT: {
                builder.append("//").append(pair.getValue().toString());
                break;
            }

            case LINE: {
                break;
            }
        }
    }

    private void mapBuilder(StringBuilder builder, int ct, IJson5Bean bean) {
        Iterator<Pair<IJson5Bean.COMMAND, Pair<String, JsonNode<?>>>> iterator = ((PairList<IJson5Bean.COMMAND, Pair<String, JsonNode<?>>>) bean.getTask()).getIterator();
        int count = 1 + ct;

        for (int i = 0; i < count - 1; i++) builder.append(JsonParser.SP);
        builder.append("{");
        while (iterator.hasNext()) {
            Pair<IJson5Bean.COMMAND, Pair<String, JsonNode<?>>> cmdPair = iterator.next();
            mapCommand(iterator.hasNext(), builder, cmdPair, count);
            for (int i = 0; i < count - 1; i++) builder.append(JsonParser.SP);
        }

        builder.append("\n");
        for (int i = 0; i < count - 1; i++) builder.append(JsonParser.SP);
        builder.append("}");
    }

    private void mapCommand(boolean hasNext, StringBuilder builder, Pair<IJson5Bean.COMMAND, Pair<String, JsonNode<?>>> pair, int count) {
        builder.append("\n");
        String keyInput = pair.getValue().getKey();
        keyInput = keyInput.contains(" ") ? String.format("\"%s\"", keyInput) : keyInput;
        switch (pair.getKey()) {

            case NODE: {
                for (int i = 0; i < count; i++) builder.append(JsonParser.SP);
                if (pair.getValue().getValue() instanceof IJson5Bean) {
                    builder.append(keyInput).append(": ").append(build((IJson5Bean) pair.getValue().getValue(), count));

                    if (hasNext) builder.append(",");
                    break;
                }

                builder.append(keyInput).append(": ");
                if (pair.getValue().getValue().getType() == JsonNode.NodeType.Array || pair.getValue().getValue().getType() == JsonNode.NodeType.Map) {
                    builder.append(prettyPrintWithoutFirstLine(JsonBuilder.asString(pair.getValue().getValue()), count));
                } else builder.append(JsonBuilder.prettyPrint(JsonBuilder.asString(pair.getValue().getValue())));

                if (hasNext) builder.append(",");
                break;
            }

            case COMMIT: {
                for (int i = 0; i < count; i++) builder.append(JsonParser.SP);
                builder.append("//").append(keyInput);
                break;
            }

            case LINE: {
                break;
            }
        }
    }

    public ArrayBean getArrayBean() {
        return new ArrayBean();
    }

    public ObjectBean getObjectBean() {
        return new ObjectBean();
    }

    public ArrayBean createArrayBean() {
        return this.put(new ArrayBean());
    }

    public ObjectBean createObjectBean() {
        return this.put(new ObjectBean());
    }

    String prettyPrintWithoutFirstLine(String node, int ct) {
        StringBuilder builder = new StringBuilder();
        int count = ct + 1;
        char[] charList = node.toCharArray();
        builder.append(charList[0]);
        builder.append("\n");
        if (count > 0) for (int i = 0; i < count; i++) builder.append(JsonParser.SP);
        for (int o = 1; o < charList.length; o++) {
            char c = charList[o];
            count = JsonBuilder.checkAndPut(builder, count, c);
        }

        return builder.toString();
    }

    /**
     * Json5Builder 中的 Array 封装器。用于封装 ArrayNode 相关数据。继承自 NullNode，可以被当做 JsonNode 分类，但不可被 JsonParser 解析、
     */
    public static final class ArrayBean extends NullNode implements IJson5Bean {
        private final PairList<COMMAND, JsonNode<?>> commandSet = new PairList<>();

        @Override
        public Object getObj() {
            return this.commandSet.clone();
        }

        @Override
        public boolean isMap() {
            return false;
        }

        @Override
        public void clean() {
            this.commandSet.clear();
        }

        @Override
        public PairList<COMMAND, JsonNode<?>> getTask() {
            return this.commandSet;
        }

        public ArrayBean add(JsonNode<?> obj) {
            this.commandSet.put(COMMAND.NODE, obj);
            return this;
        }

        public ArrayBean addNote(String note) {
            this.commandSet.put(COMMAND.COMMIT, new StringNode(note));
            return this;
        }

        public ArrayBean enterLine() {
            this.commandSet.put(COMMAND.LINE, NullNode.INSTANCE);
            return this;
        }

        @Deprecated
        public ArrayBean addBean(ArrayBean bean) {
            this.commandSet.put(COMMAND.NODE, bean);
            return this;
        }

        @Deprecated
        public ArrayBean addBean(ObjectBean bean) {
            this.commandSet.put(COMMAND.NODE, bean);
            return this;
        }
    }

    /**
     * Json5Builder 中的 Map 封装器。用于封装 MapNode 相关数据。继承自 NullNode，可以被当做 JsonNode 分类，但不可被 JsonParser 解析、
     */
    public static final class ObjectBean extends NullNode implements IJson5Bean {
        private final PairList<COMMAND, Pair<String, JsonNode<?>>> commandSet = new PairList<>();

        @Override
        public boolean isMap() {
            return true;
        }

        @Override
        public Object getObj() {
            return this.commandSet.clone();
        }

        @Override
        public void clean() {
            this.commandSet.clear();
        }

        @Override
        public PairList<COMMAND, Pair<String, JsonNode<?>>> getTask() {
            return this.commandSet;
        }

        public ObjectBean put(String key, JsonNode<?> obj) {
            this.commandSet.put(COMMAND.NODE, new Pair<>(key, obj));
            return this;
        }

        public ObjectBean addNote(String note) {
            this.commandSet.put(COMMAND.COMMIT, new Pair<>(note, NullNode.INSTANCE));
            return this;
        }

        public ObjectBean enterLine() {
            this.commandSet.put(COMMAND.LINE, new Pair<>("null", NullNode.INSTANCE));
            return this;
        }

        @Deprecated
        public ObjectBean addBean(String key, ArrayBean bean) {
            this.commandSet.put(COMMAND.NODE, new Pair<>(key, bean));
            return this;
        }

        @Deprecated
        public ObjectBean addBean(String key, ObjectBean bean) {
            this.commandSet.put(COMMAND.NODE, new Pair<>(key, bean));
            return this;
        }
    }

    @Override
    public String toString() {
        return this.build();
    }
}
