package reqres.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URL;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ListUsers {
  private Integer page;
  private Integer per_page;
  private Integer total;
  private Integer total_pages;
  private List<UserData> data;
  private Support support;

  public static ListUsers fromResponse(String listUsersAsString) throws JsonProcessingException {
    return ObjectMapperFactory.getInstance().readValue(listUsersAsString, ListUsers.class);
  }

  @Data
  public static class Support {
    private URL url;
    private String text;
  }

  @Data
  public static class UserData {
    private Integer id;
    private String email;
    private String first_name;
    private String last_name;
    private URL avatar;
  }
}
