package booking.web.controller;

import booking.BaseWebTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static booking.util.ResourceUtil.resourceToString;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@ContextConfiguration(classes = EventBatchUploadController.class)
public class EventBatchUploadControllerTest extends BaseWebTest {

    @Test
    public void batchUpload() throws Exception {
        String name1 = "Hall";
        String name2 = "Room";

        assertThat(eventService.getByName(name1), emptyIterable());
        assertThat(eventService.getByName(name2), emptyIterable());

        String fileContent1 = resourceToString("EventControllerTest_batchUpload_1.json", EventBatchUploadControllerTest.class);
        MockMultipartFile multipartFile1 = new MockMultipartFile(
                EventBatchUploadController.PART_NAME,
                "filename1.json",
                MediaType.APPLICATION_JSON_VALUE,
                fileContent1.getBytes()
        );

        String fileContent2 = resourceToString("EventControllerTest_batchUpload_2.json", EventBatchUploadControllerTest.class);
        MockMultipartFile multipartFile2 = new MockMultipartFile(
                EventBatchUploadController.PART_NAME,
                "filename2.json",
                MediaType.APPLICATION_JSON_VALUE,
                fileContent2.getBytes()
        );

        MockMultipartHttpServletRequestBuilder multipartBuilder = MockMvcRequestBuilders
                .fileUpload(EventBatchUploadController.BATCH_UPLOAD_ENDPOINT)
                .file(multipartFile1)
                .file(multipartFile2);

        mvc.perform(multipartBuilder).andExpect(status().isOk());

        assertThat(eventService.getByName(name1), hasSize(1));
        assertThat(eventService.getByName(name2), hasSize(2));
    }
}