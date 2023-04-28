package com.example.clonewhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.clonewhatsapp.Adapter.ChapterAdapter;
import com.example.clonewhatsapp.databinding.ActivityChatdetailActivityyBinding;
import com.example.clonewhatsapp.models.MessagesModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CHATDetailActivityy extends AppCompatActivity {

    ActivityChatdetailActivityyBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatdetailActivityyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        final String senderId = auth.getUid();
        String receiverId = getIntent().getStringExtra("usedId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.userNamee.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.avvvatar).into(binding.userProfilePicc);

        binding.backArroww.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CHATDetailActivityy.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<MessagesModel> messagesModels = new ArrayList<>();
        final ChapterAdapter chapterAdapter = new ChapterAdapter(messagesModels, this, receiverId);
        binding.chatRecyclerView.setAdapter(chapterAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        final String senderRoom = senderId + receiverId;
        final String receiverRoom = receiverId + senderId ;

        database.getReference().child("chats").child(senderRoom)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messagesModels.clear();
                                for (DataSnapshot snapshot1: snapshot.getChildren()){
                                    MessagesModel model = snapshot1.getValue(MessagesModel.class);
                                    model.setMessageId(snapshot1.getKey());
                                    messagesModels.add(model);
                                }
                                chapterAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.etMessage.getText().toString().isEmpty()){
                    binding.etMessage.setError("Please enter text");
                    return;
                }

               String Message =  binding.etMessage.getText().toString();
               final MessagesModel model = new MessagesModel(senderId, Message);
               model.setTimeStamp(new Date().getTime());
               binding.etMessage.setText("");

               database.getReference().child("chats")
                       .child(senderRoom)
                       .push().setValue(model)
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                                database.getReference().child("chats")
                                        .child(receiverRoom)
                                        .push().setValue(model)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                           }
                       });

            }
        });

    }
}