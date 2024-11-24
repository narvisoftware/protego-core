package app.narvi.protego;

import static app.narvi.protego.AuditProvider.Decision.PERMIT;
import static app.narvi.protego.AuditProvider.Decision.WITHHOLD;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PolicyEvaluator<E extends PolicyRule> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final List<E> rulesCollection;

  private static PolicyEvaluator soleInstance;

  public PolicyEvaluator(List<E> rulesCollection) {
    this.rulesCollection = Collections.unmodifiableList(rulesCollection);
  }

  public static <PRP extends PolicyRulesProvider> void registerProviders(PRP... policyRulesProvider) {
    if (soleInstance != null) {
      throw new IllegalAPIUsageException("RulesCollection has already being set.");
    }
    if (policyRulesProvider == null) {
      throw new NullPointerException("RulesCollection cannot be null.");
    }

    List<? super PolicyRule> policyRules = Arrays.stream(policyRulesProvider).flatMap(
        polRuleProv -> StreamSupport.stream(polRuleProv.collect().spliterator(), false)
    ).collect(Collectors.toList());

    soleInstance = new PolicyEvaluator(policyRules);
  }

  public static void evaluatePermission(Permission permission) {
    if (!hasPermission(permission)) {
      throw new PolicyException("Action not permitted");
    }
  }

  public static boolean hasPermission(Permission permission) {
    if (soleInstance == null || soleInstance.rulesCollection == null) {
      throw new IllegalStateException("RulesCollection has not being initialized.");
    }
    for (PolicyRule aPolicyRule : ((List<? extends PolicyRule>) soleInstance.rulesCollection)) {
      boolean decision = aPolicyRule.hasPermisssion(permission);
      AuditServices.getAuditProviders()
          .forEach(auditProvider -> auditProvider.audit(permission, aPolicyRule, decision ? PERMIT : WITHHOLD));
      if (decision) {
        return true;
      }
    }
    return false;

  }

  static {
    LOG.info("Loading Audit Providers");
    ServiceLoader<AuditProvider> auditLoader = ServiceLoader.load(AuditProvider.class);
    auditLoader.forEach(auditProvider ->
        LOG.info("Found Audit Provider: " + auditProvider.getClass().getCanonicalName())
    );
    auditLoader.forEach(AuditServices::addProvider);
    if (auditLoader.stream().count() == 0) {
      LOG.info("NO Audit Providers found!");
    }
  }

  public static class IllegalAPIUsageException extends RuntimeException {

    public IllegalAPIUsageException(String message) {
      super(message);
    }
  }

}