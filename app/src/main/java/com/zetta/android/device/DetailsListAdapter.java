package com.zetta.android.device;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zetta.android.ImageLoader;
import com.zetta.android.R;

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
        } else if (viewType == ListItem.TYPE_ACTION) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_action, parent, false);
            return new ActionViewHolder(v);
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
        } else if (type == ListItem.TYPE_ACTION) {
            ListItem.ActionListItem actionListItem = (ListItem.ActionListItem) listItems.get(position);
            ((ActionViewHolder) holder).bind(actionListItem, onActionClickListener);
            return;
        } else if (type == ListItem.TYPE_STREAM) {
            ListItem.StreamListItem streamListItem = (ListItem.StreamListItem) listItems.get(position);
            ((StreamViewHolder) holder).bind(streamListItem);
            return;
        } else if (type == ListItem.TYPE_PROPERTY) {
            ListItem.PropertyListItem propertyListItem = (ListItem.PropertyListItem) listItems.get(position);
            ((PropertyViewHolder) holder).bind(propertyListItem);
            return;
        } else if (type == ListItem.TYPE_EVENTS) {
            ListItem.EventsListItem eventsListItem = (ListItem.EventsListItem) listItems.get(position);
            ((EventsViewHolder) holder).bind(eventsListItem, onEventsClickListener);
            return;
        } else if (type == ListItem.TYPE_STATE) {
            ListItem.StateListItem stateListItem = (ListItem.StateListItem) listItems.get(position);
            ((StateViewHolder) holder).bind(stateListItem);
            return;
        }
        throw new IllegalStateException("Attempted to bind a type you haven't coded for: " + type);
    }

    public interface OnActionClickListener {
        void onActionClick(String label);
    }

    public interface OnEventsClickListener {
        void onEventsClick();
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

    public class ActionViewHolder extends RecyclerView.ViewHolder {

        private final TextView actionLabelWidget;
        private final Button actionToggleButton;

        public ActionViewHolder(View itemView) {
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

    public class StreamViewHolder extends RecyclerView.ViewHolder {

        private final TextView streamLabelWidget;
        private final TextView valueLabelWidget;

        public StreamViewHolder(View itemView) {
            super(itemView);
            streamLabelWidget = (TextView) itemView.findViewById(R.id.list_item_stream_label);
            valueLabelWidget = (TextView) itemView.findViewById(R.id.list_item_stream_value_label);
        }

        public void bind(ListItem.StreamListItem item) {
            streamLabelWidget.setText(item.getStream());
            valueLabelWidget.setText(item.getValue());
        }
    }

    public class PropertyViewHolder extends RecyclerView.ViewHolder {

        private final TextView propertyLabelWidget;
        private final TextView valueLabelWidget;

        public PropertyViewHolder(View itemView) {
            super(itemView);
            propertyLabelWidget = (TextView) itemView.findViewById(R.id.list_item_property_label);
            valueLabelWidget = (TextView) itemView.findViewById(R.id.list_item_property_value_label);
        }

        public void bind(ListItem.PropertyListItem item) {
            propertyLabelWidget.setText(item.getProperty());
            valueLabelWidget.setText(item.getValue());
        }
    }

    public class EventsViewHolder extends RecyclerView.ViewHolder {

        private final TextView eventsLabelWidget;

        public EventsViewHolder(View itemView) {
            super(itemView);
            eventsLabelWidget = (TextView) itemView.findViewById(R.id.list_item_events_label);
        }

        public void bind(ListItem.EventsListItem item, final OnEventsClickListener onEventsClickListener) {
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

        public void bind(ListItem.StateListItem item) {
            imageLoader.load(item.getStateImageUrl(), stateImageWidget);
            stateImageWidget.setColorFilter(item.getStateColor());
            stateLabelWidget.setText(item.getState());
            stateLabelWidget.setTextColor(item.getStateColor());

        }
    }
}
