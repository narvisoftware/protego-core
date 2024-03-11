package app.narvi.authz;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

import app.narvi.authz.PolicyRule.Decision;

public class PolicyEvaluator {

  private static RulesCollection rulesCollection;

  List<PolicyRule> rules = new CopyOnWriteArrayList<>();

  public static void registerRulesCollection(RulesCollection rulesCollection) {
    if (PolicyEvaluator.rulesCollection != null) {
      throw new NullPointerException("RulesCollection has already being set.");
    }
    if (rulesCollection == null) {
      throw new NullPointerException("RulesCollection cannot be null.");
    }
    PolicyEvaluator.rulesCollection = rulesCollection;
//    rulesCollection.
  }

  public static Decision evaluate(Permission permission) {
    if(rulesCollection == null) {
      throw new IllegalStateException("RulesCollection has not being initialized.");
    }
//    return PolicyRule.Decision.NOT_APPLICABLE;
    return null;
  }

  static {
    ServiceLoader<AuditProvider> auditLoader = ServiceLoader.load(AuditProvider.class);
    auditLoader.forEach(AuditServices::addProvider);
  }

}