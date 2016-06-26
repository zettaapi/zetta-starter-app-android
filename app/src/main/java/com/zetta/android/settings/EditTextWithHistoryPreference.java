package com.zetta.android.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.Preference;
import android.util.AttributeSet;
import android.widget.EditText;

import com.zetta.android.R;

import java.util.List;

/**
 * A {@link Preference} that allows for string input, and shows the last 5 strings inputted.
 * <p/>
 * It is a subclass of {@link DialogPreference} and shows the {@link EditText} in a dialog.
 * <p/>
 * This preference will store a string into the SharedPreferences.
 */
public class EditTextWithHistoryPreference extends DialogPreference {
    static final String SEPARATOR = HistoryCollection.SEPARATOR;

    @NonNull private final HistoryCollection historyCollection;

    public EditTextWithHistoryPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        historyCollection = new HistoryCollection();
    }

    public EditTextWithHistoryPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EditTextWithHistoryPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextPreferenceStyle);
    }

    /**
     * Saves the text to the {@link android.content.SharedPreferences}.
     *
     * @param text The text to save
     */
    public void setText(@NonNull String text) {
        final boolean wasBlocking = shouldDisableDependents();

        if (historyCollection.hasHeadItem(text)) {
            return;
        }

        historyCollection.appendTail(text);
        String history = historyCollection.toString();
        persistString(history);

        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }
    }

    /**
     * Gets the current api url from the {@link android.content.SharedPreferences}.
     *
     * @return The current preference value.
     */
    @NonNull
    public String getCurrentText() {
        return historyCollection.getHead();
    }

    @NonNull
    public List<String> getHistory() {
        return historyCollection.getHistory();
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setText(restoreValue ? getPersistedString(historyCollection.toString()) : (String) defaultValue);
        setSummary(getCurrentText());
    }

    @Override
    public boolean shouldDisableDependents() {
        return historyCollection.isEmpty() || super.shouldDisableDependents();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.text = getCurrentText();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        setText(myState.text);
    }

    private static class SavedState extends BaseSavedState {
        String text;

        public SavedState(Parcel source) {
            super(source);
            text = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(text);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
