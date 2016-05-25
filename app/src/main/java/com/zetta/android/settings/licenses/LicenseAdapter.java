package com.zetta.android.settings.licenses;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zetta.android.R;

import java.util.ArrayList;
import java.util.List;

class LicenseAdapter extends RecyclerView.Adapter<LicenseAdapter.LicenseViewHolder> {

    private final List<License> licenses = new ArrayList<>();
    private final LayoutInflater layoutInflater;

    public LicenseAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public void addAll(List<License> licenses) {
        this.licenses.addAll(licenses);
        notifyDataSetChanged();
    }

    @Override
    public LicenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_open_source_license, parent, false);
        return new LicenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LicenseViewHolder holder, int position) {
        License license = licenses.get(position);
        holder.bind(license);
    }

    @Override
    public int getItemCount() {
        return licenses.size();
    }

    public static class LicenseViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameWidget;
        private final TextView authorWidget;
        private final TextView typeWidget;
        private final TextView websiteWidget;

        public LicenseViewHolder(View itemView) {
            super(itemView);
            nameWidget = (TextView) itemView.findViewById(R.id.item_license_library_name);
            authorWidget = (TextView) itemView.findViewById(R.id.item_license_library_author);
            typeWidget = (TextView) itemView.findViewById(R.id.item_license_type);
            websiteWidget = (TextView) itemView.findViewById(R.id.item_license_website);
        }

        public void bind(License license) {
            nameWidget.setText(license.getLibraryName());
            authorWidget.setText(getString(R.string.library_author, license.getLibraryAuthor()));
            typeWidget.setText(license.getType());
            websiteWidget.setText(license.getWebsite());
        }

        private String getString(@StringRes int resourceId, Object... item) {
            return itemView.getResources().getString(resourceId, item);
        }
    }
}
