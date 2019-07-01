package bao.dev.serverpopeyes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.util.Strings;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.zip.Inflater;

import bao.dev.serverpopeyes.Common.Common;
import bao.dev.serverpopeyes.Interface.ItemClickListener;
import bao.dev.serverpopeyes.Model.Oder;
import bao.dev.serverpopeyes.Model.Request;
import bao.dev.serverpopeyes.ViewHolder.OderViewHolder;

public class OderStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    //Firebase
    FirebaseRecyclerAdapter<Request, OderViewHolder> adapter;

    FirebaseDatabase db;
    DatabaseReference requests;

    MaterialSpinner materialSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_status);

        //Init Firebase
        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Requests");

        //RecycleView
        recyclerView = findViewById(R.id.listOders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOders();

    }

    private void loadOders() {
        adapter = new FirebaseRecyclerAdapter<Request, OderViewHolder>(
                Request.class,
                R.layout.oder_layout,
                OderViewHolder.class,
                requests
        ) {
            @Override
            protected void populateViewHolder(OderViewHolder viewHolder, final Request model, int position) {
                viewHolder.txtOderId.setText("Mã đơn hàng: #"+adapter.getRef(position).getKey());
                viewHolder.txtOderStatus.setText("Tình trạng: "+Common.converCodeToStatus(model.getStatus()));
                viewHolder.txtOderAddress.setText("Địa chỉ: " +model.getAddress());
                viewHolder.txtOderPhone.setText("Số điện thoại: " + model.getPhone());

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent trackingOder = new Intent(OderStatus.this,MapsActivity.class);
                        Common.currentRequest = model;
                        startActivity(trackingOder);

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else if(item.getTitle().equals(Common.DELETE)){
            deleteOder(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteOder(String key) {
        requests.child(key).removeValue();
    }

    private void showUpdateDialog(String key, final Request item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OderStatus.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Chọn trạng thái đơn hàng");

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_oder,null);

        materialSpinner = view.findViewById(R.id.statusSpiner);
        materialSpinner.setItems("Đã xác nhận","Đơn hàng đang được chuyển đến","Đã giao !");

        alertDialog.setView(view);

        final String localKey = key;
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(materialSpinner.getSelectedIndex()));
                requests.child(localKey).setValue(item);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }
}
