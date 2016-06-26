package com.zetta.android.settings.licenses;

import android.support.annotation.NonNull;
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

    @NonNull private final List<License> licenses = new ArrayList<>();

    @NonNull private final LayoutInflater layoutInflater;

    public LicenseAdapter(@NonNull LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public void addAll(@NonNull List<License> licenses) {
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

        @NonNull private final TextView nameWidget;
        @NonNull private final TextView authorWidget;
        @NonNull private final TextView typeWidget;
        @NonNull private final TextView websiteWidget;

        public LicenseViewHolder(@NonNull View itemView) {
            super(itemView);
            nameWidget = (TextView) itemView.findViewById(R.id.item_license_library_name);
            authorWidget = (TextView) itemView.findViewById(R.id.item_license_library_author);
            typeWidget = (TextView) itemView.findViewById(R.id.item_license_type);
            websiteWidget = (TextView) itemView.findViewById(R.id.item_license_website);
        }

        public void bind(@NonNull License license) {
            nameWidget.setText(license.getLibraryName());
            authorWidget.setText(getString(R.string.library_author, license.getLibraryAuthor()));
            typeWidget.setText(license.getType());
            websiteWidget.setText(license.getWebsite());
        }

        @NonNull
        private String getString(@StringRes int resourceId, @NonNull Object... item) {
            return itemView.getResources().getString(resourceId, item);
        }
    }
}
