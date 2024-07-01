package project1.datagetting;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

class UsernameCollecting {

    private UsernameCollecting() {
        throw new IllegalStateException("Utility class");
    }
    public static Set<String> collectUsernames() throws IOException {
        Set<String> usernames = new HashSet<>();

        for (int page = 1; page <= 100; page++) {
            String apiUrl = "https://spiderum.com/api/v1/feed/getAllPosts?type=hot&page=" + page;
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept", "application/json, text/plain, */*");
            connection.setRequestProperty("accept-language", "en-US,en;q=0.9,vi;q=0.8");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                JSONObject jsonResponse = new JSONObject(new java.util.Scanner(connection.getInputStream()).useDelimiter("\\A").next());
                JSONArray items = jsonResponse.getJSONObject("posts").getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    JSONObject creator = item.getJSONObject("creator_id");
                    String username = creator.getString("name");
                    usernames.add(username);
                }
            } else {
                System.err.println("Failed to fetch data from page " + page + ". Response code: " + responseCode);
            }
            connection.disconnect();
        }
        return usernames;
    }
}
