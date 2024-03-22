package app.narvi.authz;

public interface PolicyRulesProvider {

  Iterable<? super PolicyRule> collect();

}