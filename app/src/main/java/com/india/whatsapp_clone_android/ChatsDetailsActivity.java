package com.india.whatsapp_clone_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.india.whatsapp_clone_android.databinding.ActivityChatsDetailsBinding;

import java.util.ArrayList;
import java.util.Date;

import Adapter.ChatAdapter;
import Models.MessageModel;

public class ChatsDetailsActivity extends AppCompatActivity {

    ActivityChatsDetailsBinding binding;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        binding = ActivityChatsDetailsBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderID = auth.getUid();

        String receiverID = getIntent().getStringExtra("userID");
        String userName = getIntent().getStringExtra("userName");

        binding.chatsDetailUserName.setText(userName);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModels,this,receiverID);

        binding.recyclerViewChatsDetails.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewChatsDetails.setAdapter(chatAdapter);

        final String senderRoom = senderID+receiverID;
        final String receiverRoom = receiverID+senderID;


        firebaseDatabase.getReference().child("chats")
                        .child(senderRoom)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        messageModels.clear();
                                        for(DataSnapshot dataShot : snapshot.getChildren()){
                                            MessageModel messageModel = dataShot.getValue(MessageModel.class);
                                            messageModel.setMessageId(dataShot.getKey());
                                            messageModels.add(messageModel);
                                        }
                                        chatAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


        binding.messageArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = binding.senderMessage.getText().toString();
                final MessageModel messageModel = new MessageModel(senderID, message);
                messageModel.setTimestamp(new Date().getTime());
                binding.senderMessage.setText("");

                firebaseDatabase.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                firebaseDatabase.getReference().child("chats")
                                        .child(receiverRoom)
                                        .push()
                                        .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
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