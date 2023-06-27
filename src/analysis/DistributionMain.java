package analysis;

import analysis.histogram.Histogram;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import model.Logistic;

/**
 *
 * @author tadaki
 */
public class DistributionMain extends AbstractAnalysis{

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
        DistributionMain sys = new DistributionMain(lambda, initX);
        String filename="dist-"+String.valueOf(lambda)+".txt";
        sys.openOutput(filename);
        sys.header();
        sys.header("lambda",lambda);
        sys.header("numBin",numBin);
        sys.relax(tRelax);
        sys.doExec(tmax, numBin);
        sys.close();
    }
    
    public DistributionMain(double lambda, double initX) 
            throws IOException {
        super(lambda);
        logistic.setInitX(initX);
        logistic.initialize();
    }

    public void doExec(int tmax, int numBin) throws IOException {
        List<Point2D.Double> plist = generateHistogram(tmax, numBin);
            for (Point2D.Double p : plist) {
                this.output(p.x, p.y);
        }
    }

    private List<Point2D.Double> generateHistogram(int tmax, int numBin) {
        //ヒストグラムの定義
        Histogram histogram = new Histogram(0., 1., numBin);
        for (int t = 0; t < tmax; t++) {
            double x = logistic.update();//状態を更新
            histogram.put(x);//ヒストグラムへ登録
        }
        return histogram.getHistogram();
    }

}
