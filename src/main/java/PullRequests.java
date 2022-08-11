public class PullRequests {
   private String id;
   private String user;
   private String title;
   private String state;
   private String created_at;

    public PullRequests(String id, String user, String title, String state, String dateCreated){
        this.id = id;
        this.user = user;
        this.title = title;
        this.state = state;
        this.created_at = dateCreated;
    }

    @Override
    public String toString() {
        return "\n{" +
                "\"id\":" + id +
                ", \"user\":\"" + user + '\"' +
                ", \"title\":\"" + title + '\"' +
                ", \"state\":\"" + state + '\"' +
                ", \"created_at\":\"" + created_at+ '\"' +
                '}';
    }
}
