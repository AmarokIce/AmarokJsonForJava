package club.someoneice.json.processor;

import club.someoneice.json.Pair;
import club.someoneice.json.PairList;
import club.someoneice.json.node.AbstractNode;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.StringNode;
import club.someoneice.json.processor.api.IJson5Bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
public class Json5Builder {
    private static final String sp = "    ";

    private final List<IJson5Bean> taskTable = new ArrayList<>();

    public void put(IJson5Bean bean) {
        this.taskTable.add(bean);
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        taskTable.forEach(it -> builder.append(build(it, 0)));
        return builder.toString();
    }

    private String build(IJson5Bean bean, int ct) {
        StringBuilder builder = new StringBuilder();
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
        for (int i = 0; i < count - 1; i++) builder.append(sp);
        builder.append("[");

        while (iterator.hasNext()) {
            Pair<IJson5Bean.COMMAND, JsonNode<?>> pair = iterator.next();
            for (int i = 0; i < count; i++) builder.append(sp);

            arrayCommand(iterator, builder, pair, count);

            builder.append("\r\n");
            for (int i = 0; i < count - 1; i++) builder.append(sp);
            builder.append("]");
        }
    }

    private void arrayCommand(Iterator<Pair<IJson5Bean.COMMAND, JsonNode<?>>> iterator, StringBuilder builder, Pair<IJson5Bean.COMMAND, JsonNode<?>> pair, int count) {
        switch (pair.getKey()) {
            case NODE: {
                builder.append("\r\n");
                builder.append(JsonBuilder.prettyPrint(JsonBuilder.asString(pair.getValue()), count));
                if (iterator.hasNext()) builder.append(",");
                break;
            }

            case NOTE: {
                builder.append("\r\n");
                builder.append("//").append(pair.getValue().toString());
                break;
            }

            case LINE: {
                builder.append("\r\n");
                break;
            }

            case MAP:
            case ARRAY: {
                builder.append("\r\n");
                builder.append(build((IJson5Bean) pair.getValue(), count));
                if (iterator.hasNext()) builder.append(",");
                break;
            }
        }
    }

    private void mapBuilder(StringBuilder builder, int ct, IJson5Bean bean) {
        Iterator<Pair<IJson5Bean.COMMAND, Pair<String, JsonNode<?>>>> iterator = ((PairList<IJson5Bean.COMMAND, Pair<String, JsonNode<?>>>) bean.getTask()).getIterator();
        int count = 1 + ct;

        for (int i = 0; i < count - 1; i++) builder.append(sp);
        builder.append("{");
        while (iterator.hasNext()) {
            Pair<IJson5Bean.COMMAND, Pair<String, JsonNode<?>>> cmdPair = iterator.next();
            Pair<String, JsonNode<?>> pair = cmdPair.getValue();


        }

        builder.append("\r\n");
        for (int i = 0; i < count - 1; i++) builder.append(sp);
        builder.append("}");
    }

    private void mapCommand(Iterator<Pair<IJson5Bean.COMMAND, JsonNode<?>>> iterator, StringBuilder builder, Pair<IJson5Bean.COMMAND, JsonNode<?>> pair, int count) {
        switch (pair.getKey()) {

            case NODE: {
                builder.append("\r\n");
                for (int i = 0; i < count; i++) builder.append(sp);
                builder.append(pair.getKey()).append(": ");
                if (pair.getValue().getType() == JsonNode.NodeType.Array || pair.getValue().getType() == JsonNode.NodeType.Map) {
                    builder.append(prettyPrintWithoutFirstLine(JsonBuilder.asString(pair.getValue()), count));
                } else builder.append(JsonBuilder.prettyPrint(JsonBuilder.asString(pair.getValue())));

                if (iterator.hasNext()) builder.append(",");
                break;
            }

            case NOTE: {
                builder.append("\r\n");
                for (int i = 0; i < count; i++) builder.append(sp);
                builder.append("//").append(pair.getKey());
                break;
            }

            case LINE: {
                builder.append("\r\n");
                break;
            }

            case MAP:
            case ARRAY: {
                builder.append("\r\n");
                for (int i = 0; i < count; i++) builder.append(sp);
                builder.append(pair.getKey()).append(": ").append(build((IJson5Bean) pair.getValue(), count));
                if (iterator.hasNext()) builder.append(",");
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

    public final class ArrayBean extends AbstractNode implements IJson5Bean {
        private final PairList<COMMAND, JsonNode<?>> commandSet = new PairList<>();

        @Override
        public boolean isMap() {
            return false;
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
            this.commandSet.put(COMMAND.NOTE, new StringNode(note));
            return this;
        }

        public ArrayBean enterLine() {
            this.commandSet.put(COMMAND.LINE, JsonNode.NULL);
            return this;
        }

        public ArrayBean addBean(ArrayBean bean) {
            this.commandSet.put(COMMAND.ARRAY, bean);
            return this;
        }

        public ArrayBean addBean(ObjectBean bean) {
            this.commandSet.put(COMMAND.MAP, bean);
            return this;
        }
    }

    public final class ObjectBean extends AbstractNode implements IJson5Bean {
        private final PairList<COMMAND, Pair<String, JsonNode<?>>> commandSet = new PairList<>();

        @Override
        public boolean isMap() {
            return true;
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
            this.commandSet.put(COMMAND.NOTE, new Pair<>(note, JsonNode.NULL));
            return this;
        }

        public ObjectBean enterLine() {
            this.commandSet.put(COMMAND.LINE, new Pair<>("null", JsonNode.NULL));
            return this;
        }

        public ObjectBean addBean(String key, ArrayBean bean) {
            this.commandSet.put(COMMAND.ARRAY, new Pair<>(key, bean));
            return this;
        }

        public ObjectBean addBean(String key, ObjectBean bean) {
            this.commandSet.put(COMMAND.MAP, new Pair<>(key, bean));
            return this;
        }
    }

    private static final char KEY_NEXT = 44;
    private static final char KEY_VALUE = 58;
    private static final char KEY_ARRAY_START = 91;
    private static final char KEY_ARRAY_END = 93;
    private static final char KEY_MAP_START = 123;
    private static final char KEY_MAP_END = 125;

    String prettyPrintWithoutFirstLine(String node, int ct) {
        StringBuilder builder = new StringBuilder();
        int count = ct + 1;
        char[] charList = node.toCharArray();
        builder.append(charList[0]);
        builder.append("\r\n");
        if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
        for (int o = 1; o < charList.length; o ++) {
            char c = charList[o];
            if (c == KEY_NEXT) {
                builder.append(c).append("\r\n");
                if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
            } else if (c == KEY_ARRAY_START) {
                builder.append(c).append("\r\n");
                if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
                ++count;
            } else if (c == KEY_ARRAY_END) {
                --count;
                builder.append("\r\n");
                if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
                builder.append(c);
            } else if (c == KEY_MAP_START) {
                builder.append(c).append("\r\n");
                if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
                ++count;
            } else if (c == KEY_MAP_END) {
                --count;
                builder.append("\r\n");
                if (count > 0) for (int i = 0; i < count; i ++) builder.append(sp);
                builder.append(c);
            } else if (c == KEY_VALUE) {
                builder.append(c).append(" ");
            } else builder.append(c);
        }

        return builder.toString();
    }
}
