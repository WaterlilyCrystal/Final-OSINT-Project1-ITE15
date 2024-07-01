package project1.datagetting;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import okhttp3.*;
import org.json.JSONObject;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AirtableWriter {

    Properties properties = new Properties();
    properties.load(new FileInputStream("config.properties"));

    private static final String BASE_ID = properties.getProperty("BASE_ID");
    private static final String API_KEY = properties.getProperty("API_KEY");

    private static final String TABLE_NAME = "SpiderumUsers";

    public static void main(String[] args) throws IOException {
        List<String[]> csvData = readCSVFile("SInfor.csv");
        // Skip header row
        List<String[]> dataWithoutHeader = csvData.subList(1, csvData.size());
        createTable();
        insertRecords(dataWithoutHeader);
    }

    private static List<String[]> readCSVFile(String filePath) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            return reader.readAll();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    static final String A="application/json";
    private static void insertRecords(List<String[]> csvData) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse(A);

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
                        .url("https://api.airtable.com/v0/" + BASE_ID + "/" + TABLE_NAME)
                        .post(requestBodyObject)
                        .addHeader("Authorization", "Bearer " + API_KEY)
                        .addHeader("Content-Type", A)
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

    private static void createTable() throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse(A);

        RequestBody body = RequestBody.create(mediaType, "{\"fields\":{\"Name\":\"Name\",\"UserId\":\"UserId\",\"SocialAccount\":\"SocialAccount\",\"Gender\":\"Gender\",\"Score\":\"Score\",\"Comments\":\"Comments\",\"Followers\":\"Followers\",\"Followings\":\"Followings\",\"CreatedPosts\":\"CreatedPosts\"}}");

        Request request = new Request.Builder()
                .url("https://api.airtable.com/v0/" + BASE_ID + "/" + TABLE_NAME)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", A)
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
}


