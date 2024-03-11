package app.narvi.authz;

import app.narvi.authz.PolicyRule.Decision;
import app.narvi.authz.rules.BasicPolicyRule;

public class AllowOwnTenantAccess implements PolicyRule {


  @Override
  public Decision evaluate(Permission permission) {
    return Decision.NOT_APPLICABLE;
  }

  @Override
  public String signature() {
    return null;
  }

 
}