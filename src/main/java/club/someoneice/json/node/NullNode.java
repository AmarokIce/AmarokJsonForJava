package club.someoneice.json.node;

public class NullNode extends JsonNode<Object> {
    public static final NullNode INSTANCE = new NullNode();

    protected NullNode() {
        super(null);
    }

    @Override
    public NodeType getType() {
        return NodeType.Null;
    }

    /**
     * But why?
     */
    @Override
    public NullNode copy() {
        return INSTANCE;
    }

    @Override
    public NullNode asTypeNode() {
        return this;
    }
}
