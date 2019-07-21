package analysis;

import histogram.Histogram;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import model.Logistic;
import utils.Gnuplot;

/**
 *
 * @author tadaki
 */
public class DistributionMain {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        int tRelax = 100;//緩和までのステップ
        int tmax = 100000;//ヒストグラムのための繰り返し回数
        double lambda = 0.9;
        double initX = 0.5;
        int numBin = 1000;//ビンの数
        DistributionMain sys = new DistributionMain(lambda, initX, tRelax);
        sys.doExec(tmax, numBin);
    }

    private final double lambda;
    private final Logistic sys;

    public DistributionMain(double lambda, double initX, int tRelax) {
        this.lambda = lambda;
        sys = new Logistic(lambda, initX);
        sys.initialize();
        for (int t = 0; t < tRelax; t++) {//緩和
            sys.update();
        }
    }

    public void doExec(int tmax, int numBin) throws IOException {
        List<Point2D.Double> plist = generateHistogram(tmax, numBin);
        Gnuplot gnuplot = new Gnuplot(".");
        String commands[] = gnuplotCommands();
        try (BufferedWriter outG = gnuplot.getWriter()) {
            for (String s : commands) {
                outG.write(s);
                outG.newLine();
            }
            double x = 0.5;
            for (int i = 1; i <= 6; i++) {
                x = Logistic.map(x, lambda);
                String s = "set arrow " + i + " from " + x + ",28 to " + x + ",26";
                outG.write(s);
                outG.newLine();
                double xx = x - 0.01;
                s = "set label \"" + i + "\" at " + xx + ",28.5";
                outG.write(s);
                outG.newLine();
                System.out.println(x);
            }

            outG.write("plot \"-\" with boxes notitle");
            outG.newLine();
            for (Point2D.Double p : plist) {
                outG.write(p.x + " " + p.y);
                outG.newLine();
            }
            outG.write("end");
            outG.newLine();
            outG.flush();
        }
    }

    private List<Point2D.Double> generateHistogram(int tmax, int numBin) {
        //ヒストグラムの定義
        Histogram histogram = new Histogram(0., 1., numBin);
        for (int t = 0; t < tmax; t++) {
            double x = sys.update();//状態を更新
            histogram.put(x);//ヒストグラムへ登録
        }
        return histogram.getHistogram();
    }

    private String[] gnuplotCommands() {
        String filename = "dist-" + String.valueOf(lambda) + ".pdf";
        String label = "{/Symbol l}=" + lambda;
        String lines[] = {
            "set terminal pdfcairo enhanced color solid "
            + "size 29cm,21cm font \"Times-New-Roman\" fontscale 0.8",
            "set xrange [0:1]",
            "set output \"" + filename + "\"",
            "set label \"" + label + "\" at 0.2,20"
        };
        return lines;
    }

}
