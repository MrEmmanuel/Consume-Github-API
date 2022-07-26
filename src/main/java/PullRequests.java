public class PullRequests {
    String id;
    String user;
    String title;
    String state;
    String dateCreated;

    public PullRequests(String id, String user, String title, String state, String dateCreated){
        this.id = id;
        this.user = user;
        this.title = title;
        this.state = state;
        this.dateCreated = dateCreated;
    }
    public String getId(){return this.id;}
    public String getUser(){return this.user;}
    public String getTitle(){return this.title;}
    public String getState(){return this.state;}
    public String getDateCreated(){return this.dateCreated;}

}
