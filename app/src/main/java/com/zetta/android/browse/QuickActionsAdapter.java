package com.zetta.android.browse;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zetta.android.ListItem;
import com.zetta.android.R;
import com.zetta.android.device.actions.ActionMultipleInputListItem;
import com.zetta.android.device.actions.ActionMultipleViewHolder;
import com.zetta.android.device.actions.ActionOnOffListItem;
import com.zetta.android.device.actions.ActionOnOffViewHolder;
import com.zetta.android.device.actions.ActionSingleInputListItem;
import com.zetta.android.device.actions.ActionSingleViewHolder;
import com.zetta.android.device.actions.OnActionClickListener;

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
        } else if (viewType == ListItem.TYPE_ACTION_ON_OFF) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_action_on_off, parent, false);
            return new ActionOnOffViewHolder(v);
        } else if (viewType == ListItem.TYPE_ACTION_SINGLE_INPUT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_action_input_single, parent, false);
            return new ActionSingleViewHolder(v);
        } else if (viewType == ListItem.TYPE_ACTION_MULTIPLE_INPUT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_action_input_multiple, parent, false);
            return new ActionMultipleViewHolder(v);
        }
        throw new IllegalStateException("Attempted to create view holder for a type you haven't coded for: " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ListItem.TYPE_HEADER) {
            ListItem.HeaderListItem headerListItem = (ListItem.HeaderListItem) items.get(position);
            ((HeaderViewHolder) holder).bind(headerListItem);
            return;
        } else if (type == ListItem.TYPE_ACTION_ON_OFF) {
            ActionOnOffListItem actionOnOffListItem = (ActionOnOffListItem) items.get(position);
            ((ActionOnOffViewHolder) holder).bind(actionOnOffListItem, onActionClickListener);
            return;
        } else if (type == ListItem.TYPE_ACTION_SINGLE_INPUT) {
            ActionSingleInputListItem actionSingleListItem = (ActionSingleInputListItem) items.get(position);
            ((ActionSingleViewHolder) holder).bind(actionSingleListItem, onActionClickListener);
            return;
        } else if (type == ListItem.TYPE_ACTION_MULTIPLE_INPUT) {
            ActionMultipleInputListItem actionMultipleListItem = (ActionMultipleInputListItem) items.get(position);
            ((ActionMultipleViewHolder) holder).bind(actionMultipleListItem, onActionClickListener);
            return;
        }
        throw new IllegalStateException("Attempted to bind a type you haven't coded for: " + type);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView headerTitleWidget;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitleWidget = (TextView) itemView.findViewById(R.id.list_item_action_header_label);
        }

        public void bind(ListItem.HeaderListItem item) {
            headerTitleWidget.setText(item.getTitle());
        }
    }
}
