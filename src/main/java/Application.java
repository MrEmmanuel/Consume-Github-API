import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class Application {

    public static void main(String[] args) throws IOException {
        List<PullRequests> pullRequests = new ConsumeGithubAPI().getPullRequests(
                "Umuzi-org",
                "ACN-syllabus",
                "2022-03-01",
                "2022-03-10"
        );
        System.out.println("[");
        for(int i=0; i<pullRequests.size(); i++) {
            System.out.print("{");
            System.out.print("id: " + pullRequests.get(i).id + ",");
            System.out.print("user: " + pullRequests.get(i).user + ",");
            System.out.print("title: " + pullRequests.get(i).title + ",");
            System.out.print("state: " + pullRequests.get(i).state + ",");
            System.out.print("created_at: " + pullRequests.get(i).created_at);
            System.out.print("}");
            System.out.println();
        }
        System.out.print("]");
    }
}
