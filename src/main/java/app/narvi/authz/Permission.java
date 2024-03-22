package app.narvi.authz;

public class Permission<PR, A> {

  PR protectedResource;
  A action;

  public Permission(PR protectedResource, A action) {
    this.protectedResource = protectedResource;
    this.action = action;
  }

}