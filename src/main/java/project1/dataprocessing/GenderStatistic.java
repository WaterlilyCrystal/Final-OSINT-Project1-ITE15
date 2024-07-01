package project1.dataprocessing;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenderStatistic extends project1.dataprocessing.ChartGenerator {

    private Map<String, Integer> genderCount;
    private static final Map<Integer, String> genderLabelMap = Map.of(
            0, "unspecified",
            1, "male",
            2, "female",
            3, "other"
    );

    public GenderStatistic(String csvFile) {
        super(csvFile);
        this.genderCount = new HashMap<>();
    }

    @Override
    public void generateChart() {
        try (FileReader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                int genderCode = Integer.parseInt(csvRecord.get("Gender"));
                String gender = genderLabelMap.getOrDefault(genderCode, "unknown");
                genderCount.put(gender, genderCount.getOrDefault(gender, 0) + 1);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        CategoryChart chart = createChart();
        displayChart(chart);
    }

    private CategoryChart createChart() {
        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title("Gender Distribution")
                .xAxisTitle("Gender")
                .yAxisTitle("Count")
                .theme(ChartTheme.XChart)
                .build();

        chart.addSeries("Count", new ArrayList<>(genderCount.keySet()), new ArrayList<>(genderCount.values()));

        chart.getStyler().setLegendPosition(LegendPosition.InsideNW);

        return chart;
    }

    @Override
    protected String getChartName() {
        return "gender_statistics_chart";
    }
}
