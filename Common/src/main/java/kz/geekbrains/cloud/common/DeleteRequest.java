package kz.geekbrains.cloud.common;

public class DeleteRequest extends AbstractFileMessage {

    public DeleteRequest(String fileName, String path) {
        super(fileName, path);
    }
}
