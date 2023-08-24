package club.someoneice.json.node;

public final class BooleanNode extends JsonNode<Boolean> {
    public BooleanNode(boolean obj) {
        super(obj);
    }

    @Override
    public NodeType getType() {
        return NodeType.Boolean;
    }
}
