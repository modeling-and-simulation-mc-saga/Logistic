package analysis;

import java.io.BufferedReader;
import java.io.IOException;
import model.Logistic;
import utils.Gnuplot;

/**
 *
 * @author tadaki
 */
public class AbstractAnalysis {

    protected final double lambda;
    protected final Gnuplot gnuplot;
    protected final Logistic logistic;

    public AbstractAnalysis(double lambda) throws IOException {
        this.lambda = lambda;
        logistic = new Logistic(lambda);
        gnuplot = new Gnuplot(".");
    }
    
    public double getX(){
        return logistic.getX();
    }
    
    public void relax(int numUpdate){
        for(int t=0;t<numUpdate;t++){
            logistic.update();
        }
    }
    
    public String getError() throws IOException{
        StringBuilder sb = new StringBuilder();
        try(BufferedReader in = gnuplot.getError()){
            String line;
            while((line=in.readLine())!=null){
                sb.append(line);
            }
        }
        return sb.toString();
    }

}
