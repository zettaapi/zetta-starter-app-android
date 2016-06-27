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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class DeviceDetailsSdkService {

    private static final Map<UUID, ZettaDeviceId> zettaDeviceIdCache = new HashMap<>();

    @NonNull private final ZettaSdkApi zettaSdkApi;
    @NonNull private final ZettaStyle.Parser zettaStyleParser;
    @NonNull private final ActionListItemParser actionListItemParser;

    public DeviceDetailsSdkService() {
        zettaSdkApi = ZettaSdkApi.INSTANCE;
        zettaStyleParser = new ZettaStyle.Parser();
        actionListItemParser = new ActionListItemParser();
    }

    @NonNull
    public DeviceDetailsService.Device getDeviceDetails(@NonNull ZettaDeviceId deviceId) {
        ZIKDeviceId zikDeviceId = new ZIKDeviceId(deviceId.getUuid().toString());
        ZIKServer zikServer = zettaSdkApi.getServerContaining(zikDeviceId);
        ZIKDevice zikDevice = zettaSdkApi.getLiteDevice(zikDeviceId);
        ZettaStyle zettaStyle = zettaStyleParser.parseStyle(zikServer, zikDevice);
        List<ListItem> deviceListItems = convertToDeviceListItems(zikServer, zikDevice);
        return convertToDevice(zikServer, zikDevice, zettaStyle, deviceListItems);
    }

    @NonNull
    private DeviceDetailsService.Device convertToDevice(@NonNull final ZIKServer zikServer,
                                                        @NonNull final ZIKDevice zikDevice,
                                                        @NonNull final ZettaStyle zettaStyle,
                                                        @NonNull final List<ListItem> deviceListItems) {
        return new DeviceDetailsService.Device() {
            @NonNull
            @Override
            public Spannable getSeverName() {
                Spannable name = new SpannableString(zikServer.getName());
                BackgroundColorSpan backgroundColorSpan = zettaStyle.createBackgroundColorSpan();
                name.setSpan(backgroundColorSpan, 0, name.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                ForegroundColorSpan foregroundColorSpan = zettaStyle.createForegroundColorSpan();
                name.setSpan(foregroundColorSpan, 0, name.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                return name;
            }

            @NonNull
            @Override
            public Spannable getName() {
                Spannable name = new SpannableString(zikDevice.getName());
                name.setSpan(zettaStyle.createBackgroundColorSpan(), 0, name.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                name.setSpan(zettaStyle.createForegroundColorSpan(), 0, name.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                return name;
            }

            @NonNull
            @Override
            public List<ListItem> getListItems() {
                return deviceListItems;
            }

            @NonNull
            @Override
            public Drawable createBackground() {
                int backgroundColor = zettaStyle.getBackgroundColor();
                return ImageLoader.Drawables.getSelectableDrawableFor(backgroundColor);
            }

            @Override
            public int getTintColor() {
                return zettaStyle.getForegroundColor();
            }

            @Override
            public int getBackgroundColor() {
                return zettaStyle.getBackgroundColor();
            }
        };
    }

    @NonNull
    private List<ListItem> convertToDeviceListItems(@NonNull ZIKServer zikServer, @NonNull ZIKDevice zikDevice) {
        List<ListItem> listItems = new ArrayList<>();
        ZettaStyle style = zettaStyleParser.parseStyle(zikServer, zikDevice);

        listItems.add(createPromotedListItem(zikServer, zikDevice, style));

        listItems.add(new ListItem.HeaderListItem("Actions"));

        List<ZIKTransition> transitions = zikDevice.getTransitions();
        if (transitions.isEmpty()) {
            listItems.add(createEmptyActionsListItem(style));
        }
        for (ZIKTransition transition : transitions) {
            listItems.add(actionListItemParser.parseActionListItem(getDeviceId(zikDevice), style, transition));
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

        listItems.add(createEventsListItem(style, zikDevice));

        return listItems;
    }

    @NonNull
    private ListItem createPromotedListItem(@NonNull ZIKServer zikServer, @NonNull ZIKDevice zikDevice, @NonNull ZettaStyle style) {
        Map entities = (Map) ((Map) zikServer.getProperties().get("style")).get("entities");
        String deviceType = zikDevice.getType();
        if (entities.containsKey(deviceType)) {
            Map deviceProperties = (Map) ((Map) entities.get(deviceType)).get("properties");
            if (deviceProperties.containsKey("state")) {
                if (((Map) deviceProperties.get("state")).get("display").equals("none")) {
                    Iterator iterator = deviceProperties.keySet().iterator();
                    iterator.next();
                    String promotedPropertyKey = (String) iterator.next();
                    String value = String.valueOf(zikDevice.getProperties().get(promotedPropertyKey));
                    Map promotedProperties = (Map) deviceProperties.get(promotedPropertyKey);
                    String symbol = (String) promotedProperties.get("symbol");
                    Double significantDigits = (double) promotedProperties.get("significantDigits");
                    BigDecimal bigValue = new BigDecimal(value).setScale(significantDigits.intValue(), BigDecimal.ROUND_FLOOR);
                    String roundedValue = bigValue.toString();
                    return new PromotedListItem(promotedPropertyKey, roundedValue, symbol, style);
                }
            }

        }
        String state = zikDevice.getState();
        return new StateListItem(state, style);
    }

    @NonNull
    private ListItem.EmptyListItem createEmptyActionsListItem(@NonNull ZettaStyle style) {
        return new ListItem.EmptyListItem("No actions for this device.", style);
    }

    @NonNull
    private StreamListItem createInitialStreamListItem(@NonNull ZettaStyle style,
                                                       @NonNull ZIKDevice device,
                                                       @NonNull ZIKStream zikStream) {
        String stream = zikStream.getTitle();
        String value = "";
        ZettaDeviceId zettaDeviceId = getDeviceId(device);
        return new StreamListItem(
            zettaDeviceId,
            stream,
            value,
            style
        );
    }

    @NonNull
    private ListItem.EmptyListItem createEmptyPropertiesListItem(@NonNull ZettaStyle style) {
        return new ListItem.EmptyListItem("No properties for this device.", style);
    }

    @NonNull
    private PropertyListItem createPropertyListItem(@NonNull ZettaStyle style,
                                                    @NonNull Map<String, Object> deviceProperties,
                                                    @NonNull String propertyName) {
        String propertyValue = String.valueOf(deviceProperties.get(propertyName));
        return new PropertyListItem(propertyName, propertyValue, style);
    }

    @NonNull
    private EventsListItem createEventsListItem(@NonNull ZettaStyle style, @NonNull ZIKDevice device) {
        return new EventsListItem(getDeviceId(device), "View Events (...)", style);
    }

    public void startMonitorStreamedUpdatesFor(@NonNull final ZettaDeviceId deviceId,
                                               @NonNull final DeviceDetailsService.StreamListener listener) {
        ZIKDeviceId zikDeviceId = new ZIKDeviceId(deviceId.getUuid().toString());
        ZIKServer zikServer = zettaSdkApi.getServerContaining(zikDeviceId);
        ZIKDevice zikDevice = zettaSdkApi.getLiteDevice(zikDeviceId);
        final ZettaStyle style = zettaStyleParser.parseStyle(zikServer, zikDevice);
        zettaSdkApi.startMonitoringDeviceStreamsFor(zikDeviceId, new ZettaSdkApi.ZikStreamEntryListener() {
            @Override
            public void updateFor(@NonNull ZIKServer server, @NonNull ZIKDevice device, @NonNull ZIKStreamEntry entry) {
                StreamListItem listItem = createStreamListItem(style, device, entry);
                listener.onUpdated(listItem);
            }
        });
    }

    @NonNull
    private StreamListItem createStreamListItem(@NonNull ZettaStyle style,
                                                @NonNull ZIKDevice device,
                                                @NonNull ZIKStreamEntry entry) {
        ZettaDeviceId zettaDeviceId = getDeviceId(device);
        String stream = entry.getTitle();
        String value = String.valueOf(entry.getData());
        return new StreamListItem(
            zettaDeviceId,
            stream,
            value,
            style
        );
    }

    @NonNull
    private ZettaDeviceId getDeviceId(@NonNull ZIKDevice device) {
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
