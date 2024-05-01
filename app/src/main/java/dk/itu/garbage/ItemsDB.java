package dk.itu.garbage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ItemsDB {
    private final static String GarbageURL="https://garbageserver.onrender.com";

    private List<Item> values;
    private final Semaphore init = new Semaphore(0);

    public ItemsDB() {
        values = new ArrayList<>();
        networkDB(GarbageURL, "", values);  //This will fetch all items and insert them into values
    }

    public void awaitInit() {
        if (values.size() == 0)  //******BV To prevent ShoppingActivity from waiting when onCreate is called because of life-cycle changes
            try {
                init.acquire();
            } catch (InterruptedException ie) {
            }
    }

    private void networkDB(String url, String command, List<Item> values) {
        Runnable r = new HttpThread(url + command, values, init);
        new Thread(r).start();
    }

    public synchronized void addItem(String what, String where) {
        values.add(new Item(what, where));
        networkDB(GarbageURL, "?op=insert&what=" + what + "&whereC=" + where, values);
    }

    public synchronized void removeItem(String what) {
        // Should delete all rows in values similarly to what happens in database
        for (Item t : values) {
            if (t.getWhat().equals(what)) {
                values.remove(t);
                networkDB(GarbageURL, "?op=remove&what=" + what, values);
                break;
            }
        }
    }

    public synchronized int size() {
        return values.size();
    }

    public synchronized List<Item> getValues() {
        return values;
    }

    public String getWhere(String what) {

        for (Item t : values) {
            if (t.getWhat().equals(what)) {
                return t.getWhere();
            }
        }
        return null;
    }


}
