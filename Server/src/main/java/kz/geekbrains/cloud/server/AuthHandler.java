package kz.geekbrains.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.common.auth.AuthErrorResponse;
import kz.geekbrains.cloud.common.auth.AuthRequest;
import kz.geekbrains.cloud.common.auth.AuthSuccessResponse;
import kz.geekbrains.cloud.server.service.auth.AuthService;

@Log4j2
@AllArgsConstructor
public class AuthHandler implements ServerRequestHandler {

    private AuthService authService;

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg) {
        AuthRequest authRequest = (AuthRequest) msg;

        String login = authRequest.getLogin();
        String password = authRequest.getPassword();

        if (authService.authUser(login, password)) {
            ctx.writeAndFlush(new AuthSuccessResponse(login)).addListener(channelFuture -> {
                if (channelFuture.isSuccess()) {
                    log.info(ctx.name() + "- AuthSuccessResponse sent: " + login);
                }
            });
        } else {
            String reason = "Invalid login/password";

            ctx.writeAndFlush(new AuthErrorResponse(reason)).addListener(channelFuture -> {
                if (channelFuture.isSuccess()) {
                    log.info(ctx.name() + "- AuthErrorResponse sent: " + reason);
                }
            });
        }
    }
}
