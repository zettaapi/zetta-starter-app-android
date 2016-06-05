package com.zetta.android.device.actions;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zetta.android.R;

public class ActionOnOffViewHolder extends RecyclerView.ViewHolder {

    private final TextView actionLabelWidget;
    private final AppCompatButton actionToggleButton;

    public ActionOnOffViewHolder(View itemView) {
        super(itemView);
        actionLabelWidget = (TextView) itemView.findViewById(R.id.list_item_action_label);
        actionToggleButton = (AppCompatButton) itemView.findViewById(R.id.list_item_action_toggle);
    }

    public void bind(final ActionOnOffListItem item, final OnActionClickListener onActionClickListener) {
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
