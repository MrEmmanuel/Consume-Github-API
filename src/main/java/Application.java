import java.io.IOException;
import java.util.List;

public class Application {

    public static void main(String[] args) throws IOException {
        List<PullRequests> pullRequests = new ConsumeGithubAPI().getPullRequests(
                "Umuzi-org",
                "ACN-syllabus",
                "2022-05-01",
                "2022-05-30"
        );
        System.out.println(pullRequests);
    }
}
