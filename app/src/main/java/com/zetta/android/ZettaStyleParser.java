package com.zetta.android;

import android.graphics.Color;
import android.net.Uri;

import com.apigee.zettakit.ZIKDevice;
import com.apigee.zettakit.ZIKServer;
import com.apigee.zettakit.ZIKStyle;
import com.apigee.zettakit.ZIKStyleColor;

import java.util.Map;

public class ZettaStyleParser {

    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#f2f2f2");
    private static final int DEFAULT_FOREGROUND_COLOR = Color.BLACK;
    private static final Uri DEFAULT_URI_ICON = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.device_placeholder);

    public Style parseStyle(ZIKServer zikServer) {
        ZIKStyle style = zikServer.getStyle();
        if (style == null) {
            return new Style(DEFAULT_FOREGROUND_COLOR, DEFAULT_BACKGROUND_COLOR, DEFAULT_URI_ICON);
        } else {
            int serverForegroundColor = parseForegroundColor(style);
            int serverBackgroundColor = parseBackgroundColor(style);
            Uri stateImage = DEFAULT_URI_ICON;
            return new Style(serverForegroundColor, serverBackgroundColor, stateImage);
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

    public Style parseStyle(ZIKServer server, ZIKDevice device) {
        Style serverStyle = parseStyle(server);
        ZIKStyle deviceStyle = device.getStyle();
        if (deviceStyle == null) {
            int deviceForegroundColor = serverStyle.getForegroundColor();
            int deviceBackgroundColor = serverStyle.getBackgroundColor();
            Uri image = serverStyle.getStateImage();
            return new Style(deviceForegroundColor, deviceBackgroundColor, image);
        } else {
            int deviceForegroundColor = parseForegroundColor(serverStyle, deviceStyle);
            int deviceBackgroundColor = parseBackgroundColor(serverStyle, deviceStyle);
            Uri stateImage = parseStateImage(serverStyle, deviceStyle);
            return new Style(deviceForegroundColor, deviceBackgroundColor, stateImage);
        }
    }

    private int parseForegroundColor(Style serverStyle, ZIKStyle deviceStyle) {
        ZIKStyleColor zikForegroundColor = deviceStyle.getForegroundColor();
        if (zikForegroundColor == null) {
            return serverStyle.getForegroundColor();
        } else {
            String jsonForegroundColor = zikForegroundColor.getHex();
            return Color.parseColor(jsonForegroundColor);
        }
    }

    private int parseBackgroundColor(Style serverStyle, ZIKStyle deviceStyle) {
        ZIKStyleColor zikBackgroundColor = deviceStyle.getBackgroundColor();
        if (zikBackgroundColor == null) {
            return serverStyle.getBackgroundColor();
        } else {
            String jsonBackgroundColor = zikBackgroundColor.getHex();
            return Color.parseColor(jsonBackgroundColor);
        }
    }

    private Uri parseStateImage(Style serverStyle, ZIKStyle deviceStyle) {
        Uri stateImageUri;
        Map stateImage = (Map) deviceStyle.getProperties().get("stateImage");
        if (stateImage == null) {
            stateImageUri = serverStyle.getStateImage();
        } else {
            String jsonUrl = (String) stateImage.get("url");
            stateImageUri = Uri.parse(jsonUrl);
        }
        return stateImageUri;
    }

    public static class Style {

        private final int foregroundColor;
        private final int backgroundColor;
        private final Uri stateImage;

        public Style(int foregroundColor, int backgroundColor, Uri stateImage) {
            this.foregroundColor = foregroundColor;
            this.backgroundColor = backgroundColor;
            this.stateImage = stateImage;
        }

        public int getForegroundColor() {
            return foregroundColor;
        }

        public int getBackgroundColor() {
            return backgroundColor;
        }

        public Uri getStateImage() {
            return stateImage;
        }
    }

}
