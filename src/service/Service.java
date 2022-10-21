package service;

import Logic.Logic;

public class Service {
    // 行细胞数值、列细胞数值
    private final int Row;
    private final int Col;
    // 细胞状态：0代表死细胞，1代表活细胞
    private final int[][] grid;

    // 构造方法
    public Service(int RowNum, int ColNum)
    {
        // 设置整型输入grid的行列值为ui界面中的值
        this.Row = RowNum;
        this.Col = ColNum;

        // 创建多维数组
        grid = new int[Row + 2][Col + 2];

        // 所有细胞开局全死亡
        for (int i = 0; i <= Row + 1; i++)
        {
            for (int j = 0; j <= Col + 1; j++)
            {
                grid[i][j] = 0;
            }
        }
    }

    // 获取ui界面对应的数组大小
    public int[][] getGrid()
    {
        return grid;
    }


    // 随机初始化细胞,随机初始化部分细胞状态为1
    public void randomCell(Logic a)
    {
        for (int i = 1; i <= Row; i++)
        {
            for (int j = 1; j <= Col; j++)
            {
                grid[i][j] = Math.random() > 0.5? 1 : 0;
                if(grid[i][j]==1){
                    a.setIsSelected(i-1,j-1);
                }
            }
        }

    }

    // 获取细胞的邻居数量
    public int getNeighborCount(int row, int col)
    {
        int countNeighbor = 0;

        // 以自己为中心，判断周围八个细胞状态
        for (int i = row - 1; i <= row + 1; i++)
        {
            for (int j = col - 1; j <= col + 1; j++)
            {
                // 判断到自己时，直接跳过
                if(i == row && j == col || i < 0 || j < 0 || i > 6 || j > 11)
                {
                    continue;
                }
                countNeighbor += grid[i][j]; //如果邻居还活着，邻居的状态为1，邻居数便会+1
            }
        }

        return countNeighbor;
    }

    // 繁衍
    public void update(Logic a)
    {
        // 创建与ui界面大小相同的多维数组
        int[][] newGrid = new int[Row + 2][Col + 2];

        // 遍历每一个元素，每一个元素对应一个网格细胞按钮
        for (int i = 1; i <= Row; i++)
        {
            for (int j = 1; j <= Col; j++)
            {
                // 根据邻居数量，判断细胞存亡
                switch (getNeighborCount(i, j))
                {
                    case 2:
                        // 邻居数量为2，细胞状态保持不变
                        newGrid[i][j] = grid[i][j];
                        break;
                    case 3:
                        // 邻居数量为3，死细胞变为活细胞
                        newGrid[i][j] = 1;
                        if(grid[i][j]==0){
                            a.setIsSelected(i-1,j-1);
                        }
                        break;
                    default:
                        // 其它情况一律为死细胞
                        newGrid[i][j] = 0;
                        if(grid[i][j]==1){
                            a.setIsSelected(i-1,j-1);
                        }
                }
            }
        }
        // 给grid[][]多维数组赋值为新的细胞状态
        for (int i = 1; i <= Row; i++)
        {
            for(int j = 1;j<=Col;j++)
            {
                grid[i][j] = newGrid[i][j];
            }
        }
    }

    //设定grid数组中单个对象的值
    public void setGrid(int row,int col,int value){
        grid[row + 1][col + 1]=value;
    }
}
