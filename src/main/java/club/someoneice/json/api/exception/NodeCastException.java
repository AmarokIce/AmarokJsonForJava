package club.someoneice.json.api.exception;

/**
 * 当 JsonNode 尝试内部转换类型时出现问题，将会抛出此错误。<br>
 * 这个错误不是必要的，但是它可以在一些特定情况下警告用户数据问题。
 */
public class NodeCastException extends RuntimeException{
    /**
     * 创建一个没有错误讯息的 <code>NodeCastException</code>。
     */
    public NodeCastException() {
        super();
    }

    /**
     * 创建一个包涵错误讯息的 <code>NodeCastException</code>。
     * @param s 抛出的错误讯息。
     */
    public NodeCastException(String s) {
        super(s);
    }
}
