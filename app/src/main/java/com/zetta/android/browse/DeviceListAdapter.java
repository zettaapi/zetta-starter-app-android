package com.zetta.android.browse;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zetta.android.ImageLoader;
import com.zetta.android.R;

import java.util.ArrayList;
import java.util.List;

class DeviceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ListItem> listItems = new ArrayList<>();

    private final ImageLoader imageLoader;

    DeviceListAdapter(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public void updateAll(List<ListItem> listItems) {
        this.listItems.clear();
        this.listItems.addAll(listItems);
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
        if (viewType == ListItem.TYPE_SERVER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_server, parent, false);
            return new ServerViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_device, parent, false);
            return new DeviceViewHolder(v, imageLoader);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ListItem.TYPE_SERVER) {
            ListItem.ServerListItem serverListItem = (ListItem.ServerListItem) listItems.get(position);
            ((ServerViewHolder) holder).bind(serverListItem);
        } else {
            ListItem.DeviceListItem deviceListItem = (ListItem.DeviceListItem) listItems.get(position);
            ((DeviceViewHolder) holder).bind(deviceListItem);
        }
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

        public void bind(ListItem.DeviceListItem deviceListItem) {
            nameLabelWidget.setText(deviceListItem.getName());
            stateLabelWidget.setText(deviceListItem.getState());
            imageLoader.load(deviceListItem.getStateImageUrl(), stateImageWidget);
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

        public void bind(ListItem.ServerListItem serverListItem) {
            swatchColorWidget.setBackgroundColor(serverListItem.getSwatchColor());
            nameLabelWidget.setText(serverListItem.getName());
        }
    }
}
