package reqres.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;

public final class ObjectMapperFactory {
  private static final ObjectMapper OBJECT_MAPPER =
      new ObjectMapper().findAndRegisterModules().registerModule(new BlackbirdModule());

  private ObjectMapperFactory() {}

  public static ObjectMapper getInstance() {
    return OBJECT_MAPPER;
  }
}
