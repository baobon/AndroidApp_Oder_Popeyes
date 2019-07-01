package com.example.eatit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.eatit.Common.Common;
import com.example.eatit.Interface.ItemClickListener;
import com.example.eatit.Model.Request;
import com.example.eatit.ViewHolder.OderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    public String states;
    FirebaseRecyclerAdapter<Request, OderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_status);

        //init Firebae
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = findViewById(R.id.listOders);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOders(Common.currentUser.getPhone());

    }


    private void loadOders(String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, OderViewHolder>(
                Request.class,
                R.layout.oder_layout,
                OderViewHolder.class,
                requests.orderByChild("phone")
                .equalTo(phone)
        )
        {
            @Override
            protected void populateViewHolder(OderViewHolder viewHolder, final Request model, int position) {
                states = model.getStatus();
                viewHolder.txtOderId.setText("Mã đơn hàng: #"+adapter.getRef(position).getKey());
                viewHolder.txtOderStatus.setText("Tình trạng: "+Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOderAddress.setText("Địa chỉ: " +model.getAddress());
                viewHolder.txtOderPhone.setText("Số điện thoại: " + model.getPhone());
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(OderStatus.this, convertCodeToStatus(model.getStatus()), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }


    private String convertCodeToStatus(String status) {
        if(status.equals("0")){
            return "Đã xác nhận";
        }else if(status.equals("1")){
            return "Đơn hàng đang được chuyển đến";
        }else{
            return "Đã giao !";
        }
    }

}
