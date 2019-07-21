package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import myLib.FileIO;

/**
 *
 * @author tadaki
 */
public class Gnuplot {

    public static final String nl = System.getProperty("line.separator");
    private Process process;
    private final BufferedWriter out;

    public Gnuplot() throws IOException {
        this(".");
    }

    public Gnuplot(String dir) throws IOException {
        out = openGnuplot(dir);
    }

    /**
     * open writer to gnuplot
     *
     * @param outputfile
     * @return
     * @throws IOException
     */
    private BufferedWriter openGnuplot()
            throws IOException {
        return openGnuplot(".");
    }

    private BufferedWriter openGnuplot(String dir)
            throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("gnuplot.exe").
                directory(new File(dir));
        process = processBuilder.start();
        return new BufferedWriter(
                new OutputStreamWriter(process.getOutputStream()));
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
        String dir = "work";//作業ディレクトリの指定
        Gnuplot gnuplot = new Gnuplot(dir);
        //gnuplotスクリプトの最初の部分を読み込み
        gnuplot.readHeader(dir + File.separator + "sample-template.plt");
        //出力ファイル指定
        gnuplot.setOutput("sample.eps");
        try (BufferedWriter outG = gnuplot.getWriter()) {
            outG.write("set xrange[0:20]");
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
