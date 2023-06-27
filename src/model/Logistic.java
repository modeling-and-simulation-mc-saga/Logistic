package model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Logistic Map
 *
 * @author tadaki
 */
public class Logistic {

    private double lambda;
    private double initX;
    private double x;

    /**
     * コンストラクタ
     *
     * @param lambda パラメタ[0,1]
     * @param initX 初期値
     */
    public Logistic(double lambda, double initX) {
        this.lambda = lambda;
        this.initX = initX;
        x = initX;
    }

    public Logistic(double lambda) {
        this(lambda, 0.1);
    }

    /**
     * 初期化：xを初期値に設定
     */
    public void initialize() {
        x = initX;
    }

    /**
     * 状態更新
     *
     * @return 新しいxの値
     */
    public double update() {
        x = map(x, lambda);
        return x;
    }

    /**
     * Logistic map
     *
     * @param x
     * @param lambda
     * @return
     */
    static public double map(double x, double lambda) {
        return 4. * lambda * x * (1. - x);
    }

    //***** setters and getters **************
    public void setInitX(double initX) {
        this.initX = initX;
    }

    public double getX() {
        return x;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double getLambda() {
        return lambda;
    }

    /**
     * x_n-x_(n+1) 空間内の動き
     *
     * @param n
     * @return
     */
    public List<Point2D.Double> evalOrbit(int n) {
        return evalOrbit(initX, n);
    }

    public List<Point2D.Double> evalOrbit(double initial, int n) {
        List<Point2D.Double> pList = Collections.synchronizedList(new ArrayList<>());
        x = initial;
        pList.add(new Point2D.Double(x, x));
        for (int i = 0; i < n; i++) {
            double xx = x;
            double y = update();
            pList.add(new Point2D.Double(xx, y));
            pList.add(new Point2D.Double(y, y));
        }
        return pList;
    }

    /**
     * 時間変化
     *
     * @param n
     * @return
     */
    public List<Point2D.Double> timeEvolution(int n) {
        return timeEvolution(initX, n);
    }

    public List<Point2D.Double> timeEvolution(double initial, int n) {
        List<Point2D.Double> pList = Collections.synchronizedList(new ArrayList<>());
        x = initial;
        for (int t = 0; t < n; t++) {
            pList.add(new Point2D.Double(t, x));
            update();
        }
        return pList;
    }
}
