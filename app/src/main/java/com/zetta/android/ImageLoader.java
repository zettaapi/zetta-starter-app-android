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
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Arrays;

public class ImageLoader {

    public void load(Uri uri, ImageView imageView) {
        Glide.with(imageView.getContext())
            .load(uri)
            .placeholder(R.drawable.device_placeholder)
            .crossFade()
            .error(android.R.drawable.stat_notify_error)
            .into(imageView);
        // TODO find out what is wanted to happen when it fails to load
        // i.e. what is the error image
    }

    public static class Drawables {

        public static Drawable getBackgroundDrawableFor(int color) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                StateListDrawable stateListDrawable = new StateListDrawable();
                stateListDrawable.addState(
                    new int[]{android.R.attr.state_pressed},
                    new ColorDrawable(lighten(color, 0.30D))
                );
                stateListDrawable.addState(
                    new int[]{android.R.attr.state_focused},
                    new ColorDrawable(darken(color, 0.30D))
                );
                stateListDrawable.addState(
                    new int[]{},
                    new ColorDrawable(color)
                );
                return stateListDrawable;
            } else {
                ColorStateList pressedColor = ColorStateList.valueOf(lighten(color, 50D));
                ColorDrawable defaultColor = new ColorDrawable(color);
                Drawable rippleColor = getRippleMask(color);
                return new RippleDrawable(
                    pressedColor,
                    defaultColor,
                    rippleColor
                );
            }
        }

        private static Drawable getRippleMask(int color) {
            float[] outerRadii = new float[8];
            // 3 is radius of final ripple, instead of 3 you can give required final radius
            Arrays.fill(outerRadii, 3);

            RoundRectShape r = new RoundRectShape(outerRadii, null, null);
            ShapeDrawable shapeDrawable = new ShapeDrawable(r);
            shapeDrawable.getPaint().setColor(color);
            return shapeDrawable;
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

        private static int darkenColor(int color, double fraction) {
            return (int) Math.max(color - (color * fraction), 0);
        }

        private static int lightenColor(int color, double fraction) {
            return (int) Math.min(color + (color * fraction), 255);
        }
    }
}
