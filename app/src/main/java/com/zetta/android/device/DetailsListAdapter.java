package com.zetta.android.device;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zetta.android.ImageLoader;
import com.zetta.android.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public boolean isEmpty() {
        return listItems.isEmpty();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ListItem.TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header, parent, false);
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
        } else if (type == ListItem.TYPE_ACTION_ON_OFF) {
            ListItem.ActionOnOffListItem actionOnOffListItem = (ListItem.ActionOnOffListItem) listItems.get(position);
            ((ActionOnOffViewHolder) holder).bind(actionOnOffListItem, onActionClickListener);
            return;
        } else if (type == ListItem.TYPE_ACTION_SINGLE_INPUT) {
            ListItem.ActionSingleInputListItem actionSingleListItem = (ListItem.ActionSingleInputListItem) listItems.get(position);
            ((ActionSingleViewHolder) holder).bind(actionSingleListItem, onActionClickListener);
            return;
        } else if (type == ListItem.TYPE_ACTION_MULTIPLE_INPUT) {
            ListItem.ActionMultipleInputListItem actionMultipleListItem = (ListItem.ActionMultipleInputListItem) listItems.get(position);
            ((ActionMultipleViewHolder) holder).bind(actionMultipleListItem, onActionClickListener);
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
        void onActionClick(String label, boolean on);

        void onActionClick(String label, String input);

        void onActionClick(String label, Map<String, String> inputs);
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

    public static class ActionOnOffViewHolder extends RecyclerView.ViewHolder {

        private final TextView actionLabelWidget;
        private final AppCompatButton actionToggleButton;

        public ActionOnOffViewHolder(View itemView) {
            super(itemView);
            actionLabelWidget = (TextView) itemView.findViewById(R.id.list_item_action_label);
            actionToggleButton = (AppCompatButton) itemView.findViewById(R.id.list_item_action_toggle);
        }

        public void bind(final ListItem.ActionOnOffListItem item, final OnActionClickListener onActionClickListener) {
            actionLabelWidget.setText(item.getLabel());
            actionToggleButton.setText(item.getAction());
            actionToggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onActionClickListener.onActionClick(item.getLabel(), true); // TODO real input
                }
            });
            actionToggleButton.setTextColor(item.getActionTextColorList());
            actionToggleButton.setSupportBackgroundTintList(item.getActionColorList());
        }
    }

    public static class ActionSingleViewHolder extends RecyclerView.ViewHolder {

        private final TextInputLayout actionHintWidget;
        private final EditText actionInputWidget;
        private final AppCompatButton actionButton;

        public ActionSingleViewHolder(View itemView) {
            super(itemView);
            actionHintWidget = (TextInputLayout) itemView.findViewById(R.id.list_item_action_input_layout);
            actionInputWidget = (EditText) itemView.findViewById(R.id.list_item_action_input);
            actionButton = (AppCompatButton) itemView.findViewById(R.id.list_item_action_button);
        }

        public void bind(final ListItem.ActionSingleInputListItem item, final OnActionClickListener onActionClickListener) {
            actionHintWidget.setHint(item.getLabel());
            actionButton.setText(item.getAction());
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String input = actionInputWidget.getText().toString();
                    onActionClickListener.onActionClick(item.getLabel(), input);
                    actionInputWidget.getText().clear();
                    actionInputWidget.clearFocus();
                }
            });
            actionButton.setTextColor(item.getActionTextColorList());
            actionButton.setSupportBackgroundTintList(item.getActionColorList());
        }
    }

    public static class ActionMultipleViewHolder extends RecyclerView.ViewHolder {

        private static final int[] rowIds = {R.id.row_1, R.id.row_2, R.id.row_3, R.id.row_4, R.id.row_5, R.id.row_6};

        private final List<EditText> boundInputRows = new ArrayList<>();

        private final AppCompatButton actionButton;

        public ActionMultipleViewHolder(View itemView) {
            super(itemView);
            actionButton = (AppCompatButton) itemView.findViewById(R.id.list_item_action_button);
        }

        public void bind(final ListItem.ActionMultipleInputListItem item, final OnActionClickListener onActionClickListener) {
            if (item.getLabels().size() > 6) {
                itemView.setVisibility(View.GONE);
                Log.e("Zetta", "Sorry demo only supports upto 6 custom input actions. Cannot display " + item.getLabels());
                return;
            }
            final List<String> labels = item.getLabels();
            for (int i = 0; i < labels.size(); i++) {
                TextInputLayout rowWidget = (TextInputLayout) itemView.findViewById(rowIds[i]);
                rowWidget.setVisibility(View.VISIBLE);
                rowWidget.setHint(labels.get(i));

                EditText actionInputWidget = (EditText) rowWidget.findViewById(R.id.list_item_action_input);
                boundInputRows.add(actionInputWidget);
            }

            actionButton.setText(item.getAction());
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> labelledInput = new HashMap<>();
                    for (int i = 0; i < labels.size(); i++) {
                        String label = labels.get(i);
                        EditText actionInputWidget = boundInputRows.get(i);
                        String inputForLabel = actionInputWidget.getText().toString();
                        actionInputWidget.getText().clear();
                        actionInputWidget.clearFocus();
                        labelledInput.put(label, inputForLabel);
                    }
                    onActionClickListener.onActionClick(item.getAction(), labelledInput);
                }
            });
            actionButton.setTextColor(item.getActionTextColorList());
            actionButton.setSupportBackgroundTintList(item.getActionColorList());
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

        public void bind(ListItem.StreamListItem item) {
            streamLabelWidget.setText(item.getStream());
            valueLabelWidget.setText(item.getValue());
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

        public void bind(ListItem.PropertyListItem item) {
            propertyLabelWidget.setText(item.getProperty());
            valueLabelWidget.setText(item.getValue());
        }
    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder {

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
