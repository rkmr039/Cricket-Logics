package com.ojasxlabs.cricketlogics;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //ShareActionProvider shareActionProvider;

    long no_of_matches;
    private String team1,team2,winner,date;
    private ListView mListView;
    private ArrayList<String> data= new ArrayList<>();

    private DatabaseReference db_ref;
    //private DatabaseReference match_ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        db_ref=FirebaseDatabase.getInstance().getReference();
        mListView = (ListView) findViewById(R.id.list);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        mListView.setAdapter(arrayAdapter);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }// assign action to menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.setings) {
            // Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
            Intent settingIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingIntent);
            return false;
        }
        else if(id == R.id.share) {
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            startActivity(Intent.createChooser(i,"Share Using"));
            return super.onOptionsItemSelected(item);
        }
        else{
            Intent feedbackIntent = new Intent(MainActivity.this, FeedbackActivity.class);
            startActivity(feedbackIntent);
            return false;
        }

    }
    @Override
    public void onStart(){
        super.onStart();
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                no_of_matches = dataSnapshot.getChildrenCount();
                Toast.makeText(getApplicationContext(),"no of matches = "+ no_of_matches,Toast.LENGTH_LONG).show();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    team1=ds.child("Team 1").getValue(String.class);
                    team2=ds.child("Team 2").getValue(String.class);
                    winner=ds.child("Winner").getValue(String.class);
                    date=ds.child("Date").getValue(String.class);
                    //data.add("Match "+ no_of_matches);
                    data.add(team1+" "+team2+" "+winner+" "+date);
                    //data.add(team2);

                    Toast.makeText(getApplicationContext(),team1+" "+team2+" "+winner+" "+date+" ",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
