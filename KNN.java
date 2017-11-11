public class classifier{

    public static void main(String[] args){
        int[] input = new int[5];
        int[][] data = new int[5][5];
        int[] classes = {1,2,3,4,5};
        classifier temp = new classifier();
        double k = temp.KNN(input,data,classes,3);
        System.out.println(k);
    }
    
    public double dist(int[] a, int[] b, int type){
        double sum = 0;
        //Euclidean
        if (type == 0){
            for(int i=0; i < a.length; i++){
                sum = sum + (a[i]-b[i])*(a[i]-b[i]);
            } 
        }
        //Cosine similarity
        if (type == 1){
            //Assumes not all 0
            double ab = 0;
            double a2 = 0;
            double b2 = 0;
            for(int i=0; i < a.length; i++){
                ab += a[i]*b[i];
                a2 += a[i]*a[i];
                b2 += b[i]*b[i];
            } 
            sum = ab/(Math.sqrt(a2*b2));
            if (a2==0){
                if (b2==0){
                    sum = 1;
                }
            }
            //Make it so lower number means more similar
            else{
                sum = 1-sum;
            }
        }
        return sum;
    };
    
    /**
    *Extremely simple KNN on untransformed data
    **/
    
    public double KNN(int[] input, int[][] data, int[] classes,int n){
        int[] ind = new int[n];
        double[] dist = new double[n];
    
        //Initialize distances to max possible
        //May need to change to different type
        for (int i = 0;i<n;i++){
            dist[i] = Double.MAX_VALUE;
        }
    
        //Run through each element of data
        for(int i = 0; i < data.length;i++){
            //Calculate distance between new point and data
            //1 = cosine sim, 0 = euclidean dist
            double distance = dist(input,data[i],1);
            //Loop through our history of closest points
            for(int j = 0;j<n;j++){
                //If new data point is closer, replace and shift rest of history
                if (distance<=dist[j]){
                    //Go backwards through array updating entries
                    for(int k = 1;k<n-j;k++){
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
        for (int i =0;i<3;i++){
            System.out.println(ind[i]);
        }
        //Take average
        double output = (double)sum/n;
        return output;
    };
}
}