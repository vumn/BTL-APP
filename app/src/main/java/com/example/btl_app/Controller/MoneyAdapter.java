package com.example.btl_app.Controller;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_app.Model.MoneyItem;
import com.example.btl_app.R;

import java.util.List;

public class MoneyAdapter extends RecyclerView.Adapter<MoneyAdapter.ViewHolder> {

    private List<MoneyItem> list;

    public MoneyAdapter(List<MoneyItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_money, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MoneyItem item = list.get(position);

        holder.txtMoney.setText(item.getText());

        if (item.isSelected()) {

            holder.txtMoney.setBackgroundColor(Color.parseColor("#E39B3B"));
            holder.txtMoney.setTextColor(Color.BLACK);

            // Animation nhấp nháy
            ObjectAnimator animator = ObjectAnimator.ofFloat(
                    holder.txtMoney,
                    "alpha",
                    1f,
                    0.3f,
                    1f
            );

            animator.setDuration(500);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.start();

        } else {

            holder.txtMoney.setBackgroundColor(Color.TRANSPARENT);
            holder.txtMoney.setTextColor(Color.parseColor("#F7B500"));
            holder.txtMoney.setAlpha(1f);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtMoney;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMoney = itemView.findViewById(R.id.txtMoney);
        }
    }
}