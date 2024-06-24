package com.example.trading_pro.coin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trading_pro.R;

import java.util.List;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinViewHolder> {

    private List<Coin> coinList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Coin coin);
    }

    public CoinAdapter(List<Coin> coinList, OnItemClickListener onItemClickListener) {
        this.coinList = coinList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coin, parent, false);
        return new CoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder holder, int position) {
        Coin coin = coinList.get(position);
        holder.bind(coin, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return coinList.size();
    }

    static class CoinViewHolder extends RecyclerView.ViewHolder {
        TextView coinName;
        TextView timeframe;
        TextView isCounted;

        public CoinViewHolder(@NonNull View itemView) {
            super(itemView);
            coinName = itemView.findViewById(R.id.coin_name);
            timeframe = itemView.findViewById(R.id.timeframe);
            isCounted = itemView.findViewById(R.id.is_counted);
        }

        public void bind(final Coin coin, final OnItemClickListener listener) {
            coinName.setText(coin.getCoinName());
            timeframe.setText("T/F: " + coin.getTimeframe());
            isCounted.setText("IsC: " + coin.isCounted());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(coin);
                }
            });
        }
    }
}