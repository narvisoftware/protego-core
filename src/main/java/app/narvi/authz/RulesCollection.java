package app.narvi.authz;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;

public class RulesCollection {

  private static final Set<PolicyRulesProvider> rulesProviders = new ConcurrentHashMap<>().newKeySet();
  private static final Set<PolicyRule> rules = new ConcurrentHashMap<>().newKeySet();

  public static void setPrivateKeyAndInitProviders(String privateKey) {

  }

  public static void registerProvider(PolicyRulesProvider rulesProvider) {
    rulesProviders.add(rulesProvider);
    reloadRules();
  }

  public static void reloadRules() {
    rules.clear();
    rulesProviders
        .stream()
        .flatMap(provider -> StreamSupport.stream(provider.collect().spliterator(), false))
        .map(ruleClass -> {
          Constructor[] constructors = ruleClass.getDeclaredConstructors();
          Constructor paramConstructor = Arrays.stream(constructors)
              .filter(constructor -> Arrays.equals(constructor.getParameterTypes(), new Class[]{Parameter.class}))
              .findAny().orElse();
          return null;
        })
        .forEach(rules::add);
  }

  static {
    ServiceLoader<PolicyRulesProvider> auditLoader = ServiceLoader.load(PolicyRulesProvider.class);
    auditLoader.forEach(RulesCollection::registerProvider);
  }


}