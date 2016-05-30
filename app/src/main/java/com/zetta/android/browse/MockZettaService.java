package com.zetta.android.browse;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import java.net.MalformedURLException;
import java.net.URL;
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

        final List<ListItem> items;
        try {
            items = Arrays.asList(
                new ListItem.ServerListItem(banglorForegroundColor, getBackgroundDrawableFor(defaultColor), "bangalor"),
                new ListItem.DeviceListItem("Door", "closed",
                                            new URL("http://www.zettaapi.org/icons/door-closed.png"),
                                            banglorForegroundColor,
                                            getBackgroundDrawableFor(defaultColor)
                ),
                new ListItem.DeviceListItem("Photocell", "ready",
                                            new URL("http://www.zettaapi.org/icons/photocell-ready.png"),
                                            banglorForegroundColor,
                                            getBackgroundDrawableFor(defaultColor)
                ),
                new ListItem.DeviceListItem("Security System", "disarmed",
                                            new URL("http://www.zettaapi.org/icons/security-disarmed.png"),
                                            banglorForegroundColor,
                                            getBackgroundDrawableFor(defaultColor)
                ),
                new ListItem.DeviceListItem("Window", "closed",
                                            new URL("http://www.zettaapi.org/icons/window-closed.png"),
                                            banglorForegroundColor,
                                            getBackgroundDrawableFor(defaultColor)
                ),
                new ListItem.ServerListItem(newOrleansForegroundColor, getBackgroundDrawableFor(defaultColor), "neworleans"),
                new ListItem.DeviceListItem("Motion", "no-motion",
                                            new URL("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                            newOrleansForegroundColor,
                                            getBackgroundDrawableFor(Color.parseColor("#aaaaff"))
                ),
                new ListItem.DeviceListItem("Thermometer", "ready",
                                            new URL("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                            newOrleansForegroundColor,
                                            getBackgroundDrawableFor(Color.parseColor("#aaaaff"))
                ),
                new ListItem.DeviceListItem("Camera", "ready",
                                            new URL("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                            newOrleansForegroundColor,
                                            getBackgroundDrawableFor(Color.parseColor("#aaaaff"))
                ),
                new ListItem.ServerListItem(detroitForegroundColor, getBackgroundDrawableFor(defaultColor), "detroit"),
                new ListItem.DeviceListItem("Motion1", "no-motion",
                                            new URL("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                            detroitForegroundColor,
                                            getBackgroundDrawableFor(defaultColor)
                ),
                new ListItem.DeviceListItem("Thermometer1", "ready",
                                            new URL("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                            detroitForegroundColor,
                                            getBackgroundDrawableFor(defaultColor)
                ),
                new ListItem.DeviceListItem("Camera1", "ready",
                                            new URL("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                            detroitForegroundColor,
                                            getBackgroundDrawableFor(defaultColor)
                ),
                new ListItem.DeviceListItem("Motion2", "no-motion",
                                            new URL("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                            detroitForegroundColor,
                                            getBackgroundDrawableFor(defaultColor)
                ),
                new ListItem.DeviceListItem("Thermometer2", "ready",
                                            new URL("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                            detroitForegroundColor,
                                            getBackgroundDrawableFor(defaultColor)
                ),
                new ListItem.DeviceListItem("Camera2", "ready",
                                            new URL("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                            detroitForegroundColor,
                                            getBackgroundDrawableFor(defaultColor)
                ),
                new ListItem.DeviceListItem("Motion3", "no-motion",
                                            new URL("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                            detroitForegroundColor,
                                            getBackgroundDrawableFor(Color.parseColor("#236e4e"))
                ),
                new ListItem.DeviceListItem("Thermometer3", "ready",
                                            new URL("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                            detroitForegroundColor,
                                            getBackgroundDrawableFor(Color.parseColor("#111111"))
                ),
                new ListItem.DeviceListItem("Camera3", "ready",
                                            new URL("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                            detroitForegroundColor,
                                            getBackgroundDrawableFor(defaultColor)
                ),
                new ListItem.ServerListItem(stageForegroundColor, getBackgroundDrawableFor(defaultColor), "stage"),
                new ListItem.EmptyServerListItem("No devices online for this server", getBackgroundDrawableFor(defaultColor))
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

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

    private static Drawable getBackgroundDrawableFor(int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(
                new int[]{android.R.attr.state_pressed},
                new ColorDrawable(ColorUtil.lighten(color, 0.30D))
            );
            stateListDrawable.addState(
                new int[]{android.R.attr.state_focused},
                new ColorDrawable(ColorUtil.darken(color, 0.30D))
            );
            stateListDrawable.addState(
                new int[]{},
                new ColorDrawable(color)
            );
            return stateListDrawable;
        } else {
            ColorStateList pressedColor = ColorStateList.valueOf(ColorUtil.lighten(color, 50D));
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

    private static class ColorUtil {
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
