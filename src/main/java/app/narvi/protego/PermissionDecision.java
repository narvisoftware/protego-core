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
    if(decision == null) {
      throw new NullPointerException("The chosen decision cannot be null!");
    }
    this.decision = decision;
    this.reasonDescription = reasonDescription;
  }

  public static PermissionDecisionBuilder with(Decision decision) {
    return new PermissionDecisionBuilder(decision);
  }

  public Decision getDecision() {
    return decision;
  }

  public String getReasonDescription() {
    return reasonDescription;
  }

  public static class PermissionDecisionBuilder {
    private Decision decision;

    private PermissionDecisionBuilder(Decision decision) {
      this.decision = decision;
    }

    public PermissionDecision reason(String reasonDescription) {
      return new PermissionDecision(decision, reasonDescription);
    }

  }

}
