package com.delllogistics.storage;

import java.io.InputStream;

public interface StorageInterface {

    boolean uploadFile(String filename, byte[] bytes);

    boolean uploadFile(String filename, InputStream input);

    String getStorageDomain();
}
