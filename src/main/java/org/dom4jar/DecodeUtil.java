package org.dom4jar;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

public class DecodeUtil {

    public static String decodeStr(String keyStr, String content) throws Exception {
        try {
            byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(),keyStr.getBytes("UTF-8")).getEncoded();
            AES aes = SecureUtil.aes(key);
            return aes.decryptStr(content);
        }catch (Exception e){
            return null;
        }
    }

    public static String encodeStr(String keyStr,String content) throws Exception {
        try {
            byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(),keyStr.getBytes("UTF-8")).getEncoded();
            AES aes = SecureUtil.aes(key);
            return aes.encryptBase64(content);
        }catch (Exception e){
            return null;
        }
    }

    public static String sign(Map map,String sign){
        TreeMap<String,Object> treeMap =new TreeMap<>(map);
        AtomicReference<String> autoStr=new AtomicReference<>("");
        treeMap.forEach((key,value)->{
            autoStr.set(autoStr.get()+(key+"="+value)+"&");
        });
        return MD5.create().digestHex(autoStr.get()) ;
    }
}
