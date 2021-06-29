package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import myLib.utils.FileIO;

/**
 * gnuplotを用いて、直接作図しPDFを生成する
 *
 * @author tadaki
 */
public class Gnuplot {

    public static final String nl = System.getProperty("line.separator");
    private static final String GnuplotCommand="/opt/homebrew/bin/gnuplot";
    private Process process;
    private final BufferedWriter out;
    private final BufferedReader err;

    /**
     * 指定したディレクトリに出力
     *
     * @throws IOException
     */
    public Gnuplot(String dir) throws IOException {
        out = openGnuplot(dir);
        err = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));
    }

    public Gnuplot() throws IOException {
        this(".");
    }

    /**
     * open writer to gnuplot
     *
     * @param outputfile
     * @return
     * @throws IOException
     */
    private BufferedWriter openGnuplot(String dir)
            throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(GnuplotCommand).
                directory(new File(dir));
        process = processBuilder.start();
        return new BufferedWriter(
                new OutputStreamWriter(process.getOutputStream()));
    }

    private BufferedWriter openGnuplot()
            throws IOException {
        return openGnuplot(".");
    }

    public void readHeader(String filename) throws IOException {
        BufferedReader in = FileIO.openReader(filename);
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = in.readLine()) != null) {
            sb.append(line).append(nl);
        }
        out.write(sb.toString());
    }

    /**
     *
     * @param filename
     * @throws IOException
     */
    public void setOutput(String filename) throws IOException {
        out.write("set output \"");
        out.write(filename);
        out.write("\"");
        out.newLine();
    }

    public BufferedWriter getWriter() {
        return out;
    }

    public BufferedReader getError() {
        return err;
    }

    public void close() throws IOException {
        out.close();
    }

    /**
     * サンプル
     *
     * @param args
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {
        String dir = ".";//作業ディレクトリの指定
        Gnuplot gnuplot = new Gnuplot(dir);
        String header[]={
            "set terminal pdfcairo enhanced color solid "
            + "size 29cm,21cm font \"Times-New-Roman\" fontscale 1.2",
            "set xrange [0:20]",
            "set yrange [-1:1]",
            "set title \"TITLE\""
        };
        try ( BufferedWriter outG = gnuplot.getWriter()) {
            for(String s:header){
                outG.write(s);
                outG.newLine();
            }
            outG.write("set output \"sample.pdf\"");
            outG.newLine();
            outG.write("plot \"-\" title \"data\", sin(x) with line notitle");
            //出力データをgnuplotヘ送る
            for (int i = 0; i < 100; i++) {
                double x = 0.2 * i;
                outG.write(String.valueOf(x) + " " + String.valueOf(Math.sin(x)));
                outG.newLine();
            }
            outG.write("end");//データ終了
            outG.newLine();
            outG.flush();
        }
    }
}
