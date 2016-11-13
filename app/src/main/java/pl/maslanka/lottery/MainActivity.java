package pl.maslanka.lottery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable, NavigationView.OnNavigationItemSelectedListener  {

    protected static Logic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.naviagtion_drawer);

        logic = new Logic(this);
        logic.newWallet();
        logic.setValuesToHitsDescriptionsArray();
        Logic.setStaticContext(getApplicationContext());
        Logic.setPrizeAmounts(24, 179, 5696, 2000000);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Button compComp = (Button) findViewById(R.id.comp_comp);
        Button compUser = (Button) findViewById(R.id.comp_user);
        Button showWallet = (Button) findViewById(R.id.show_wallet);

        Logic.setButtonBlueBackground(findViewById(R.id.comp_comp));
        Logic.setButtonBlueBackground(findViewById(R.id.comp_user));
        Logic.setButtonBlueBackground(findViewById(R.id.show_wallet));


        compComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CompComp.class);
                startActivity(intent);
            }
        });

        compUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CompUser.class);
                startActivity(intent);
            }
        });

        showWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), ShowWallet.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_manage:
                Intent p = new Intent(getApplicationContext(), Prefs.class);
                startActivity(p);
                break;
            case R.id.nav_share:
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.i_won));
                intent.putExtra(android.content.Intent.EXTRA_TEXT, String.format(getResources().getString(R.string.share_app)
                        + "\n\n" + getResources().getString(R.string.link_to_app)));
                startActivity(intent);
                TastyToast.makeText(this, getResources().getString(R.string.thanks_for_recommend), Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                break;
            case R.id.nav_about:
                showAbout();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void showAbout() {
        // Inflate the about message contents
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);

        // When linking text, force to always use default color. This works
        // around a pressed color state bug.
        TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
        int defaultColor = textView.getTextColors().getDefaultColor();
        textView.setTextColor(defaultColor);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.app_logo);
        builder.setTitle(R.string.app_name);
        builder.setNeutralButton(getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Nothing to show
            }
        });
        builder.setView(messageView);
        builder.create();
        builder.show();
    }
}