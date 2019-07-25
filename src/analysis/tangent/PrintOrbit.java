package analysis.tangent;

import analysis.*;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import model.Logistic;
import myLib.Utils;

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
        boolean afterRelax=true;
        double lambda=0.957;
        PrintOrbit sys = new PrintOrbit(lambda);
        double initX = .45;
        if(afterRelax){
            sys.relax(100);
            initX = sys.getX();
        }
        int numIteration = 40;
        int period = 3;
        sys.setMagnify(true);
        sys.doExec(initX, numIteration, period);
        System.err.println(sys.getError());
    }

    private boolean magnify=false;
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
        List<Point2D.Double> pList = tangentOrbit(initX, numIteration);
        String gnuplotCommandsLines[] = gnuplotCommands(period);

        try (BufferedWriter outG = gnuplot.getWriter()) {
            for (String s : gnuplotCommandsLines) {
                outG.write(s);
                outG.newLine();
            }
            for (Point2D.Double p:pList) {
                outG.write(p.x + " " + p.y);
                outG.newLine();
            }
            outG.write("end");
            outG.newLine();
            outG.flush();
        }
    }

    private List<Point2D.Double> tangentOrbit(double initial,int numIteration){
                List<Point2D.Double> pList = Utils.createList();
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
    private String[] gnuplotCommands(int period) {
        String label = "{/Symbol l}=" + lambda;
        String filename = Logistic.class.getSimpleName() + "-"
                + String.valueOf(lambda) + ".pdf";
        if(magnify){
            filename = Logistic.class.getSimpleName() + "-"
                + String.valueOf(lambda) + "-magnify.pdf";
        }
        //Logistic写像の文字列生成
        String func = "f(x)";
        for (int i = 1; i < period; i++) {
            func = "f(" + func + ")";
        }
        String range = "[0:1]";
        String tic = "0.5";
        if(magnify){
            range = "[0.47:0.57]";
            tic = "0.02";
        }
        String lines[] = {
            "set terminal pdfcairo enhanced color solid "
            + "size 21cm,21cm font \"Times-New-Roman\" fontscale 0.8",
            "set size square",
            "set xrange "+range,
            "set yrange "+range,
            "set xtic "+tic,
            "set ytic "+tic,
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

    public void setMagnify(boolean magnify) {
        this.magnify = magnify;
    }

}
