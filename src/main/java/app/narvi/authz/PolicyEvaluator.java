package app.narvi.authz;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.crypto.Cipher;

import app.narvi.authz.PolicyRule.Decision;

public class PolicyEvaluator {

  public static final String NEW_LINE_CHARACTER = "\n";
  public static final String PUBLIC_KEY_START_KEY_STRING = "-----BEGIN PUBLIC KEY-----";
  public static final String PUBLIC_KEY_END_KEY_STRING = "-----END PUBLIC KEY-----";
  public static final String EMPTY_STRING = "";
  public static final String NEW_CR_CHARACTER = "\r";

  private static PolicyRule rulesCollection;

  public static void registerProvider(PolicyRulesProvider policyRulesProvider) {
    if (PolicyEvaluator.rulesCollection != null) {
      throw new NullPointerException("RulesCollection has already being set.");
    }
    if (policyRulesProvider == null) {
      throw new NullPointerException("RulesCollection cannot be null.");
    }
    for(PolicyRule aPolicy : policyRulesProvider.collect()) {

    }
  }

  public static void main(String[] args) {

  }

  private void verifyRule(PolicyRule policyRule) throws Exception {
    String signature = policyRule.signature();
    byte[] decryptedSignature = decrypt(signature);
    String ruleClass = policyRule.getClass().getCanonicalName();

    MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
    sha1.reset();
    sha1.update(ruleClass.getBytes(StandardCharsets.UTF_8));
    String hashString = Base64.getEncoder().encodeToString(sha1.digest());
    System.out.println("sha1 = " + hashString);
  }

  private byte[] decrypt(String signature) throws Exception {
    //decrypt
    byte[] pubKeyBytes = Files.readAllBytes(Paths.get("publicKey.pub"));
    String publicKeyString = new String(pubKeyBytes, StandardCharsets.UTF_8);
    publicKeyString = publicKeyString.replaceAll(NEW_LINE_CHARACTER, EMPTY_STRING)
        .replaceAll(PUBLIC_KEY_START_KEY_STRING, EMPTY_STRING)
        .replaceAll(PUBLIC_KEY_END_KEY_STRING, EMPTY_STRING)
        .replaceAll(NEW_CR_CHARACTER, EMPTY_STRING);

    byte[] decoded = Base64
        .getDecoder()
        .decode(publicKeyString);

    KeySpec keySpec = new X509EncodedKeySpec(decoded);

    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey publicKey = keyFactory.generatePublic(keySpec);

    final Cipher cipher2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher2.init(Cipher.DECRYPT_MODE, publicKey);

    return cipher2.doFinal(signature.getBytes(StandardCharsets.UTF_8));
  }

  public static Decision evaluate(Permission permission) {
    if(rulesCollection == null) {
      throw new IllegalStateException("RulesCollection has not being initialized.");
    }
   return PolicyRule.Decision.NOT_APPLICABLE;
  }

  static {
    ServiceLoader<AuditProvider> auditLoader = ServiceLoader.load(AuditProvider.class);
    auditLoader.forEach(AuditServices::addProvider);
  }

}