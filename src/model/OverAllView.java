package model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utils.Gnuplot;

/**
 *
 * @author tadaki
 */
public class OverAllView {

    public static void main(String args[]) throws IOException {
        double fromLambda = 0.7;
        OverAllView sys = new OverAllView();
        sys.doExec(fromLambda, 1.);
    }

    private boolean showSuperStableLines = false;
    private final int numPoints = 1000;
    private final int numUpdate = 100;

    public OverAllView() throws IOException {

    }

    public void doExec(double fromLambda) throws IOException {
        doExec(fromLambda, 1.);
    }

    public void doExec(double fromLambda, double toLambda) throws IOException {
        String commands[] = gnuplotCommands(fromLambda, toLambda);
        Gnuplot gnuplot = new Gnuplot(".");
        try (BufferedWriter outG = gnuplot.getWriter()) {
            for (String s : commands) {
                outG.write(s);
                outG.newLine();
            }
            for (int i = 1; i <= numPoints; i++) {
                double lambda = (toLambda - fromLambda) * i / numPoints
                        + fromLambda;
                Logistic logistic = new Logistic(lambda);
                for (int j = 0; j < numPoints; j++) {
                    logistic.update();
                }
                for (int j = 0; j < numUpdate; j++) {
                    double y = logistic.update();
                    outG.write(lambda + " " + y);
                    outG.newLine();
                }
            }
            outG.write("end");
            outG.newLine();
            if (showSuperStableLines) {
                for (int k = 1; k < 6; k++) {
                    for (int i = 1; i < numPoints; i++) {
                        double lambda = (toLambda - fromLambda) * i / numPoints
                                + fromLambda;
                        double x = 0.5;
                        for (int j = 0; j < k; j++) {
                            x = Logistic.map(x, lambda);
                        }
                        outG.write(lambda + " " + x);
                        outG.newLine();
                    }
                    outG.newLine();
                }
                outG.write("end");
                outG.newLine();
            }
            outG.flush();
        }
    }

    public void setShowSuperStableLines(boolean showSuperStableLines) {
        this.showSuperStableLines = showSuperStableLines;
    }

    private String[] gnuplotCommands(double fromLambda, double toLambda) {
        String filename = OverAllView.class.getSimpleName() + ".pdf";

        String lines[] = {
            "set terminal pdfcairo enhanced color solid "
            + "size 29cm,21cm font \"Times-New-Roman\" fontscale 0.8",
            "set output \"" + filename + "\"",
            "set xlabel \"{/Symbol l}\"",
            "set ylabel \"{/:Italic x}\"",
            "set xrange [" + String.valueOf(fromLambda)
            + ":" + String.valueOf(toLambda) + "]",
            "set yrange [0:1]",
            "set xtic 0.1",
            "set border lw 3",};
        List<String> commands = new ArrayList<>();
        for (String s : lines) {
            commands.add(s);
        }
        if (showSuperStableLines) {
            commands.add("set label 2 \"2\" at 0.9,0.2");
            commands.add("set label 3 \"3\" at 0.89,0.7");
            commands.add("set label 4 \"4\" at 0.85,0.55");
        }
        String line = "plot \"-\" pt 7 ps 0.1 notitle";
        if (showSuperStableLines) {
            line += ",\"-\" with line lt -1 lw 2 notitle";
        }
        commands.add(line);
        return commands.toArray(new String[commands.size()]);
    }

}
