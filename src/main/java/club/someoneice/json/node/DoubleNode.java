package club.someoneice.json.node;

public final class DoubleNode extends JsonNode<Double> {
    public DoubleNode(double obj) {
        super(obj);
    }

    @Override
    public NodeType getType() {
        return NodeType.Double;
    }
}
