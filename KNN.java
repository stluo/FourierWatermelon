public class classifier{

    public static void main(String[] args){
        int[] input = new int[5];
        int[][] data = new int[5][5];
        int[] classes = {1,2,3,4,5};
        classifier temp = new classifier();
        double k = temp.KNN(input,data,classes,3);
        System.out.println(k);
    }

    /**
    *Simple Euclidean distance
    **/
    
    public int dist(int[] a, int[] b){
        int sum = 0;
        for(int i=0; i < a.length; i++){
            sum = sum + (a[i]-b[i])*(a[i]-b[i]);
        }
        return sum;
    };
    
    /**
    *Extremely simple KNN on untransformed data
    **/
    
    public double KNN(int[] input, int[][] data, int[] classes,int n){
        int[] ind = new int[n];
        int[] dist = new int[n];
    
        //Initialize distances to max possible
        //May need to change to different type
        for (int i = 0;i<n;i++){
            dist[i] = Integer.MAX_VALUE;
        }
    
        //Run through each element of data
        for(int i = 0; i < data.length;i++){
            //Calculate distance between new point and data
            int distance = dist(input,data[i]);
            //Loop through our history of closest points
            for(int j = 0;j<n;j++){
                //If new data point is closer, replace and shift rest of history
                if (distance<dist[j]){
                    //Go backwards through array updating entries
                    //Is it n-j-1???
                    for(int k = 1;k<n-j-1;k++){
                        ind[n-k] = ind[n-k-1];
                        dist[n-k] = dist[n-k-1];
                    }
                    ind[j]=i;
                    dist[j]=distance;
                    //After update, break loop through dist
                    break;
                }
            }
        }
        //Given history, construct sum of response for closest neighbors
        int sum = 0;
        for (int i = 0;i<n;i++){
            sum+=classes[ind[i]];
        }
        //Take average
        double output = (double)sum/n;
        return output;
    };
}