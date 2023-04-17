package api.preconditionbuilders;


import api.core.client.HttpClient;
import api.dto.GenericResponse;
import api.dto.rx.admin.user.UserDto;
import api.dto.rx.admin.user.UserRequest;
import api.dto.rx.admin.user.UserRole;
import api.services.UserService;
import configurations.User;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import zutils.FakerUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static zutils.FakerUtils.randomMail;

@Slf4j
@Getter
@AllArgsConstructor
public class UsersPrecondition {

    private Integer responseCode;
    private UserDto userResponse;
    private UserRequest userRequest;
    private GenericResponse<UserDto> userGetAllResponse;

    private UsersPrecondition(UsersPreconditionBuilder builder) {

        this.userRequest = builder.userRequest;
        this.responseCode = builder.responseCode;
        this.userResponse = builder.userResponse;
        this.userGetAllResponse = builder.userGetAllResponse;
    }

    public static UsersPreconditionBuilder user() {

        return new UsersPreconditionBuilder();
    }

    public static class UsersPreconditionBuilder {

        private Response response;
        private Integer responseCode;
        private UserDto userResponse;
        private UserRequest userRequest;
        private GenericResponse<UserDto> userGetAllResponse;
        private UserService userService = new UserService();

        public UsersPreconditionBuilder createNewUser(UserRole role) {
            this.userRequest = UserRequest.builder()
                    .name(FakerUtils.captionWithSuffix(String.format("%s_%s", "auto", role.getDefinition())))
                    .mail(randomMail())
                    .role(role.getRole())
                    .publisherId(role.getRole() == 0 ? 4 : null)
                    .password("Password1")
                    .isEnabled(true)
                    .build();

            this.response = userService.createUser(userRequest);
            this.userResponse = response.as(UserDto.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public UsersPreconditionBuilder createSinglePublisherUser(Integer publisherId) {
            this.userRequest = UserRequest.builder()
                    .name(FakerUtils.captionWithSuffix("auto_single"))
                    .mail(User.TEMP_USER.getMail())
                    .role(UserRole.SINGLE_PUBLISHER.getRole())
                    .publisherId(publisherId)
                    .password(User.TEMP_USER.getPassword())
                    .isEnabled(true)
                    .build();

            this.response = userService.createUser(userRequest);
            this.userResponse = response.as(UserDto.class);
            this.responseCode = response.getStatusCode();

            return this;
        }
        public UsersPreconditionBuilder updateUser(UserDto user) {
            this.userResponse = UserDto.builder()
                    .id(user.getId())
                    .mail(user.getMail())
                    .name(user.getName())
                    .role(user.getRole())
                    .publisherId(user.getPublisherId())
                    .publisherName(user.getPublisherName())
                    .loginId(user.getLoginId())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .isEnabled(user.getIsEnabled())
                    .build();

            this.response = userService.updateUser(userResponse);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public UsersPreconditionBuilder getUserById(int id) {
            this.response = userService.getUserById(id);
            this.userResponse = response.as(UserDto.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public UsersPreconditionBuilder getAllUsers() {
            this.response = userService.getAll();
            this.userGetAllResponse = this.response.as(new TypeRef<GenericResponse<UserDto>>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        public UsersPrecondition build() {

            return new UsersPrecondition(this);
        }

        private List<UserDto> getUserResponseList() {

            return Arrays.asList(response.jsonPath().getObject("items", UserDto[].class));
        }

        public UsersPreconditionBuilder deleteUser(int id) {
            this.response = userService.deleteUser(id);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public UsersPreconditionBuilder setCredentials(User user) {
            HttpClient.setCredentials(user);

            return this;
        }

        public UsersPreconditionBuilder getUsersWithFilter(Map<String, Object> queryParams) {
            this.response = userService.getUsersWithFilter(queryParams);

            this.userGetAllResponse = this.response.as(new TypeRef<GenericResponse<UserDto>>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }
    }
}
