package analysis.tangent;

import analysis.*;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import model.Logistic;
import myLib.Utils;

/**
 *
 * @author tadaki
 */
public class PrintMotion extends AbstractAnalysis {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {
        double lambda = 0.957;
        PrintMotion sys = new PrintMotion(lambda);
        int numIteration = 5;
        int numUpdate = 100;
        sys.relax(100);
        sys.doExec(numIteration, numUpdate);
        System.err.println(sys.getError());
    }

    public PrintMotion(double lambda) throws IOException {
        super(lambda);
    }

    private List<Point2D.Double> tangentOrbit(double xInit, int numUpdate) {
        List<Point2D.Double> pList = Utils.createList();
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
        double xInit2 = 0.2001;
        List<Point2D.Double> pList = tangentOrbit(xInit, numUpdate);
        double f[] = new double[numIteration];
        f[0] = 0.5;
        for (int i = 1; i < f.length; i++) {
            f[i] = Logistic.map(f[i - 1], lambda);
        }

        String commands[] = gnuplotCommands();
        try (BufferedWriter outG = gnuplot.getWriter()) {
            for (String s : commands) {
                outG.write(s);
                outG.newLine();
            }
            outG.write("plot \"-\" with line lw 2 notitle");
            outG.newLine();
            outputData(pList, outG);
            outG.flush();
        }
    }

    private void outputData(List<Point2D.Double> list, BufferedWriter out)
            throws IOException {
        for (Point2D.Double p : list) {
            out.write(p.x + " " + p.y);
            out.newLine();
        }
        out.write("end");
        out.newLine();
    }

    private String[] gnuplotCommands() {
        String label = "{/Symbol l}=" + lambda;
        String filename = PrintMotion.class.getSimpleName() + "-"
                + String.valueOf(lambda) + ".pdf";
        String lines[] = {
            "set terminal pdfcairo enhanced color solid "
            + "size 29cm,21cm font \"Times-New-Roman\" fontscale 0.8",
            "set yrange [0:1]",
            "set xlabel \"{/:Italic t}\"",
            "set ylabel \"{/:Italic x}\"",
            "set output \"" + filename + "\"",
            "set label \"" + label + "\" at 20,0.1 "
        };
        return lines;
    }

}
