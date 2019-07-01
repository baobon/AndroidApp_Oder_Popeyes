package bao.dev.serverpopeyes.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import bao.dev.serverpopeyes.Interface.ItemClickListener;
import bao.dev.serverpopeyes.R;

public class OderViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnCreateContextMenuListener {

    public TextView txtOderId,txtOderAddress,txtOderStatus,txtOderPhone;

    private ItemClickListener itemClickListener;

    public OderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOderAddress = itemView.findViewById(R.id.oder_adress);
        txtOderId = itemView.findViewById(R.id.oder_id);
        txtOderPhone = itemView.findViewById(R.id.oder_phone);
        txtOderStatus = itemView.findViewById(R.id.oder_status);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }


    //Creat  long click
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the Action");
        menu.add(0,0,getAdapterPosition(),"Update");
        menu.add(0,1,getAdapterPosition(),"Delete");

    }
}
