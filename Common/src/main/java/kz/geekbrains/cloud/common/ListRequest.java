package kz.geekbrains.cloud.common;

import lombok.Value;

@Value
public class ListRequest extends AbstractMessage {

    private String path;

}
