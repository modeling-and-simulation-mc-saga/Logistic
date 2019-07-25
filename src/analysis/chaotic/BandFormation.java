package analysis.chaotic;

import analysis.AbstractAnalysis;
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
public class BandFormation extends AbstractAnalysis {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        double lambda = 0.9;
        int numIteration = 20;
        int numPoint = 200;
        BandFormation sys = new BandFormation(lambda);
        sys.doExec(numPoint, numIteration);
    }

    public BandFormation(double lambda) throws IOException {
        super(lambda);
    }

    public void doExec(int numPoint, int numIteration) throws IOException {
        List<Point2D.Double> data = Utils.createList();
        List<Double> current = Utils.createList();
        List<Point2D.Double> mark0 = Utils.createList();
        List<Point2D.Double> mark1 = Utils.createList();
        int t = 1;
        double dx = 1. / numPoint;
        for (int i = 1; i < numPoint; i++) {
            current.add(dx * i);
        }
        while (t < numIteration) {
            for (Double d : current) {
                data.add(new Point2D.Double(d, (double) t));
            }
            mark0.add(new Point2D.Double(current.get(0), (double) t));
            mark1.add(new Point2D.Double(current.get(1), (double) t));
            for (int i = 0; i < current.size(); i++) {
                double d = current.get(i);
                double y = Logistic.map(d, lambda);
                current.set(i, y);
            }
            t++;
        }

        double f[] = new double[5];
        double x = 0.5;
        for (int i = 0; i < 4; i++) {
            double y = Logistic.map(x, lambda);
            f[i + 1] = y;
            x = y;
        }
        String commands[] = gnuplotCommands(numIteration, f);
        try (BufferedWriter outG = gnuplot.getWriter()) {
            for (String s : commands) {
                outG.write(s);
                outG.newLine();
            }
            plotData(data, outG);
            plotData(mark0, outG);
            plotData(mark1, outG);
            outG.flush();

        }
    }

    private void plotData(List<Point2D.Double> pList, BufferedWriter out) throws IOException {
        for (Point2D.Double p : pList) {
            out.write(p.x + " " + p.y);
            out.newLine();
        }
        out.write("end");
        out.newLine();
    }

    private String[] gnuplotCommands(int numIteration, double f[]) {
        String label = "{/Symbol l}=" + lambda;
        String filename = BandFormation.class.getSimpleName() + "-"
                + String.valueOf(lambda) + ".pdf";

        String lines[] = {
            "set terminal pdfcairo enhanced color solid "
            + "size 29cm,21cm font \"Times-New-Roman\" fontscale 1",
            "set size square",
            "set xrange [0:1]",
            "set yrange [0:" + numIteration + "]",
            "set xtic 0.1",
            "set ytic 1",
            "set xlabel \"{/:Italic x}\"",
            "set ylabel \"{/:Italic t}\"",
            "set border lw 3",
            "set output \"" + filename + "\"",};
        List<String> sLines = Utils.createList();
        for (String s : lines) {
            sLines.add(s);
        }
        for (int i = 1; i < f.length; i++) {
            String s = "set arrow " + i + " from " + f[i] + ",0 to " + f[i] + "," + numIteration + " nohead";
            sLines.add(s);
        }
        String plotCommand = "plot \"-\" pt 7 ps .5 notitle,"
                + "\"-\" pt 5 ps 2 lc \"black\" notitle,"
                + "\"-\" pt 5 ps 2 lc \"red\" notitle";
        sLines.add(plotCommand);
        return sLines.toArray(new String[sLines.size()]);
    }
}
