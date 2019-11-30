package com.example.lndonesiablend.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.TextUtils;

import androidx.core.content.PermissionChecker;

import com.example.lndonesiablend.bean.CallLogBean;
import com.example.lndonesiablend.bean.ContactBean;
import com.example.lndonesiablend.bean.SmsBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SiKang on 2018/10/19.
 * 数据库辅助类，用于涉及数据库查询的业务封装
 */

public class DataBaseHelper {
    enum CollectType {
        CONTACT,
        CALL_LOG,
        SMS_LOG
    }

    private static Context context;
    private static ContentResolver resolver;
    private static LocationManager mLocationManager;


    public static void init(Context ctx) {

        if (ctx == null) {
            throw new IllegalArgumentException("context cannot be null");
        }

        context = ctx;
        resolver = ctx.getContentResolver();
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * 根据uri查询指定联系人信息
     *
     * @return String[name, number]
     */
    public static String[] queryContactByUri(Context context, Uri uri) {
        String[] data = null;
        if (uri != null) {
            Cursor cursor = resolver.query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                    null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                String number = cursor.getString(0);
                String name = cursor.getString(1);
                data = new String[]{name, number};
            }
            cursor.close();
        }
        return data;
    }


    /**
     * 获取联系人信息
     */
    @SuppressLint("MissingPermission")
    public static List<ContactBean> getContacts() {

        List<ContactBean> logs = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = Nested.getCursor(CollectType.CONTACT);

            if (cursor == null) {
                return logs;
            }
            Map<Long, List<ContactBean.NumberEntity>> contactIdNumbersMap = Nested.queryAllContactNumbers();
            Map<Long, ContactBean.ContactDetail> contactDetailMap = Nested.queryAllContactDetail();

            while (cursor.moveToNext()) {
                com.annimon.stream.Optional<ContactBean> oneOpt = Nested.getOneContact(cursor, contactIdNumbersMap, contactDetailMap);
                if (oneOpt.isPresent()) {
                    logs.add(oneOpt.get());
                }
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return logs;
    }


    /**
     * 获取通话记录
     */
    @SuppressLint("MissingPermission")
    public static List<CallLogBean> getCallLogs(int maxCount) {
        List<CallLogBean> logs = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = Nested.getCursor(CollectType.CALL_LOG);

            if (cursor == null) {
                return logs;
            }

            while (cursor.moveToNext()) {
                com.annimon.stream.Optional<CallLogBean> oneOpt = Nested.getOneCallLog(cursor);
                if (oneOpt.isPresent()) {
                    logs.add(oneOpt.get());
                }

                if (logs.size() >= maxCount) {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return logs;
    }


    /**
     * 获取短信记录
     */
    @SuppressLint("MissingPermission")
    public static List<SmsBean> getSms(int maxCount) {
        List<SmsBean> logs = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = Nested.getCursor(CollectType.SMS_LOG);
            if (cursor == null) {
                return logs;
            }
            while (cursor.moveToNext()) {
                com.annimon.stream.Optional<SmsBean> oneOpt = Nested.getOneSms(cursor);
                if (oneOpt.isPresent()) {
                    logs.add(oneOpt.get());
                }

                if (logs.size() >= maxCount) {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return logs;
    }

    /**
     * 获取安装应用信息
     *
     * @return
     */
    public static JSONArray getAppList() {
        JSONArray map = new JSONArray();
        try {
            PackageManager pm = context.getPackageManager();
            List<PackageInfo> packages = pm.getInstalledPackages(0);
            {

                for (PackageInfo packageInfo : packages) {
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
                        String packageName = packageInfo.packageName;
                        Drawable drawable = packageInfo.applicationInfo.loadIcon(pm);

                        JSONObject item = new JSONObject();
                        item.put("packageName", packageName);
                        item.put("appName", appName);
                        map.put(item);
                    }
                }

            }
        } catch (Exception e) {
        }
        return map;
    }


    /**
     * 获取定位信息
     */
    @SuppressLint("MissingPermission")
    public static Location getLocation() {

        Location loc = null;

        List<String> providers = mLocationManager.getProviders(true);

        if (loc == null && providers.contains(LocationManager.GPS_PROVIDER)) {
            loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (loc == null && providers.contains(LocationManager.NETWORK_PROVIDER)) {
            loc = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (loc == null && providers.contains(LocationManager.PASSIVE_PROVIDER)) {
            loc = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }

        return loc;
    }


    private static String[] permissionsTocheck = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_SMS,
            Manifest.permission.ACCESS_COARSE_LOCATION,//粗精度定位
            Manifest.permission.ACCESS_FINE_LOCATION,//卫星定位
            Manifest.permission.READ_PHONE_STATE

    };

    /**
     * 判断当前权限状态
     *
     * @return
     */
    public static JSONArray getPermissionState() {
        JSONArray array = new JSONArray();

        for (int i = 0; i < permissionsTocheck.length; i++) {
            try {
                JSONObject object = new JSONObject();
                object.put("checkTime", System.currentTimeMillis());
                object.put("permissionType", permissionsTocheck[i]);

                if (permissionGranted(permissionsTocheck[i], context)) {
                    object.put("isGranted", "GRANTED");
                } else {
                    object.put("isGranted", "REFUSED");
                }

                array.put(object);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return array;
    }


    @SuppressLint("WrongConstant")
    public static boolean permissionGranted(String permission, Context ctx) {
        if (permission == null) {
            return true;
        }
        return Build.VERSION.SDK_INT < 23 || PermissionChecker.checkSelfPermission(ctx, permission) == PackageManager.PERMISSION_GRANTED;
    }


    private static class Nested {
        @SuppressLint("MissingPermission")
        private static Cursor getCursor(CollectType type) {

            if (resolver == null) {
                return null;
            }
            switch (type) {
                case CONTACT:
                    return resolver.query(ContactsContract.Contacts.CONTENT_URI,
                            null, null, null, null);

                case CALL_LOG:
                    return resolver
                            .query(CallLog.Calls.CONTENT_URI,
                                    new String[]{CallLog.Calls.CACHED_NAME,
                                            CallLog.Calls.NUMBER,
                                            CallLog.Calls.TYPE,
                                            CallLog.Calls.DATE,
                                            CallLog.Calls.DURATION},
                                    null,
                                    null,
                                    CallLog.Calls.DEFAULT_SORT_ORDER);

                case SMS_LOG:
                    return resolver.query(Uri.parse("content://sms"), new String[]{
                                    "person", //reference to item in {@code content://contacts/people}
                                    "address", //The address of the other party.
                                    "type",
                                    "date",
                                    "body",
                                    "subject"},
                            null,
                            null,
                            "date DESC");
                default:
                    return null;

            }
        }

        /**
         * 取出一条通话记录
         */
        private static com.annimon.stream.Optional<CallLogBean> getOneCallLog(Cursor cursor) {
            try {
                final String cachedName = cursor.getString(0);
                final String number = cursor.getString(1);
                final int type = cursor.getInt(2);
                final long date = cursor.getLong(3);
                final long duration = cursor.getLong(4);

                CallLogBean callLogEntity = new CallLogBean() {{
                    setCachedName(cachedName);
                    setNumber(number);
                    setType(type);
                    setDate(date);
                    setDuration(duration);
                }};
                return com.annimon.stream.Optional.of(callLogEntity);
            } catch (Exception e) {
                return com.annimon.stream.Optional.empty();
            }
        }


        /**
         * 查询一条联系人信息
         */
        //do not close the cursor
        private static com.annimon.stream.Optional<ContactBean> getOneContact(Cursor cursor,
                                                                              Map<Long, List<ContactBean.NumberEntity>> contactIdNumbersMap,
                                                                              Map<Long, ContactBean.ContactDetail> contactDetailMap) {
            try {
                final String name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                final long lastUpdate = readLastUpdateTime(cursor);

                long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                final List<ContactBean.NumberEntity> numbers = contactIdNumbersMap.get(contactId);

                final ContactBean.ContactDetail detail = contactDetailMap.get(contactId);

                ContactBean entity = new ContactBean() {{
                    setName(name);
                    setLastUpdate(lastUpdate);

                    setNumber(numbers);

                    if (detail != null) {
                        setContact_times(detail.getContact_times());
                        setLast_contact_time(detail.getLast_contact_time());
                        setNickname(detail.getNickname());
                        setRelation(detail.getRelation());
                        setStatus(detail.getStatus());
                    }
                }};

                return com.annimon.stream.Optional.of(entity);

            } catch (Exception e) {
                return com.annimon.stream.Optional.empty();
            }
        }


        /**
         * 查询一条短信记录
         */
        private static com.annimon.stream.Optional<SmsBean> getOneSms(Cursor cursor) {

            try {
                final String address = cursor.getString(1);
                final int type = cursor.getInt(2);
                final long date = cursor.getLong(3);
                final String body = cursor.getString(4);
                final String subject = cursor.getString(5);

                SmsBean smsBean = new SmsBean() {{
                    setAddress(address);
                    setType(type);
                    setDate(date);
                    setBody(body);
                    setSubject(subject);
                }};
                return com.annimon.stream.Optional.of(smsBean);
            } catch (Exception e) {
                return com.annimon.stream.Optional.empty();
            }
        }

        private static long readLastUpdateTime(Cursor cursor) {
            long lastUpdateTime = 0;

            int index = cursor.getColumnIndex("contact_last_updated_timestamp");

            if (index > -1) {
                return cursor.getLong(index);
            }

            index = cursor.getColumnIndex("contact_status_ts");
            if (index > -1) {
                return cursor.getLong(index);
            }

            return lastUpdateTime;
        }

        /**
         * 查询所有电话号码
         */
        private static Map<Long, List<ContactBean.NumberEntity>> queryAllContactNumbers() {

            Map<Long, List<ContactBean.NumberEntity>> idNumbersMap = new HashMap<>();
            Cursor cursor = null;
            try {
                cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

                if (cursor == null) {
                    return idNumbersMap;
                }

                while (cursor.moveToNext()) {
                    final long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                    com.annimon.stream.Optional<ContactBean.NumberEntity> entityOpt = readOneNumber(cursor);

                    if (!entityOpt.isPresent()) {
                        continue;
                    }

                    List<ContactBean.NumberEntity> numbers = idNumbersMap.get(contactId);
                    if (numbers == null) {
                        List<ContactBean.NumberEntity> init = new ArrayList<>();
                        init.add(entityOpt.get());
                        idNumbersMap.put(contactId, init);
                    } else {
                        numbers.add(entityOpt.get());
                    }
                }

                cursor.close();
            } catch (Exception e) {
//                UploadUtils.uploadException(e, "queryAllContactNumbers");
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            return idNumbersMap;
        }

        /**
         * 取出一条电话号码
         */
        private static com.annimon.stream.Optional<ContactBean.NumberEntity> readOneNumber(Cursor cursor) {
            try {
                final String number = readNumber(cursor);
                final int contactTimes = readContactTimes(cursor);
                final long lastContactTime = readLastContactTime(cursor);

                final String customerData = readCustomerData(cursor);

                ContactBean.NumberEntity entity = new ContactBean.NumberEntity() {{
                    setNumber(number);
                    setLast_time_used("" + lastContactTime);
                    setTime_used(contactTimes);
                    setType_label(customerData == null ? "" : customerData);
                }};

                return com.annimon.stream.Optional.of(entity);
            } catch (Exception e) {
//                UploadUtils.uploadException(e, "readOneNumber");
                return com.annimon.stream.Optional.empty();
            }
        }


        private static long readLastContactTime(Cursor cursor) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                int index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LAST_TIME_USED);
                if (index > -1) {
                    return cursor.getLong(index);
                }
            } else {
                int index = cursor.getColumnIndex("last_time_contacted");
                if (index > -1) {
                    return cursor.getLong(index);
                }
            }

            return 0;
        }

        private static String readCustomerData(Cursor cursor) {

            int data2Index = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA2));
            if (data2Index > -1) {
                CharSequence typeLabel = ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(), data2Index, "CUSTOME");
                if (typeLabel != null) {
                    return typeLabel.toString();
                }
            }

            return "";
        }


        private static String readNumber(Cursor cursor) {
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (!TextUtils.isEmpty(number)) {
                number = number.replace("-", "");
                number = number.replace(" ", "");
            }
            return number;
        }

        private static int readContactTimes(Cursor cursor) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                int index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TIMES_USED);
                if (index > -1) {
                    return cursor.getInt(index);
                }
            } else {
                int index = cursor.getColumnIndex("times_contacted");
                if (index > -1) {
                    return cursor.getInt(index);
                }
            }

            return 0;
        }

        private static Map<Long, ContactBean.ContactDetail> queryAllContactDetail() {

            Map<Long, ContactBean.ContactDetail> contactIdDetailMap = new HashMap<>();

            try {
                Cursor cursor = resolver.query(ContactsContract.Data.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

                if (cursor == null) {
                    return contactIdDetailMap;
                }

                while (cursor.moveToNext()) {

                    try {
                        ContactBean.ContactDetail detail = new ContactBean.ContactDetail();

                        final long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID));
                        int contactTimes = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.TIMES_CONTACTED));
                        String status = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.CONTACT_STATUS));
                        long lastContact = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data.LAST_TIME_CONTACTED));

                        detail.setContact_times(contactTimes);
                        detail.setLast_contact_time(lastContact);
                        detail.setStatus(status);

                        int columnIndex = cursor.getColumnIndex("nickname");
                        if (columnIndex > -1) {
                            String nicky = cursor.getString(columnIndex);
                            detail.setNickname(nicky);
                        }
                        columnIndex = cursor.getColumnIndex("data2");
                        if (columnIndex > -1) {
                            String relationShip = cursor.getString(columnIndex);
                            detail.setRelation(relationShip);
                        }

                        contactIdDetailMap.put(contactId, detail);
                    } catch (Exception e) {
//                        UploadUtils.uploadException(e, "queryAllContactDetail.while");
                    }
                }

                cursor.close();

            } catch (Exception e) {
            }

            return contactIdDetailMap;
        }

        private static ContactBean.ContactDetail queryContactDetail(long contactId){
            ContactBean.ContactDetail detail = new ContactBean.ContactDetail();

            Cursor cursor = resolver.query(ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.CONTACT_ID + " = " + contactId,
                    null,
                    null);

            if(cursor == null){
                return detail;
            }

            if (cursor.moveToNext()){
                int contactTimes = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.TIMES_CONTACTED));
                String status = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.CONTACT_STATUS));
                long lastContact = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data.LAST_TIME_CONTACTED));

                detail.setContact_times(contactTimes);
                detail.setLast_contact_time(lastContact);
                detail.setStatus(status);

                int columnIndex = cursor.getColumnIndex("nickname");
                if (columnIndex > -1) {
                    String nicky = cursor.getString(columnIndex);
                    detail.setNickname(nicky);
                }
                columnIndex = cursor.getColumnIndex("data2");
                if (columnIndex > -1) {
                    String relationShip = cursor.getString(columnIndex);
                    detail.setRelation(relationShip);
                }
            }

            cursor.close();

            return detail;
        }
    }
}
