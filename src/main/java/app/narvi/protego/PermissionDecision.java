package app.narvi.protego;

public class PermissionDecision {

  public enum Decision {
    PERMIT,
    DENY,
    ABSTAIN
  }

  private Decision decision;
  private String reasonDescription;

  public PermissionDecision(Decision decision, String reasonDescription) {
    this.decision = decision;
    this.reasonDescription = reasonDescription;
  }

  public Decision getDecision() {
    return decision;
  }

  public String getReasonDescription() {
    return reasonDescription;
  }

}
