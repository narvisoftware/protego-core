package app.narvi.authz;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class AuditServices {

  private static final Set<AuditProvider> auditProviders = new ConcurrentHashMap<>().newKeySet();

  public static void addProvider(AuditProvider auditProvider){
    auditProviders.add(auditProvider);
  }

  public static Set<AuditProvider> getAuditProviders() {
    return Collections.unmodifiableSet(auditProviders);
  }

}