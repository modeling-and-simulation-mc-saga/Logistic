package analysis;

import java.io.BufferedReader;
import java.io.IOException;
import utils.Gnuplot;

/**
 *
 * @author tadaki
 */
public class AbstractAnalysis {

    protected final double lambda;
    protected final Gnuplot gnuplot;

    public AbstractAnalysis(double lambda) throws IOException {
        this.lambda = lambda;
        gnuplot = new Gnuplot(".");
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
