package app.narvi.protego;

import static app.narvi.protego.PermissionDecision.Decision.ABSTAIN;
import static app.narvi.protego.PermissionDecision.Decision.DENY;
import static app.narvi.protego.PermissionDecision.Decision.PERMIT;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import app.narvi.protego.PermissionDecision.Decision;

public interface AuditProvider<P extends Permission, PR extends PolicyRule> {

  void audit(P permission, Votes<PR> votes, Set<PR> rulesSkippedFromVoting);

  class Votes<PR extends PolicyRule> implements Iterable<Vote<PR>> {

    private Set<Vote<PR>> votes = new HashSet<>();

    @Override
    public Iterator<Vote<PR>> iterator() {
      return votes.iterator();
    }

    void add(PermissionDecision permissionDecision, PR policyRule) {
      votes.add(new Vote(permissionDecision, policyRule));
    }

    void addAbstainVote(String reasonDescription, PR policyRule) {
      this.add(new PermissionDecision(ABSTAIN, reasonDescription), policyRule);
    }

    public boolean containsPolicyRule(PolicyRule policyRule) {
      for (Vote<PR> vote : votes) {
        if (vote.getPolicyRule().equals(policyRule)) {
          return true;
        }
      }
      return false;
    }

    public Set<PR> getRulesVotingDecision(Decision voteDecision) {
      Set<PR> policyRules = new HashSet<>();
      for (Vote<PR> vote : votes) {
        if (vote.getPermissionDecision().getDecision() == voteDecision) {
          policyRules.add(vote.getPolicyRule());
        }
      }
      return policyRules;
    }

    public Decision getDecision() {
      if (!getRulesVotingDecision(PERMIT).isEmpty()) {
        return PERMIT;
      }
      if (!getRulesVotingDecision(DENY).isEmpty()) {
        return DENY;
      }
      return ABSTAIN;
    }

    public Set<Vote<PR>> getVotes() {
      return Collections.unmodifiableSet(votes);
    }

  }

  class Vote<PR extends PolicyRule> {

    private PermissionDecision permissionDecision;
    private PR policyRule;

    Vote(PermissionDecision permissionDecision, PR policyRule) {
      this.permissionDecision = permissionDecision;
      this.policyRule = policyRule;
    }

    public PermissionDecision getPermissionDecision() {
      return permissionDecision;
    }

    public PR getPolicyRule() {
      return policyRule;
    }

  }

}