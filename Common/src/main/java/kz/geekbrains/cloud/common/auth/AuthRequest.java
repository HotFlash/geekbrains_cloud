package kz.geekbrains.cloud.common.auth;

import lombok.Value;
import kz.geekbrains.cloud.common.AbstractMessage;

@Value
public class AuthRequest extends AbstractMessage {

    private String login;
    private String password;
}
