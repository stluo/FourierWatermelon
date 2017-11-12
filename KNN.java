import java.util.*;

public class classifier{

    public static void main(String[] args){
        double[] input = {1,2,3,4,5};
        double[][] d = {{2,4,6,8,10},{2,4,6,8,9},{2,3,5,5,6},{2,3,6,5,6},{2,3,4,5,6}};
        List<List<Double>> data = new ArrayList<>();
        for(int i = 0; i<d.length;i++){
            List<Double> temp = new ArrayList<>();
            for(int j = 0;j<d[i].length;j++){
                temp.add(d[i][j]);
            }
            data.add(temp);
        }
        List<Integer> classes = new ArrayList<Integer>();
        classes.add(1);
        classes.add(2);
        classes.add(3);
        classes.add(4);
        classes.add(5);
        classifier temp = new classifier(data,classes);
        int k = temp.KNN(input);
        System.out.println(k);
    }
    List<List<Double>> data = new ArrayList<>();
    List<Integer> classes = new ArrayList<>();
    int n = 0;

    classifier(List<List<Double>> d,List<Integer> c){  
        this.data = d;  
        this.classes = c;  
        if (this.data.size()<4){
            n = this.data.size();
        }
        else{
            n = 4;
        }
    }

    public void update(double[] input,int c){
        List<Double> temp = new ArrayList<>();
        for (int i = 0;i<input.length;i++){
            temp.add(input[i]);
        }
        this.data.add(temp);
        this.classes.add(c);
    }
    
    public double dist(List<Double> a, double[] b, int type){
        double sum = 0;
        //Euclidean
        if (type == 0){
            for(int i=0; i < a.size(); i++){
                sum = sum + (a.get(i)-b[i])*(a.get(i)-b[i]);
            } 
        }
        //Cosine similarity
        if (type == 1){
            double ab = 0;
            double a2 = 0;
            double b2 = 0;
            for(int i=0; i < a.size(); i++){
                ab += a.get(i)*b[i];
                a2 += a.get(i)*a.get(i);
                b2 += b[i]*b[i];
            } 
            //Solve divide by 0 vectors by mapping 0 to 1 (not sure how legit this is)
            if (a2==0){
                a2=1;
            }
            if(b2==0){
                b2=1;
            }
            sum = 1-ab/(Math.sqrt(a2*b2));
            //Make it so lower number means more similar
        }
        return sum;
    };
    
    /**
    *Extremely simple KNN on untransformed data
    **/
    
    public int KNN(double[] input){
        int[] ind = new int[n];
        double[] dist = new double[n];
    
        //Initialize distances to max possible
        //May need to change to different type
        for (int i = 0;i<n;i++){
            dist[i] = Double.MAX_VALUE;
        }
    
        //Run through each element of data
        for(int i = 0; i < this.data.size();i++){
            //Calculate distance between new point and data
            //1 = cosine sim, 0 = euclidean dist
            double distance = dist(this.data.get(i),input,1);
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
            sum+=this.classes.get(ind[i]);
        }
        for (int i =0;i<3;i++){
            System.out.println(ind[i]);
        }
        //Take average
        int output = sum/n;
        return output;
    };
}