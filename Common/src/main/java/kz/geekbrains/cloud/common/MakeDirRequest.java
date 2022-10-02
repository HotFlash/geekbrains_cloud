package kz.geekbrains.cloud.common;

import kz.geekbrains.cloud.common.AbstractFileMessage;

public class MakeDirRequest extends AbstractFileMessage {

    public MakeDirRequest(String fileName, String path) {
        super(fileName, path);
    }
}
