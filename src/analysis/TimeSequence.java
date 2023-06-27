package analysis;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;

/**
 * Show the time sequence
 * 
 * Output to TimeSequence-lambda.txt
 * @author tadaki
 */
public class TimeSequence extends AbstractAnalysis {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {
        boolean chaos = true;
//        double lambda = (1.+2*Math.sqrt(2.))/4-0.00005;
        double lambda = 0.9;
        TimeSequence sys = new TimeSequence(chaos, lambda);
        int numIteration = 5;
        int numUpdate = 60;
        sys.doExec(numIteration, numUpdate);
    }

    public TimeSequence(boolean chaos, double lambda) throws IOException {
        super(lambda);
    }

    public void doExec(int numIteration, int numUpdate) throws IOException {
        String filename = "TimeSequence-" + String.valueOf(lambda) + ".txt";
        this.openOutput(filename);
        this.header();
        this.header("lambda",lambda);
        double xInit = 0.2;
        double xInit2 = 0.2001;
        List<Point2D.Double> pList = logistic.timeEvolution(xInit, numUpdate);
        List<Point2D.Double> pList2 = logistic.timeEvolution(xInit2, numUpdate);
        for(Point2D.Double p : pList){
            this.output(p.x, p.y);
        }
        this.output("");
        for(Point2D.Double p : pList2){
            this.output(p.x, p.y);
        }
        close();
    }

}
