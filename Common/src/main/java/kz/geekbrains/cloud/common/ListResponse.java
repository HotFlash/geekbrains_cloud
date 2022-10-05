package kz.geekbrains.cloud.common;

import lombok.Value;

import java.util.List;

@Value
public class ListResponse extends AbstractMessage {

    private List<FileInfo> list;
    private String path;
}
