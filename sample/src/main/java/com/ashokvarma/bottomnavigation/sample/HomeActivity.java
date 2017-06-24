package com.ashokvarma.bottomnavigation.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.crashlytics.android.Crashlytics;

import java.util.Arrays;

import io.fabric.sdk.android.Fabric;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, BottomNavigationBar.OnTabSelectedListener, AdapterView.OnItemSelectedListener {

    BottomNavigationBar bottomNavigationBar;

    FloatingActionButton fabHome;

    Spinner modeSpinner;
    Spinner itemSpinner;
    CheckBox bgStatic;
    CheckBox bgRipple;
    CheckBox autoHide;

    Button toggleHide;
    Button toggleBadge;

    TextView message;
    TextView scrollableText;

    int lastSelectedPosition = 0;

    @Nullable
    TextBadgeItem numberBadgeItem;

    @Nullable
    ShapeBadgeItem shapeBadgeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home);

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        fabHome = (FloatingActionButton) findViewById(R.id.fab_home);

        modeSpinner = (Spinner) findViewById(R.id.mode_spinner);
        itemSpinner = (Spinner) findViewById(R.id.item_spinner);
        bgStatic = (CheckBox) findViewById(R.id.bg_static);
        bgRipple = (CheckBox) findViewById(R.id.bg_ripple);
        autoHide = (CheckBox) findViewById(R.id.auto_hide);

        toggleHide = (Button) findViewById(R.id.toggle_hide);
        toggleBadge = (Button) findViewById(R.id.toggle_badge);

        message = (TextView) findViewById(R.id.message);
        scrollableText = (TextView) findViewById(R.id.scrollable_text);

        modeSpinner.setOnItemSelectedListener(this);
        itemSpinner.setOnItemSelectedListener(this);
        bgRipple.setOnCheckedChangeListener(this);
        bgStatic.setOnCheckedChangeListener(this);
        autoHide.setOnCheckedChangeListener(this);

        toggleHide.setOnClickListener(this);
        toggleBadge.setOnClickListener(this);
        fabHome.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Arrays.asList(new String[]{"MODE_DEFAULT", "MODE_FIXED", "MODE_SHIFTING", "MODE_FIXED_NO_TITLE", "MODE_SHIFTING_NO_TITLE"}));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(adapter);

        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Arrays.asList(new String[]{"3 items", "4 items", "5 items"}));
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSpinner.setAdapter(itemAdapter);

        bottomNavigationBar.setTabSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_github:
                String url = "https://github.com/Ashok-Varma/BottomNavigation";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toggle_hide) {
            if (bottomNavigationBar != null) {
                bottomNavigationBar.toggle();
            }
        } else if (v.getId() == R.id.toggle_badge) {
            if (numberBadgeItem != null) {
                numberBadgeItem.toggle();
            }
            if (shapeBadgeItem != null) {
                shapeBadgeItem.toggle();
            }
        } else if (v.getId() == R.id.fab_home) {
            final Snackbar snackbar = Snackbar.make(message, "Fab Clicked", Snackbar.LENGTH_LONG);
            snackbar.setAction("dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.bg_ripple:
                bgStatic.setChecked(!isChecked);
                break;
            case R.id.bg_static:
                bgRipple.setChecked(!isChecked);
                break;
        }
        refresh();
    }

    private void refresh() {

        bottomNavigationBar.clearAll();
//        bottomNavigationBar.setFab(fabHome, BottomNavigationBar.FAB_BEHAVIOUR_TRANSLATE_AND_STICK);
        bottomNavigationBar.setFab(fabHome);

        setScrollableText(lastSelectedPosition);

        numberBadgeItem = new TextBadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.blue)
                .setText("" + lastSelectedPosition)
                .setHideOnSelect(autoHide.isChecked());

        shapeBadgeItem = new ShapeBadgeItem()
                .setShape(ShapeBadgeItem.SHAPE_STAR_5_VERTICES)
                .setShapeColorResource(R.color.teal)
                .setGravity(Gravity.TOP | Gravity.END)
                .setHideOnSelect(autoHide.isChecked());


        bottomNavigationBar.setMode(modeSpinner.getSelectedItemPosition());


        if (bgStatic.isChecked()) {
            bottomNavigationBar
                    .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        } else if (bgRipple.isChecked()) {
            bottomNavigationBar
                    .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        }

        if (itemSpinner.getSelectedItemPosition() == 0) {
            bottomNavigationBar
                    .addItem(new BottomNavigationItem(R.drawable.ic_location_on_white_24dp, "Nearby").setActiveColorResource(R.color.orange).setBadgeItem(numberBadgeItem))
                    .addItem(new BottomNavigationItem(R.drawable.ic_find_replace_white_24dp, "Find").setActiveColorResource(R.color.teal))
                    .addItem(new BottomNavigationItem(R.drawable.ic_favorite_white_24dp, "Categories").setActiveColorResource(R.color.blue).setBadgeItem(shapeBadgeItem))
                    .setFirstSelectedPosition(lastSelectedPosition > 2 ? 2 : lastSelectedPosition)
                    .initialise();
        } else if (itemSpinner.getSelectedItemPosition() == 1) {
            bottomNavigationBar
                    .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp, "Home").setActiveColorResource(R.color.orange).setBadgeItem(numberBadgeItem))
                    .addItem(new BottomNavigationItem(R.drawable.ic_book_white_24dp, "Books").setActiveColorResource(R.color.teal))
                    .addItem(new BottomNavigationItem(R.drawable.ic_music_note_white_24dp, "Music").setActiveColorResource(R.color.blue).setBadgeItem(shapeBadgeItem))
                    .addItem(new BottomNavigationItem(R.drawable.ic_tv_white_24dp, "Movies & TV").setActiveColorResource(R.color.brown))
                    .setFirstSelectedPosition(lastSelectedPosition > 3 ? 3 : lastSelectedPosition)
                    .initialise();
        } else if (itemSpinner.getSelectedItemPosition() == 2) {
            bottomNavigationBar
                    .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp, "Home").setActiveColorResource(R.color.orange).setBadgeItem(numberBadgeItem))
                    .addItem(new BottomNavigationItem(R.drawable.ic_book_white_24dp, "Books").setActiveColorResource(R.color.teal))
                    .addItem(new BottomNavigationItem(R.drawable.ic_music_note_white_24dp, "Music").setActiveColorResource(R.color.blue).setBadgeItem(shapeBadgeItem))
                    .addItem(new BottomNavigationItem(R.drawable.ic_tv_white_24dp, "Movies & TV").setActiveColorResource(R.color.brown))
                    .addItem(new BottomNavigationItem(R.drawable.ic_videogame_asset_white_24dp, "Games").setActiveColorResource(R.color.grey))
                    .setFirstSelectedPosition(lastSelectedPosition)
                    .initialise();
        }
    }

    @Override
    public void onTabSelected(int position) {
        lastSelectedPosition = position;
        setMessageText(position + " Tab Selected");
        if (numberBadgeItem != null) {
            numberBadgeItem.setText(Integer.toString(position));
        }
        setScrollableText(position);
    }

    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {
        setMessageText(position + " Tab Reselected");
    }

    private void setMessageText(String messageText) {
        message.setText(messageText);
    }

    private void setScrollableText(int position) {
        switch (position) {
            case 0:
                scrollableText.setText(R.string.para1);
                break;
            case 1:
                scrollableText.setText(R.string.para2);
                break;
            case 2:
                scrollableText.setText(R.string.para3);
                break;
            case 3:
                scrollableText.setText(R.string.para4);
                break;
            case 4:
                scrollableText.setText(R.string.para5);
                break;
            default:
                scrollableText.setText(R.string.para6);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        refresh();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}