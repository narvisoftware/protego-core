package app.narvi.authz;

public interface AuditProvider {

  public void audit(Permission permission, PolicyRule policyRule, SecurityContext securityContext);

}