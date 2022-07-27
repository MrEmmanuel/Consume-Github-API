public class PullRequests {
   public String id;
   public String user;
   public String title;
   public String state;
   public String created_at;

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
