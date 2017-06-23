package org.egov.domain.service;


import org.egov.domain.model.Message;
import org.egov.domain.model.MessageIdentity;
import org.egov.domain.model.Tenant;
import org.egov.persistence.repository.MessageCacheRepository;
import org.egov.persistence.repository.MessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {
    private static final String ENGLISH_INDIA = "en_IN";
    private static final String TENANT_ID = "tenant_123";
    private static final String MR_IN = "mr_IN";

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageCacheRepository messageCacheRepository;

    @InjectMocks
    private MessageService messageService;


    @Test
    public void test_should_augment_messages_for_given_tenant_with_non_overridden_default_messages() {
        String tenantId = "a";
        final Tenant defaultTenant = new Tenant(Tenant.DEFAULT_TENANT);
        final MessageIdentity messageIdentity1 = MessageIdentity.builder()
            .code("code1")
            .locale(ENGLISH_INDIA)
            .module("module")
            .tenant(defaultTenant)
            .build();
        Message defaultMessage1 = Message.builder()
            .messageIdentity(messageIdentity1)
            .message("default message1")
            .build();
        List<Message> defaultEnglishMessages = Collections.singletonList(defaultMessage1);
        final MessageIdentity messageIdentity2 = MessageIdentity.builder()
            .code("code2")
            .locale(MR_IN)
            .module("module")
            .tenant(new Tenant("a"))
            .build();
        Message tenantMessage1 = Message.builder()
            .messageIdentity(messageIdentity2)
            .message("marathi message for tenant a")
            .build();
        List<Message> marathiMessagesForGivenTenant = Collections.singletonList(tenantMessage1);
        when(messageRepository.findByTenantIdAndLocale(new Tenant("default"), ENGLISH_INDIA))
            .thenReturn(defaultEnglishMessages);
        when(messageRepository.findByTenantIdAndLocale(new Tenant("a"), MR_IN))
            .thenReturn(marathiMessagesForGivenTenant);
        when(messageCacheRepository.getMessages(anyString(), any())).thenReturn(null);
        when(messageCacheRepository.getComputedMessages(anyString(), any())).thenReturn(null);

        List<Message> actualMessages = messageService.getMessages(MR_IN, new Tenant(tenantId));

        assertEquals(2, actualMessages.size());
        assertEquals("code1", actualMessages.get(0).getCode());
        assertEquals("code2", actualMessages.get(1).getCode());
    }

    @Test
    public void test_should_cache_computed_messages_post_computation() {
        String tenantId = "a";

        final Tenant defaultTenant = new Tenant(Tenant.DEFAULT_TENANT);
        final MessageIdentity messageIdentity1 = MessageIdentity.builder()
            .code("code1")
            .locale(ENGLISH_INDIA)
            .module("module")
            .tenant(defaultTenant)
            .build();
        Message defaultMessage1 = Message.builder()
            .messageIdentity(messageIdentity1)
            .message("default message1")
            .build();
        List<Message> defaultEnglishMessages = Collections.singletonList(defaultMessage1);
        when(messageRepository.findByTenantIdAndLocale(new Tenant("default"), ENGLISH_INDIA))
            .thenReturn(defaultEnglishMessages);
        when(messageRepository.findByTenantIdAndLocale(new Tenant("a"), MR_IN))
            .thenReturn(Collections.emptyList());
        when(messageCacheRepository.getMessages(anyString(), any())).thenReturn(null);
        when(messageCacheRepository.getComputedMessages(anyString(), any())).thenReturn(null);

        messageService.getMessages(MR_IN, new Tenant(tenantId));

        verify(messageCacheRepository).cacheComputedMessages(MR_IN, new Tenant(tenantId), defaultEnglishMessages);
    }

    @Test
    public void test_should_cache_messages_for_given_tenant_and_locale_post_data_store_retrieval() {
        String tenantId = "a";

        final Tenant defaultTenant = new Tenant(Tenant.DEFAULT_TENANT);
        final MessageIdentity messageIdentity1 = MessageIdentity.builder()
            .code("code1")
            .locale(ENGLISH_INDIA)
            .module("module")
            .tenant(defaultTenant)
            .build();
        Message defaultMessage1 = Message.builder()
            .messageIdentity(messageIdentity1)
            .message("default message1")
            .build();
        List<Message> defaultEnglishMessages = Collections.singletonList(defaultMessage1);
        when(messageRepository.findByTenantIdAndLocale(new Tenant("default"), ENGLISH_INDIA))
            .thenReturn(defaultEnglishMessages);
        final List<Message> tenantSpecificMessages = Collections.emptyList();
        when(messageRepository.findByTenantIdAndLocale(new Tenant("a"), MR_IN))
            .thenReturn(tenantSpecificMessages);
        when(messageCacheRepository.getMessages(anyString(), any())).thenReturn(null);
        when(messageCacheRepository.getComputedMessages(anyString(), any())).thenReturn(null);

        messageService.getMessages(MR_IN, new Tenant(tenantId));

        verify(messageCacheRepository).cacheMessages(ENGLISH_INDIA, new Tenant("default"), defaultEnglishMessages);
        verify(messageCacheRepository).cacheMessages(MR_IN, new Tenant("default"), tenantSpecificMessages);
        verify(messageCacheRepository).cacheMessages(MR_IN, new Tenant("a"), tenantSpecificMessages);
    }

    @Test
    public void test_should_get_messages_with_precedence_based_on_tenant_hierarchy() {
        String tenantId = "a.b.c";

        final Tenant defaultTenant = new Tenant(Tenant.DEFAULT_TENANT);
        final MessageIdentity messageIdentity1 = MessageIdentity.builder()
            .code("code1")
            .locale(ENGLISH_INDIA)
            .module("module")
            .tenant(defaultTenant)
            .build();
        Message defaultMessage1 = Message.builder()
            .messageIdentity(messageIdentity1)
            .message("default message1")
            .build();
        final MessageIdentity messageIdentity2 = MessageIdentity.builder()
            .code("code2")
            .locale(ENGLISH_INDIA)
            .module("module")
            .tenant(defaultTenant)
            .build();
        Message defaultMessage2 = Message.builder()
            .messageIdentity(messageIdentity2)
            .message("default message2")
            .build();
        final MessageIdentity messageIdentity3 = MessageIdentity.builder()
            .code("code3")
            .locale(ENGLISH_INDIA)
            .module("module")
            .tenant(defaultTenant)
            .build();
        Message defaultMessage3 = Message.builder()
            .messageIdentity(messageIdentity3)
            .message("default message3")
            .build();
        List<Message> defaultEnglishMessages = Arrays.asList(defaultMessage1, defaultMessage2, defaultMessage3);
        final MessageIdentity messageIdentity4 = MessageIdentity.builder()
            .code("code1")
            .locale(MR_IN)
            .module("module")
            .tenant(new Tenant("a.b.c"))
            .build();
        Message tenantMessage1 = Message.builder()
            .messageIdentity(messageIdentity4)
            .message("marathi message for tenant a.b.c")
            .build();
        final MessageIdentity messageIdentity5 = MessageIdentity.builder()
            .code("code5")
            .locale(MR_IN)
            .module("module")
            .tenant(new Tenant("a.b.c"))
            .build();
        Message tenantMessage2 = Message.builder()
            .messageIdentity(messageIdentity5)
            .message("marathi message for tenant a.b.c")
            .build();
        List<Message> marathiMessagesForGivenTenant = Arrays.asList(tenantMessage1, tenantMessage2);
        final MessageIdentity messageIdentity6 = MessageIdentity.builder()
            .code("code2")
            .locale(MR_IN)
            .module("module")
            .tenant(new Tenant("a.b"))
            .build();
        Message tenantParentMessage1 = Message.builder()
            .messageIdentity(messageIdentity6)
            .message("marathi message for tenant a.b")
            .build();
        final MessageIdentity messageIdentity7 = MessageIdentity.builder()
            .code("code4")
            .locale(MR_IN)
            .module("module")
            .tenant(new Tenant("a.b"))
            .build();
        Message tenantParentMessage2 = Message.builder()
            .messageIdentity(messageIdentity7)
            .message("marathi message for tenant a.b")
            .build();
        List<Message> marathiMessagesForTenantParent = Arrays.asList(tenantParentMessage1, tenantParentMessage2);

        when(messageRepository.findByTenantIdAndLocale(new Tenant("default"), ENGLISH_INDIA))
            .thenReturn(defaultEnglishMessages);
        when(messageRepository.findByTenantIdAndLocale(new Tenant("a.b.c"), MR_IN))
            .thenReturn(marathiMessagesForGivenTenant);
        when(messageRepository.findByTenantIdAndLocale(new Tenant("a.b"), MR_IN))
            .thenReturn(marathiMessagesForTenantParent);
        when(messageRepository.findByTenantIdAndLocale(new Tenant("a"), MR_IN))
            .thenReturn(Collections.emptyList());
        when(messageCacheRepository.getMessages(anyString(), any())).thenReturn(null);
        when(messageCacheRepository.getComputedMessages(anyString(), any())).thenReturn(null);

        List<Message> actualMessages = messageService.getMessages(MR_IN, new Tenant(tenantId));

        assertEquals(5, actualMessages.size());
        assertEquals("code1", actualMessages.get(0).getCode());
        assertEquals("marathi message for tenant a.b.c", actualMessages.get(0).getMessage());
        assertEquals("code2", actualMessages.get(1).getCode());
        assertEquals("marathi message for tenant a.b", actualMessages.get(1).getMessage());
        assertEquals("code3", actualMessages.get(2).getCode());
        assertEquals("default message3", actualMessages.get(2).getMessage());
        assertEquals("code4", actualMessages.get(3).getCode());
        assertEquals("marathi message for tenant a.b", actualMessages.get(3).getMessage());
        assertEquals("code5", actualMessages.get(4).getCode());
        assertEquals("marathi message for tenant a.b.c", actualMessages.get(4).getMessage());
    }

    @Test
    public void test_should_return_computed_messages_from_cache_when_present() {
        String tenantId = "a.b.c";
        final Tenant defaultTenant = new Tenant(Tenant.DEFAULT_TENANT);
        final MessageIdentity messageIdentity1 = MessageIdentity.builder()
            .code("code1")
            .locale(ENGLISH_INDIA)
            .module("module")
            .tenant(defaultTenant)
            .build();
        Message defaultMessage1 = Message.builder()
            .messageIdentity(messageIdentity1)
            .message("default message1")
            .build();
        final MessageIdentity messageIdentity2 = MessageIdentity.builder()
            .code("code2")
            .locale(ENGLISH_INDIA)
            .module("module")
            .tenant(defaultTenant)
            .build();
        Message defaultMessage2 = Message.builder()
            .messageIdentity(messageIdentity2)
            .message("default message2")
            .build();
        List<Message> expectedMessages = Arrays.asList(defaultMessage1, defaultMessage2);
        when(messageCacheRepository.getComputedMessages(MR_IN, new Tenant(tenantId)))
            .thenReturn(expectedMessages);

        List<Message> actualMessages = messageService.getMessages(MR_IN, new Tenant(tenantId));

        assertEquals(2, actualMessages.size());
    }

    @Test
    public void test_should_return_messages_from_cache_when_present() {
        String tenantId = "a";
        final Tenant defaultTenant = new Tenant(Tenant.DEFAULT_TENANT);
        final MessageIdentity messageIdentity1 = MessageIdentity.builder()
            .code("code1")
            .locale(ENGLISH_INDIA)
            .module("module")
            .tenant(new Tenant("a"))
            .build();
        Message defaultMessage1 = Message.builder()
            .messageIdentity(messageIdentity1)
            .message("default message1")
            .build();
        List<Message> defaultEnglishMessages = Collections.singletonList(defaultMessage1);
        final MessageIdentity messageIdentity2 = MessageIdentity.builder()
            .code("code2")
            .locale(MR_IN)
            .module("module")
            .tenant(new Tenant("a"))
            .build();
        Message tenantMessage1 = Message.builder()
            .messageIdentity(messageIdentity2)
            .message("marathi message for tenant a")
            .build();
        List<Message> marathiMessagesForGivenTenant = Collections.singletonList(tenantMessage1);
        when(messageCacheRepository.getMessages(MR_IN, new Tenant("a")))
            .thenReturn(marathiMessagesForGivenTenant);
        when(messageCacheRepository.getMessages(ENGLISH_INDIA, new Tenant("default")))
            .thenReturn(defaultEnglishMessages);
        when(messageCacheRepository.getComputedMessages(anyString(), any())).thenReturn(null);

        List<Message> actualMessages = messageService.getMessages(MR_IN, new Tenant(tenantId));

        assertEquals(2, actualMessages.size());
        assertEquals("code1", actualMessages.get(0).getCode());
        assertEquals("code2", actualMessages.get(1).getCode());
    }

    @Test
    public void test_should_save_messages() {
        List<Message> modelMessages = getMessages();

        messageService.create(modelMessages);

        verify(messageRepository).save(modelMessages);
    }
    
    private List<Message> getMessages() {
        final MessageIdentity messageIdentity1 = MessageIdentity.builder()
            .code("core.msg.OTPvalidated")
            .locale(MR_IN)
            .module("module")
            .tenant(new Tenant(TENANT_ID))
            .build();
        Message message1 = Message.builder()
            .messageIdentity(messageIdentity1)
            .message("OTP यशस्वीपणे प्रमाणित")
            .build();
        final MessageIdentity messageIdentity2 = MessageIdentity.builder()
            .code("core.lbl.imageupload")
            .locale(MR_IN)
            .module("module")
            .tenant(new Tenant(TENANT_ID))
            .build();
        Message message2 = Message.builder()
            .messageIdentity(messageIdentity2)
            .message("प्रतिमा यशस्वीरित्या अपलोड")
            .build();
        final MessageIdentity messageIdentity3 = MessageIdentity.builder()
            .code("core.msg.entermobileno")
            .locale(ENGLISH_INDIA)
            .module("module")
            .tenant(new Tenant(TENANT_ID))
            .build();
        Message message3 = Message.builder()
            .messageIdentity(messageIdentity3)
            .message("EnterMobileNumber")
            .build();
        final MessageIdentity messageIdentity4 = MessageIdentity.builder()
            .code("core.msg.enterfullname")
            .locale(ENGLISH_INDIA)
            .module("module")
            .tenant(new Tenant(TENANT_ID))
            .build();
        Message message4 = Message.builder()
            .messageIdentity(messageIdentity4)
            .message("Enter fullname")
            .build();

        return (Arrays.asList(message1, message2, message3, message4));
    }

}