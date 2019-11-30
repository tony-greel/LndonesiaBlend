package com.example.lndonesiablend.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: weiyun
 * @Time: 2019/6/28
 * @Description: 上传通讯录实体
 */
public class UploadContactsInfo implements Serializable {
    public String user_id;
    public String self_mobile;
    public List<OtherContactsInfo> record;


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

    public List<OtherContactsInfo> getRecord() {
        return record;
    }

    public void setRecord(List<OtherContactsInfo> record) {
        this.record = record;
    }

    public static class OtherContactsInfo implements Serializable {
        public String other_name;
        public String other_mobile;

        public String getOther_name() {
            return other_name;
        }

        public void setOther_name(String other_name) {
            this.other_name = other_name;
        }

        public String getOther_mobile() {
            return other_mobile;
        }

        public void setOther_mobile(String other_mobile) {
            this.other_mobile = other_mobile;
        }


        @Override
        public String toString() {
            return "OtherContactsInfo{" +
                    "other_name='" + other_name + '\'' +
                    ", other_mobile='" + other_mobile + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UploadContactsInfo{" +
                "user_id='" + user_id + '\'' +
                ", self_mobile='" + self_mobile + '\'' +
                ", record=" + record +
                '}';
    }
}
