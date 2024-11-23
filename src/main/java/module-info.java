module app.narvi.protego.core {
  requires static org.slf4j;

  exports app.narvi.protego;
  uses app.narvi.protego.AuditProvider;
}