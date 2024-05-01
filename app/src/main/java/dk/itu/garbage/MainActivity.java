package dk.itu.garbage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fm;
    Fragment fragmentUI, fragmentList;
    private GarbageViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(GarbageViewModel.class);
        // Context is needed to set up SQLite
        fm = getSupportFragmentManager();
        viewModel.awaitInit();
        setUpFragments();
    }


    private void setUpFragments() {
        FragmentManager fm = getSupportFragmentManager();
         fragmentUI = fm.findFragmentById(R.id.container_ui);
         fragmentList = fm.findFragmentById(R.id.container_list);
        if ((fragmentUI == null) && (fragmentList == null)) {
            fragmentUI = new UIFragment();
            fragmentList = new ListFragment();
            fm.beginTransaction()
                    .add(R.id.container_ui, fragmentUI)
                    .add(R.id.container_list, fragmentList)
                    .commit();
        }
    }
}