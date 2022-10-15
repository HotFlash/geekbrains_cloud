package kz.geekbrains.cloud.common;

import lombok.Value;

@Value
public class UserConfigResponse extends AbstractMessage{
    private Integer capacity;
    private String login;

}
