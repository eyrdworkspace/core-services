package org.egov.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.TestConfiguration;
import org.egov.domain.model.Message;
import org.egov.domain.model.Tenant;
import org.egov.domain.service.MessageService;
import org.egov.web.contract.NewMessagesRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(MessageController.class)
@Import(TestConfiguration.class)
public class MessageControllerTest {

    private static final String TENANT_ID = "tenant123";
    private static final String LOCALE = "mr_IN";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Test
    public void test_should_fetch_messages_for_given_locale() throws Exception {
        final List<Message> modelMessages = getModelMessages();
        when(messageService.getMessages(LOCALE, new Tenant(TENANT_ID)))
            .thenReturn(modelMessages);
         mockMvc.perform(get("/messages")
            .param("tenantId", TENANT_ID)
            .param("locale", LOCALE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().json(getFileContents("messagesResponse.json")));
    }

    @Test
    public void test_should_save_new_messages() throws Exception {
        final Message message1 = Message.builder()
          	.code("code1")
            .message("message1")
            .locale(LOCALE)
            .tenant(new Tenant(TENANT_ID))
            .build();
        final Message message2 = Message.builder()
            .code("code2")
            .message("message2")
            .locale(LOCALE)
            .tenant(new Tenant(TENANT_ID))
            .build();
        List<Message> expectedMessages = Arrays.asList(message1, message2);
        mockMvc.perform(post("/messages")
            .content(getFileContents("newMessagesRequest.json")).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().json(getFileContents("messagesResponse.json")));
        verify(messageService).createMessages(expectedMessages);
    }

    @Test
    public void
    test_should_return_bad_request_with_error_response_when_create_message_request_does_not_have_mandatory_parameters
        () throws Exception {
        mockMvc.perform(post("/messages")
            .content(getFileContents("newMessagesRequestWithMissingMandatoryParameters.json"))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().json(getFileContents("mandatoryFieldsMissingErrorResponse.json")));
    }
    
    @Test
	public void test_should_create_messages() throws Exception {
		mockMvc.perform(post("/messages/_create").content(getFileContents("createNewMessageRequest.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("createNewMessageResponse.json")));
		verify(messageService).createMessage(any(NewMessagesRequest.class));
	}
    
    @Test
	public void test_should_give_bad_request_message_when_mandatory_fields_not_available_create_messages() throws Exception {
		mockMvc.perform(post("/messages/_create").content(getFileContents("createNewMessageRequestMissingMandatoryFields.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("createNewMessageRequestMissingMandatoryFieldsResponse.json")));
	}
    
    @Test
	public void test_should_update_messages() throws Exception {
		mockMvc.perform(post("/messages/_update").content(getFileContents("createNewMessageRequest.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("createNewMessageResponse.json")));
		verify(messageService).createMessage(any(NewMessagesRequest.class));
	}
    
    @Test
	public void test_should_give_bad_request_message_when_mandatory_fields_not_available_update_messages() throws Exception {
		mockMvc.perform(post("/messages/_update").content(getFileContents("createNewMessageRequestMissingMandatoryFields.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("createNewMessageRequestMissingMandatoryFieldsResponse.json")));
	}
    

    private String getFileContents(String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader()
                .getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Message> getModelMessages() {
        final Message message1 = Message.builder()
            .code("code1")
            .message("message1")
            .tenant(new Tenant("tenant123"))
            .locale(LOCALE)
            .build();
        final Message message2 = Message.builder()
            .code("code2")
            .message("message2")
            .tenant(new Tenant("tenant123"))
            .locale(LOCALE)
            .build();
        return Arrays.asList(message1, message2);
    }
}