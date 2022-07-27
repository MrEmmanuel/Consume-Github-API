import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;

public class ConsumeGithubAPI {
    public ArrayList<PullRequests> getPullRequests(String owner, String repositoryName, String startDate, String endDate) throws IOException {
        ArrayList<PullRequests> listOfPullRequests;
        String jsonResponse = makeHttpRequest(createUrl(owner,repositoryName));
        listOfPullRequests = parseJson(jsonResponse, startDate,endDate);

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
            return "Error 404 User or Repo Not Found";
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
            throw new MalformedURLException("Error 404 User or Repo Not Found");
        } finally {
            if (connection != null)
                connection.disconnect();
            if(stream != null)
                stream.close();
        }
        return jsonResponse;
    }

    private static boolean checkDate(String start, String end, String createdDate){
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            Date startDate = format.parse(start.substring(0,10));
            Date endDate = format.parse(end.substring(0,10));
            Date created_at = format.parse(createdDate.substring(0,10));
            return created_at.after(startDate) && created_at.before(endDate);
        } catch (Exception e) {
            return false;
        }
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

    static ArrayList<PullRequests> parseJson(String jsonResponse, String start, String end) throws JSONException {
        String id;
        String user;
        String title;
        String created_at;
        String state;
        String closed_at;
        String update_at;
        String merged_at;

        ArrayList<PullRequests> pullRequests = new ArrayList<>();

        JSONArray response = new JSONArray(jsonResponse);
        for (int i=0;i<response.length();i++){
            id = response.getJSONObject(i).get("id").toString();
            user = response.getJSONObject(i).getJSONObject("user").get("login").toString();
            title = response.getJSONObject(i).get("title").toString();
            state = response.getJSONObject(i).get("state").toString();
            created_at = response.getJSONObject(i).get("created_at").toString().substring(0,10);
            closed_at = response.getJSONObject(i).get("closed_at").toString();
            update_at = response.getJSONObject(i).get("updated_at").toString();
            merged_at = response.getJSONObject(i).get("merged_at").toString();

            if(checkDate(start,end,created_at) || checkDate(start,end,closed_at) || checkDate(start,end,update_at) || checkDate(start,end,merged_at)) {
                pullRequests.add(new PullRequests(id, user, title, state, created_at));
            }
        }
        return pullRequests;
    }

}
