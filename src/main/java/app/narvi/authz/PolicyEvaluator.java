package app.narvi.authz;

import static app.narvi.authz.PolicyRule.Decision.NOT_APPLICABLE;
import static app.narvi.authz.PolicyRule.Decision.PERMIT;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

import app.narvi.authz.PolicyRule.Decision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PolicyEvaluator<E extends PolicyRule> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
    if (evaluate(permission) == NOT_APPLICABLE) {
      throw new PolicyException("Action not permitted");
    }
  }

  public static Decision evaluate(Permission permission) {
    if (soleInstance.rulesCollection == null) {
      throw new IllegalStateException("RulesCollection has not being initialized.");
    }
    for (PolicyRule aPolicyRule : ((List<? extends PolicyRule>) soleInstance.rulesCollection)) {
      Decision decision = aPolicyRule.evaluate(permission);
      AuditServices.getAuditProviders()
          .forEach(auditProvider -> auditProvider.audit(permission, aPolicyRule, decision));
      if (decision == PERMIT) {
        return PERMIT;
      }
    }
    return NOT_APPLICABLE;

  }

  static {
    LOG.info("Loading Audit Provides");
    ServiceLoader<AuditProvider> auditLoader = ServiceLoader.load(AuditProvider.class);
    auditLoader.forEach(auditProvider ->
        LOG.info("Found Audit Provider: " + auditProvider.getClass().getCanonicalName())
    );
    auditLoader.forEach(AuditServices::addProvider);
    if (auditLoader.stream().count() == 0) {
      LOG.info("NO Audit Providers found!");
    }
  }

}