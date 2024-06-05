package com.example.bikemonitor;

import android.app.Notification;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bikemonitor.databinding.FragmentSlideshowBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikemonitor.databinding.ActivityMainBinding;

import java.lang.reflect.Array;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        ActionBar mainBar = getSupportActionBar();


//        DrawerLayout drawer = binding.drawerLayout;
//        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.nav_open, R.string.nav_close);
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button

//        drawer.addDrawerListener(actionBarDrawerToggle);
        // to make the Navigation drawer icon always appear on the action bar
//        mainBar.setDisplayHomeAsUpEnabled(true);

        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setOpenableLayout(drawer)
//                .build();

        NavHostFragment hostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(
                                        R.id.nav_host_fragment_content_main);

        NavController navController = hostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNav , navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if((navDestination.getId() == R.id.nav_login) || (navDestination.getId() == R.id.nav_register)||
                        (navDestination.getId() == R.id.nav_devicelist)){
                    bottomNav.setVisibility(View.GONE);
                    binding.appBarMain.toolbar.setVisibility(View.GONE);
                }
                else {
                    bottomNav.setVisibility(View.VISIBLE);
                    binding.appBarMain.toolbar.setVisibility(View.VISIBLE);
                }
            }
        });
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
}

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.main, menu);
         return true;
     }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout_button){
            NavHostFragment hostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(
                                                                    R.id.nav_host_fragment_content_main);
            NavController navController = hostFragment.getNavController();
            NavDestination currentDestination = navController.getCurrentDestination();
            if(currentDestination.getId() == R.id.nav_gallery) {
                navController.navigate(new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_nav_gallery_to_nav_login;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                });
            }
            else if(currentDestination.getId() == R.id.nav_home){
                navController.navigate(new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_nav_home_to_nav_login;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                });
            }
            else if(currentDestination.getId() == R.id.nav_slideshow){

                navController.navigate(new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_nav_slideshow_to_nav_login;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                });
            }
        }
        else if(item.getItemId() == R.id.device_list){
            NavHostFragment hostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(
                    R.id.nav_host_fragment_content_main);
            NavController navController = hostFragment.getNavController();
            NavDestination currentDestination = navController.getCurrentDestination();
            if(currentDestination.getId() == R.id.nav_gallery) {
                navController.navigate(new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_nav_gallery_to_nav_devicelist;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                });
            }
            else if(currentDestination.getId() == R.id.nav_home){
                navController.navigate(new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_nav_home_to_nav_devicelist;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                });
            }
            else if(currentDestination.getId() == R.id.nav_slideshow){

                navController.navigate(new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_nav_slideshow_to_nav_devicelist;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                });
            }
        }
//        else if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        //actionBarDrawerToggle.syncState();
    }
    //    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}
