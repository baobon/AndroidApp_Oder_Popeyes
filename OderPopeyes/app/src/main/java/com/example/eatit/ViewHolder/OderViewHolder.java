package com.example.eatit.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.eatit.Interface.ItemClickListener;
import com.example.eatit.R;




public class OderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOderId,txtOderAddress,txtOderStatus,txtOderPhone;

    private ItemClickListener itemClickListener;

    public OderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOderAddress = itemView.findViewById(R.id.oder_adress);
        txtOderId = itemView.findViewById(R.id.oder_id);
        txtOderPhone = itemView.findViewById(R.id.oder_phone);
        txtOderStatus = itemView.findViewById(R.id.oder_status);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}