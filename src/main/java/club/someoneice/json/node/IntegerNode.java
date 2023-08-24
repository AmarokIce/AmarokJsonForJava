package club.someoneice.json.node;

public final class IntegerNode extends JsonNode<Integer> {
    public IntegerNode(int obj) {
        super(obj);
    }

    @Override
    public NodeType getType() {
        return NodeType.Int;
    }
}
