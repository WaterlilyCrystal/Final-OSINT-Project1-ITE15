package project1.dataprocessing;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;


public class ScoreDistribution extends ChartGenerator{

    private LinkedHashMap<String, Integer> scoreRanges;
    static final String LEVEL1="0-100";
    static final String LEVEL2="100-200";
    static final String LEVEL3="200-500";
    static final String LEVEL4="500-1000";
    static final String LEVEL5=">1000";

    public ScoreDistribution(String csvFile) {
        super(csvFile);

        scoreRanges = new LinkedHashMap<>();
        scoreRanges.put(LEVEL1, 0);
        scoreRanges.put(LEVEL2, 0);
        scoreRanges.put(LEVEL3, 0);
        scoreRanges.put(LEVEL4, 0);
        scoreRanges.put(LEVEL5, 0);
    }

    @Override
    public void generateChart() {
        try (FileReader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord table : csvParser) {
                int score = Integer.parseInt(table.get("Score"));
                updateScoreRange(score);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        CategoryChart chart = createChart();
        displayChart(chart);
    }

    private void updateScoreRange(int score) {
        if (score >= 0 && score < 100) {
            scoreRanges.put(LEVEL1, scoreRanges.get(LEVEL1) + 1);
        } else if (score >= 100 && score < 200) {
            scoreRanges.put(LEVEL2, scoreRanges.get(LEVEL2) + 1);
        } else if (score >= 200 && score < 500) {
            scoreRanges.put(LEVEL3, scoreRanges.get(LEVEL3) + 1);
        } else if (score >= 500 && score < 1000) {
            scoreRanges.put(LEVEL4, scoreRanges.get(LEVEL4) + 1);
        } else {
            scoreRanges.put(LEVEL5, scoreRanges.get(LEVEL5) + 1);
        }
    }

    private CategoryChart createChart() {
        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title("Score Distribution")
                .xAxisTitle("Score Ranges")
                .yAxisTitle("Count")
                .build();

        ArrayList<String> xData = new ArrayList<>(scoreRanges.keySet());
        ArrayList<Integer> yData = new ArrayList<>(scoreRanges.values());

        chart.addSeries("Score Distribution", xData, yData);

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);

        return chart;
    }

    @Override
    protected String getChartName() {
        return "score_distribution";
    }
}

