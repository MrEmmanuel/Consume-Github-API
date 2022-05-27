import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConsumeGithubAPI {
    public ArrayList<PullRequests> getPullRequests(String owner, String repositoryName, String startDate, String endDate) throws IOException {
        ArrayList<PullRequests> listOfPullRequests = new ArrayList<>();
        String jsonResponse = makeHttpRequest(createUrl(owner,repositoryName));


        return listOfPullRequests;
    }

    public static URL createUrl(String user, String repoName) throws MalformedURLException {

        String urlString = "https://api.github.com/repos/" + user + "/" + repoName +"/pulls?state=all";
        URL url = new URL(urlString);
        return url;
    }

    static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if(url == null) {
            return "invalid url";
        }
        HttpURLConnection connection = null;
        InputStream stream = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.connect();

            if (connection.getResponseCode() == 200){
                stream = connection.getInputStream();
                jsonResponse = readFromInputStream(stream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
            if(stream != null)
                stream.close();
        }
        return jsonResponse;
    }

    private static String readFromInputStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder output = new StringBuilder();

        String line = reader.readLine();
        while (line != null){
            output.append(line);
            line = reader.readLine();
        }
        return output.toString();

    }

    static ArrayList<PullRequests> parseJson(String jsonResponse) throws JSONException {
        String id;
        String user;
        String title;
        String dateCreated;
        String state;
        ArrayList<PullRequests> pullRequests = new ArrayList<>();

        JSONArray response = new JSONArray(jsonResponse);
        for (int i=0;i<response.length();i++){
            id = response.getJSONObject(i).get("id").toString();
            user = response.getJSONObject(i).get("user").toString();
            title = response.getJSONObject(i).get("title").toString();
            dateCreated = response.getJSONObject(i).get("dateCreated").toString();
            state = response.getJSONObject(i).get("state").toString();

            pullRequests.add(new PullRequests(id,user,title,state,dateCreated));
        }
        return pullRequests;
    }

}
