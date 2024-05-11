package app.narvi.authz;

public interface PolicyRule {

  enum Decision {
    PERMIT,
    WITHHOLD
  }

  Decision evaluate(Permission permission);

}