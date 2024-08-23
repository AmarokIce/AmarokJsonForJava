package club.someoneice.json.node;

public final class BooleanNode extends JsonNode<Boolean> {
    public BooleanNode(boolean obj) {
        super(obj);
    }

    @Override
    public NodeType getType() {
        return NodeType.Boolean;
    }

    @Override
    public BooleanNode copy() {
        return new BooleanNode(obj);
    }

    @Override
    public BooleanNode asTypeNode() {
        return this;
    }
}
