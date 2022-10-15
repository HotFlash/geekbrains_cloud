package kz.geekbrains.cloud.common;

import lombok.Value;

@Value
public class ChangeUsersDetailsRequest extends AbstractMessage{
    private String login;
    private String oldPassword;
    private String newPassword;
}
