public class MinPathSum {

    /**
     * 这种解法比较好理解， 但是会有问题
     * @param grid
     * @return
     */
    static int sum(int[][] grid) {
        int i = grid.length - 1;
        int j = grid[0].length - 1;

        if (i == 0 && j == 0) {
            return grid[0][0];
        }

        int sum = grid[i][j];
        while (i > 0 && j > 0) {
            if (grid[i][j - 1] < grid[i - 1][j]) {
                sum += grid[i][j - 1];
                j--;
            } else {
                sum += grid[i - 1][j];
                i--;
            }
        }
        while (i > 0) {
            sum += grid[i - 1][j];
            i--;
        }
        while (j > 0) {
            sum += grid[i][j - 1];
            j--;
        }

        return sum;
    }

    //             7
    //1,4,8,6,2,2,1,7
    //4,7,3,1,4,5,5,1
    //8,8,2,1,1,8,0,1
    //8,9,2,9,8,0,8,9
    //5,7,5,7,1,8,5,5
    //7,0,9,4,5,6,5,6
    //4,9,9,7,9,1,9,0
    //6

    /**
     * 解题思路</br>
     * 1. 最上一行和最左一列 所经过的的最小和为原点到目标元素的所有元素之和</br>
     * 2. 当目标元素为其他位置时，所经过路径最小和为目标元素 上方或左方元素最小和加上当前元素值</br>
     * <p>
     * 转换成数学公式即：</br>
     * 当i=0 , j>0 时 （最上一行）：</br>
     * dp[0][j] = dp[0][j-1]+grid[i][j]</br>
     * 当i>0, j=0时: (最左一列)</br>
     * dp[i][0] = dp[i-1][0]+grid[i][j]</br>
     * 当i=0,j=0时：(在原点)</br>
     * dp[0][0] = grid[0][0]</br>
     *
     * @param grid
     * @return
     */
    static int dpSum(int[][] grid) {

        int i = grid.length - 1;
        int j = grid[0].length - 1;

        if (i == 0 && j == 0) {
            return grid[0][0];
        }


        int dp[][] = new int[i + 1][j + 1];
        dp[0][0] = grid[0][0];

        // 设置第一行
        for (int line = 1; line <= j; line++) {
            dp[0][line] = grid[0][line];
        }
        // 设置第一列
        for (int col = 1; col <= i; col++) {
            dp[col][0] = grid[col][0];
        }
        /**
         * dp[i][j] = grid[i][j] + Min(dp[i-1][j], dp[i][j-1])
         */
        for (int line = 1; line <= j; line++) {
            for (int col = 1; col <= i; col++) {
                dp[col][line] = grid[col][line] + Math.min(dp[col - 1][line], dp[col][line - 1]);
            }
        }
        return dp[i][j];
    }


    //1, 9, 1, 1
    //2, 9, 0, 1
    //8, 9, 9, 9
    public static void main(String[] args) {
        //int grid[][] = {{1, 4, 8, 6, 2, 2, 1, 7}, {4, 7, 3, 1, 4, 5, 5, 1}, {8, 8, 2, 1, 1, 8, 0, 1}, {8, 9, 2, 9, 8, 0, 8, 9}, {5, 7, 5, 7, 1, 8, 5, 5}, {7, 0, 9, 4, 5, 6, 5, 6}, {4, 9, 9, 7, 9, 1, 9, 0}};

        int grid[][] = {{1, 9, 1, 1}, {2, 9, 0, 1}, {8, 9, 9, 9}};

        System.out.println(sum(grid));
        System.out.println(dpSum(grid));
    }
}
