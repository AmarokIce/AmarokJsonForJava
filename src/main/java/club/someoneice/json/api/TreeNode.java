package club.someoneice.json.api;

public interface TreeNode<T> {
	/**
	 * SafeVarages: <br>
	 * 	继承后替换为所属的泛型，此处泛型堆为安全接口。<br>
	 * 	禁止直接序列化 TreeNode.
	 * @param childs The nodes.
	 * */
	@SuppressWarnings("unchecked")
	void addChild(T... childs);
}
