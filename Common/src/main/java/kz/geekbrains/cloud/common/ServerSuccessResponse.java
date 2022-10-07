package kz.geekbrains.cloud.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class ServerSuccessResponse extends AbstractMessage {
    @Getter
    private Boolean result;
}
