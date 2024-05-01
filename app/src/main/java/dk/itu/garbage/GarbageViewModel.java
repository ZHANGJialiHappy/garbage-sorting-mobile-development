package dk.itu.garbage;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class GarbageViewModel extends ViewModel {
    private static final ItemsDB itemsDB = new ItemsDB();
    private final MutableLiveData<GarbageUiState> uiState =
            new MutableLiveData<>(GarbageUiState.getEmpty());

    public LiveData<GarbageUiState> getUiState() {
        return uiState;
    }

    public void awaitInit(){
        itemsDB.awaitInit();
        updateUiState();
    }


    public void onFindItemClick(TextView what_input, TextView where_input, FragmentActivity activity) {
        String whatS = what_input.getText().toString().trim();
        if (whatS.length() == 0) {
            showToast(activity);
            return;
        }
        if (containsItem(itemsDB.getValues(), whatS)) {
            where_input.setText(itemsDB.getWhere(whatS));

        } else where_input.setText("not found");
        // Clear focus from text views
        what_input.clearFocus();
        where_input.clearFocus();

        // Close keyboard
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(what_input.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(where_input.getWindowToken(), 0);
    }

    public void onAddItemClick(TextView what_input, TextView where_input, FragmentActivity activity) {
        String whatS = what_input.getText().toString().trim();
        String whereS = where_input.getText().toString().trim();
        if ((whatS.length() > 0) && (whereS.length() > 0)) {
            itemsDB.addItem(whatS, whereS);
            what_input.setText("");
            where_input.setText("");

            // Clear focus from text views
            what_input.clearFocus();
            where_input.clearFocus();

            // Close keyboard
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(what_input.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(where_input.getWindowToken(), 0);
            updateUiState();
            showToast(activity, "Add " + whatS +" successfully!");
        } else
            showToast(activity);
    }

    public void onDeleteItemClick(TextView what_input, TextView where_input, FragmentActivity activity) {
        String whatS = what_input.getText().toString().trim();
        if (whatS.length() == 0) {
            showToast(activity);
            return;
        }
        String message;
        if (containsItem(itemsDB.getValues(), whatS)) {
            itemsDB.removeItem(whatS);
            what_input.setText("");
            where_input.setText("");
            updateUiState();
            message = "Removed " + whatS;
        } else message = whatS + " not found";
        showToast(activity, message);
        // Clear focus from text views
        what_input.clearFocus();
        where_input.clearFocus();

        // Close keyboard
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(what_input.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(where_input.getWindowToken(), 0);
    }

    private boolean containsItem(List<Item> itemList, String what) {
        for (Item item : itemList) {
            if (item.getWhat().equals(what)) {
                return true;
            }
        }
        return false;
    }
    private void updateUiState() {
        uiState.setValue(new GarbageUiState(itemsDB.getValues(), itemsDB.size()));

    }

    private void showToast(FragmentActivity activity) {
        Toast.makeText(activity, R.string.empty_toast, Toast.LENGTH_LONG).show();
    }

    private void showToast(FragmentActivity activity, CharSequence message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }


    public static class GarbageUiState {
        final private List<Item> itemList;
        final int itemListSize;

        public GarbageUiState(List<Item> itemList, int itemListSize) {
            this.itemList = itemList;
            this.itemListSize = itemListSize;
        }

        public List<Item> getItemList() {
            return itemList;
        }

        public int getItemListSize() {
            return itemListSize;
        }

        public static GarbageUiState getEmpty() {
            return new GarbageUiState(new ArrayList<>(), 0);
        }


    }
}
