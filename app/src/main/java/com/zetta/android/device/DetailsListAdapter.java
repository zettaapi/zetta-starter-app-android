package com.zetta.android.device;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.novoda.notils.logger.simple.Log;
import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;
import com.zetta.android.R;
import com.zetta.android.device.actions.ActionMultipleInputListItem;
import com.zetta.android.device.actions.ActionMultipleViewHolder;
import com.zetta.android.device.actions.ActionSingleInputListItem;
import com.zetta.android.device.actions.ActionSingleViewHolder;
import com.zetta.android.device.actions.ActionToggleListItem;
import com.zetta.android.device.actions.ActionToggleViewHolder;
import com.zetta.android.device.actions.OnActionClickListener;

import java.util.ArrayList;
import java.util.List;

class DetailsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ListItem> listItems = new ArrayList<>();

    private final ImageLoader imageLoader;
    private final OnActionClickListener onActionClickListener;
    private final OnEventsClickListener onEventsClickListener;

    public DetailsListAdapter(ImageLoader imageLoader,
                              OnActionClickListener onActionClickListener,
                              OnEventsClickListener onEventsClickListener) {
        this.imageLoader = imageLoader;
        this.onActionClickListener = onActionClickListener;
        this.onEventsClickListener = onEventsClickListener;
    }

    public void updateAll(List<ListItem> listItems) {
        this.listItems.clear();
        this.listItems.addAll(listItems);
        notifyDataSetChanged();
    }

    public void update(ListItem listItem) {
        int i = listItems.indexOf(listItem);
        if (i == -1) {
            Log.e("Not found in list");
            return;
        }
        listItems.remove(i);
        listItems.add(i, listItem);
        notifyItemChanged(i);
    }

    @Override
    public int getItemViewType(int position) {
        return listItems.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public boolean isEmpty() {
        return listItems.isEmpty();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ListItem.TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header, parent, false);
            return new HeaderViewHolder(v);
        } else if (viewType == ListItem.TYPE_ACTION_TOGGLE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_action_toggle, parent, false);
            return new ActionToggleViewHolder(v);
        } else if (viewType == ListItem.TYPE_ACTION_SINGLE_INPUT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_action_input_single, parent, false);
            return new ActionSingleViewHolder(v);
        } else if (viewType == ListItem.TYPE_ACTION_MULTIPLE_INPUT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_action_input_multiple, parent, false);
            return new ActionMultipleViewHolder(v);
        } else if (viewType == ListItem.TYPE_STREAM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_stream, parent, false);
            return new StreamViewHolder(v);
        } else if (viewType == ListItem.TYPE_PROPERTY) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_property, parent, false);
            return new PropertyViewHolder(v);
        } else if (viewType == ListItem.TYPE_EVENTS) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_events, parent, false);
            return new EventsViewHolder(v);
        } else if (viewType == ListItem.TYPE_STATE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_state, parent, false);
            return new StateViewHolder(v, imageLoader);
        } else if (viewType == ListItem.TYPE_EMPTY) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_empty, parent, false);
            return new EmptyViewHolder(v);
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
        } else if (type == ListItem.TYPE_ACTION_TOGGLE) {
            ActionToggleListItem actionToggleListItem = (ActionToggleListItem) listItems.get(position);
            ((ActionToggleViewHolder) holder).bind(actionToggleListItem, onActionClickListener);
            return;
        } else if (type == ListItem.TYPE_ACTION_SINGLE_INPUT) {
            ActionSingleInputListItem actionSingleListItem = (ActionSingleInputListItem) listItems.get(position);
            ((ActionSingleViewHolder) holder).bind(actionSingleListItem, onActionClickListener);
            return;
        } else if (type == ListItem.TYPE_ACTION_MULTIPLE_INPUT) {
            ActionMultipleInputListItem actionMultipleListItem = (ActionMultipleInputListItem) listItems.get(position);
            ((ActionMultipleViewHolder) holder).bind(actionMultipleListItem, onActionClickListener);
            return;
        } else if (type == ListItem.TYPE_STREAM) {
            StreamListItem streamListItem = (StreamListItem) listItems.get(position);
            ((StreamViewHolder) holder).bind(streamListItem);
            return;
        } else if (type == ListItem.TYPE_PROPERTY) {
            PropertyListItem propertyListItem = (PropertyListItem) listItems.get(position);
            ((PropertyViewHolder) holder).bind(propertyListItem);
            return;
        } else if (type == ListItem.TYPE_EVENTS) {
            EventsListItem eventsListItem = (EventsListItem) listItems.get(position);
            ((EventsViewHolder) holder).bind(eventsListItem, onEventsClickListener);
            return;
        } else if (type == ListItem.TYPE_STATE) {
            StateListItem stateListItem = (StateListItem) listItems.get(position);
            ((StateViewHolder) holder).bind(stateListItem);
            return;
        } else if (type == ListItem.TYPE_EMPTY) {
            ListItem.EmptyListItem emptyListItem = (ListItem.EmptyListItem) listItems.get(position);
            ((EmptyViewHolder) holder).bind(emptyListItem);
            return;
        }
        throw new IllegalStateException("Attempted to bind a type you haven't coded for: " + type);
    }

    public interface OnEventsClickListener {
        void onEventsClick();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

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

    public static class StreamViewHolder extends RecyclerView.ViewHolder {

        private final TextView streamLabelWidget;
        private final TextView valueLabelWidget;

        public StreamViewHolder(View itemView) {
            super(itemView);
            streamLabelWidget = (TextView) itemView.findViewById(R.id.list_item_stream_label);
            valueLabelWidget = (TextView) itemView.findViewById(R.id.list_item_stream_value_label);
        }

        public void bind(StreamListItem item) {
            streamLabelWidget.setText(item.getStream());
            streamLabelWidget.setTextColor(item.getForegroundColor());
            valueLabelWidget.setText(item.getValue());
            valueLabelWidget.setTextColor(item.getForegroundColor());
            itemView.setBackground(item.getBackground());
        }
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {

        private final TextView propertyLabelWidget;
        private final TextView valueLabelWidget;

        public PropertyViewHolder(View itemView) {
            super(itemView);
            propertyLabelWidget = (TextView) itemView.findViewById(R.id.list_item_property_label);
            valueLabelWidget = (TextView) itemView.findViewById(R.id.list_item_property_value_label);
        }

        public void bind(PropertyListItem item) {
            propertyLabelWidget.setText(item.getProperty());
            propertyLabelWidget.setTextColor(item.getForegroundColor());
            valueLabelWidget.setText(item.getValue());
            valueLabelWidget.setTextColor(item.getForegroundColor());
            itemView.setBackground(item.getBackgroundDrawable());
        }
    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder {

        private final TextView eventsLabelWidget;

        public EventsViewHolder(View itemView) {
            super(itemView);
            eventsLabelWidget = (TextView) itemView.findViewById(R.id.list_item_events_label);
        }

        public void bind(EventsListItem item, final OnEventsClickListener onEventsClickListener) {
            eventsLabelWidget.setText(item.getDescription());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEventsClickListener.onEventsClick();
                }
            });
        }
    }

    public static class StateViewHolder extends RecyclerView.ViewHolder {

        private final ImageLoader imageLoader;
        private final TextView stateLabelWidget;
        private final ImageView stateImageWidget;

        public StateViewHolder(View itemView, ImageLoader imageLoader) {
            super(itemView);
            this.imageLoader = imageLoader;
            stateImageWidget = (ImageView) itemView.findViewById(R.id.list_item_state_image);
            stateLabelWidget = (TextView) itemView.findViewById(R.id.list_item_state_label);
        }

        public void bind(StateListItem item) {
            imageLoader.load(item.getStateImageUri(), stateImageWidget);
            stateImageWidget.setColorFilter(item.getStateColor());
            stateLabelWidget.setText(item.getState());
            stateLabelWidget.setTextColor(item.getStateColor());

        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {

        private final TextView messageWidget;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            messageWidget = (TextView) itemView.findViewById(R.id.list_item_empty_message);
        }

        public void bind(ListItem.EmptyListItem listItem) {
            messageWidget.setText(listItem.getMessage());
            itemView.setBackground(listItem.getBackground());
        }

    }
}
