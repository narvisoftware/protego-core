package app.narvi.protego;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.google.common.reflect.TypeToken;

public abstract class PolicyRule<P extends Permission> {

  protected Class<P> supportedPermissionType;

  public PolicyRule() {
    Type superClassType = getClass().getGenericSuperclass();
    while (superClassType instanceof Class) {
      superClassType = ((Class) superClassType).getGenericSuperclass();
    }
    Type permissionType = ((ParameterizedType) superClassType).getActualTypeArguments()[0];
    supportedPermissionType = (Class<P>) TypeToken.of(permissionType).getRawType();
  }

  public boolean isPermissionSupported(P permission) {
    return supportedPermissionType.isAssignableFrom(permission.getClass());
  }

  public Class<P> getSupportedPermissionType() {
    return supportedPermissionType;
  }

  public abstract PermissionDecision hasPermission();

}