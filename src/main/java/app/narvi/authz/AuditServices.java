package app.narvi.authz;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class AuditServices {
  private static final Set<AuditProvider> auditProviders = new ConcurrentHashMap<>().newKeySet();

//  public static void registerProvider(AuditProvider auditProvider){
//    auditProviders.clear();
//    auditProviders.add(auditProvider);
//  }

  public static void addProvider(AuditProvider auditProvider){
    auditProviders.add(auditProvider);
  }



}