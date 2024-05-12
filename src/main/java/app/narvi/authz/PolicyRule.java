package app.narvi.authz;

public interface PolicyRule {

  boolean hasPermisssion(Permission permission);

}