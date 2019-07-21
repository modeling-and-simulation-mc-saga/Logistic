package analysis;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import model.Logistic;

/**
 * Show orbits
 *
 * @author tadaki
 */
public class PrintOrbit extends AbstractAnalysis {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {
//        double lambda = (1+2*Math.sqrt(2))/4-0.001;
//        double lambda = 0.961;
        double lambda = 0.864;
        PrintOrbit sys = new PrintOrbit(lambda);
        double initX = .1;
        int numIteration = 40;
        int period = 1;
        sys.doExec(initX, numIteration, period);
        System.err.println(sys.getError());
    }

    /**
     * Constructor
     *
     * @param lambda
     * @throws IOException
     */
    public PrintOrbit(double lambda) throws IOException {
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
        Logistic logistic = new Logistic(lambda);
        List<Point2D.Double> pList = logistic.evalOrbit(initX, numIteration);
        String gnuplotCommandsLines[] = gnuplotCommands(period);

        try (BufferedWriter outG = gnuplot.getWriter()) {
            for (String s : gnuplotCommandsLines) {
                outG.write(s);
                outG.newLine();
            }
            for (Point2D.Double p : pList) {
                outG.write(p.x + " " + p.y);
                outG.newLine();
            }
            outG.write("end");
            outG.newLine();
            outG.flush();
        }
    }

    private String[] gnuplotCommands(int period) {
        String label = "{/Symbol l}=" + lambda;
        String filename = Logistic.class.getSimpleName() + "-"
                + String.valueOf(lambda) + ".pdf";

        //Logistic写像の文字列生成
        String func = "f(x)";
        for (int i = 1; i < period; i++) {
            func = "f(" + func + ")";
        }
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
            "set ylabel \"{/:Italic x}_{{/:Italic n}+1}\"",
            "a=4*" + lambda,
            "f(x) = a*x*(1-x)",
            "set output \"" + filename + "\"",
            "set label \"" + label + "\" at 0.6,0.1",
            "plot \"-\" with line lw 3 notitle,"
            + "x with line lt 8 lw 3 notitle"
            + ", " + func + " with line lt 8 lw 3 notitle"
        };
        return lines;
    }

}
