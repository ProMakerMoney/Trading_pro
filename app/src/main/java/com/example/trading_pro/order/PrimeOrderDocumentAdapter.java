package com.example.trading_pro.order;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trading_pro.R;


import java.util.List;

public class PrimeOrderDocumentAdapter extends RecyclerView.Adapter<PrimeOrderDocumentAdapter.DocumentViewHolder> {
    private List<PrimeOrderDocument> documents;

    public PrimeOrderDocumentAdapter(List<PrimeOrderDocument> documents) {
        this.documents = documents;
    }

    @Override
    public PrimeOrderDocumentAdapter.DocumentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new PrimeOrderDocumentAdapter.DocumentViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(PrimeOrderDocumentAdapter.DocumentViewHolder holder, int position) {
        PrimeOrderDocument document = documents.get(position);
        holder.id.setText(document.getId().toString());
        holder.type.setText(document.getType().toString());
        holder.margin.setText(String.format("%.2f", document.getMargin()) + "$");
        holder.perProfitOrder.setText(String.format("%.2f", document.getPerProfitOrder()) + "%");
        holder.profit.setText(String.format("%.2f", document.getProfit()) + "$");
        holder.perProfitDeposit.setText(String.format("%.2f", document.getPerProfitDeposit()) + "%");

    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    public static class DocumentViewHolder extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView type;
        public TextView margin;
        public TextView perProfitOrder;
        public TextView profit;
        public TextView perProfitDeposit;

        public DocumentViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.order_id);
            type = itemView.findViewById(R.id.order_type);
            margin = itemView.findViewById(R.id.order_margin);
            perProfitOrder = itemView.findViewById(R.id.order_profit_per);
            profit = itemView.findViewById(R.id.order_profit);
            perProfitDeposit = itemView.findViewById(R.id.order_per);
        }
    }
}
