package api.preconditionbuilders;

import api.core.client.HttpClient;
import api.dto.GenericResponse;
import api.dto.rx.device.Device;
import api.services.DeviceService;
import configurations.User;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public class DevicePrecondition {

    private Integer responseCode;
    private GenericResponse<Device> deviceGetAllResponse;

    private DevicePrecondition(DevicePreconditionBuilder builder) {
        this.responseCode = builder.responseCode;
        this.deviceGetAllResponse = builder.deviceGetAllResponse;
    }

    public static DevicePreconditionBuilder device() {

        return new DevicePreconditionBuilder();
    }

    public static class DevicePreconditionBuilder {

        private Response response;
        private Integer responseCode;
        private GenericResponse<Device> deviceGetAllResponse;
        private DeviceService openPricingService = new DeviceService();



        public DevicePreconditionBuilder getDeviceLList() {
            this.response = openPricingService.getAll();

            this.deviceGetAllResponse = this.response.as(new TypeRef<GenericResponse<Device>>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        public DevicePreconditionBuilder setCredentials(User user) {
            HttpClient.setCredentials(user);

            return this;
        }

        public DevicePrecondition build() {

            return new DevicePrecondition(this);
        }
    }
}
