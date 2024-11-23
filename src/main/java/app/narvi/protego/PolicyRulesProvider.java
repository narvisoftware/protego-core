package app.narvi.protego;

public interface PolicyRulesProvider {

  Iterable<? super PolicyRule> collect();

}