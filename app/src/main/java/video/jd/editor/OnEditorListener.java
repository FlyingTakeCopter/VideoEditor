package video.jd.editor;

/**
 * 执行完成/错误 时的回调接口
 */
public interface OnEditorListener {
	void onSuccess();

	void onFailure();

	void onProgress(float progress);
}
