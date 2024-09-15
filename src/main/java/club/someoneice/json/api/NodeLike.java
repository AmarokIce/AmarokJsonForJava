package club.someoneice.json.api;

import club.someoneice.json.node.JsonNode;

public interface NodeLike {


    /**
     * 数据验证。
     * @return {@code asJsonNode} 之后获取的数据。
     */
    JsonNode.NodeType getType();

    /**
     * 保证数据最终能变为 JsonNode。
     * @return 为目标类创建的 JsonNode 返回值。
     */
    JsonNode<?> asJsonNode();
}
