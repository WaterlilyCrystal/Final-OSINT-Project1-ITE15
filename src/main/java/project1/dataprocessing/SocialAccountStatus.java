package project1.dataprocessing;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.knowm.xchart.*;
import org.knowm.xchart.style.PieStyler;

import java.io.FileReader;
import java.io.IOException;

public class SocialAccountStatus extends ChartGenerator{

    public SocialAccountStatus(String csvFile) {
        super(csvFile);
    }

    @Override
    public void generateChart() {
        int publicCount = 0;
        int nonPublicCount = 0;
        try (FileReader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord table : csvParser) {
                String account = table.get("Social Account");
                if (account!= null &&!account.isEmpty()) {
                    publicCount++;
                } else {
                    nonPublicCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        PieChart chart = createChart(publicCount, nonPublicCount);
        displayChart(chart);
    }

    private PieChart createChart(int publicCount, int nonPublicCount) {
        PieChart chart = new PieChartBuilder()
                .width(800)
                .height(600)
                .title("Social Account Status")
                .build();

        chart.addSeries("Public", publicCount);
        chart.addSeries("Non-Public", nonPublicCount);

        chart.getStyler().setAnnotationType(PieStyler.AnnotationType.LabelAndPercentage);
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setLegendPosition(PieStyler.LegendPosition.OutsideS);

        return chart;
    }

    @Override
    protected String getChartName() {
        return "social_account_status";
    }
}
