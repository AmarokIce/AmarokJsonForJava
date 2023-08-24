package club.someoneice.json.node;

public final class FloatNode extends JsonNode<Float> {
    public FloatNode(float obj) {
        super(obj);
    }

    @Override
    public NodeType getType() {
        return NodeType.Float;
    }
}
