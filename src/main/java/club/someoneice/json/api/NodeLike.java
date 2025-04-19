package club.someoneice.json.api;

import club.someoneice.json.JSON;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;

public interface NodeLike {
    /**
     * 数据验证。
     * @return {@code asJsonNode} 之后获取的数据。
     */
    default JsonNode.NodeType getType() {
        return JsonNode.NodeType.Map;
    }

    /**
     * 保证数据最终能变为 JsonNode。
     * @return 为目标类创建的 JsonNode 返回值。
     */
    default JsonNode<?> asJsonNode() {
        try {
            return JSON.json.tryPushFromClass(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从 Json 回到 Class
     * @return 通过 JsonNode 创建的实例对象。
     */
    default <T extends NodeLike> T fromJsonNode(JsonNode<?> node) {
        MapNode root = node.asMapNodeOrEmpty();
        if (root.isEmpty()) return null;
        try {
            return (T) this.getClass().cast(JSON.json.tryPullAsClass(this.getClass(), root));
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
