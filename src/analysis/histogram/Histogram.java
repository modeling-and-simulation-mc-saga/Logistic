package analysis.histogram;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ヒストグラム
 *
 * @author tadaki
 */
public class Histogram {

    private final double a;//範囲の下限
    private final double b;//範囲の上限
    private final double w;//bin の幅
    private final int hist[];//ヒストグラム

    public Histogram(double a, double b, int M) {
        this.a = a;
        this.b = b;
        w = (b - a) / M;
        hist = new int[M];
    }

    /**
     * 値を一つ登録する
     *
     * @param x
     * @return
     */
    public boolean put(double x) {
        if (x < a || x >= b) {
            return false;
        }
        //xが入るべきbinの番号を調べる
        int k = (int) ((x - a) / w);
        //bin のカウントを一つ増やす
        hist[k]++;
        return true;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public int[] getHist() {
        return hist;
    }

    /**
     * 結果をリストとして取得する
     *
     * @return
     */
    public List<Point2D.Double> getHistogram() {
        List<Point2D.Double> plist = Collections.synchronizedList(new ArrayList<>());
        //カウントの総和を求める
        int sum = 0;
        for (int i = 0; i < hist.length; i++) {
            sum += hist[i];
        }
        
        for (int i = 0; i < hist.length; i++) {
            double x = a + i * w + w / 2.;//binの中央値
            double y = (double) hist[i] / sum / w;//binに入る割合
            plist.add(new Point2D.Double(x, y));
        }
        return plist;
    }
}
