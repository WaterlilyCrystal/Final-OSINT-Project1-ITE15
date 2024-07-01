package project1.dataprocessing;

public class Main {
    public static void main(String[] args) {
        final String sourceFilePath="SInfor.csv";

        GenderStatistic genderStatistic = new GenderStatistic(sourceFilePath);
        genderStatistic.generateChart();

        SocialAccountStatus socialAccountStatus = new SocialAccountStatus(sourceFilePath);
        socialAccountStatus.generateChart();

        ScoreDistribution scoreDistribution = new ScoreDistribution(sourceFilePath);
        scoreDistribution.generateChart();
    }
}

