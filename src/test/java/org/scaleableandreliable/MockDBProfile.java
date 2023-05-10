package org.scaleableandreliable;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class MockDBProfile implements QuarkusTestProfile {

  public Map<String, String> getConfigOverrides() {
    return Map.of("quarkus.scheduler.enabled", "false", "quarkus.datasource.jdbc.url", "none");
  }
}
