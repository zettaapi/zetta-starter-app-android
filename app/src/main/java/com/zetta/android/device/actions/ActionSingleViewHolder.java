package com.zetta.android.device.actions;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.novoda.notils.meta.AndroidUtils;
import com.zetta.android.R;

public class ActionSingleViewHolder extends RecyclerView.ViewHolder {

    private final TextInputLayout actionHintWidget;
    private final EditText actionInputWidget;
    private final AppCompatButton actionButton;

    public ActionSingleViewHolder(View itemView) {
        super(itemView);
        actionHintWidget = (TextInputLayout) itemView.findViewById(R.id.list_item_action_input_layout);
        actionInputWidget = (EditText) itemView.findViewById(R.id.list_item_action_input);
        actionButton = (AppCompatButton) itemView.findViewById(R.id.list_item_action_button);
    }

    public void bind(final ActionSingleInputListItem item, final OnActionClickListener onActionClickListener) {
        actionHintWidget.setHint(item.getLabel());
        actionButton.setText(item.getAction());
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = actionInputWidget.getText().toString();
                onActionClickListener.onActionClick(item.getLabel(), input);
                actionInputWidget.getText().clear();
                actionInputWidget.clearFocus();
                AndroidUtils.requestHideKeyboard(actionInputWidget.getContext(), actionInputWidget);
            }
        });
        actionButton.setTextColor(item.getActionTextColor());
        actionButton.setBackground(item.getActionBackground());
    }
}
