package app.narvi.authz;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public interface RulesCollection {

  Iterable<PolicyRulesProvider> getRulesProviders();

}