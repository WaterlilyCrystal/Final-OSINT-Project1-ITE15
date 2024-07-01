package project1.datagetting;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

import org.json.JSONObject;

import static project1.datagetting.UsernameCollecting.collectUsernames;

public class InforCollecting {
    public static void main(String[] args) throws IOException {
        Set<String> usernames = collectUsernames();

        try (FileWriter file = new FileWriter("SInfor.csv", true)) {
            // Write CSV header
            file.write("Name,UserId,SocialAccount,Gender,Score,Comments,Followers,Followings,CreatedPosts\n");

            // Process each profile
            for (String profileName : usernames) {
                processProfile(profileName, file);
            }
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processProfile(String profileName, FileWriter file) {
        try {
            String apiUrl = "https://spiderum.com/api/v2/user/getUserNumberDataInfo?profile_name=" + profileName;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept", "application/json, text/plain, */*");
            connection.setRequestProperty("accept-language", "en-US,en;q=0.9,vi;q=0.8");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36");

            JSONObject jsonResponse = new JSONObject(new java.util.Scanner(connection.getInputStream()).useDelimiter("\\A").next());

            JSONObject profile = jsonResponse.getJSONObject("profile");
            JSONObject count = jsonResponse.getJSONObject("count");
            JSONObject externalInfo = count.getJSONObject("getExternalInfo");

            int gender = externalInfo.optInt("gender"); // Use optInt to handle missing fields
            String socialAccount = "";
            final String key = "\"social_account\"";
            if (externalInfo.has(key) && !externalInfo.getJSONArray(key).isEmpty()) {
                socialAccount = externalInfo.getJSONArray(key).getJSONObject(0).optString("url", ""); // Provide default value if not found
            }

            String name = profile.getString("name");
            int score = profile.getInt("score");
            String userId = profile.getString("_id");
            int comments = count.isNull("comments") ? 0 : count.getInt("comments"); // Handle null comments
            int followers = count.isNull("followers") ? 0 : count.getInt("followers");
            int followings = count.isNull("followings") ? 0 : count.getInt("followings");
            int createdPosts = count.isNull("createdPosts") ? 0 : count.getInt("createdPosts");

            // Write UserProfile to CSV
            UserProfile userProfile = new UserProfile(name, userId, socialAccount, gender, score, comments, followers, followings, createdPosts);
            file.write(userProfile.toCSV() + "\n");

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}