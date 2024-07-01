package project1.dataprocessing;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.internal.chartpart.Chart;


import java.io.IOException;

public abstract class ChartGenerator {
    protected String csvFile;

    protected ChartGenerator(String csvFile) {
        this.csvFile = csvFile;
    }

    public abstract void generateChart();

    protected void displayChart(Chart chart) {
        try {
            BitmapEncoder.saveBitmap(chart, getChartName(), BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new SwingWrapper<>(chart).displayChart();
    }

    protected abstract String getChartName();

}
