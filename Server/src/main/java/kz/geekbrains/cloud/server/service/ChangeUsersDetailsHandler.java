package kz.geekbrains.cloud.server.service;

import io.netty.channel.ChannelHandlerContext;
import kz.geekbrains.cloud.common.ChangeUsersDetailsRequest;
import kz.geekbrains.cloud.common.auth.AuthErrorResponse;
import kz.geekbrains.cloud.common.auth.AuthSuccessResponse;
import kz.geekbrains.cloud.server.ServerRequestHandler;
import kz.geekbrains.cloud.server.service.auth.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
public class ChangeUsersDetailsHandler implements ServerRequestHandler {

    private AuthService authService;

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg) {
        ChangeUsersDetailsRequest usersDetailsRequest = (ChangeUsersDetailsRequest) msg;
        String login = usersDetailsRequest.getLogin();
        String oldPassword = usersDetailsRequest.getOldPassword();
        String newPassword = usersDetailsRequest.getNewPassword();

        if (authService.authUser(login, oldPassword)) {
            if (authService.ChangeUserPassword(login, newPassword)) {
                String message = "true";
                ctx.writeAndFlush(new AuthSuccessResponse(message)).addListener(channelFuture -> {
                    if (channelFuture.isSuccess()) {
                        log.info(ctx.name() + "- password was changed: " + message);
                    }
                });
            } else {
                String reason = "Invalid operation while chane password";
                ctx.writeAndFlush(new AuthErrorResponse(reason)).addListener(channelFuture -> {
                    if (channelFuture.isSuccess()) {
                        log.info(ctx.name() + "- cant chane password: " + reason);
                    }
                });
            }

        } else {
            String reason = "Invalid old password";
            ctx.writeAndFlush(new AuthErrorResponse(reason)).addListener(channelFuture -> {
                if (channelFuture.isSuccess()) {
                    log.info(ctx.name() + "- cant chane password: " + reason);
                }
            });
        }
    }
}
