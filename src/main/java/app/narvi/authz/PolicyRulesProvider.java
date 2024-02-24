package app.narvi.authz;

public interface PolicyRulesProvider {

  Iterable<Class<PolicyRule>> collect();
}