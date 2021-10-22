package com.trueandtrust.shoplex.model.extra;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

public class TextInputDropDownMenu extends AppCompatAutoCompleteTextView {

    public TextInputDropDownMenu(@NonNull Context context) {
        super(context);
    }

    public TextInputDropDownMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextInputDropDownMenu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setInputType(InputType.TYPE_NULL);
    }

    @Override
    public boolean getFreezesText() {
        return false;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        if (TextUtils.isEmpty(getText())) {
            return parcelable;
        }

        CustomSavedState customSavedState = new CustomSavedState(parcelable);
        customSavedState.text = getText().toString();
        return customSavedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof CustomSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        CustomSavedState customSavedState = (CustomSavedState) state;
        setText(customSavedState.text, false);
        super.onRestoreInstanceState(customSavedState.getSuperState());
    }

    private static final class CustomSavedState extends BaseSavedState {

        private String text;

        public CustomSavedState(Parcelable superState) {
            super(superState);
        }

        public CustomSavedState(Parcel source) {
            super(source);
            text = source.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(text);
        }

        private static final Creator<CustomSavedState> CREATOR = new Creator<CustomSavedState>() {
            @Override
            public CustomSavedState createFromParcel(Parcel source) {
                return new CustomSavedState(source);
            }

            @Override
            public CustomSavedState[] newArray(int size) {
                return new CustomSavedState[size];
            }
        };
    }

}