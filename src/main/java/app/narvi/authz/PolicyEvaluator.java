package app.narvi.authz;

import java.util.ServiceLoader;

import app.narvi.authz.PolicyRule.Decision;

public class PolicyEvaluator {

  public static Decision evaluate(Permission permission) {
    return PolicyRule.Decision.NOT_APPLICABLE;
  }

  public static void mustHave(Permission permission) {
    //if (evaluate(permission) == )
  }


  static {
    ServiceLoader<AuditProvider> auditLoader = ServiceLoader.load(AuditProvider.class);
    auditLoader.forEach(AuditServices::addProvider);
  }

}