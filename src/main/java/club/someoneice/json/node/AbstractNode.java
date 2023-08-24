package club.someoneice.json.node;

public class AbstractNode extends JsonNode<Object> {
    protected AbstractNode() {
        super(null);
    }

    @Override
    public NodeType getType() {
        return NodeType.Null;
    }
}
