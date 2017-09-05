package com.omer.restDemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omer.restDemo.rest.model.Message;
import com.omer.restDemo.rest.model.Person;
import com.omer.restDemo.rest.repository.MessageRepository;
import com.omer.restDemo.rest.repository.PersonRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestDemoApplication.class)
@WebAppConfiguration
public class RestDemoApplicationTests {

    @Rule
    public JUnitRestDocumentation jUnitRestDocumentation = new JUnitRestDocumentation("target/snippets");

    private MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private Person senderOne, senderTwo, receiverOne, receiverTwo;

    private Message messageOne, messageTwo;

    private List<Person> persons;
    private List<Message> messages;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    private RestDocumentationResultHandler docHandler;


    @Before
    public void setUp() throws Exception {
        this.docHandler = document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()));

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(jUnitRestDocumentation))
                .alwaysDo(this.docHandler)
                .build();


        messageRepository.deleteAll();
        personRepository.deleteAll();

        senderOne = new Person("syed", "khureshi");
        senderTwo = new Person("omer", "salar");

        receiverOne = new Person("Sai", "Undurthi");
        receiverTwo = new Person("Anshul", "Vyas");

        personRepository.save(senderOne);
        personRepository.save(senderTwo);
        personRepository.save(receiverOne);
        personRepository.save(receiverTwo);

        messageOne = new Message(senderOne, receiverOne, "This is message one");
        messageTwo = new Message(senderTwo, receiverTwo, "This is message tow");

        messageRepository.save(messageOne);
        messageRepository.save(messageTwo);
    }


    /**
     * Test for PersonController.getPerson method.
     *
     * @throws Exception
     */
    @Test
    public void testGetPersons() throws Exception {
        System.out.println();
        System.out.println(mockMvc.perform(get("/persons")).andReturn().getResponse().getContentAsString());
        System.out.println();

        mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0]['person']['firstName']", is("syed")))
                .andExpect(jsonPath("$[0]['person']['lastName']", is("khureshi")))
                .andExpect(jsonPath("$[3]['person']['firstName']", is("Anshul")))
                .andExpect(jsonPath("$[3]['person']['lastName']", is("Vyas")))
                .andDo(this.docHandler.document(
                        responseHeaders(
                                headerWithName("Content-Type").description("The Content-Type of the payload, e.g. `application/json`")
                        )
                        // responseFields(
                        //         fieldWithPath("[].person.id").type(JsonFieldType.NUMBER).description("Id of the person."),
                        //         fieldWithPath("[].person.firstName").type(JsonFieldType.STRING).description("First name of the person."),
                        //         fieldWithPath("[].person.lastName").type(JsonFieldType.STRING).description("Last name of the person."),
                        //         subsectionWithPath("[].links").type(JsonFieldType.ARRAY).description("Links to resources.")
                        // ),
                        // links(
                        //         linkWithRel("self").description("Link to the person."),
                        //         linkWithRel("from-messages").description("Link to list of all sent messages."),
                        //         linkWithRel("to-messages").description("Link to list of all received messages."))
                ));
    }

    /**
     * Test for PersonController.getPersons method.
     *
     * @throws Exception
     */
    @Test
    public void testGetPerson() throws Exception {

        System.out.println();
        System.out.println(senderOne.getId());
        System.out.println();
        // mockMvc.perform(get("/persons/{senderId}/", senderOne.getId()))
        mockMvc
                .perform(
                        RestDocumentationRequestBuilders.get("/persons/{id}/", senderOne.getId())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$['person']['firstName']", is(senderOne.getFirstName())))
                .andExpect(jsonPath("$['person']['lastName']", is(senderOne.getLastName())))
                .andDo(this.docHandler.document(
                        pathParameters(
                                parameterWithName("id").description("Id of the person")
                        ),
                        responseFields(
                                subsectionWithPath("person").type(JsonFieldType.OBJECT).description("The person object."),
                                fieldWithPath("person.id").type(JsonFieldType.NUMBER).description("Id of the person."),
                                fieldWithPath("person.firstName").type(JsonFieldType.STRING).description("First name of the person."),
                                fieldWithPath("person.lastName").type(JsonFieldType.STRING).description("Last name of the person."),
                                subsectionWithPath("links").type(JsonFieldType.ARRAY).description("Links to resources.")
                        ),
                        links(
                                linkWithRel("self").description("Link to the person."),
                                linkWithRel("from-messages").description("Link to list of all sent messages."),
                                linkWithRel("to-messages").description("Link to list of all received messages.")
                        )));

    }

    /**4
     * This method tests PersonController.createPerson method.
     *
     * @throws Exception
     */
    @Test
    public void testCreatePerson() throws Exception {
        Person person = new Person("person", "human");
        mockMvc.perform(
                post("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$['person']['firstName']", is(person.getFirstName())))
                .andExpect(jsonPath("$['person']['lastName']", is(person.getLastName())))
                .andDo(this.docHandler.document(
                        requestFields(
                                // subsectionWithPath("$.*").type(JsonFieldType.OBJECT).description("The person to be created."),
                                fieldWithPath("id").type(JsonFieldType.NULL).description("Auto generated dd of person. Leave this null in request."),
                                fieldWithPath("firstName").type(JsonFieldType.STRING).description("First name of person."),
                                fieldWithPath("lastName").type(JsonFieldType.STRING).description("Last name of person.")
                        ),
                        responseFields(
                                subsectionWithPath("person").type(JsonFieldType.OBJECT).description("The person object just created."),
                                fieldWithPath("person.id").type(JsonFieldType.NUMBER).description("Id of the created person."),
                                fieldWithPath("person.firstName").type(JsonFieldType.STRING).description("First name of the created person."),
                                fieldWithPath("person.lastName").type(JsonFieldType.STRING).description("Last name of the created person."),
                                subsectionWithPath("links").type(JsonFieldType.ARRAY).description("Links to resources.")
                        ),
                        links(
                                linkWithRel("self").description("Link to the created person."),
                                linkWithRel("from-messages").description("Link to list of all sent messages."),
                                linkWithRel("to-messages").description("Link to list of all received messages.")
                        )));

    }

    /**
     * This method tests MessageController.getMessages method.
     *
     * @throws Exception
     */
    @Test
    public void testGetMessages() throws Exception {

        System.out.println();
        System.out.println(mockMvc.perform(get("/messages")).andReturn().getResponse().getContentAsString());
        System.out.println();


        mockMvc.perform(get("/messages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]['message']['messageDesc']", is(messageOne.getMessageDesc())))
                .andExpect(jsonPath("$[0]['message']['sender']['id']", is(messageOne.getSender().getId().intValue())))
                .andExpect(jsonPath("$[0]['message']['receiver']['id']", is(messageOne.getReceiver().getId().intValue())))
                .andDo(this.docHandler.document(
                        // responseFields(
                        //         subsectionWithPath("message").type(JsonFieldType.OBJECT).description("The message object."),
                        //         fieldWithPath("message.messageDesc").type(JsonFieldType.NUMBER).description("The textual message."),
                        //         subsectionWithPath("message.sender").type(JsonFieldType.ARRAY).description("The person object representing the sender of this message."),
                        //         subsectionWithPath("message.receiver").type(JsonFieldType.ARRAY).description("The person object representing the receiver of this message."),
                        //         subsectionWithPath("links").type(JsonFieldType.ARRAY).description("Links to resources.")
                        // ),
                        // links(
                        //         linkWithRel("self").description("Link to the this message."),
                        //         linkWithRel("sender").description("Link to the person object representing the sender of this message. "),
                        //         linkWithRel("receiver").description("Link to the person object representing the receiver of this message. ")
                ));
    }


    /**
     * This method tests MessageController.getMessages method.
     *
     * @throws Exception
     */
    @Test
    public void testGetMessage() throws Exception {

        // mockMvc.perform(get("/messages/{messageId}", messageOne.getId()))
        mockMvc.perform(RestDocumentationRequestBuilders.get("/messages/{messageId}", messageOne.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$['message']['messageDesc']", is(messageOne.getMessageDesc())))
                .andExpect(jsonPath("$['message']['sender']['id']", is(messageOne.getSender().getId().intValue())))
                .andExpect(jsonPath("$['message']['receiver']['id']", is(messageOne.getReceiver().getId().intValue())))
                .andDo(this.docHandler.document(
                        pathParameters(
                                parameterWithName("messageId").description("Id of the message")
                        ),
                        responseFields(
                                subsectionWithPath("message").type(JsonFieldType.OBJECT).description("The message object."),
                                subsectionWithPath("message.id").type(JsonFieldType.NUMBER).description("Id of this message."),
                                fieldWithPath("message.messageDesc").type(JsonFieldType.STRING).description("The textual message."),
                                subsectionWithPath("message.sender").type(JsonFieldType.OBJECT).description("The person object representing the sender of this message."),
                                subsectionWithPath("message.receiver").type(JsonFieldType.OBJECT).description("The person object representing the receiver of this message."),
                                subsectionWithPath("links").type(JsonFieldType.ARRAY).description("Links to resources.")
                        ),
                        links(
                                linkWithRel("self").description("Link to the this message."),
                                linkWithRel("sender").description("Link to the person object representing the sender of this message. "),
                                linkWithRel("receiver").description("Link to the person object representing the receiver of this message. ")
                        )));

    }


    /**
     * This method tests the MessageController.createMessage method.
     *
     * @throws Exception
     */
    @Test
    public void testCreateMessage() throws Exception {
        Message message = new Message(senderOne, receiverTwo, "Message from senderOne to receiverTwo.");

        mockMvc.perform(
                post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$['message']['sender']['id']", is(message.getSender().getId().intValue())))
                .andExpect(jsonPath("$['message']['receiver']['id']", is(message.getReceiver().getId().intValue())))
                .andExpect(jsonPath("$['message']['messageDesc']", is(message.getMessageDesc())))
                .andDo(this.docHandler.document(
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NULL).description("Auto generated dd of person. Leave this null in request."),
                                subsectionWithPath("sender").type(JsonFieldType.OBJECT).description("The person object representing the sender of this message."),
                                fieldWithPath("sender.id").type(JsonFieldType.NUMBER).description("Id of the sender."),
                                fieldWithPath("sender.firstName").type(JsonFieldType.STRING).description("First name of the sender."),
                                fieldWithPath("sender.lastName").type(JsonFieldType.STRING).description("Last name of the sender."),
                                subsectionWithPath("receiver").type(JsonFieldType.OBJECT).description("The person object representing the receiver of this message."),
                                fieldWithPath("receiver.id").type(JsonFieldType.NUMBER).description("Id of the receiver."),
                                fieldWithPath("receiver.firstName").type(JsonFieldType.STRING).description("First name of the receiver."),
                                fieldWithPath("receiver.lastName").type(JsonFieldType.STRING).description("Last name of the receiver."),
                                fieldWithPath("messageDesc").type(JsonFieldType.STRING).description("The textual message.")
                        ),
                        responseFields(
                                subsectionWithPath("message").type(JsonFieldType.OBJECT).description("The message object that was just created."),
                                fieldWithPath("message.messageDesc").type(JsonFieldType.STRING).description("The textual message."),
                                subsectionWithPath("message.sender").type(JsonFieldType.OBJECT).description("The person object representing the sender of this message."),
                                subsectionWithPath("message.receiver").type(JsonFieldType.OBJECT).description("The person object representing the receiver of this message."),
                                subsectionWithPath("links").type(JsonFieldType.ARRAY).description("Links to resources.")
                        ),
                        links(
                                linkWithRel("self").description("Link to the this message."),
                                linkWithRel("sender").description("Link to the person object representing the sender of this message. "),
                                linkWithRel("receiver").description("Link to the person object representing the receiver of this message. ")
                        )));

    }


    /**
     * This method tests MessageController.getMessagesFrom method.
     *
     * @throws Exception
     */
    @Test
    public void testGetMessagesFrom() throws Exception {

        mockMvc.perform(get("/messages?senderId={id}", senderOne.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]['message']['messageDesc']", is(messageOne.getMessageDesc())))
                .andExpect(jsonPath("$[0]['message']['sender']['id']", is(messageOne.getSender().getId().intValue())))
                .andExpect(jsonPath("$[0]['message']['receiver']['id']", is(messageOne.getReceiver().getId().intValue())))
                .andDo(this.docHandler.document(
                        requestParameters(
                                parameterWithName("senderId").description("Id of the sender.")
                        )
                        // ,
                        // responseFields(
                        //         subsectionWithPath("[].message").type(JsonFieldType.OBJECT).description("The message object."),
                        //         fieldWithPath("[].message.messageDesc").type(JsonFieldType.STRING).description("The textual message."),
                        //         subsectionWithPath("[].message.sender").type(JsonFieldType.OBJECT).description("The person object representing the sender of this message."),
                        //         subsectionWithPath("[].message.receiver").type(JsonFieldType.OBJECT).description("The person object representing the receiver of this message."),
                        //         subsectionWithPath("[].links").type(JsonFieldType.ARRAY).description("Links to resources.")
                        // ),
                        // links(
                        //         linkWithRel("self").description("Link to the this message."),
                        //         linkWithRel("sender").description("Link to the person object representing the sender of this message. "),
                        //         linkWithRel("receiver").description("Link to the person object representing the receiver of this message. "))
                ));

    }


    /**
     * This method tests MessageController.getMessagesTo method.
     *
     * @throws Exception
     */
    @Test
    public void testGetMessagesTo() throws Exception {
        mockMvc.perform(get("/messages?receiverId={id}", receiverOne.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]['message']['messageDesc']", is(messageOne.getMessageDesc())))
                .andExpect(jsonPath("$[0]['message']['sender']['id']", is(messageOne.getSender().getId().intValue())))
                .andExpect(jsonPath("$[0]['message']['receiver']['id']", is(messageOne.getReceiver().getId().intValue())))
                .andDo(this.docHandler.document(
                        requestParameters(
                                parameterWithName("receiverId").description("Id of the receiver.")
                        )
                        // ,
                        // responseFields(
                        //         subsectionWithPath("[0].message").type(JsonFieldType.OBJECT).description("The message object."),
                        //         fieldWithPath("[0].message.id").type(JsonFieldType.NUMBER).description("Id of this message."),
                        //         fieldWithPath("[0].message.messageDesc").type(JsonFieldType.STRING).description("The textual message."),
                        //         subsectionWithPath("[0].message.sender").type(JsonFieldType.OBJECT).description("The person object representing the sender of this message."),
                        //         subsectionWithPath("[0].message.receiver").type(JsonFieldType.OBJECT).description("The person object representing the receiver of this message."),
                        //         subsectionWithPath("[0].links").type(JsonFieldType.ARRAY).description("Links to resources.")
                        // ),
                        // links(
                        //         linkWithRel("self").description("Link to the this message."),
                        //         linkWithRel("sender").description("Link to the person object representing the sender of this message. "),
                        //         linkWithRel("receiver").description("Link to the person object representing the receiver of this message. ")
                        // )
                ));

    }


    /**
     * This method tests MessageController.getMessagesFromAndTo method.
     *
     * @throws Exception
     */
    @Test
    public void testGetMessagesFromAndTo() throws Exception {
        mockMvc.perform(get("/messages?senderId={senderId}&receiverId={receiverId}", senderOne.getId(), receiverOne.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]['message']['messageDesc']", is(messageOne.getMessageDesc())))
                .andExpect(jsonPath("$[0]['message']['sender']['id']", is(messageOne.getSender().getId().intValue())))
                .andExpect(jsonPath("$[0]['message']['receiver']['id']", is(messageOne.getReceiver().getId().intValue())))
                .andDo(this.docHandler.document(
                        requestParameters(
                                parameterWithName("senderId").description("Id of the sender."),
                                parameterWithName("receiverId").description("Id of the receiver.")
                        )
                        // ,
                        // responseFields(
                        //         subsectionWithPath("message").type(JsonFieldType.OBJECT).description("The message object."),
                        //         fieldWithPath("message.messageDesc").type(JsonFieldType.STRING).description("The textual message."),
                        //         subsectionWithPath("message.sender").type(JsonFieldType.OBJECT).description("The person object representing the sender of this message."),
                        //         subsectionWithPath("message.receiver").type(JsonFieldType.OBJECT).description("The person object representing the receiver of this message."),
                        //         subsectionWithPath("links").type(JsonFieldType.ARRAY).description("Links to resources.")
                        // ),
                        // links(
                        //         linkWithRel("self").description("Link to the this message."),
                        //         linkWithRel("sender").description("Link to the person object representing the sender of this message. "),
                        //         linkWithRel("receiver").description("Link to the person object representing the receiver of this message. ")
                        // )
                ));


    }

}
