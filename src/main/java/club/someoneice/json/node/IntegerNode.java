package club.someoneice.json.node;

public final class IntegerNode extends JsonNode<Integer> {
    public IntegerNode(int obj) {
        super(obj);
    }

    @Override
    public NodeType getType() {
        return NodeType.Int;
    }

    @Override
    public IntegerNode copy() {
        return new IntegerNode(obj);
    }

    @Override
    public IntegerNode asTypeNode() {
        return this;
    }
}
