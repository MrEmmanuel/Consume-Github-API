import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;

public class ConsumeGithubAPI {
    public static void getPullRequests(String owner, String repositoryName, String startDate, String endDate) throws IOException {
        ArrayList<PullRequests> listOfPullRequests;
        String jsonResponse = makeHttpRequest(createUrl(owner,repositoryName));
        listOfPullRequests = parseJson(jsonResponse, startDate,endDate);

        System.out.println(listOfPullRequests);
    }

    public static URL createUrl(String user, String repoName) throws MalformedURLException {

        String urlString = "https://api.github.com/repos/" + user + "/" + repoName +"/pulls";
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
            connection.setConnectTimeout(20000);
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
    public static LocalDate getDateFromString(String string, DateTimeFormatter format) {

        LocalDate date = LocalDate.parse(string, format);

        return date;
    }
    private static boolean checkDate(String start, String end, String createdDate){

        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try{

            LocalDate startDate = getDateFromString(start, formatDate);
            LocalDate endDate = getDateFromString(end, formatDate);
            LocalDate created_at = getDateFromString(createdDate, formatDate);
            boolean results = created_at.isAfter(startDate) && created_at.isBefore(endDate);
            return results;

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
        String updated_at;
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
            updated_at = response.getJSONObject(i).get("updated_at").toString();
            merged_at = response.getJSONObject(i).get("merged_at").toString();

            if( checkDate(start,end,closed_at) || checkDate(start,end,updated_at) || checkDate(start,end,merged_at) || checkDate(start,end,created_at) ) {
                pullRequests.add(new PullRequests(id, user, title, state, created_at));
            }
        }
        return pullRequests;
    }

}
