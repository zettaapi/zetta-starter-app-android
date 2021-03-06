package com.zetta.android;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.novoda.notils.logger.simple.Log;

import java.lang.reflect.Field;
import java.util.Arrays;

public class ImageLoader {

    public void load(@NonNull Uri uri, @NonNull ImageView imageView) {
        Glide.with(imageView.getContext())
            .load(uri)
            .placeholder(R.drawable.device_placeholder)
            .crossFade()
            .error(android.R.drawable.stat_notify_error)
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .into(imageView);
        // TODO find out what is wanted to happen when it fails to load
        // i.e. what is the error image
    }

    public static class Drawables {

        @NonNull
        public static Drawable getSelectableDrawableFor(int color) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                StateListDrawable stateListDrawable = new StateListDrawable();
                stateListDrawable.addState(
                    new int[]{android.R.attr.state_pressed},
                    new ColorDrawable(lightenOrDarken(color, 0.20D))
                );
                stateListDrawable.addState(
                    new int[]{android.R.attr.state_focused},
                    new ColorDrawable(lightenOrDarken(color, 0.40D))
                );
                stateListDrawable.addState(
                    new int[]{},
                    new ColorDrawable(color)
                );
                return stateListDrawable;
            } else {
                ColorStateList pressedColor = ColorStateList.valueOf(lightenOrDarken(color, 0.2D));
                ColorDrawable defaultColor = new ColorDrawable(color);
                Drawable rippleColor = getRippleMask(color);
                return new RippleDrawable(
                    pressedColor,
                    defaultColor,
                    rippleColor
                );
            }
        }

        @NonNull
        private static Drawable getRippleMask(int color) {
            float[] outerRadii = new float[8];
            // 3 is radius of final ripple, instead of 3 you can give required final radius
            Arrays.fill(outerRadii, 3);

            RoundRectShape r = new RoundRectShape(outerRadii, null, null);
            ShapeDrawable shapeDrawable = new ShapeDrawable(r);
            shapeDrawable.getPaint().setColor(color);
            return shapeDrawable;
        }

        public static int lightenOrDarken(int color, double fraction) {
            if (canLighten(color, fraction)) {
                return lighten(color, fraction);
            } else {
                return darken(color, fraction);
            }
        }

        public static int lighten(int color, double fraction) {
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            red = lightenColor(red, fraction);
            green = lightenColor(green, fraction);
            blue = lightenColor(blue, fraction);
            int alpha = Color.alpha(color);
            return Color.argb(alpha, red, green, blue);
        }

        public static int darken(int color, double fraction) {
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            red = darkenColor(red, fraction);
            green = darkenColor(green, fraction);
            blue = darkenColor(blue, fraction);
            int alpha = Color.alpha(color);

            return Color.argb(alpha, red, green, blue);
        }

        private static boolean canLighten(int color, double fraction) {
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            return canLightenComponent(red, fraction)
                && canLightenComponent(green, fraction)
                && canLightenComponent(blue, fraction);
        }

        private static boolean canLightenComponent(int colorComponent, double fraction) {
            int red = Color.red(colorComponent);
            int green = Color.green(colorComponent);
            int blue = Color.blue(colorComponent);
            return red + (red * fraction) < 255
                && green + (green * fraction) < 255
                && blue + (blue * fraction) < 255;
        }

        private static int darkenColor(int color, double fraction) {
            return (int) Math.max(color - (color * fraction), 0);
        }

        private static int lightenColor(int color, double fraction) {
            return (int) Math.min(color + (color * fraction), 255);
        }

        public static void setInputTextLayoutColor(@NonNull TextInputLayout textInputLayout, @NonNull ColorStateList colorStateList) {
            try {
                Field fDefaultTextColor = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                fDefaultTextColor.setAccessible(true);
                fDefaultTextColor.set(textInputLayout, colorStateList);

                Field fFocusedTextColor = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                fFocusedTextColor.setAccessible(true);
                fFocusedTextColor.set(textInputLayout, colorStateList);
            } catch (Exception e) {
                Log.e(e, "TextInputLayout text color unable to be changed.");
            }
        }
    }
}
