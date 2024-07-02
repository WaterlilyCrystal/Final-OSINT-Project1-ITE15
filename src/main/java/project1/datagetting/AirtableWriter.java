package project1.datagetting;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import okhttp3.*;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AirtableWriter {
    private final String baseId;
    private final String apiKey;
    private final String tableName;
    private static final String AIRTABLE_API_URL = "https://api.airtable.com/v0/";
    private static final String MEDIA_TYPE_JSON = "application/json";
    private static final OkHttpClient client = new OkHttpClient();

    public AirtableWriter(String baseId, String apiKey, String tableName) {
        this.baseId = baseId;
        this.apiKey = apiKey;
        this.tableName = tableName;
    }

    public void processDataFromCSV(String csvFilePath) throws IOException {
        List<String[]> csvData = readCSVFile(csvFilePath);
        // Skip header row
        List<String[]> dataWithoutHeader = csvData.subList(1, csvData.size());
        createTable();
        insertRecords(dataWithoutHeader);
    }

    private List<String[]> readCSVFile(String filePath) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            return reader.readAll();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTable() throws IOException {
        MediaType mediaType = MediaType.parse(MEDIA_TYPE_JSON);

        RequestBody body = RequestBody.create(mediaType,
                "{\"fields\":{\"Name\":\"Name\",\"UserId\":\"UserId\",\"SocialAccount\":\"SocialAccount\",\"Gender\":\"Gender\",\"Score\":\"Score\",\"Comments\":\"Comments\",\"Followers\":\"Followers\",\"Followings\":\"Followings\",\"CreatedPosts\":\"CreatedPosts\"}}");

        Request request = new Request.Builder()
                .url(AIRTABLE_API_URL + baseId + "/" + tableName)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", MEDIA_TYPE_JSON)
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    private void insertRecords(List<String[]> csvData) throws IOException {
        MediaType mediaType = MediaType.parse(MEDIA_TYPE_JSON);

        for (String[] row : csvData) {
            try {
                String name = row[0];
                String userId = row[1];
                String socialAccount = row[2];
                int gender = Integer.parseInt(row[3]);
                int score = Integer.parseInt(row[4]);
                int comments = Integer.parseInt(row[5]);
                int followers = Integer.parseInt(row[6]);
                int followings = Integer.parseInt(row[7]);
                int createdPosts = Integer.parseInt(row[8]);

                JSONObject fields = new JSONObject();
                fields.put("Name", name);
                fields.put("Score", score);
                fields.put("Comments", comments);
                fields.put("Followers", followers);
                fields.put("Gender", gender);
                fields.put("Followings", followings);
                fields.put("CreatedPosts", createdPosts);
                fields.put("SocialAccount", socialAccount);
                fields.put("UserId", userId);

                JSONObject requestBody = new JSONObject();
                requestBody.put("fields", fields);

                RequestBody requestBodyObject = RequestBody.create(mediaType, requestBody.toString());

                Request request = new Request.Builder()
                        .url(AIRTABLE_API_URL + baseId + "/" + tableName)
                        .post(requestBodyObject)
                        .addHeader("Authorization", "Bearer " + apiKey)
                        .addHeader("Content-Type", MEDIA_TYPE_JSON)
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    System.out.println("Error inserting record: " + response.body().string());
                }
            } catch (NumberFormatException e) {
                System.out.println("Error parsing data in row: " + Arrays.toString(row));
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Error inserting record: ");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter Base ID: ");
        String baseId = scanner.nextLine().trim();

        System.out.print("Please enter API Key: ");
        String apiKey = scanner.nextLine().trim();

        System.out.print("Please enter Table name: ");
        String tableName = scanner.nextLine().trim();

        AirtableWriter airtableWriter = new AirtableWriter(baseId, apiKey, tableName);
        airtableWriter.processDataFromCSV("SInfor.csv");

        scanner.close();
    }
}
