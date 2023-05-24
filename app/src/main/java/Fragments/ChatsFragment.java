package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.india.whatsapp_clone_android.R;
import com.india.whatsapp_clone_android.databinding.FragmentChatsBinding;

import java.util.ArrayList;

import Adapter.UserChatsAdapter;
import Models.SignUpModel;

public class ChatsFragment extends Fragment {

    public ChatsFragment() {
        // Required empty public constructor
    }

    FragmentChatsBinding binding;
    ArrayList<SignUpModel> userData = new ArrayList<>();

    FirebaseDatabase firebaseDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        firebaseDatabase = FirebaseDatabase.getInstance();
        binding = FragmentChatsBinding.inflate(inflater, container, false);

        UserChatsAdapter adapter = new UserChatsAdapter(userData, getContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        firebaseDatabase.getReference().child("WhatsUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userData.clear();
                for(DataSnapshot dataShot : snapshot.getChildren()){
                    SignUpModel model = dataShot.getValue(SignUpModel.class);
                    model.setUser_ID(dataShot.getKey());
                    if(!model.getUser_ID().equals(FirebaseAuth.getInstance().getUid())){
                        userData.add(model);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });

        return binding.getRoot();
    }
}