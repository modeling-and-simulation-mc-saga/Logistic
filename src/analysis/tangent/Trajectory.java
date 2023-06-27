package analysis.tangent;

import analysis.*;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import model.Logistic;

/**
 * Show orbits
 *
 * @author tadaki
 */
public class Trajectory extends AbstractAnalysis {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {
        boolean afterRelax = true;
        double lambda = 0.957;
        double initX = .45;
        Trajectory sys = new Trajectory(lambda);
        String filename = "Logistic-"+lambda+"-from"+initX+"-magnify.txt";
        sys.openOutput(filename);
        sys.header();
        sys.header("lambda",lambda);
        if (afterRelax) {
            sys.relax(100);
            initX = sys.getX();
        }
        int numIteration = 40;
        int period = 3;
        sys.setMagnify(true);
        sys.doExec(initX, numIteration, period);
    }

    private boolean magnify = false;

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
     * @param period
     * @throws IOException
     */
    public void doExec(double initX, int numIteration, int period)
            throws IOException {
        List<Point2D.Double> pList = tangentOrbit(initX, numIteration);

        for (Point2D.Double p : pList) {
            this.output(p.x, p.y);
        }
        this.close();
    }

    private List<Point2D.Double> tangentOrbit(double initial, int numIteration) {
        List<Point2D.Double> pList = Collections.synchronizedList(new ArrayList<>());
        logistic.setInitX(initial);
        double x = logistic.getX();
        pList.add(new Point2D.Double(x, x));
        for (int i = 0; i < numIteration; i++) {
            double xx = logistic.getX();
            logistic.update();
            logistic.update();
            double y = logistic.update();
            pList.add(new Point2D.Double(xx, y));
            pList.add(new Point2D.Double(y, y));
        }
        return pList;

    }


    public void setMagnify(boolean magnify) {
        this.magnify = magnify;
    }

}
