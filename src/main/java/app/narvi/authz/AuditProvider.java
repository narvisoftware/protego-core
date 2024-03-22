package app.narvi.authz;

import app.narvi.authz.PolicyRule.Decision;

public interface AuditProvider {

  void audit(Permission permission, PolicyRule policyRule, Decision decision);

}