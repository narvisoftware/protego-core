package app.narvi.protego;

public interface AuditProvider {

  enum Decision {
    PERMIT,
    WITHHOLD
  }

  void audit(Permission permission, PolicyRule policyRule, Decision decision);

}