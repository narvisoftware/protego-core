package app.narvi.authz;

public interface AuditProvider {

  public enum Decision {
    PERMIT,
    WITHHOLD
  }

  void audit(Permission permission, PolicyRule policyRule, Decision decision);

}