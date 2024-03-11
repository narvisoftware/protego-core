package app.narvi.authz;

public interface PolicyRule {

  public enum Decision {
    PERMIT,
    NOT_APPLICABLE
  }

  Decision evaluate(Permission permission);

  String signature();
}