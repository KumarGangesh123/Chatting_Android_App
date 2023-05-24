package Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.india.whatsapp_clone_android.ChatsDetailsActivity;
import com.india.whatsapp_clone_android.R;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;

import Fragments.ChatsFragment;
import Models.SignUpModel;

public class UserChatsAdapter extends RecyclerView.Adapter<UserChatsAdapter.ViewHolder>{

    ArrayList<SignUpModel> userData;
    Context context;

    public UserChatsAdapter(ArrayList<SignUpModel> userData, Context context) {
        this.userData = userData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_chats_view,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SignUpModel user = userData.get(position);

        holder.chatsUserName.setText(user.getUser_name());

        FirebaseDatabase.getInstance().getReference().child("chats")
                        .child(FirebaseAuth.getInstance().getUid()+user.getUser_ID())
                                .orderByChild("timestamp")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.hasChildren()){
                                                    for(DataSnapshot dataShot : snapshot.getChildren()){
                                                        holder.chatsLastMessage.setText(dataShot.child("message").getValue(String.class));
                                                    }
                                                }else{
                                                    holder.chatsLastMessage.setText("Last Message");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatsDetailsActivity.class);
                intent.putExtra("userID",user.getUser_ID());
                intent.putExtra("userName",user.getUser_name());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return userData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView chatsUserName, chatsLastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chatsUserName = itemView.findViewById(R.id.chatUserName);
            chatsLastMessage = itemView.findViewById(R.id.lastMessageUser);

        }
    }



}
