package kz.geekbrains.cloud.common.reg;

import kz.geekbrains.cloud.common.ServerErrorResponse;

public class RegErrorResponse extends ServerErrorResponse {

  public RegErrorResponse(String reason) {
    super(reason);
  }
}
