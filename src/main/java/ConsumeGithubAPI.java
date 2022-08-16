import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;

public class ConsumeGithubAPI {
    public static void getPullRequests(String owner, String repositoryName, String startDate, String endDate) throws Exception {
        ArrayList<PullRequests> listOfPullRequests = new ArrayList<>();
        ArrayList<PullRequests> pullList;

        int page = 1;
        String jsonResponse = makeHttpRequest(createUrl(owner,repositoryName, page));
        JSONArray response = new JSONArray(jsonResponse);
        while(!response.isEmpty()){
            pullList = parseJson(jsonResponse, startDate,endDate);
            listOfPullRequests.addAll(pullList);
            page++;
            jsonResponse = makeHttpRequest(createUrl(owner,repositoryName,page));
            response = new JSONArray(jsonResponse);
        }
        System.out.println(listOfPullRequests);
    }

    public static URL createUrl(String user, String repoName, int pageNumber) throws MalformedURLException {

        String urlString = "https://api.github.com/repos/" + user + "/" + repoName +"/pulls?state=all&per_page=100&page=" + pageNumber;
        return new URL(urlString);
    }

    private static String makeHttpRequest(URL url) throws Exception {
        String jsonResponse = "";
        if(url == null) {
            throw new Exception("Error 404 User or Repo Not Found");
        }
        HttpURLConnection connection = null;
        InputStream stream = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() == 200){
                stream = connection.getInputStream();
                jsonResponse = readFromInputStream(stream);
            }else{
                throw new Exception("Error 404 User or Repo Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
            if(stream != null)
                stream.close();
        }
        return jsonResponse;
    }

    private static LocalDate getDateFromString(String string) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(!string.equalsIgnoreCase("null")) {
            return LocalDate.parse(string, format);
        }else{
            return null;
        }
    }

    private static boolean checkDate(String start, String end, String createdDate){
        LocalDate startDate, endDate, createdAt;
        try{
            if(!createdDate.equalsIgnoreCase("null")) {
                startDate =getDateFromString(start);
                endDate = getDateFromString(end);
                createdAt = getDateFromString(createdDate.substring(0, 10));
                return ((createdAt.isAfter(startDate) || createdAt.isEqual(startDate)) && (createdAt.isBefore(endDate) || createdAt.isEqual(endDate)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String readFromInputStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder output = new StringBuilder();

        String line = reader.readLine();
        while (line != null){
            output.append(line);
            line = reader.readLine();
        }
        reader.close();
        return output.toString();
    }

    private static ArrayList<PullRequests> parseJson(String jsonResponse, String start, String end) throws JSONException {
        String id;
        String user;
        String title;
        String createdAt;
        String state;
        String closedAt;
        String updatedAt;
        String mergedAt;

        ArrayList<PullRequests> pullRequests = new ArrayList<>();
        JSONArray response = new JSONArray(jsonResponse);
        for (int i=0;i<response.length();i++){
            id = response.getJSONObject(i).get("id").toString();
            user = response.getJSONObject(i).getJSONObject("user").get("login").toString();
            title = response.getJSONObject(i).get("title").toString();
            state = response.getJSONObject(i).get("state").toString();
            createdAt = response.getJSONObject(i).get("created_at").toString().substring(0,10);
            closedAt = response.getJSONObject(i).get("closed_at").toString();
            updatedAt = response.getJSONObject(i).get("updated_at").toString();
            mergedAt = response.getJSONObject(i).get("merged_at").toString();

            if( (checkDate(start,end,createdAt)) ||
                    (checkDate(start,end,closedAt)) ||
                    (checkDate(start,end,updatedAt)) ||
                    (checkDate(start,end,mergedAt))) {
                pullRequests.add(new PullRequests(id, user, title, state, createdAt));
            }
        }
        return pullRequests;
    }

}