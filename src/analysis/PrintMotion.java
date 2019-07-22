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
public class PrintMotion extends AbstractAnalysis {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {
        boolean chaos = true;
//        double lambda = (1.+2*Math.sqrt(2.))/4-0.00005;
        double lambda = 0.9;
        PrintMotion sys = new PrintMotion(chaos, lambda);
        int numIteration = 5;
        int numUpdate = 60;
        sys.doExec(numIteration, numUpdate);
        System.err.println(sys.getError());
    }

    private final boolean chaos;

    public PrintMotion(boolean chaos, double lambda) throws IOException {
        super(lambda);
        this.chaos = chaos;
    }

    public void doExec(int numIteration, int numUpdate) throws IOException {
        double xInit = 0.2;
        double xInit2 = 0.2001;
        List<Point2D.Double> pList = logistic.timeEvolution(xInit, numUpdate);
        List<Point2D.Double> pList2 = logistic.timeEvolution(xInit2, numUpdate);
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
            outG.write("plot \"-\" with line lw 2 notitle,");
            outG.write("\"-\" with line lw 2 notitle");
            if (chaos) {
                for (int i = 1; i < f.length; i++) {
                    outG.write("," + f[i] + " with line lt " + i + "notitle");
                }
            }
            outG.newLine();
            outputData(pList, outG);
            outputData(pList2, outG);
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
