package com.zetta.android.device;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import com.apigee.zettakit.ZIKDevice;
import com.apigee.zettakit.ZIKDeviceId;
import com.apigee.zettakit.ZIKServer;
import com.apigee.zettakit.ZIKStream;
import com.apigee.zettakit.ZIKStreamEntry;
import com.apigee.zettakit.ZIKTransition;
import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaSdkApi;
import com.zetta.android.ZettaStyle;
import com.zetta.android.device.actions.ActionListItemParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class DeviceDetailsSdkService {

    private static final Map<UUID, ZettaDeviceId> zettaDeviceIdCache = new HashMap<>();

    private final ZettaSdkApi zettaSdkApi;
    private final ZettaStyle.Parser zettaStyleParser;
    private final ActionListItemParser actionListItemParser;

    public DeviceDetailsSdkService() {
        zettaSdkApi = ZettaSdkApi.INSTANCE;
        zettaStyleParser = new ZettaStyle.Parser();
        actionListItemParser = new ActionListItemParser();
    }

    @NonNull
    public DeviceDetailsService.Device getDeviceDetails(ZettaDeviceId deviceId) {
        ZIKDeviceId zikDeviceId = new ZIKDeviceId(deviceId.getUuid().toString());

        final ZIKServer zikServer = zettaSdkApi.getServerContaining(zikDeviceId);
        final ZIKDevice zikDevice = zettaSdkApi.getLiteDevice(zikDeviceId);
        final ZettaStyle zettaStyle = zettaStyleParser.parseStyle(zikServer, zikDevice);
        final List<ListItem> deviceListItems = convertToDeviceListItems(zikServer, zikDevice);
        return new DeviceDetailsService.Device() {
            @Override
            public Spannable getName() {
                Spannable name = new SpannableString(zikDevice.getName());
                int backgroundColor = zettaStyle.getBackgroundColor();
                name.setSpan(new BackgroundColorSpan(backgroundColor), 0, name.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                int foregroundColor = zettaStyle.getForegroundColor();
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(foregroundColor);
                name.setSpan(foregroundColorSpan, 0, name.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                return name;
            }

            @Override
            public Spannable getSeverName() {
                Spannable name = new SpannableString(zikServer.getName());
                int backgroundColor = zettaStyle.getBackgroundColor();
                BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(backgroundColor);
                name.setSpan(backgroundColorSpan, 0, name.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                int foregroundColor = zettaStyle.getForegroundColor();
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(foregroundColor);
                name.setSpan(foregroundColorSpan, 0, name.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                return name;
            }

            @Override
            public Drawable getBackground() {
                int backgroundColor = zettaStyle.getBackgroundColor();
                return ImageLoader.Drawables.getBackgroundDrawableFor(backgroundColor);
            }

            @Override
            public List<ListItem> getListItems() {
                return deviceListItems;
            }

            @Override
            public int getTintColor() {
                return zettaStyle.getForegroundColor();
            }
        };
    }

    @NonNull
    private List<ListItem> convertToDeviceListItems(ZIKServer zikServer, ZIKDevice zikDevice) {
        List<ListItem> listItems = new ArrayList<>();
        listItems.add(new ListItem.HeaderListItem("Actions"));

        ZettaStyle style = zettaStyleParser.parseStyle(zikServer, zikDevice);

        List<ZIKTransition> transitions = zikDevice.getTransitions();
        if (transitions.isEmpty()) {
            listItems.add(createEmptyActionsListItem(style));
        }
        for (ZIKTransition transition : transitions) {
            listItems.add(actionListItemParser.parseActionListItem(style, transition));
        }

        listItems.add(new ListItem.HeaderListItem("Streams"));

        List<ZIKStream> allStreams = zikDevice.getAllStreams();
        for (ZIKStream stream : allStreams) {
            listItems.add(createInitialStreamListItem(style, zikDevice, stream));
        }

        listItems.add(new ListItem.HeaderListItem("Properties"));

        Map<String, Object> deviceProperties = zikDevice.getProperties();
        for (String propertyName : deviceProperties.keySet()) {
            if (propertyName.equals("style")) {
                continue;
            }
            listItems.add(createPropertyListItem(style, deviceProperties, propertyName));
        }
        if (deviceProperties.isEmpty()) {
            listItems.add(createEmptyPropertiesListItem(style));
        }

        listItems.add(new ListItem.HeaderListItem("Events"));

        listItems.add(createEventsListItem(style));

        return listItems;
    }

    @NonNull
    private ListItem.EmptyListItem createEmptyActionsListItem(ZettaStyle style) {
        Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(style.getBackgroundColor());
        return new ListItem.EmptyListItem("No actions for this device.", backgroundDrawable);
    }

    @NonNull
    private StreamListItem createInitialStreamListItem(ZettaStyle style, ZIKDevice device, ZIKStream zikStream) {
        String stream = zikStream.getTitle();
        String value = "";
        ZettaDeviceId zettaDeviceId = getDeviceId(device);
        Drawable deviceBackgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(style.getBackgroundColor());
        return new StreamListItem(
            zettaDeviceId,
            stream,
            value,
            style.getForegroundColor(),
            deviceBackgroundDrawable
        );
    }

    @NonNull
    private ListItem.EmptyListItem createEmptyPropertiesListItem(ZettaStyle style) {
        Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(style.getBackgroundColor());
        return new ListItem.EmptyListItem("No properties for this device.", backgroundDrawable);
    }

    @NonNull
    private PropertyListItem createPropertyListItem(ZettaStyle style, Map<String, Object> deviceProperties, String propertyName) {
        String propertyValue = String.valueOf(deviceProperties.get(propertyName));
        Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(style.getBackgroundColor());
        return new PropertyListItem(propertyName, propertyValue, backgroundDrawable, style.getForegroundColor());
    }

    @NonNull
    private EventsListItem createEventsListItem(ZettaStyle style) {
        Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(style.getBackgroundColor());
        return new EventsListItem("View Events (...)", backgroundDrawable, style.getForegroundColor());
    }

    public void startMonitorStreamedUpdatesFor(final ZettaDeviceId deviceId, final DeviceDetailsService.StreamListener listener) {
        ZIKDeviceId zikDeviceId = new ZIKDeviceId(deviceId.getUuid().toString());
        ZIKServer zikServer = zettaSdkApi.getServerContaining(zikDeviceId);
        ZIKDevice zikDevice = zettaSdkApi.getLiteDevice(zikDeviceId);
        final ZettaStyle style = zettaStyleParser.parseStyle(zikServer, zikDevice);
        zettaSdkApi.startMonitoringDeviceStreamsFor(zikDeviceId, new ZettaSdkApi.ZikStreamEntryListener() {
            @Override
            public void updateFor(ZIKServer server, ZIKDevice device, ZIKStreamEntry entry) {
                StreamListItem listItem = createStreamListItem(style, device, entry);
                listener.onUpdated(listItem);
            }
        });
    }

    @NonNull
    private StreamListItem createStreamListItem(ZettaStyle style, ZIKDevice device, ZIKStreamEntry entry) {
        ZettaDeviceId zettaDeviceId = getDeviceId(device);
        String stream = entry.getTitle();
        String value = String.valueOf(entry.getData());
        Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(style.getBackgroundColor());
        return new StreamListItem(
            zettaDeviceId,
            stream,
            value,
            style.getForegroundColor(),
            backgroundDrawable
        );
    }

    @NonNull
    private ZettaDeviceId getDeviceId(ZIKDevice device) {
        UUID uuid = device.getDeviceId().getUuid();
        if (zettaDeviceIdCache.containsKey(uuid)) {
            return zettaDeviceIdCache.get(uuid);
        } else {
            ZettaDeviceId zettaDeviceId = new ZettaDeviceId(uuid);
            zettaDeviceIdCache.put(uuid, zettaDeviceId);
            return zettaDeviceId;
        }
    }

    public void stopMonitoringStreamedUpdates() {
        zettaSdkApi.stopMonitoringDeviceStreams();
    }
}
