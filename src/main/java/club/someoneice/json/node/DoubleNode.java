package club.someoneice.json.node;

public final class DoubleNode extends JsonNode<Double> {
    public DoubleNode(double obj) {
        super(obj);
    }

    @Override
    public NodeType getType() {
        return NodeType.Double;
    }

    @Override
    public DoubleNode copy() {
        return new DoubleNode(obj);
    }

    @Override
    public DoubleNode asTypeNode() {
        return this;
    }
}
