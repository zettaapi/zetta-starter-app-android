package com.zetta.android.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zetta.android.R;

import java.util.ArrayList;
import java.util.List;

public class ApiUrlPreferenceDialogFragment extends PreferenceDialogFragmentCompat {

    public static final String TAG = "tag.ApiUrlPreferenceDialogFragment";

    private EditText apiUrlInputWidget;

    public static ApiUrlPreferenceDialogFragment newInstance(String key) {
        ApiUrlPreferenceDialogFragment fragment = new ApiUrlPreferenceDialogFragment();
        Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected View onCreateDialogView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.preference_dialog_api_url, null);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        apiUrlInputWidget = (EditText) view.findViewById(R.id.api_url_edit_text);
        String currentApiUrl = getApiUrlPreference().getCurrentText();
        apiUrlInputWidget.setText(currentApiUrl);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.api_url_history_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        HistoryAdapter adapter = new HistoryAdapter(new HistoryClickListener() {
            @Override
            public void onUrlSelected(String url) {
                apiUrlInputWidget.setText(url);
            }
        });
        adapter.addAll(getApiUrlPreference().getHistory());
        recyclerView.setAdapter(adapter);

    }

    private EditTextWithHistoryPreference getApiUrlPreference() {
        return (EditTextWithHistoryPreference) getPreference();
    }

    @Override
    protected boolean needInputMethod() {
        return true;
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {

            String value = apiUrlInputWidget.getText().toString();
            if (value.contains(EditTextWithHistoryPreference.SEPARATOR)) {
                Toast.makeText(getContext(), "Illegal Character '['", Toast.LENGTH_SHORT).show();
                return;
            }
            EditTextWithHistoryPreference editTextWithHistoryPreference = getApiUrlPreference();
            if (value.equals(editTextWithHistoryPreference.getCurrentText())) {
                return;
            }
            if (!Patterns.WEB_URL.matcher(value).matches()) {
                Toast.makeText(getContext(), "Invalid URL", Toast.LENGTH_SHORT).show();
                return;
            }
            if (editTextWithHistoryPreference.callChangeListener(value)) {
                editTextWithHistoryPreference.setText(value);
                editTextWithHistoryPreference.setSummary(value);
            }
        }
    }

    private static class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

        private final List<String> urls = new ArrayList<>();
        private final HistoryClickListener clickListener;

        private HistoryAdapter(HistoryClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public void addAll(List<String> urls) {
            this.urls.clear();
            this.urls.addAll(urls);
            notifyDataSetChanged();
        }

        @Override
        public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, parent, false);
            return new HistoryViewHolder(v, clickListener);
        }

        @Override
        public void onBindViewHolder(HistoryViewHolder holder, int position) {
            holder.bind(urls.get(position));
        }

        @Override
        public int getItemCount() {
            return urls.size();
        }
    }

    private static class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final HistoryClickListener clickListener;
        private final TextView urlLabelWidget;

        public HistoryViewHolder(View itemView, HistoryClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
            urlLabelWidget = (TextView) itemView.findViewById(R.id.list_item_history_url_label);
        }

        public void bind(String historyUrl) {
            urlLabelWidget.setText(historyUrl);
        }

        @Override
        public void onClick(View v) {
            clickListener.onUrlSelected(urlLabelWidget.getText().toString());
        }
    }

    public interface HistoryClickListener {
        void onUrlSelected(String url);
    }
}
