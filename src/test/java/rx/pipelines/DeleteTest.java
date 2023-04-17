package rx.pipelines;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.admin.user.UserDto;
import api.dto.rx.inventory.adspot.AdSpot;
import api.dto.rx.inventory.media.Media;
import api.dto.rx.protection.Protection;
import api.dto.rx.yield.openpricing.OpenPricing;
import api.preconditionbuilders.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import rx.BaseTest;
import zutils.ObjectMapperUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static configurations.User.USER_FOR_DELETION;

@Slf4j
public class DeleteTest extends BaseTest {

    @Test(priority = 1)
    public void deleteProtections() {
       DevicePrecondition.device().getDeviceLList().build();
    }

}
