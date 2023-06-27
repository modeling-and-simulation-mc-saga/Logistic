package analysis.tangent;

import analysis.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Logistic;

/**
 *
 * @author tadaki
 */
public class TimeSequence extends AbstractAnalysis {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {
        double lambda = 0.957;
        TimeSequence sys = new TimeSequence(lambda);
        int numIteration = 5;
        int numUpdate = 100;
        sys.relax(100);
        sys.doExec(numIteration, numUpdate);
    }

    public TimeSequence(double lambda) throws IOException {
        super(lambda);
    }

    private List<Point2D.Double> tangentOrbit(double xInit, int numUpdate) {
        List<Point2D.Double> pList = Collections.synchronizedList(new ArrayList<>());
        logistic.setInitX(xInit);
        double x = logistic.getX();
        for (int t = 0; t < numUpdate; t++) {
            pList.add(new Point2D.Double(t, x));
            logistic.update();
            logistic.update();
            x = logistic.update();
        }
        return pList;

    }

    public void doExec(int numIteration, int numUpdate) throws IOException {
        double xInit = 0.2;
//        double xInit2 = 0.2001;
        String filename = "PrintMotion-" + String.valueOf(lambda) + ".txt";
        this.openOutput(filename);
        this.header();
        this.header("lambda",lambda);
        List<Point2D.Double> pList = tangentOrbit(xInit, numUpdate);
        double f[] = new double[numIteration];
        f[0] = 0.5;
        for (int i = 1; i < f.length; i++) {
            f[i] = Logistic.map(f[i - 1], lambda);
        }

            outputData(pList);
            this.close();
        }
    

    private void outputData(List<Point2D.Double> list)
            throws IOException {
        for (Point2D.Double p : list) {
            this.output(p.x, p.y);
        }
    }

}
