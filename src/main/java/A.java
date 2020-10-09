public class A {

    static int run(int[][] arr,int i,int j){

        if (i==0 && j==0) {
            return arr[i][j];
        }

        if (i>0 && j>0) {
            if (arr[i][j-1]<arr[i-1][j]) {
                return arr[i][j-1]+run(arr,i-1,j);
            } else {
                return arr[i-1][j]+run(arr,i,j-1);
            }
        }

        if (i>0) {
            return arr[i-1][j]+run(arr,i,j-1);
        }
        if (j>0) {
            return arr[i][j-1]+run(arr,i-1,j);
        }
        return -1;
    }




            //1 9 1 1
            //2 9 0 1
            //8 9 9 9
    public static void main(String[] args) {
        //int arr[][] = new int[][]{};
        int arr[][] = {{1,9,1,1},{2,9,0,1},{8,9,9,9}};


        System.out.println(run(arr,2,3));
    }
}
