package beans;

public class Entity {

    private String name;
    private String email;

    public Entity() {
    }

    // constructors / standard setters / getters
    public Entity(String n, String e) {
        this.name = n;
        this.email = e;

    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setName(String n) {
        this.name = n;
    }

    public void setEmail(String e) {
        this.email = e;
    }

}
