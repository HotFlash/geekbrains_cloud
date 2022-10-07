package kz.geekbrains.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.common.reg.RegErrorResponse;
import kz.geekbrains.cloud.common.reg.RegRequest;
import kz.geekbrains.cloud.common.reg.RegSuccessResponse;
import kz.geekbrains.cloud.server.service.auth.AuthService;

import java.sql.SQLException;

@Log4j2
@AllArgsConstructor
public class RegHandler implements ServerRequestHandler {

    private AuthService authService;

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg) {
        RegRequest regRequest = (RegRequest) msg;

        String login = regRequest.getLogin();
        String password = regRequest.getPassword();
        int capacity = regRequest.getCapacity();

        try {
            authService.insertUser(login, password, capacity);

            ctx.writeAndFlush(new RegSuccessResponse(true)).addListener(channelFuture -> {
                if (channelFuture.isSuccess()) {
                    log.info(ctx.name() + "- RegSuccessResponse sent: User " + regRequest.getLogin() + " registered");
                }
            });
        } catch (SQLException e) {
            String reason;

            if (e.toString().contains("UNIQUE constraint failed")) {
                reason = "Login " + regRequest.getLogin() + " is already in use";
            } else {
                reason = "Registration failed";
            }

            ctx.writeAndFlush(new RegErrorResponse(reason)).addListener(channelFuture -> {
                if (channelFuture.isSuccess()) {
                    log.info(ctx.name() + " RegErrorResponse sent: " + reason);
                }
            });
        }
    }
}
