package app.narvi.protego;

import java.util.Set;

public interface PolicyRulesProvider {

  <P extends Permission> Set<? extends PolicyRule> collect(P permission);

}