package kz.geekbrains.cloud.common;

import lombok.Value;

@Value
public class FileRequest extends AbstractMessage {

    String fileName;
    String path;

}
