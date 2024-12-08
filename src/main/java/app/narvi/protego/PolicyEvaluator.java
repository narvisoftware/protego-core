package app.narvi.protego;

import static app.narvi.protego.PermissionDecision.Decision.PERMIT;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.Collections;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.narvi.protego.AuditProvider.Votes;

public class PolicyEvaluator<PRP extends PolicyRulesProvider> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final Set<PRP> policyRulesProviders;

  private static PolicyEvaluator soleInstance;

  private PolicyEvaluator(Set<PRP> policyRulesProviders) {
    this.policyRulesProviders = Collections.unmodifiableSet(policyRulesProviders);
  }

  public static <PRP extends PolicyRulesProvider> void registerProviders(PRP... policyRulesProviders) {
    if (soleInstance != null) {
      throw new IllegalAPIUsageException("RulesCollection has already being set.");
    }
    if (policyRulesProviders == null || policyRulesProviders.length == 0) {
      throw new NullPointerException("At least one RulesCollection must be provided.");
    }

    soleInstance = new PolicyEvaluator(Set.of(policyRulesProviders));
  }

  public static void evaluatePermission(Permission permission) {
    if (!hasPermission(permission)) {
      throw new PolicyException("Action not permitted");
    }
  }

  public static boolean hasPermission(Permission permission) {
    if (soleInstance == null || soleInstance.policyRulesProviders == null) {
      throw new IllegalStateException("RulesCollection has not being initialized.");
    }
    if (permission == null) {
      throw new NullPointerException("Permission sent for evaluation cannot be null.");
    }
    Set<? extends PolicyRule> policyRules = ((Set<? extends PolicyRulesProvider>) soleInstance.policyRulesProviders)
        .stream()
        .map(prp -> prp.collect(permission))
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());

    Votes<PolicyRule> votes = new Votes<>();
    boolean permissionGranted = false;
    for (PolicyRule aPolicyRule : policyRules) {
      if (!aPolicyRule.isPermissionSupported(permission)) {
        votes.addAbstainVote(
            "Permission is not of type " + aPolicyRule.getSupportedPermissionType(),
            aPolicyRule
        );
        continue;
      }
      PermissionDecision permissionDecision = aPolicyRule.hasPermission();
      votes.add(permissionDecision, aPolicyRule);
      if (permissionDecision.getDecision() == PERMIT) {
        permissionGranted = true;
        break;
      }
    }
    Set<PolicyRule> skippedFromVoting = policyRules.stream()
        .filter(pr -> !votes.containsPolicyRule(pr))
        .collect(Collectors.toSet());
    AuditServices.getAuditProviders()
        .forEach(auditProvider -> auditProvider.audit(permission, votes, skippedFromVoting));
    return permissionGranted;

  }

  static {
    LOG.info("Loading Audit Providers");
    ServiceLoader<AuditProvider> auditLoader = ServiceLoader.load(AuditProvider.class);
    auditLoader.forEach(auditProvider ->
        LOG.info("Found Audit Provider (in Java service providers): " + auditProvider.getClass().getCanonicalName())
    );
    auditLoader.forEach(ap -> {
      if (!AuditServices.containsAuditProvider(ap)) {
        AuditServices.addProvider(ap);
        LOG.info("Audit Provider (from Java service providers): " + ap.getClass().getCanonicalName()
            + " it will be loaded.");
      } else {
        LOG.info("Audit Provider (from Java service providers): " + ap.getClass().getCanonicalName()
            + " is NOT loaded because already exist.");
      }
    });
    if (auditLoader.stream().count() == 0) {
      LOG.info("NO Audit Providers found in Java service providers!");
    }
  }

  public static class IllegalAPIUsageException extends RuntimeException {

    public IllegalAPIUsageException(String message) {
      super(message);
    }
  }

}