package kz.geekbrains.cloud.common;


public class MakeDirRequest extends AbstractFileMessage {

    public MakeDirRequest(String fileName, String path) {
        super(fileName, path);
    }
}
