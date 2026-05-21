package com.example.btl_app.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_app.Model.MoneyItem;
import com.example.btl_app.R;

import java.util.List;

public class MoneyAdapter
        extends RecyclerView.Adapter<MoneyAdapter.ViewHolder> {

    private List<MoneyItem> list;

    public MoneyAdapter(List<MoneyItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_money,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        MoneyItem item = list.get(position);

        holder.txtLevel.setText(
                String.valueOf(15 - position));

        holder.txtMoney.setText(item.getMoney());

        if (item.isSelected()) {

            holder.imgBg.setImageResource(
                    R.drawable.bg_money_selected);

        } else {

            holder.imgBg.setImageResource(
                    R.drawable.bg_money_item);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgBg;
        TextView txtLevel, txtMoney;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgBg = itemView.findViewById(R.id.imgBg);
            txtLevel = itemView.findViewById(R.id.txtLevel);
            txtMoney = itemView.findViewById(R.id.txtMoney);
        }
    }
}