package kz.geekbrains.cloud.common;

import lombok.Value;


@Value
public class UserConfigRequest extends AbstractMessage {
   private String login;
    private Integer capacity;
}
