package analysis.chaotic;

import analysis.AbstractAnalysis;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Logistic;

/**
 *
 * @author tadaki
 */
public class BandFormation extends AbstractAnalysis {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        double lambda = 0.9;
        int numIteration = 30;
        int numPoint = 256;
//        int numPoint = 5;
        BandFormation sys = new BandFormation(lambda);
        sys.doExec(numPoint, numIteration);
    }

    public BandFormation(double lambda) throws IOException {
        super(lambda);
    }

    public void doExec(int numPoint, int numIteration) throws IOException {
        this.openOutput("BandFormation-"+String.valueOf(lambda)+".txt");
        this.header();
        this.header("lambda",lambda);
        this.header("n",numPoint);
        double dx = 1. / numPoint;
        List<List<Double>> data = new ArrayList<>();
        for(int i =1; i<numPoint;i++){
            List<Double> d = new ArrayList<>();
            d.add(dx*i);
            data.add(d);
        }
        for(int t=0;t<numIteration;t++){
            for(int i=0;i<numPoint-1;i++){
                double x = data.get(i).get(t);
                double y = Logistic.map(x, lambda);
                data.get(i).add(y);
            }
        }
        
        for(int t=0;t<numIteration;t++){
            StringBuilder sb=new StringBuilder();
            sb.append(t);
            for (int i=0;i<numPoint-1;i++){
                sb.append(" ").append(data.get(i).get(t));
            }
            this.output(sb.toString());
        }
        this.close();
    }
}
