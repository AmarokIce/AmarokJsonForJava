package club.someoneice.json.api;

import club.someoneice.json.node.JsonNode;

public interface JsonLike {
    /**
     * 保证数据最终能变为 JsonNode。
     * @return 为目标类创建的 JsonNode 返回值。
     */
    JsonNode<?> asJsonNode();
}
