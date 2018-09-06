package com.delllogistics.util;

import java.time.LocalDateTime;
import java.util.zip.CRC32;

/**
 * Created by jiajie on 15/04/2017.
 */
public class CRC32Util {

    public static String getHash() {
        CRC32 crc32 = new CRC32();
        crc32.update(LocalDateTime.now().toString().getBytes());
        return Long.toHexString(crc32.getValue());
    }
}
