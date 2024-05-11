package app.narvi.authz;

public class Permission<A, PR> {

  private A action;
  private PR protectedResource;


  public Permission(A action, PR protectedResource) {
    this.action = action;
    this.protectedResource = protectedResource;
  }

  public PR getProtectedResource() {
    return protectedResource;
  }

  public A getAction() {
    return action;
  }
}