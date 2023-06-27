package analysis;

import java.io.IOException;

/**
 *
 * @author tadaki
 */
public class OverAllView extends AbstractAnalysis {

    public static void main(String args[]) throws IOException {
//        double fromLambda = 0.7;
        double fromLambda = 0.95;
        OverAllView sys = new OverAllView();
        sys.doExec(fromLambda, 1.);
    }

    private final int numPoints = 1024;
    private final int numUpdate = 128;

    public OverAllView() throws IOException {
        super(0.);
    }

    public void doExec(double fromLambda) throws IOException {
        doExec(fromLambda, 1.);
    }

    public void doExec(double fromLambda, double toLambda) throws IOException {
        String filename = "OverAllView-" + String.valueOf(fromLambda)
                + "-" + String.valueOf(toLambda) + ".txt";
        this.openOutput(filename);
        for (int i = 1; i <= numPoints; i++) {
            double lambda = (toLambda - fromLambda) * i / numPoints
                    + fromLambda;
            logistic.setLambda(lambda);
            logistic.initialize();
            for (int j = 0; j < numPoints; j++) {
                logistic.update();
            }
            for (int j = 0; j < numUpdate; j++) {
                double y = logistic.update();
                output(lambda, y);
            }
        }

        this.close();
    }

}
