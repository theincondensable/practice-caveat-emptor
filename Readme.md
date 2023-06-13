**Individual Objects can outline the Application Process.
**ORM: is the Automated and Transparent Persistence of objects of the Application to the Tables in a Database.

**Application Development Strategies:
    1. TopDown: Starting with Domain Model.
    2. BottomUp: Starting with an existing Database Schema.

#0: JDBC:
it's a Java API that can access any kind of Tabular Data (like a Relational DB).
   1. Connection: connects to a Database.
      1. DriverManager.getConnection() to which we must pass "URL, Username, Password" to obtain a Connection to DB.
   2. Statement: Send Queries to the Database.
      1. we create statements by calling createStatement() method of the connection object.
   3. ResultSet: is the Object we have in response of executing our statement.

Connection con = DriverManager.getConnection("url", "username", "password");
Statement st = con.createStatement();
ResultSet res = st.executeQuery("SELECT * FROM <TABLE_NAME>");
while(res.next()) {
    sout(res.getString("<COLUMN_NAME>"));
    sout(res.getString("<COLUMN_NAME>"));
}

#1: Custom Type Converters:
If your database stores the name of a user as a single NAME column, but your User class has firstname and lastname fields.
    a custom type converter in the persistence service is a better way to handle many of these kinds of situations.
    It helps to have several options.

#2: Hibernate Dirty-Checking:
Hibernate automatically detects state changes so that it can synchronize the updated state with the database. It’s
    usually safe to return a different instance from the getter method than the instance passed by Hibernate
    to the setter. Hibernate compares them by value—not by object identity—to determine whether the attribute’s
    persistent state needs to be updated. 

The following getter method does not result in unnecessary SQL "UPDATE"s:
    public String getFirstname() {
           return new String(firstname);
    }

BUT There is an important point to note about dirty checking when persisting collections.
    If you have an Item entity with a Set<Bid> field that’s accessed through the setBids setter, this code will result
    in an unnecessary SQL UPDATE:
     item.setBids(bids);
     em.persist(item);
     item.setBids(bids);

This happens because Hibernate has its own collection implementations: PersistentSet, PersistentList, or PersistentMap.
    Providing setters for an entire collection is not good practice anyway.

#3: Hibernate Exception Thrown by Accessor Methods Handling:
If Hibernate uses accessor methods when loading and storing instances, and a RuntimeException (unchecked) is thrown,
    the current transaction is rolled back.
    If you throw a checked-application exception, Hibernate wraps the exception into a RuntimeException.

#4: Scaffolding Code:
We left the association-related attributes, Item#bids and Bid#item, out of figure 3.4. These properties and the methods
    that manipulate their values are called scaffolding code. This is what the scaffolding code for the Bid class
    looks like:
    public class Bid {
        private Item item;
        public Item getItem() {
            return item;
        }
        public void setItem(Item item) {
            this.item = item;
        }
    }

The item property allows navigation from a Bid to the related Item. This is an association with many-to-one
    multiplicity; users can make many bids for each item. Here is the Item class’s scaffolding code:
    public class Item {
        private Set<Bid> bids = new HashSet<>();
        public Set<Bid> getBids() {
            return Collections.unmodifiableSet(bids);
        }
    }

#4: Interfaces for Properties that are of Collections type:
JPA requires Interfaces like "Set", "List", "Collection" rather than Concrete implementations like "HashSet", for example.
    for example, if you use "HashSet", not only allows the Application to avoid adding duplicate records,it also
    allows you to avoid any "NullPointerException" when someone is accessing the property of a new record.

#5: Bidirectional Links in Java does not look complicated, but it is:
JPA does not manage Persistent associations.
The whole thing is that when you are manipulating one side, the other side must be handled manually (by code).

So, if you want to manipulate an association, you must write the same code you'd write without Hibernate.
if an Association is Bi-Directional, you must consider Both Sides of the relationship.

consider the Item and Bid classes above.
for Item Class that has a One-To-Many relationship with Bid class, you should write a convenient method:
public void addBid(Bid bid) {
    if (bid == null)
        throw new NullPointerException("Can't add null Bid");
    if (bid.getItem() != null)
        throw new IllegalStateException("Bid is already assigned to an Item");
    bids.add(bid);
    bid.setItem(this);
}

addBid() method not only reduces the lines of code when dealing with Item and Bid, but also enforces the cardinality of
    the association. you should always this.
    in SQL Database modeling all you have to do is defining Constraint as Foreign Key, compared to a Network and
    a Pointer Model, it is so simple since instead of declarative constraint, you need procedural code to guarantee
    data integrity.

