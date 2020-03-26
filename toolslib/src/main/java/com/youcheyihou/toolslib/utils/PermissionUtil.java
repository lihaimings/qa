package com.youcheyihou.toolslib.utils;

import android.Manifest;
import android.app.Activity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ppjun
 * @date 2018/10/09.
 * 动态权限申请类
 */
public class PermissionUtil {

    /**
     * 防止反射
     */
    private PermissionUtil() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    /**
     * 权限获取回调接口
     */
    public static class RequestPermissionAdapter implements RequestPermission {

        @Override
        public void onRequestPermissionSuccess() {

        }

        @Override
        public void onRequestPermissionFailure(List<String> permissions) {

        }

        @Override
        public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {

        }
    }

    /**
     * 权限获取回调接口
     */
    public interface RequestPermission {
        /**
         * 获取权限成功
         */
        void onRequestPermissionSuccess();

        /**
         * 用户拒绝了这个请求，不过下次还是会请求这个权限，当然没勾上下次不再询问的情况下
         */
        void onRequestPermissionFailure(List<String> permissions);

        /**
         * 用户拒绝了这个请求，并设置不再询问，这时候要到app设置打开这个权限
         */
        void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions);
    }

    /**
     * 申请权限方法
     */
    public static void requestPermission(Activity activity, final RequestPermission requestPermission
            , String... permissions) {

        if (permissions == null || permissions.length == 0) {
            return;
        }
        RxPermissions rxPermissions = new RxPermissions(activity);
        List<String> needRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission);
            }
        }
        if (needRequest.isEmpty()) {
            requestPermission.onRequestPermissionSuccess();
        } else {
//            //开始申请权限
//            rxPermissions.requestEach(needRequest.toArray(new String[needRequest.size()]))
//                    .buffer(permissions.length)
//                    .subscribe(new Subscriber<List<Permission>>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onNext(List<Permission> permissions) {
//                            List<String> failurePermissions = new ArrayList<>();
//                            List<String> AskNeverAgainPermissions = new ArrayList<>();
//                            for (Permission p : permissions) {
//                                if (!p.granted) {
//                                    if (p.shouldShowRequestPermissionRationale) {
//                                        failurePermissions.add(p.name);
//                                    } else {
//                                        AskNeverAgainPermissions.add(p.name);
//                                    }
//                                }
//                            }
//                            if (failurePermissions.size() > 0) {
//                                requestPermission.onRequestPermissionFailure(failurePermissions);
//                            }
//                            if (AskNeverAgainPermissions.size() > 0) {
//                                requestPermission.onRequestPermissionFailureWithAskNeverAgain(AskNeverAgainPermissions);
//                            }
//                            if (failurePermissions.size() == 0 && AskNeverAgainPermissions.size() == 0) {
//                                requestPermission.onRequestPermissionSuccess();
//                            }
//                        }
//                    });
        }
    }

    /**
     * 获取手机状态权限
     */
    public static void readPhoneState(Activity activity, final RequestPermission requestPermission) {
        requestPermission(activity, requestPermission, Manifest.permission.READ_PHONE_STATE);
    }

    /**
     * 获取手机外部存储权限
     */
    public static void externalStorage(Activity activity, final RequestPermission requestPermission) {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        requestPermission(activity, requestPermission, permissions);
    }

    /**
     * 读取手机外部存储权限
     */
    public static void readExternalStorage(Activity activity, final RequestPermission requestPermission) {
        requestPermission(activity, requestPermission, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * 获取手机外部存储权限和手机状态权限
     */
    public static void getStorageAndPhoneState(Activity activity, final RequestPermission requestPermission) {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
        requestPermission(activity, requestPermission, permissions);
    }


    /**
     * 请求打电话权限
     */
    public static void callPhone(Activity activity, RequestPermission requestPermission) {
        requestPermission(activity, requestPermission, Manifest.permission.CALL_PHONE);
    }

    /**
     * 打开相机拍照
     */
    public static void openCamera(Activity activity, RequestPermission requestPermission) {
        requestPermission(activity, requestPermission, Manifest.permission.CAMERA);
    }

    /**
     * 定位权限
     */
    public static void accessLocation(Activity activity, RequestPermission requestPermission) {
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        requestPermission(activity, requestPermission, permissions);
    }

    /**
     * 录音权限
     */
    public static void recordAudio(Activity activity, RequestPermission requestPermission) {
        requestPermission(activity, requestPermission, Manifest.permission.RECORD_AUDIO);
    }

    /**
     * 申请相机拍照和录音权限
     */
    public static void openCameraAudio(Activity activity, RequestPermission requestPermission) {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        requestPermission(activity, requestPermission, permissions);
    }

}