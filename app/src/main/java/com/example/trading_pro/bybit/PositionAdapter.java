package com.example.trading_pro.bybit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trading_pro.R;


import java.util.List;

public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.PositionViewHolder> {

    private List<Position> positions;
    private OnCloseButtonClickListener onCloseButtonClickListener;

    public PositionAdapter(List<Position> positions, OnCloseButtonClickListener listener) {
        this.positions = positions;
        this.onCloseButtonClickListener = listener;
    }


    @NonNull
    @Override
    public PositionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_position, parent, false);
        return new PositionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PositionViewHolder holder, int position) {
        Position positionObj = positions.get(position);
        holder.symbolTextView.setText(positionObj.getSymbol());
        holder.sizeTextView.setText(positionObj.getSize());
        holder.avgPriceTextView.setText(positionObj.getAvgPrice());
        holder.riskLimitValueTextView.setText(positionObj.getRiskLimitValue());
        holder.unrealisedPnl.setText(positionObj.getUnrealisedPnl());

        // Устанавливаем действие при нажатии на кнопку "Закрыть"
        holder.closeButton.setOnClickListener(v -> {
            if (onCloseButtonClickListener != null) {
                onCloseButtonClickListener.onCloseButtonClick(positionObj.getSymbol());
            }
        });
    }

    @Override
    public int getItemCount() {
        return positions.size();
    }

    public void updatePositions(List<Position> newPositions) {
        this.positions = newPositions;
        notifyDataSetChanged();
    }

    public interface OnCloseButtonClickListener {
        void onCloseButtonClick(String tradingPair);
    }

    public static class PositionViewHolder extends RecyclerView.ViewHolder {
        TextView symbolTextView;
        TextView sizeTextView;
        TextView avgPriceTextView;
        TextView riskLimitValueTextView;
        TextView unrealisedPnl;
        AppCompatButton closeButton;

        public PositionViewHolder(@NonNull View itemView) {
            super(itemView);
            symbolTextView = itemView.findViewById(R.id.tradingPair);
            sizeTextView = itemView.findViewById(R.id.positionValue);
            avgPriceTextView = itemView.findViewById(R.id.entryPrice);
            riskLimitValueTextView = itemView.findViewById(R.id.riskLimitValue);
            unrealisedPnl = itemView.findViewById(R.id.pnl);
            closeButton = itemView.findViewById(R.id.closePosition);
        }
    }
}