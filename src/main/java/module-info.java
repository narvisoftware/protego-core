module app.narvi.protego.core {
  requires static org.slf4j;

  exports app.narvi.authz;
  uses app.narvi.authz.AuditProvider;
}