package app.narvi.authz;


public class DummyPolicyRule implements PolicyRule {


  @Override
  public Decision evaluate(Permission permission) {
    return Decision.NOT_APPLICABLE;
  }

  @Override
  public String signature() {
    return null;
  }


}