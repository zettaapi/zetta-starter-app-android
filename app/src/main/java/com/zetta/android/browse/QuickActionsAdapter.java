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

    private final List<ListItem> items = new ArrayList<>();
    private final OnActionClickListener onActionClickListener;

    public QuickActionsAdapter(OnActionClickListener onActionClickListener) {
        this.onActionClickListener = onActionClickListener;
    }

    public void updateAll(List<ListItem> listItems) {
        this.items.clear();
        this.items.addAll(listItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ListItem.TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_quick_action_header, parent, false);
            return new HeaderViewHolder(v);
        } else if (viewType == ListItem.TYPE_ACTION) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_quick_action, parent, false);
            return new QuickActionViewHolder(v);
        }
        throw new IllegalStateException("Attempted to create view holder for a type you haven't coded for: " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ListItem.TYPE_HEADER) {
            ListItem.HeaderQuickActionsListItem headerListItem = (ListItem.HeaderQuickActionsListItem) items.get(position);
            ((HeaderViewHolder) holder).bind(headerListItem);
            return;
        } else if (type == ListItem.TYPE_ACTION) {
            ListItem.QuickActionListItem actionListItem = (ListItem.QuickActionListItem) items.get(position);
            ((QuickActionViewHolder) holder).bind(
                actionListItem,
                onActionClickListener
            );
            return;
        }
        throw new IllegalStateException("Attempted to bind a type you haven't coded for: " + type);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnActionClickListener {
        void onActionClick(String label);
    }

    public static class QuickActionViewHolder extends RecyclerView.ViewHolder {

        private final TextView actionLabelWidget;
        private final Button actionToggleButton;

        public QuickActionViewHolder(View itemView) {
            super(itemView);
            actionLabelWidget = (TextView) itemView.findViewById(R.id.list_item_action_label);
            actionToggleButton = (Button) itemView.findViewById(R.id.list_item_action_toggle);
        }

        public void bind(final ListItem.QuickActionListItem item, final OnActionClickListener onActionClickListener) {
            actionLabelWidget.setText(item.getLabel());
            actionToggleButton.setText(item.getAction());
            actionToggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onActionClickListener.onActionClick(item.getLabel());
                }
            });
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView headerTitleWidget;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitleWidget = (TextView) itemView.findViewById(R.id.list_item_action_header_label);
        }

        public void bind(ListItem.HeaderQuickActionsListItem item) {
            headerTitleWidget.setText(item.getTitle());
        }
    }
}
