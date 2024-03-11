package app.narvi.authz;

public interface PolicyRulesProvider {

  Iterable<PolicyRule> collect();

}