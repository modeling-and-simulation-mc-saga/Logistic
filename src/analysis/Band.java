package analysis;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import model.Logistic;

/**
 *
 * @author tadaki
 */
public class Band extends AbstractAnalysis{

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {
//        double lambda = (1+2*Math.sqrt(2))/4-0.001;
        double lambda = 0.9;
        Band sys = new Band(lambda);
        int numUpdate = 50;
        sys.doExec(numUpdate);
        System.err.println(sys.getError());
    }

    private final Logistic logistic;

    public Band(double lambda) throws IOException {
        super(lambda);
        logistic = new Logistic(lambda);
        for (int t = 0; t < 100; t++) {
            logistic.update();
        }
    }

    public void doExec(int numUpdate) throws IOException {
        double y[] = new double[6];
        double x = 0.5;
        for (int i = 1; i < y.length; i++) {
            x = Logistic.map(x, lambda);
            y[i] = x;
        }
        y[0] = 0.5;
        List<Point2D.Double> pList = 
                logistic.evalOrbit(logistic.getX(), numUpdate);
        String commands[] = gnuplotCommands(y);
        try (BufferedWriter outG = gnuplot.getWriter()) {
            for (String s : commands) {
                outG.write(s);
                outG.newLine();
            }
            outG.write("plot \"-\" with line lw 1 notitle,"
                    + "\"-\" with line lt -1 lw 1 notitle,"
                    + "x with line lt 8 lw 1 notitle, "
                    + "f(x) with line lt -1 lw 1 notitle,"
            );
            outG.newLine();
            for (Point2D.Double p : pList) {
                outG.write(p.x + " " + p.y);
                outG.newLine();
            }
            outG.write("end");
            outG.newLine();
            Double yy[] = {y[2], 0.5, y[4], y[3], y[1]};
            for (Double d : yy) {
                outG.write(d + " 0");
                outG.newLine();
                double z = Logistic.map(d, lambda);
                outG.write(d + " " + z);
                outG.newLine();
                outG.write("0 " + z);
                outG.newLine();
                outG.newLine();
                outG.write("0 " + z);
                outG.newLine();
                outG.write(z + " " + z);
                outG.newLine();
                outG.newLine();
            }
            outG.write("end");
            outG.newLine();
            outG.flush();
        }
    }

    private String[] gnuplotCommands(double y[]) {
        String label = "{/Symbol l}=" + lambda;
        String filename = Band.class.getSimpleName() + "-"
                + String.valueOf(lambda) + ".pdf";
        String lines[] = {
            "set terminal pdfcairo enhanced color solid "
            + "size 21cm,21cm font \"Times-New-Roman\" fontscale 0.8",
            "set size square",
            "set xrange [0:1]",
            "set yrange [0:1]",
            "set xtic 0.5",
            "set ytic 0.5",
            "set border lw 3",
            "set xlabel \"{/:Italic x_n}\"",
            "set ylabel \"{/:Italic x}"
            + "_{{/:Italic n}+1}}\"",
            "a=4*" + lambda,
            "f(x) = a*x*(1-x)",
            "set output \"" + filename + "\"",
            "set label \"" + label + "\" at 0.6,0.95",
            "set object 1 rect from " + y[2] + ",0 to "
            + y[0] + ",0.02 fc rgb \"red\"",
            "set object 2 rect from " + y[0] + ",0 to "
            + y[4] + ",0.02 fc rgb \"green\"",
            "set object 3 rect from " + y[4] + ",0 to "
            + y[3] + ",0.02 fc rgb \"blue\"",
            "set object 4 rect from " + y[3] + ",0 to "
            + y[1] + ",0.02 fc rgb \"orange\"",
            "set object 5 rect from 0.02," + y[3] + " to 0.04,"
            + y[1] + " fc rgb \"red\"",
            "set object 6 rect from 0," + y[5] + " to 0.02,"
            + y[1] + " fc rgb \"green\"",
            "set object 7 rect from 0," + y[4] + " to 0.02,"
            + y[5] + " fc rgb \"blue\"",
            "set object 8 rect from 0," + y[2] + " to 0.02,"
            + y[4] + " fc rgb \"orange\""

        };
        return lines;
    }

}
