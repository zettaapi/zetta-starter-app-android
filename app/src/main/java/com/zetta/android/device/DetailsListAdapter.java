package com.zetta.android.device;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zetta.android.ImageLoader;
import com.zetta.android.R;

import java.util.ArrayList;
import java.util.List;

class DetailsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ListItem> listItems = new ArrayList<>();

    private final ImageLoader imageLoader;
    private final OnActionClickListener onActionClickListener;

    public DetailsListAdapter(ImageLoader imageLoader, OnActionClickListener onActionClickListener) {
        this.imageLoader = imageLoader;
        this.onActionClickListener = onActionClickListener;
    }

    public void updateAll(List<ListItem> listItems) {
        this.listItems.clear();
        this.listItems.addAll(listItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return listItems.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ListItem.TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header, parent, false);
            return new HeaderViewHolder(v);
        } else if (viewType == ListItem.TYPE_ACTIONS) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_action, parent, false);
            return new ActionsViewHolder(v);
        }
        throw new IllegalStateException("Attempted to create view holder for a type you haven't coded for: " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ListItem.TYPE_HEADER) {
            ListItem.HeaderListItem headerListItem = (ListItem.HeaderListItem) listItems.get(position);
            ((HeaderViewHolder) holder).bind(headerListItem);
            return;
        } else if (type == ListItem.TYPE_ACTIONS) {
            ListItem.ActionListItem actionListItem = (ListItem.ActionListItem) listItems.get(position);
            ((ActionsViewHolder) holder).bind(actionListItem, onActionClickListener);
            return;
        }
        throw new IllegalStateException("Attempted to bind a type you haven't coded for: " + type);
    }

    public interface OnActionClickListener {
        void onActionClick(String label);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView headerWidget;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerWidget = (TextView) itemView.findViewById(R.id.list_item_header_label);
        }

        public void bind(ListItem.HeaderListItem headerListItem) {
            String title = headerListItem.getTitle();
            headerWidget.setText(title);
        }
    }

    public class ActionsViewHolder extends RecyclerView.ViewHolder {

        private final TextView actionLabelWidget;
        private final Button actionToggleButton;

        public ActionsViewHolder(View itemView) {
            super(itemView);
            actionLabelWidget = (TextView) itemView.findViewById(R.id.list_item_action_label);
            actionToggleButton = (Button) itemView.findViewById(R.id.list_item_action_toggle);
        }

        public void bind(final ListItem.ActionListItem item, final OnActionClickListener onActionClickListener) {
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
}
