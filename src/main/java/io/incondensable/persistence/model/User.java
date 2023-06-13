package io.incondensable.persistence.model;

import java.util.StringTokenizer;

/**
 * @author abbas
 * assume that the Database stores the Name of the user as a single NAME column.
 * but the User class has firstname and lastname fields.
 */
public class User {

    private String firstname;
    private String lastname;

    public String getName() {
        return firstname + lastname;
    }

    public void setName(String name) {
        StringTokenizer tokenizer = new StringTokenizer(name);
        firstname = tokenizer.nextToken();
        lastname = tokenizer.nextToken();
    }

    public static void main(String[] args) {
        User user = new User();
        user.setName("Mohammad Ali");
        System.out.println(user.getName());
    }

}
