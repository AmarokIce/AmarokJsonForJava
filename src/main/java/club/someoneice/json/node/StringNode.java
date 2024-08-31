package club.someoneice.json.node;

public final class StringNode extends JsonNode<String> {
    public StringNode(String obj) {
        super(obj);
    }

    @Override
    public NodeType getType() {
        return NodeType.String;
    }

    @Override
    public StringNode copy() {
        return new StringNode(obj);
    }

    @Override
    public StringNode asTypeNode() {
        return this;
    }
}
