package app.narvi.authz;

import static app.narvi.authz.PolicyRule.Decision.NOT_APPLICABLE;
import static app.narvi.authz.PolicyRule.Decision.PERMIT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

import app.narvi.authz.PolicyRule.Decision;

public class PolicyEvaluator<E extends PolicyRule> {

  public static final String NEW_LINE_CHARACTER = "\n";
  public static final String PUBLIC_KEY_START_KEY_STRING = "-----BEGIN PUBLIC KEY-----";
  public static final String PUBLIC_KEY_END_KEY_STRING = "-----END PUBLIC KEY-----";
  public static final String EMPTY_STRING = "";
  public static final String NEW_CR_CHARACTER = "\r";

  private List<E> rulesCollection;

  public static PolicyEvaluator soleInstance = new PolicyEvaluator();

  public static <PRP extends PolicyRulesProvider> void registerProviders(PRP... policyRulesProvider) {
    if (soleInstance.rulesCollection != null) {
      throw new NullPointerException("RulesCollection has already being set.");
    }
    if (policyRulesProvider == null) {
      throw new NullPointerException("RulesCollection cannot be null.");
    }

    soleInstance.rulesCollection = new ArrayList<>();

    Arrays.stream(policyRulesProvider).flatMap(
        polRuleProv -> StreamSupport.stream(polRuleProv.collect().spliterator(), false)
    ).forEach(
        aProvider -> soleInstance.rulesCollection.add(aProvider)
    );
  }

  public static void evaluatePermission(Permission permission) {
    if(evaluate(permission) == NOT_APPLICABLE) {
      throw new PolicyException("Action not permitted");
    }
  }

  public static Decision evaluate(Permission permission) {
    if (soleInstance.rulesCollection == null) {
      throw new IllegalStateException("RulesCollection has not being initialized.");
    }
    for (PolicyRule aPolicyRule : ((List<? extends PolicyRule>) soleInstance.rulesCollection)) {
      if (aPolicyRule.evaluate(permission) == PERMIT) {
        return PERMIT;
      }
    }
    return NOT_APPLICABLE;
  }

  static {
    ServiceLoader<AuditProvider> auditLoader = ServiceLoader.load(AuditProvider.class);
    auditLoader.forEach(AuditServices::addProvider);
  }

}