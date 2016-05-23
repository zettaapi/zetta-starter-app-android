package com.zetta.android.browse;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zetta.android.R;

import java.util.ArrayList;
import java.util.List;

class QuickActionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> items = new ArrayList<>();

    public void updateAll(List<String> listItems) {
        this.items.clear();
        this.items.addAll(listItems);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_quick_action, parent, false);
        return new QuickActionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((QuickActionViewHolder) holder).bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class QuickActionViewHolder extends RecyclerView.ViewHolder {

        private final TextView actionNameWidget;
        private final Button actionToggleButton;

        public QuickActionViewHolder(View itemView) {
            super(itemView);
            actionNameWidget = (TextView) itemView.findViewById(R.id.list_item_action_name);
            actionToggleButton = (Button) itemView.findViewById(R.id.list_item_action_toggle);
        }

        public void bind(String item) {
            actionNameWidget.setText(item);
            actionToggleButton.setText(item);
        }
    }
}
