package kz.geekbrains.cloud.common.reg;

import lombok.Value;
import kz.geekbrains.cloud.common.AbstractMessage;

@Value
public class RegRequest extends AbstractMessage {

    String login;
    String password;
    int capacity;
}
