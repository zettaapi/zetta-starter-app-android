package com.zetta.android.browse;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;
import com.zetta.android.R;

import java.util.ArrayList;
import java.util.List;

class DeviceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ListItem> listItems = new ArrayList<>();

    private final ImageLoader imageLoader;
    private final OnDeviceClickListener onDeviceClickListener;

    DeviceListAdapter(ImageLoader imageLoader, OnDeviceClickListener onDeviceClickListener) {
        this.imageLoader = imageLoader;
        this.onDeviceClickListener = onDeviceClickListener;
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
        return getItemCount() == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ListItem.TYPE_SERVER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_server, parent, false);
            return new ServerViewHolder(v);
        } else if (viewType == ListItem.TYPE_DEVICE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_device, parent, false);
            return new DeviceViewHolder(v, imageLoader);
        } else if (viewType == ListItem.TYPE_EMPTY) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_empty, parent, false);
            return new EmptyViewHolder(v);
        }
        throw new IllegalStateException("Attempted to create view holder for a type you haven't coded for: " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ListItem.TYPE_SERVER) {
            ServerListItem serverListItem = (ServerListItem) listItems.get(position);
            ((ServerViewHolder) holder).bind(serverListItem);
            return;
        } else if (type == ListItem.TYPE_DEVICE) {
            DeviceListItem deviceListItem = (DeviceListItem) listItems.get(position);
            ((DeviceViewHolder) holder).bind(deviceListItem, onDeviceClickListener);
            return;
        } else if (type == ListItem.TYPE_EMPTY) {
            ListItem.EmptyListItem emptyListItem = (ListItem.EmptyListItem) listItems.get(position);
            ((EmptyViewHolder) holder).bind(emptyListItem);
            return;
        }
        throw new IllegalStateException("Attempted to bind a type you haven't coded for: " + type);
    }

    public interface OnDeviceClickListener {

        void onDeviceClick(ZettaDeviceId deviceId);

        void onDeviceLongClick(ZettaDeviceId deviceId);

    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {

        private final ImageLoader imageLoader;
        private final TextView nameLabelWidget;
        private final TextView stateLabelWidget;
        private final ImageView stateImageWidget;

        public DeviceViewHolder(View itemView, ImageLoader imageLoader) {
            super(itemView);
            this.imageLoader = imageLoader;
            nameLabelWidget = (TextView) itemView.findViewById(R.id.list_item_device_name);
            stateLabelWidget = (TextView) itemView.findViewById(R.id.list_item_device_state);
            stateImageWidget = (ImageView) itemView.findViewById(R.id.list_item_device_state_image);
        }

        public void bind(final DeviceListItem deviceListItem, final OnDeviceClickListener onDeviceClickListener) {
            itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDeviceClickListener.onDeviceClick(deviceListItem.getDeviceId());
                    }
                });
            itemView.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onDeviceClickListener.onDeviceLongClick(deviceListItem.getDeviceId());
                        return true;
                    }
                });

            itemView.setBackground(deviceListItem.getBackground());
            nameLabelWidget.setText(deviceListItem.getName());
            stateLabelWidget.setText(deviceListItem.getState());
            imageLoader.load(deviceListItem.getStateImageUri(), stateImageWidget);
            stateImageWidget.setColorFilter(deviceListItem.getStateImageColor());
        }

    }

    public static class ServerViewHolder extends RecyclerView.ViewHolder {

        private final View swatchColorWidget;
        private final TextView nameLabelWidget;

        public ServerViewHolder(View itemView) {
            super(itemView);
            swatchColorWidget = itemView.findViewById(R.id.list_item_server_swatch);
            nameLabelWidget = (TextView) itemView.findViewById(R.id.list_item_server_name);
        }

        public void bind(ServerListItem serverListItem) {
            swatchColorWidget.setBackgroundColor(serverListItem.getSwatchColor());
            nameLabelWidget.setText(serverListItem.getName());
            itemView.setBackground(serverListItem.getBackground());
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
