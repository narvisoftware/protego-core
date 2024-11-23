package app.narvi.protego;

public class DoubleResourcePermission<PR1, PR2, A> {

  private PR1 protectedResource1;
  private PR2 protectedResource2;
  private A action;

  public DoubleResourcePermission(A action, PR1 protectedResource1, PR2 protectedResource2) {
    this.protectedResource1 = protectedResource1;
    this.protectedResource2 = protectedResource2;
    this.action = action;
  }

  public PR1 getFirstProtectedResource() {
    return protectedResource1;
  }

  public PR2 getSecondProtectedResource() {
    return protectedResource2;
  }

  public A getAction() {
    return action;
  }
}