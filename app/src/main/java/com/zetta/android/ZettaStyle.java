package com.zetta.android;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import com.apigee.zettakit.ZIKDevice;
import com.apigee.zettakit.ZIKServer;
import com.apigee.zettakit.ZIKStyle;
import com.apigee.zettakit.ZIKStyleColor;

import java.util.Map;

public class ZettaStyle {

    private final int foregroundColor;
    private final int backgroundColor;
    private final Uri stateImage;
    private final TintMode tintMode;

    public ZettaStyle(int foregroundColor, int backgroundColor, Uri stateImage, TintMode tintMode) {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.stateImage = stateImage;
        this.tintMode = tintMode;
    }

    public ZettaStyle(ZettaStyle otherStyle) {
        this.foregroundColor = otherStyle.foregroundColor;
        this.backgroundColor = otherStyle.backgroundColor;
        this.stateImage = otherStyle.stateImage;
        this.tintMode = otherStyle.tintMode;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public ColorStateList getForegroundColorList() {
        return ColorStateList.valueOf(foregroundColor);
    }

    public Drawable createForegroundDrawable() {
        return ImageLoader.Drawables.getSelectableDrawableFor(foregroundColor);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public ForegroundColorSpan createForegroundColorSpan() {
        return new ForegroundColorSpan(foregroundColor);
    }

    public ColorStateList getBackgroundColorList() {
        return ColorStateList.valueOf(backgroundColor);
    }

    public Drawable createBackgroundDrawable() {
        return ImageLoader.Drawables.getSelectableDrawableFor(backgroundColor);
    }

    public BackgroundColorSpan createBackgroundColorSpan() {
        return new BackgroundColorSpan(backgroundColor);
    }

    public Uri getStateImage() {
        return stateImage;
    }

    public int getTintColor() {
        if (tintMode.equals(TintMode.ORIGINAL)) {
            return Color.TRANSPARENT;
        } else {
            return foregroundColor;
        }
    }

    public static class Parser {
        private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#f2f2f2");
        private static final int DEFAULT_FOREGROUND_COLOR = Color.BLACK;
        private static final Uri DEFAULT_URI_ICON = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.device_placeholder);
        private static final TintMode DEFAULT_TINT = TintMode.ORIGINAL;

        public ZettaStyle parseStyle(ZIKServer zikServer) {
            ZIKStyle style = zikServer.getStyle();
            if (style == null) {
                return new ZettaStyle(DEFAULT_FOREGROUND_COLOR, DEFAULT_BACKGROUND_COLOR, DEFAULT_URI_ICON, DEFAULT_TINT);
            } else {
                int serverForegroundColor = parseForegroundColor(style);
                int serverBackgroundColor = parseBackgroundColor(style);
                Uri stateImage = DEFAULT_URI_ICON;
                TintMode tintMode = DEFAULT_TINT;
                return new ZettaStyle(serverForegroundColor, serverBackgroundColor, stateImage, tintMode);
            }
        }

        private int parseForegroundColor(ZIKStyle serverStyle) {
            ZIKStyleColor zikForegroundColor = serverStyle.getForegroundColor();
            if (zikForegroundColor == null) {
                return DEFAULT_FOREGROUND_COLOR;
            } else {
                String jsonForegroundColor = zikForegroundColor.getHex();
                return Color.parseColor(jsonForegroundColor);
            }
        }

        private int parseBackgroundColor(ZIKStyle serverStyle) {
            ZIKStyleColor zikBackgroundColor = serverStyle.getBackgroundColor();
            if (zikBackgroundColor == null) {
                return DEFAULT_BACKGROUND_COLOR;
            } else {
                String jsonBackgroundColor = zikBackgroundColor.getHex();
                return Color.parseColor(jsonBackgroundColor);
            }
        }

        public ZettaStyle parseStyle(ZIKServer server, ZIKDevice device) {
            ZettaStyle serverStyle = parseStyle(server);
            ZIKStyle deviceStyle = device.getStyle();
            if (deviceStyle == null) {
                return new ZettaStyle(serverStyle);
            } else {
                int deviceForegroundColor = parseForegroundColor(serverStyle, deviceStyle);
                int deviceBackgroundColor = parseBackgroundColor(serverStyle, deviceStyle);
                Uri image = parseStateImage(serverStyle, deviceStyle);
                TintMode tintMode = parseTintMode(serverStyle, deviceStyle);
                return new ZettaStyle(deviceForegroundColor, deviceBackgroundColor, image, tintMode);
            }
        }

        private int parseForegroundColor(ZettaStyle serverStyle, ZIKStyle deviceStyle) {
            ZIKStyleColor zikForegroundColor = deviceStyle.getForegroundColor();
            if (zikForegroundColor == null) {
                return serverStyle.foregroundColor;
            } else {
                String jsonForegroundColor = zikForegroundColor.getHex();
                return Color.parseColor(jsonForegroundColor);
            }
        }

        private int parseBackgroundColor(ZettaStyle serverStyle, ZIKStyle deviceStyle) {
            ZIKStyleColor zikBackgroundColor = deviceStyle.getBackgroundColor();
            if (zikBackgroundColor == null) {
                return serverStyle.backgroundColor;
            } else {
                String jsonBackgroundColor = zikBackgroundColor.getHex();
                return Color.parseColor(jsonBackgroundColor);
            }
        }

        private Uri parseStateImage(ZettaStyle serverStyle, ZIKStyle deviceStyle) {
            Map stateImage = (Map) deviceStyle.getProperties().get("stateImage");
            if (stateImage == null) {
                return serverStyle.stateImage;
            } else {
                String jsonUrl = (String) stateImage.get("url");
                return Uri.parse(jsonUrl);
            }
        }

        private TintMode parseTintMode(ZettaStyle serverStyle, ZIKStyle deviceStyle) {
            Map stateImage = (Map) deviceStyle.getProperties().get("stateImage");
            if (stateImage == null) {
                return serverStyle.tintMode;
            } else {
                String jsonTintMode = (String) stateImage.get("tintMode");
                return TintMode.parse(jsonTintMode);
            }
        }
    }

    public enum TintMode {
        ORIGINAL, TINTED;

        public static TintMode parse(String jsonTintMode) {
            if ("original".equals(jsonTintMode)) {
                return ORIGINAL;
            } else {
                return TINTED;
            }
        }
    }
}
