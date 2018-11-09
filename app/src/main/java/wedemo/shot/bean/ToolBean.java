package wedemo.shot.bean;

public class ToolBean {
    private int mIconRes;
    private String toolName;

    public ToolBean(int mIconRes, String toolName) {
        this.mIconRes = mIconRes;
        this.toolName = toolName;
    }

    public int getmIconRes() {
        return mIconRes;
    }

    public void setmIconRes(int mIconRes) {
        this.mIconRes = mIconRes;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }
}
