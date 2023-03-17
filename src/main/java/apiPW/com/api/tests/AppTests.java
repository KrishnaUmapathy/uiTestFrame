package apiPW.com.api.tests;



import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.TestRunner;
import org.testng.annotations.BeforeTest;

import com.github.javafaker.Faker;
import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

import apiPW.com.api.factory.BaseTest;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.node.ArrayNode;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.node.ObjectNode;

public class AppTests {
	
	
	BaseTest bT = new BaseTest();
	
	
	String loginToken;
	String sharedXref;
	String agreementNo;
	Faker faker = new Faker();
	protected APIRequestContext apiRequestContext;
	protected Playwright playwright;
	Random rand = new Random();
	double premium;
	

@Before
public  void setup() 
        {
    playwright = Playwright.create();
  
	}


	
	
	@Given("environment is loaded")
	public APIRequestContext setUp()
	{
	Map<String, String> headers= new HashMap<>();
	headers.put("Content-Type", "application/json");
	apiRequestContext = playwright.request().newContext(new APIRequest.NewContextOptions()
						.setBaseURL("https://cle-api-pf-demo.services.staging.premfina.com")
						.setExtraHTTPHeaders(headers));

	return apiRequestContext;
		
	}
	
	
	@Given("ping the environment and status should be success")
	public void pingEnv()
	{
		
		int pingResponseCode = apiRequestContext.get("/ping").status();
		Assert.assertEquals(200,pingResponseCode);
		System.out.println("Status code is "+pingResponseCode);
	}

	
	
	@When("user logs into API with valid credentials and login is success")
	public String Login()
	{
				
		Map<String, String> payLoadComment = new HashMap<>();
		payLoadComment.put("user","PREMFINA_API" );
		payLoadComment.put("password", "mdB}M8R+auZ+~g}Z");
		loginToken = 	apiRequestContext.post("/login",RequestOptions.create().setData(payLoadComment)).text();
		Assert.assertNotNull(loginToken, "token is null");
		return loginToken;
							
	}

	
	@Given("requests for quickQuote")
	public void quickQuote()
	{
				
			
		Map<String, String> quickQuotePayLoad= new HashMap<>();
		quickQuotePayLoad.put("schemeCode","TES001" );
		quickQuotePayLoad.put("deposit", "50");
		quickQuotePayLoad.put("premium","5000" );
		quickQuotePayLoad.put("startDate", "2021-02-10");
		String QuoteResponse = 	apiRequestContext.post("/get/quote/"+loginToken,RequestOptions.create().setData(quickQuotePayLoad)).text();				
		System.out.println(QuoteResponse);
	
	}
	
	@Given("posts the persistQuote")
	public String persistQuote()
	{
		
		premium = 500.02 + rand.nextDouble() * 300;
		//create a object mapper to map multiple objects in a request
		ObjectMapper mapper = new ObjectMapper();
		
		//GetQuote object from persist Quote
		ObjectNode getQuote = mapper.createObjectNode();
        getQuote.put("schemeCode", "TES001");
        getQuote.put("deposit", "67");
        getQuote.put("premium", String.format("%.2f", premium));
        getQuote.put("flatRate", 10);
        getQuote.put("startDate", "2023-10-02");

       //address object from persist Quote
        ObjectNode address = mapper.createObjectNode();
        address.put("addressLine1", "12 B");
        address.put("addressLine2", "Carshalton road");
        address.put("postalTown", "Sutton");
        address.put("country", "United Kingdom");
        address.put("postCode", "SM14LH");

        
        //address will be wrapped into addresses object
        ArrayNode addresses = mapper.createArrayNode();
        addresses.add(address);

        //contact object
        ObjectNode contact = mapper.createObjectNode();
        contact.put("email", "testing@premfina.com");
        contact.put("mobile", "07443577777");

        //contact object will be wrapped into contacts
        ArrayNode contacts = mapper.createArrayNode();
        contacts.add(contact);

        //customer object
        ObjectNode customer = mapper.createObjectNode();
        customer.put("title", "MR");
        customer.put("surname", faker.name().lastName());
        customer.put("firstName", "Esign AC");
        customer.put("dateOfBirth", "12-12-1973");
        customer.set("addresses", addresses);
        customer.set("contacts", contacts);

        //policy object
        ObjectNode policy = mapper.createObjectNode();
        policy.put("startDate", "03-10-2023");
        policy.put("cancellationType", "Pro Rata - Cancellable");
        policy.put("reference", "PFTEST1");
        policy.put("insurer", "Aviva Plc");
        policy.put("coverType", "Car");
        policy.put("premium", String.format("%.2f", premium));

        //policy objects will be wrapped into policies
        ArrayNode policies = mapper.createArrayNode();
        policies.add(policy);

        
        //bank account object
        ObjectNode bankAccount = mapper.createObjectNode();
        bankAccount.put("sortCode", "000001");
        bankAccount.put("accountNumber", "00000001");
        bankAccount.put("accountName", "Returns Accounts");

        //Agreement object consists of customer, policies and bankAccount
        ObjectNode agreement = mapper.createObjectNode();
        agreement.set("customer", customer);
        agreement.set("policies", policies);
        agreement.set("bankAccount", bankAccount);

        //Persist Quote consists of below object groups
        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.put("daysValid", "30");
        requestBody.put("preCheck", true);
        requestBody.set("getQuote", getQuote);
        requestBody.set("agreement", agreement);
        
        String printresponse = requestBody.toString();
        System.out.println(printresponse);
        
        APIResponse response =   apiRequestContext.post("/persist/quote/"+loginToken,RequestOptions.create().setData(requestBody.toString()));	
        String responseBody = response.text();
        int statusCode = response.status();
        sharedXref = JsonPath.parse(responseBody).read("$.sharedXref");
        System.out.println("Response status code: " + statusCode);
        System.out.println("Response body: " + responseBody);
        System.out.println("sharedXref : " + sharedXref);
        return sharedXref;
       
	}
	
	@Given("Update PersistQuote DD")
	public void updateBankAccount()
	{
		Map<String, String> updateBankAccount= new HashMap<>();
		updateBankAccount.put("sortCode","000001" );
		updateBankAccount.put("accountNumber", "00000001");
		updateBankAccount.put("accountName","Returns Account" );
		
		 String printresponse = updateBankAccount.toString();
	     System.out.println(printresponse);
	     
	     String url = "/update/pquote/"+sharedXref+"/"+loginToken;
	     System.out.println(url);
		
		APIResponse updateBankDDResponse = 	apiRequestContext.post("/update/pquote/"+sharedXref+"/"+loginToken,RequestOptions.create().setData(updateBankAccount));				
		String updateBankDDRes = updateBankDDResponse.text();
		//int updateBankDDStatusCode = updateBankDDResponse.status();
		//Assert.assertEquals(200,updateBankDDStatusCode);
		System.out.println(updateBankDDRes);
	
	}
	
	
	@Given("activate the quote")
	public String activateQuote()
	{
		
		String url = "/activate/quote/"+sharedXref+"/"+loginToken;
	     System.out.println(url);
		APIResponse activateQuoteCode = apiRequestContext.post("/activate/quote/"+sharedXref+"/"+loginToken);
		String activateQuoteCodeRes = activateQuoteCode.text();
		//Assert.assertEquals(200,activateQuoteCode);
		agreementNo = JsonPath.parse(activateQuoteCodeRes).read("$.agreementNumber");
		System.out.println("Response "+activateQuoteCodeRes);
		return agreementNo;
	}
	
	@After
	public void tearDown() {
		apiRequestContext.dispose();
	    playwright.close();
	}

	
}
