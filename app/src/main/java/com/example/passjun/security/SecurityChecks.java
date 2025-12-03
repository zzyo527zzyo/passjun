package com.example.passjun.security;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Debug;
import android.provider.Settings;
import android.util.Log;
import java.io.File;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class SecurityChecks {

    public static boolean checkForDebugger() {
        if (Debug.isDebuggerConnected()) {
            return false;
        }
        return true;
    }



    public static boolean SHA256_Valid(Context context) {
        try {
            // 获取当前应用的签名信息
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;

            // 计算当前签名的哈希值（这里使用SHA-256）
            //获取SHA-256哈希算法实例
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(signatures[0].toByteArray());
            byte[] digest = md.digest();
            String currentSignature = bytesToHex(digest);
            System.out.println("签名哈希: " + currentSignature); // 同时输出到System.out
            // 这里替换为你预期的正确签名哈希值
            String expectedSignature = "f8cf791cd2ef25dfdd3eb7197b5cd87e3e7da0351687cfa8008e868196f1978c";
            return currentSignature.equalsIgnoreCase(expectedSignature);

        } catch (Exception e) {
            return false;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }



}


