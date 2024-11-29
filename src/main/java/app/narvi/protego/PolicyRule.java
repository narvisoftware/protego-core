package app.narvi.protego;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class PolicyRule<P extends Permission> {

  protected Class<P> supportedPermissionType;

  public PolicyRule() {
    Type superClassType = getClass().getGenericSuperclass();
    while (superClassType instanceof Class) {
      superClassType = ((Class) superClassType).getGenericSuperclass();
    }
    supportedPermissionType = (Class<P>) ((ParameterizedType) superClassType).getActualTypeArguments()[0];
  }

  public boolean isPermissionSupported(P permission) {
    return supportedPermissionType.isAssignableFrom(permission.getClass());
  }

  public Class<P> getSupportedPermissionType() {
    return supportedPermissionType;
  }

  public abstract PermissionDecision hasPermission();

}