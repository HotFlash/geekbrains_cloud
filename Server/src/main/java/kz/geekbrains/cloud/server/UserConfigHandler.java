package kz.geekbrains.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import kz.geekbrains.cloud.common.UserConfigRequest;
import kz.geekbrains.cloud.common.UserConfigResponse;
import kz.geekbrains.cloud.server.service.auth.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.sql.SQLException;

@Log4j2
@AllArgsConstructor
public class UserConfigHandler implements ServerRequestHandler {
    private AuthService authService;

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg) {
        UserConfigRequest userConfigRequest = (UserConfigRequest) msg;
        String login = userConfigRequest.getLogin();

        if (userConfigRequest.getCapacity() > 0) {
            try {
                Integer result = authService.ChangeUserCapacity(login, userConfigRequest.getCapacity());
                ctx.writeAndFlush(new UserConfigResponse(result, login)).addListener(channelFuture -> {
                    if (channelFuture.isSuccess()) {
                    } else {
                        log.error(ctx.name() + " can't change capacity with error: " + result);
                    }
                });
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                Integer result = authService.ReadCapacity(login);
                ctx.writeAndFlush(new UserConfigResponse(result, login)).addListener(channelFuture -> {
                    if (channelFuture.isSuccess()) {
                    } else {
                        log.error(ctx.name() + " capacity sent with error: " + result);
                    }
                });
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
