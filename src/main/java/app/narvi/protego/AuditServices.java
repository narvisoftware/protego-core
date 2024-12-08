package app.narvi.protego;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AuditServices {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final Map<String, AuditProvider> auditProviders = new ConcurrentHashMap<>();

  public static void addProvider(AuditProvider auditProvider) {
    String newAuditProviderName = auditProvider.getClass().getSimpleName();
    if (auditProviders.containsKey(newAuditProviderName)) {
      throw new IllegalArgumentException("Audit provider " + newAuditProviderName + " already exist");
    }
    LOG.info("Service provider " + newAuditProviderName + " was added to the providers list");
    auditProviders.put(newAuditProviderName, auditProvider);
  }

  public static void removeAuditProvider(AuditProvider auditProvider) {
    removeAuditProvider(auditProvider.getClass());
  }

  public static void removeAuditProvider(Class<? extends AuditProvider> auditProviderClass) {
    removeAuditProvider(auditProviderClass.getSimpleName());
  }

  public static void removeAuditProvider(String auditProviderName) {
    if (!containsAuditProvider(auditProviderName)) {
      throw new IllegalArgumentException("Audit provider " + auditProviderName + " does not exist.");
    }
    LOG.info("Service provider " + auditProviderName + " was removed from the providers list");
    auditProviders.remove(auditProviderName);
  }

  public static boolean containsAuditProvider(AuditProvider auditProvider) {
    return containsAuditProvider(auditProvider.getClass());
  }

  public static boolean containsAuditProvider(Class<? extends AuditProvider> auditProviderClass) {
    return containsAuditProvider(auditProviderClass.getSimpleName());
  }

  public static boolean containsAuditProvider(String auditProviderName) {
    return auditProviders.containsKey(auditProviderName);
  }

  public static Iterable<AuditProvider> getAuditProviders() {
    return Collections.unmodifiableCollection(auditProviders.values());
  }

}