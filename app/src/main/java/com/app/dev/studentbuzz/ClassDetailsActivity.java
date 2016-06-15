package com.app.dev.studentbuzz;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class ClassDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    Button lvw,event,ann,asg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);
        String name = getIntent().getExtras().getString("c_name");
        lvw = (Button) findViewById(R.id.dailyupdates);
        event = (Button) findViewById(R.id.Event);
        ann = (Button) findViewById(R.id.Ann);
        asg = (Button) findViewById(R.id.Assign);
        lvw.setOnClickListener(this);
        event.setOnClickListener(this);
        ann.setOnClickListener(this);
        asg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.dailyupdates:
                String name = getIntent().getExtras().getString("c_name");
                Intent intent = new Intent(ClassDetailsActivity.this,DailyUpdates.class);
                intent.putExtra("c_name", name);
                startActivity(intent);
                break;
            case R.id.Event:
                String ename = getIntent().getExtras().getString("c_name");
                Intent intente = new Intent(ClassDetailsActivity.this,EventActivity.class);
                intente.putExtra("c_name", ename);
                startActivity(intente);
                break;
            case R.id.Ann:
                String aname = getIntent().getExtras().getString("c_name");
                Intent intenta = new Intent(ClassDetailsActivity.this,AnnActivity.class);
                intenta.putExtra("c_name", aname);
                startActivity(intenta);
                break;
            case R.id.Assign:
                String asname = getIntent().getExtras().getString("c_name");
                Intent intentas = new Intent(ClassDetailsActivity.this,AssignActivity.class);
                intentas.putExtra("c_name", asname);
                startActivity(intentas);
                break;
        }

    }
}