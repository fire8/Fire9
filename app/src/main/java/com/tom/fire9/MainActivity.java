package com.tom.fire9;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user !=null) {
            String uid = user.getUid();
            Log.d("MainActivity", "UID:"+uid);
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference users = db.getReference("users");
            users.child(uid).child("phone").setValue("098887766");
            users.child(uid).child("nickname")
                    .setValue("Jack");
            DatabaseReference friendRef = users
                    .child(uid)
                    .child("friends").push();
            Map<String, Object> map = new HashMap<>();
            map.put("name", "Mary");
            map.put("phone", "88776688");
            friendRef.setValue(map);


        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (user!=null){
            auth.signOut();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // check login?
        if (FirebaseAuth.getInstance().getCurrentUser()==null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else{

        }

//        Firebase.setAndroidContext(this);

        ListView list = (ListView) findViewById(R.id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1
        );
        list.setAdapter(adapter);
//        before2016(adapter);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference("contacts")
                .addChildEventListener(new com.google.firebase.database.ChildEventListener() {
                    @Override
                    public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                        String name =
                                (String) dataSnapshot.child("name").getValue();
                        adapter.add(name);
                    }

                    @Override
                    public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {
                        String name =
                                (String) dataSnapshot.child("name").getValue();
                        adapter.remove(name);
                    }

                    @Override
                    public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    private void before2016(final ArrayAdapter<String> adapter) {
        new Firebase("https://fire9-c63d7.firebaseio.com/contacts/")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String name =
                                (String) dataSnapshot.child("name").getValue();
                        adapter.add(name);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        String name =
                                (String) dataSnapshot.child("name").getValue();
                        adapter.remove(name);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
    }
}
