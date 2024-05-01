package dk.itu.garbage;

public class Item {
    private String what;
    private String where;

    public Item(String what, String where) {
        this.what = what;
        this.where = where;
    }

    public String getWhat() {
        return what;
    }

    public String getWhere() {
        return where;
    }

    public String toString() {
        return "put " + what + " in: " + where;
    }

}


