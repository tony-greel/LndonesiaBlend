package com.example.lndonesiablend.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: weiyun
 * @Time: 2019/7/3
 * @Description: 上传设备信息
 */
public class UploadDeviceInfoBean implements Serializable {
    public String user_id;
    public String self_mobile;
    public String imei;
    public String sim;
    public String brands;
    public String mobile_model;
    public String cpu_model;
    public String cpu_cores;
    public String ram;
    public String rom;
    public String resolution;
    public String version;
    public List<AppList> applist;
    public String sports_info;
    public String bluetoothId;
    public String wifi;

    public static class AppList {
        public String firstTime;
        public String lastTime;
        public String name;
        public String packageName;
        public String versionCode;

        public String getFirstTime() {
            return firstTime;
        }

        public void setFirstTime(String firstTime) {
            this.firstTime = firstTime;
        }

        public String getLastTime() {
            return lastTime;
        }

        public void setLastTime(String lastTime) {
            this.lastTime = lastTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        @Override
        public String toString() {
            return "AppList{" +
                    "firstTime='" + firstTime + '\'' +
                    ", lastTime='" + lastTime + '\'' +
                    ", name='" + name + '\'' +
                    ", packageName='" + packageName + '\'' +
                    ", versionCode='" + versionCode + '\'' +
                    '}';
        }
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSelf_mobile() {
        return self_mobile;
    }

    public void setSelf_mobile(String self_mobile) {
        this.self_mobile = self_mobile;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public String getMobile_model() {
        return mobile_model;
    }

    public void setMobile_model(String mobile_model) {
        this.mobile_model = mobile_model;
    }

    public String getCpu_model() {
        return cpu_model;
    }

    public void setCpu_model(String cpu_model) {
        this.cpu_model = cpu_model;
    }

    public String getCpu_cores() {
        return cpu_cores;
    }

    public void setCpu_cores(String cpu_cores) {
        this.cpu_cores = cpu_cores;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getRom() {
        return rom;
    }


    public void setRom(String rom) {
        this.rom = rom;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<AppList> getApplist() {
        return applist;
    }

    public void setApplist(List<AppList> applist) {
        this.applist = applist;
    }

    public String getSports_info() {
        return sports_info;
    }

    public void setSports_info(String sports_info) {
        this.sports_info = sports_info;
    }

    public String getBluetoothId() {
        return bluetoothId;
    }

    public void setBluetoothId(String bluetoothId) {
        this.bluetoothId = bluetoothId;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    @Override
    public String toString() {
        return "UploadDeviceInfo{" +
                "user_id='" + user_id + '\'' +
                ", self_mobile='" + self_mobile + '\'' +
                ", imei='" + imei + '\'' +
                ", sim='" + sim + '\'' +
                ", brands='" + brands + '\'' +
                ", mobile_model='" + mobile_model + '\'' +
                ", cpu_model='" + cpu_model + '\'' +
                ", cpu_cores='" + cpu_cores + '\'' +
                ", ram='" + ram + '\'' +
                ", rom='" + rom + '\'' +
                ", resolution='" + resolution + '\'' +
                ", version='" + version + '\'' +
                ", applist=" + applist +
                ", sports_info='" + sports_info + '\'' +
                ", bluetoothId='" + bluetoothId + '\'' +
                ", wifi='" + wifi + '\'' +
                '}';
    }
}
