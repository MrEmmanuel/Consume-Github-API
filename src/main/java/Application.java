
public class Application {

    public static void main(String[] args) throws Exception {
         ConsumeGithubAPI.getPullRequests(
                "Umuzi-org",
                "ACN-syllabus",
                "2022-03-01",
                "2022-03-10"
        );
    }
}
