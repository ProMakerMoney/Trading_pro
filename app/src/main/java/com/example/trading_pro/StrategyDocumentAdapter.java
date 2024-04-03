package com.example.trading_pro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StrategyDocumentAdapter extends RecyclerView.Adapter<StrategyDocumentAdapter.DocumentViewHolder> {
    private List<StrategyDocument> documents;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(StrategyDocument document);
    }

    public StrategyDocumentAdapter(List<StrategyDocument> documents) {
        this.documents = documents;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public DocumentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_strategy, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DocumentViewHolder holder, int position) {
        StrategyDocument document = documents.get(position);
        holder.title.setText(document.getName());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(document);
            }
        });
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    public static class DocumentViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public DocumentViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

    public void updateData(List<StrategyDocument> newData) {
        documents.clear();
        documents.addAll(newData);
        notifyDataSetChanged();
    }

}
