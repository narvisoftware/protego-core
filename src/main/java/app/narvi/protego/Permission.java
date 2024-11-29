package app.narvi.protego;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Permission<A> {

  protected A action;
  //key=class's simple name. val=object instance
  protected Map<String, Object> protectedResources = new LinkedHashMap<>();

  public Permission(A action, Object... protectedResources) {
    this.action = action;
    for(Object aProtectedResource : protectedResources) {
      this.protectedResources.put(aProtectedResource.getClass().getSimpleName(), aProtectedResource);
    }
  }

  public A getAction() {
    return action;
  }

  public Map<String, Object> getProtectedResources() {
    return protectedResources;
  }

}