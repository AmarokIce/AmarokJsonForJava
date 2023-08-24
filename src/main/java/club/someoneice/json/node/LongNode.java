package club.someoneice.json.node;

public final class LongNode extends JsonNode<Long> {
    public LongNode(long obj) {
        super(obj);
    }

    @Override
    public NodeType getType() {
        return NodeType.Long;
    }
}
