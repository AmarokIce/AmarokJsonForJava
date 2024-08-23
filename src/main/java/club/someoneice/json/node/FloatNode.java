package club.someoneice.json.node;

public final class FloatNode extends JsonNode<Float> {
    public FloatNode(float obj) {
        super(obj);
    }

    @Override
    public NodeType getType() {
        return NodeType.Float;
    }

    @Override
    public FloatNode copy() {
        return new FloatNode(obj);
    }

    @Override
    public FloatNode asTypeNode() {
        return this;
    }
}
