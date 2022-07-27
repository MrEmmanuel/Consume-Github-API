public class PullRequests {
    String id;
    String user;
    String title;
    String state;
    String created_at;

    public PullRequests(String id, String user, String title, String state, String dateCreated){
        this.id = id;
        this.user = user;
        this.title = title;
        this.state = state;
        this.created_at = dateCreated;
    }
}
