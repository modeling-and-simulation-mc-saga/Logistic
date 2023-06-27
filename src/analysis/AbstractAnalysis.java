package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import model.Logistic;
//import utils.Gnuplot;

/**
 *
 * @author tadaki
 */
public class AbstractAnalysis {

    protected final double lambda;
//    protected final Gnuplot gnuplot;
    protected final Logistic logistic;
    protected PrintStream out;
    public AbstractAnalysis(double lambda) throws IOException {
        this.lambda = lambda;
        logistic = new Logistic(lambda);
//        gnuplot = new Gnuplot(".");
    }
    
    public double getX(){
        return logistic.getX();
    }
    
    public void relax(int numUpdate){
        for(int t=0;t<numUpdate;t++){
            logistic.update();
        }
    }
    
    public void openOutput(String filename) throws FileNotFoundException{
        out = new PrintStream(new File(filename));
    }
    
    public void header(){
        Date date = new Date();
        out.println("#"+date.toString());
    }
    
    public void header(String name, double value){
        String s = "#"+name+":"+String.valueOf(value);
        out.println(s);
    }
    public void output(String s){
        out.println(s);
    }
    
    public void output(double x,double y){
        out.println(x+" "+y);
    }
    
    public void close(){out.close();}
/*    
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
*/
}
