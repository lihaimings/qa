package com.haiming.paper.interfaces;

import com.haiming.paper.Utils.UIUtil;

public interface Constacers {
    String PACKAGE_NAME = UIUtil.getPackageName();
    /*========== SP跨进程通信的常量 ==========*/
    String CONTENT="content://";
    String AUTHORITY= PACKAGE_NAME+".sphelper";
    String SEPARATOR= "/";
    String CONTENT_URI =CONTENT+AUTHORITY;
    String TYPE_STRING_SET="string_set";
    String TYPE_STRING="string";
    String TYPE_INT="int";
    String TYPE_LONG="long";
    String TYPE_FLOAT="float";
    String TYPE_BOOLEAN="boolean";
    String VALUE= "value";

    String USER_ID = "userId";
    String TYPE_CONTAIN="contain";
    String NULL_STRING= "null";
    String TYPE_CLEAN="clean";
    String TYPE_GET_ALL="get_all";


    String CURSOR_COLUMN_NAME = "cursor_name";
    String CURSOR_COLUMN_TYPE = "cursor_type";
    String CURSOR_COLUMN_VALUE = "cursor_value";
}
