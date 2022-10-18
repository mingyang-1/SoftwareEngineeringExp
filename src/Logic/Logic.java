package Logic;

public class Logic {
    // 行细胞数、列细胞数
    private int rowNum = 5;
    private final int colNum = 10;

    // 界面中细胞是否死完
    private boolean isDead;
    // 是否正在繁衍
    private boolean isRunning;
    // 细胞是否被选中
    private final boolean[][] isSelected;

    // 构造方法
    public Logic() {
        isSelected = new boolean[rowNum][colNum];

        // 默认初始化所有细胞未选中
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                isSelected[i][j] = false;
            }
        }
    }

    // 获取所有细胞是否死亡
    public boolean getIsDead() {
        return isDead;
    }

    // 设置所有细胞是否死亡
    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

    // 获取是否正在繁衍状态
    public boolean getIsRunning() {
        return isRunning;
    }

    // 设置是否正在繁衍状态
    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    // 获取细胞是否选中
    public boolean getIsSelected(int row, int col) {
        return isSelected[row][col];
    }

    // 设置细胞状态反转
    public void setIsSelected(int row, int col) {
        isSelected[row][col] = !isSelected[row][col];
    }
}
