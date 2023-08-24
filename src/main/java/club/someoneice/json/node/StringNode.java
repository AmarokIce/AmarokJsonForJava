package club.someoneice.json.node;

public class StringNode extends JsonNode<String> {
    public StringNode(String obj) {
        super(obj);
    }

    @Override
    public NodeType getType() {
        return NodeType.String;
    }
}
