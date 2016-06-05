package com.zetta.android.browse;

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
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.zetta.android.ListItem;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

class MockZettaService {

    /**
     * TODO note this structure is only the UI structure and it is not what I expect to be return from the 'zetta library'
     */
    public static void getListItems(final Callback callback) {
        int defaultColor = Color.parseColor("#f2f2f2");
        int banglorForegroundColor = Color.parseColor("#0000ff");
        int newOrleansForegroundColor = Color.parseColor("#dd33ff");
        int detroitForegroundColor = Color.parseColor("#dd3322");
        int stageForegroundColor = Color.parseColor("#008822");

        final List<ListItem> items = Arrays.asList(
            new ServerListItem(banglorForegroundColor, ColorUtil.getBackgroundDrawableFor(defaultColor), "bangalor"),
            new DeviceListItem("Door", "closed",
                               Uri.parse("http://www.zettaapi.org/icons/door-closed.png"),
                               banglorForegroundColor,
                               ColorUtil.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Photocell", "ready",
                               Uri.parse("http://www.zettaapi.org/icons/photocell-ready.png"),
                               banglorForegroundColor,
                               ColorUtil.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Security System", "disarmed",
                               Uri.parse("http://www.zettaapi.org/icons/security-disarmed.png"),
                               banglorForegroundColor,
                               ColorUtil.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Window", "closed",
                               Uri.parse("http://www.zettaapi.org/icons/window-closed.png"),
                               banglorForegroundColor,
                               ColorUtil.getBackgroundDrawableFor(defaultColor)
            ),
            new ServerListItem(newOrleansForegroundColor, ColorUtil.getBackgroundDrawableFor(defaultColor), "neworleans"),
            new DeviceListItem("Motion", "no-motion",
                               Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                               newOrleansForegroundColor,
                               ColorUtil.getBackgroundDrawableFor(Color.parseColor("#aaaaff"))
            ),
            new DeviceListItem("Thermometer", "ready",
                               Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                               newOrleansForegroundColor,
                               ColorUtil.getBackgroundDrawableFor(Color.parseColor("#aaaaff"))
            ),
            new DeviceListItem("Camera", "ready",
                               Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                               newOrleansForegroundColor,
                               ColorUtil.getBackgroundDrawableFor(Color.parseColor("#aaaaff"))
            ),
            new ServerListItem(detroitForegroundColor, ColorUtil.getBackgroundDrawableFor(defaultColor), "detroit"),
            new DeviceListItem("Motion1", "no-motion",
                               Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                               detroitForegroundColor,
                               ColorUtil.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Thermometer1", "ready",
                               Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                               detroitForegroundColor,
                               ColorUtil.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Camera1", "ready",
                               Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                               detroitForegroundColor,
                               ColorUtil.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Motion2", "no-motion",
                               Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                               detroitForegroundColor,
                               ColorUtil.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Thermometer2", "ready",
                               Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                               detroitForegroundColor,
                               ColorUtil.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Camera2", "ready",
                               Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                               detroitForegroundColor,
                               ColorUtil.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Motion3", "no-motion",
                               Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                               detroitForegroundColor,
                               ColorUtil.getBackgroundDrawableFor(Color.parseColor("#236e4e"))
            ),
            new DeviceListItem("Thermometer3", "ready",
                               Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                               detroitForegroundColor,
                               ColorUtil.getBackgroundDrawableFor(Color.parseColor("#111111"))
            ),
            new DeviceListItem("Camera3", "ready",
                               Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                               detroitForegroundColor,
                               ColorUtil.getBackgroundDrawableFor(defaultColor)
            ),
            new ServerListItem(stageForegroundColor, ColorUtil.getBackgroundDrawableFor(defaultColor), "stage"),
            new EmptyServerListItem("No devices online for this server", ColorUtil.getBackgroundDrawableFor(defaultColor))
        );

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.on(items);
                    }
                });
            }
        }).start();
    }

    interface Callback {
        void on(List<ListItem> listItems);
    }

    private static class ColorUtil {

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
