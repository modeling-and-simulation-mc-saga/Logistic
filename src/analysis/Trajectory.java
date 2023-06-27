package analysis;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;

/**
 * Show trajectory in the (x_n, x_{n+1}) plane
 * 
 * Output to Logistic-lambda-from-x_0.txt
 *
 * @author tadaki
 */
public class Trajectory extends AbstractAnalysis {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {
//        double lambda = (1+2*Math.sqrt(2))/4-0.001;
//        double lambda = 0.961;
//        double lambda = 0.864;
//        double lambda=0.957;
        double lambda = 0.9;
        boolean afterRelax = true;
        Trajectory sys = new Trajectory(lambda);
        double initX = .1;
        int relaxTime = 100;
        int numIteration = 100;
        String filename = "Logistic-"+lambda+"-from"+initX+".txt";
        sys.openOutput(filename);
        sys.header();
        sys.header("lambda",lambda);
        if (!afterRelax) {
            sys.doExec(initX, numIteration);
        } else {
            sys.relax(relaxTime);
            initX = sys.getX();
            sys.doExec(initX, numIteration);
        }
        sys.close();
    }

    /**
     * Constructor
     *
     * @param lambda
     * @throws IOException
     */
    public Trajectory(double lambda) throws IOException {
        super(lambda);
    }

    /**
     * 系を動作させ、PDFを生成する
     *
     * @param initX
     * @param numIteration
     */
    public void doExec(double initX, int numIteration){
        List<Point2D.Double> pList = logistic.evalOrbit(initX, numIteration);
        for(Point2D.Double p:pList){
            this.output(p.x, p.y);
        }
    }
}
