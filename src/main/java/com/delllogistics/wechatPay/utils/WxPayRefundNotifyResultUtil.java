package com.delllogistics.wechatPay.utils;

import com.delllogistics.util.MD5Util;
import com.delllogistics.wechatPay.converter.MyWxPayOrderNotifyResultConverter;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.thoughtworks.xstream.XStream;
import me.chanjar.weixin.common.util.xml.XStreamInitializer;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.Security;

/** 微信退款解析XML工具类
 * Created by xzm on 2018-4-3.
 */
public class WxPayRefundNotifyResultUtil {

    private final static String cipherType="AES/ECB/PKCS7Padding";

    static{
        Security.addProvider(new BouncyCastleProvider());
    }

    public static WxPayRefundNotifyResult fromXML(String xmlString, String mchKey) throws WxPayException {
        WxPayRefundNotifyResult result = BaseWxPayResult.fromXML(xmlString, WxPayRefundNotifyResult.class);
        String reqInfoString = result.getReqInfoString();
        try {
            Cipher cipher = Cipher.getInstance(cipherType);
            SecretKeySpec key = new SecretKeySpec(MD5Util.encrypt(mchKey).toLowerCase().getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            result.setReqInfo(WxPayRefundNotifyResult.ReqInfo.fromXML(new String(cipher.doFinal(Base64.decodeBase64(reqInfoString)))));
        } catch (Exception e) {
            throw new WxPayException("解密退款通知加密信息时出错", e);
        }

        return result;
    }

    public static void main(String[] args) throws WxPayException {
        String xmlString="<xml><return_code>SUCCESS</return_code><appid><![CDATA[wxe184298fc71118e4]]></appid><mch_id><![CDATA[1497713532]]></mch_id><nonce_str><![CDATA[b7c2ad02d4991510daca01aa0b09be6d]]></nonce_str><req_info><![CDATA[icxy1o7uUsVRkeZCFTwl3CiFdHXz0r3TuL7PQXQ/wfLzdKxtqcFtsNsdUhE5+VKaohMhxZ5gtp8GELqMoo6AG+5XNJFdnZbzqmQRDM2w6ShD/+u8gmg8FsM5muR3TyZ4TSX7ohbd669unALpcZcIdLiSGB64eFvnPkRh8IUtCqg26wA13TjvLXD/qsR03KOtbMXO8KwSWZMrEY7754uXiZDmAh6sb7wMJrZGSe4GpwqVj4zPawy48z5EPwjYVIAgsUxLRh4MSJYqhLZOSFKdxmrwtcFVZDucvcMDs6U0PW5VzJE0YgcKmR9Cxgw5i2GhxIUmE23q/wifnWcd1rCFqEKphGH1ZOGEQym0bTDdGvNEZHhYZTaStJut61Nn1xg7x8sk6RwE2KBoL5AVlMgp5AdDQXgIojXYcsx940pp8Ukz4e5APl8bxA51+dWnPhEHcnVz0CtJchmmQlpD387yKjyTUuN6pa1B1FGUdTb5ccfE1jmImKIb4xUTsaJ16URuJLIi2I02YhaO3pRMttWbYh+yseisc2Cc8RuwnzKU4oywpzn600zYxk3u7kFFPhOp9+z1XZUhFC7Vej8NrL+3UgwCzMJZme0PWwzdcF/CkHkxjkVAqdjylax2XR5WyRAmJINpyEvYEqi5If+QIipHaxvo+c/11ev5Oq6JNYBzRfXldOm9VkICrwewVxD9vl3W7TlOTvJuewgwr9DggIjwJE+L8uQiNo4bxt1sXKssLCi7n05Z7U1H+CmmPvpzPu/W3Fk1+k4Tve2L7Df233MegqmU8yHPt1LJfFygRovi7jzoesdByRbD1dj69ivnxIpXtBhs7C7qjD3C5ZCBYleUQ5vd2LxeeI60J/iYH9yndQPG6TwrYFKp0D+bfojYNe4ZECMg8xInWDRpQZCrXtnXvxwegKNfuaYcOWpSld7J2PdMUQH5hgwAJucPSCOl4hTEc0M9F/5lvahKADKgbSIvSMgkeFWxkJ1olq7+Ko6RoSH4Pe7E99iivfve+Ss4UeQ2CJI/ueFCAwqIFxpumfxZsv4Ttgx0vGLQypSJRpKywxo=]]></req_info></xml>";
        String mchKey="883RBKAL9qC1G4erU3AfRCNPMds7CiVS";
        WxPayRefundNotifyResult wxPayRefundNotifyResult = fromXML(xmlString, mchKey);
        System.out.println(wxPayRefundNotifyResult);
    }
}
