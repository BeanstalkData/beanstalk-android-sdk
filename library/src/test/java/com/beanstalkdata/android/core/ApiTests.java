package com.beanstalkdata.android.core;

import com.beanstalkdata.android.BuildConfig;
import com.beanstalkdata.android.model.Contact;
import com.beanstalkdata.android.model.Coupon;
import com.beanstalkdata.android.model.type.ImageType;
import com.beanstalkdata.android.model.type.MessageContentType;
import com.beanstalkdata.android.model.type.MessageType;
import com.beanstalkdata.android.model.type.PlatformType;
import com.beanstalkdata.android.request.AuthenticateUserRequest;
import com.beanstalkdata.android.request.ContactRequest;
import com.beanstalkdata.android.response.CardBalanceResponse;
import com.beanstalkdata.android.response.CouponResponse;
import com.beanstalkdata.android.response.GiftCardListResponse;
import com.beanstalkdata.android.response.LocationResponse;
import com.beanstalkdata.android.response.PaymentTokenResponse;
import com.beanstalkdata.android.response.PushMessageByIdResponse;
import com.beanstalkdata.android.response.PushMessagesResponse;
import com.beanstalkdata.android.response.PushSuccessResponse;
import com.beanstalkdata.android.response.RegisterGiftCardResponse;
import com.beanstalkdata.android.response.RewardsCountResponse;
import com.beanstalkdata.android.response.StoresResponse;
import com.beanstalkdata.android.response.TrackTransactionResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.Ignore;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.MockResponse;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiTests {

    private static final int TIMEOUT = 60;

    private static final String STATUS_ERROR = "error";
    private static final String STATUS_LOGGED_OUT = "logged out";
    private static final String STATUS_INVALID_TOKEN = "invalid token";
    private static final String STATUS_SUCCESS = "Success";
    private static final String STATUS_SUCCESS2 = "success";
    private static final String STATUS_PROGRESS = "{\"Category\":[{\"Name\":\"Orders\",\"Count\":\"0\"}]}";
    private static final String STATUS_SESSION_OK = "valid";
    private static final String STATUS_SESSION_ERROR = "invalid token";

    private static final String EMAIL1 = "not_found@example.com";
    private static final String PASSWORD1 = "WRONG PASSWORD";
    private static final String AUTH_REQ1 = "email=not_found%40example.com&password=WRONG%20PASSWORD&key=JOAA-RXHF-KFVU-JWKJ-GVIB&time=-1";
    private static final String TOKEN1 = "INVALID TOKEN";
    private static final String LOGOUT_REQ1 = "contact=1234567890&token=INVALID%20TOKEN";
    private static final String SESSION_CHECK_REQ1 = "contact=1234567890&token=";

    private static final String EMAIL2 = "test@example.com";
    private static final String PHONE2 = "1855567443";
    private static final String PASSWORD2 = "12345678";
    private static final String AUTH_REQ2 = "email=test%40example.com&password=12345678&key=JOAA-RXHF-KFVU-JWKJ-GVIB&time=-1";
    private static final String ID2 = "1234567890";
    private static final String TOKEN2 = "de763efff3f11b80034de9b0b5a6575131f6fa19";
    private static final String LOGOUT_REQ2 = "contact=1234567890&token=de763efff3f11b80034de9b0b5a6575131f6fa19";
    private static final String SESSION_CHECK_REQ2 = "contact=1234567890&token=de763efff3f11b80034de9b0b5a6575131f6fa19";

    private static final String ID3 = "16666008";
    private static final String TOKEN3 = "8ba376567471cbcc4bfb3188b4e5aabbc553f413";
    private static final String EMAIL3 = "new@example.com";
    private static final String DOB3 = "1991-09-20";
    private static final String FIRST_NAME3 = "John";
    private static final String LAST_NAME3 = "Smith";
    private static final String PHONE3 = "1202555010";
    private static final String ZIP3 = "10001";
    private static final String REWARD3 = "Custom reward";
    private static final String PASSWORD3 = "12345678";
    private static final String GIFT_CARD_NUMBER3 = "6276000000221935380";
    private static final String GIFT_CARD_PIN3 = "1234";
    private static final double LAT3 = 28.6403769;
    private static final double LNG3 = -81.467637;
    private static final String LOYALTY_ID3 = "00001";
    private static final String PAYMENT_ID3 = "00001";
    private static final String COUPONS3 = "Free_113135_110111_117";
    private static final String FKEY3 = "123073";
    private static final String USERNAME3 = "New User";
    private static final String DETAILS3 = "Test transaction details";
    private static final String DEVICE_TOKEN3 = "Some android device token";
    private static final String MESSAGE_ID3 = "12345678";
    private static final int MAX_MESSAGES3 = 2;

    private static final String EMAIL4 = "novadine_test@2.com";

    private static MockWebServer server;
    private static BeanstalkDataApi service;

    private static final Dispatcher dispatcher = new Dispatcher() {
        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
            String body = request.getBody().readUtf8();
            String path = request.getPath();
            String method = request.getMethod();
            switch (method) {
                case "GET":
                    return processGet(path);
                case "POST":
                    return processPost(path, body);
                default:
                    return new MockResponse().setResponseCode(500);
            }
        }

        private MockResponse processGet(String path) {
            switch (path) {
                case "/contacts?type=email&key=JOAA-RXHF-KFVU-JWKJ-GVIB&q=" + EMAIL1:
                    return new MockResponse()
                            .setResponseCode(200)
                            // NOTE: it should be "[]" but current API returns string with "null" text
                            .setBody("null");
                case "/contacts?type=email&key=JOAA-RXHF-KFVU-JWKJ-GVIB&q=" + EMAIL2:
                    Contact contact1 = new Contact();
                    contact1.setEmail(EMAIL2);
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(new Gson().toJson(new Contact[]{contact1}));
                case "/contacts?type=cell_number&key=JOAA-RXHF-KFVU-JWKJ-GVIB&q=" + PHONE2:
                    Contact contact2 = new Contact();
                    contact2.setPhone(PHONE2);
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(new Gson().toJson(new Contact[]{contact2}));
                case "/contacts?key=JOAA-RXHF-KFVU-JWKJ-GVIB&q=" + ID3:
                    Contact contact3 = new Contact();
                    contact3.setFirstName(FIRST_NAME3);
                    contact3.setLastName(LAST_NAME3);
                    contact3.setEmail(EMAIL3);
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(new Gson().toJson(new Contact[]{contact3}));
                case "/contacts?type=email&key=JOAA-RXHF-KFVU-JWKJ-GVIB&q=" + EMAIL4:
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(String.format("[{\"Novadine_User\":true,\"contactEmail\":\"%s\"}]", EMAIL4));
                case "/bsdPayment/list?key=JOAA-RXHF-KFVU-JWKJ-GVIB&contactId=" + ID3 + "&token=" + TOKEN3:
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{}");
                case "/bsdStores/locate/?key=JOAA-RXHF-KFVU-JWKJ-GVIB&lat=28.6403769&long=-81.467637":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{}");
                case "/bsdLoyalty/indentifyCustomer.php?field=CustomerID&key=JOAA-RXHF-KFVU-JWKJ-GVIB&value=123073":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{}");
                case "/bsdLoyalty/getOffersM.php?key=JOAA-RXHF-KFVU-JWKJ-GVIB&Card=" + ID3:
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{\"Coupon\":[{\"CouponNo\":\"Free Side Item\"}]}");
                default:
                    return new MockResponse().setResponseCode(404);
            }
        }

        private MockResponse processPost(String path, String body) {
            switch (path) {
                case "/authenticateUser/":
                    if (body.equals(AUTH_REQ1)) {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(STATUS_ERROR);
                    }
                    if (body.equals(AUTH_REQ2)) {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(String.format("[%s,\"%s\"]", ID2, TOKEN2));
                    }
                case "/logoutUser/":
                    if (body.equals(LOGOUT_REQ2)) {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(STATUS_LOGGED_OUT);
                    }
                    if (body.equals(LOGOUT_REQ1)) {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(STATUS_INVALID_TOKEN);
                    }
                case "/addContact/?key=JOAA-RXHF-KFVU-JWKJ-GVIB":
                    // NOTE: According to the docs in update case the API should return "Update" string instead of "Add"
                    // but at the moment nothing updates and the API returns string "Add".
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(String.format("[\"%s\",\"Add\"]", ID3));
                case "/addUser/":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(STATUS_SUCCESS);
                case "/bsdLoyalty/?function=updatePassword":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(STATUS_SUCCESS2);
                case "/bsdLoyalty/ResetPassword.php?key=JOAA-RXHF-KFVU-JWKJ-GVIB":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(String.format("Email has been sent to %s with the new password!", EMAIL3));
                case "/bsdLoyalty/getProgress.php?key=JOAA-RXHF-KFVU-JWKJ-GVIB":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(STATUS_PROGRESS);
                case "/bsdPayment/register/":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(String.format("{\"status\":true,\"success\":{\"code\":2,\"message\":{\"response\":{\"cardNumber\":\"%s\"},\"success\":true}}}", GIFT_CARD_NUMBER3));
                case "/bsdPayment/inquiry/":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{}");
                case "/bsdPayment/startPayment/":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{\"paymentToken\":\"16582\"}");
                case "/bsdPayment/preferred/":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{}");
                case "/bsdTransactions/add/?key=JOAA-RXHF-KFVU-JWKJ-GVIB&contact=16666008&username=New%20User":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{\"status\":true,\"success\":{}}");
                case "/pushNotificationEnroll":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{\"success\":true}");
                case "/pushNotificationModify":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{\"success\":true}");
                case "/pushNotificationDelete":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{\"success\":true}");
                case "/pushNotification/getMessages":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{\"messages\":[]}");
                case "/pushNotification/getMessagesByOsAndType":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{\"messages\":[]}");
                case "/pushNotification/getMessagesByImageType":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{\"messages\":[]}");
                case "/pushNotification/updateStatus":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{\"success\":true}");
                case "/pushNotification/getMessageById":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{\"message\":{}}");
                case "/checkSession/":
                    if (body.equals(SESSION_CHECK_REQ1)) {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(STATUS_SESSION_ERROR);
                    }
                    if (body.equals(SESSION_CHECK_REQ2)) {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(STATUS_SESSION_OK);
                    }
                default:
                    return new MockResponse().setResponseCode(404);
            }
        }
    };

    @BeforeClass
    public static void initService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        server = new MockWebServer();
        server.setDispatcher(dispatcher);
        HttpUrl baseUrl = server.url("/");

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        httpClient.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        httpClient.readTimeout(TIMEOUT, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        service = retrofit.create(BeanstalkDataApi.class);
    }

    @AfterClass
    public static void stopService() throws IOException {
        server.shutdown();
    }

    private JSONArray linkMockJsonArrayWithBody(String body) {
        // Parse arrays represented as [1234567890,"de763efff3f11b80034de9b0b5a6575131f6fa19"] or ["16666008","Add"] etc
        JSONArray jsonArray = mock(JSONArray.class);
        String[] body_array = body.replace("[", "").replace("]", "").replace("\"", "").split(",");
        for (int i = 0; i < body_array.length; i++) {
            when(jsonArray.optString(i)).thenReturn(body_array[i]);
        }
        when(jsonArray.length()).thenReturn(body_array.length);
        return jsonArray;
    }

    @Test
    public void testGson() {
        String json = "{\"isEnabled\":\"true\", \"username\":\"someUser\", \"password\":\"password\", \"first_name\":\"Nick\",\"last_name\":\"Smith\", \"registered\":\"2015-01-04\", \"roles\":\"user\", \"deviceId\": \"978\", \"email\": \"smith@somemail.com\"}";
        Gson gson = new Gson();
        Object o = gson.fromJson(json, Object.class);
        assertNotNull(o);
    }

    @Test
    public void findNonExistentUserByEmail() throws Exception {
        Call<Contact[]> stringCall = service.getContactByEmail(BuildConfig.APP_KEY, EMAIL1);
        Response<Contact[]> execute = stringCall.execute();

        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        Contact[] body = execute.body();
        assertTrue(body == null);
        // NOTE: the assert below is for the case if [] is returned by the API.
        //assertTrue(body.length == 0);
    }

    @Test
    public void findUserByEmail() throws Exception {
        Call<Contact[]> stringCall = service.getContactByEmail(BuildConfig.APP_KEY, EMAIL2);
        Response<Contact[]> execute = stringCall.execute();

        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        Contact[] body = execute.body();
        assertTrue(body.length > 0);

        Contact contact = body[0];
        assertTrue(EMAIL2.equals(contact.getEmail()));
    }

    @Test
    public void findUserByPhone() throws Exception {
        Call<Contact[]> stringCall = service.getContactByPhone(BuildConfig.APP_KEY, PHONE2);
        Response<Contact[]> execute = stringCall.execute();

        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        Contact[] body = execute.body();
        assertTrue(body.length > 0);

        Contact contact = body[0];
        assertTrue(PHONE2.equals(contact.getPhone()));
    }

    @Test
    public void authorizeUserError() throws Exception {
        AuthenticateUserRequest request = new AuthenticateUserRequest(EMAIL1, PASSWORD1);
        Call<ResponseBody> call = service.authenticateUser(
                request.getEmail(), request.getPassword(), BuildConfig.APP_KEY, request.getTime());
        Response<ResponseBody> execute = call.execute();

        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        String body = execute.body().string();
        assertEquals(body, STATUS_ERROR);
    }

    @Test
    public void authorizeUser() throws Exception {
        AuthenticateUserRequest request = new AuthenticateUserRequest(EMAIL2, PASSWORD2);
        Call<ResponseBody> call = service.authenticateUser(
                request.getEmail(), request.getPassword(), BuildConfig.APP_KEY, request.getTime());
        Response<ResponseBody> execute = call.execute();

        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        String body = execute.body().string();
        assertNotEquals(body, STATUS_ERROR);

        JSONArray array = linkMockJsonArrayWithBody(body);
        assertEquals(array.length(), 2);
        assertEquals(array.optString(0), ID2);
        assertEquals(array.optString(1), TOKEN2);
    }

    @Test
    public void logoutUserError() throws Exception {
        Call<String> call = service.logoutUser(ID2, TOKEN1);
        Response<String> execute = call.execute();

        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        String body = execute.body();
        assertEquals(body, STATUS_INVALID_TOKEN);
    }

    @Test
    public void logoutUser() throws Exception {
        Call<String> call = service.logoutUser(ID2, TOKEN2);
        Response<String> execute = call.execute();

        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        String body = execute.body();
        assertEquals(body, STATUS_LOGGED_OUT);
    }

    @Test
    public void checkSessionError() throws Exception {
        Call<String> call = service.checkSession(ID2, "");
        Response<String> execute = call.execute();

        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        String body = execute.body();
        assertEquals(body, STATUS_SESSION_ERROR);
    }

    @Test
    public void checkSession() throws Exception {
        Call<String> call = service.checkSession(ID2, TOKEN2);
        Response<String> execute = call.execute();

        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        String body = execute.body();
        assertEquals(body, STATUS_SESSION_OK);
    }

    @Test
    public void registerUser() throws Exception {
        /*
        NOTE: User's contact must be added at first. The API returns array like ["16666008","Add"].
        ContactID is the first element and it must be used to next API call to create a new user.
        NOTE: The API doesn't required all the fields set in the constructor. It worked fine for the minimal
        set of attributes like Email, FirstName, LastName.
         */
        ContactRequest contactRequest = new ContactRequest(DOB3, EMAIL3, FIRST_NAME3, LAST_NAME3, PHONE3, ZIP3, REWARD3);
        contactRequest.setPassword(PASSWORD3);

        Call<ResponseBody> call = service.createContact(BuildConfig.APP_KEY, contactRequest.asParams());
        Response<ResponseBody> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        String body = execute.body().string();
        assertNotNull(body);

        JSONArray jsonArray = linkMockJsonArrayWithBody(body);
        assertEquals(2, jsonArray.length());
        assertEquals("Add", jsonArray.optString(1));

        String contactId = jsonArray.optString(0);
        Call<String> user = service.createUser(contactRequest.getEmail(), contactRequest.getPassword(), BuildConfig.APP_KEY, contactId);
        Response<String> user_execute = user.execute();
        assertEquals(user_execute.code(), HttpURLConnection.HTTP_OK);

        String userCreateResponse = user_execute.body();
        assertNotNull(userCreateResponse);
        assertEquals("Success", userCreateResponse);
    }

    @Test
    public void getContact() throws Exception {
        Call<Contact[]> call = service.getContact(BuildConfig.APP_KEY, ID3);
        Response<Contact[]> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        Contact[] body = execute.body();
        assertNotNull(body);
        assertEquals(1, body.length);

        Contact contact = body[0];
        assertTrue(EMAIL3.equals(contact.getEmail()));
        assertTrue(FIRST_NAME3.equals(contact.getFirstName()));
        assertTrue(LAST_NAME3.equals(contact.getLastName()));
    }

    @Test
    public void getContactRaw() throws Exception {
        Call<ResponseBody> call = service.getContactRaw(BuildConfig.APP_KEY, ID3);
        Response<ResponseBody> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        String body = execute.body().string();
        assertNotNull(body);
    }

    @Test
    public void updateContact() throws Exception {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("contactId", ID3);
        data.put("contactZipCode", ZIP3);
        Call<String[]> call = service.updateContact(BuildConfig.APP_KEY, data);
        Response<String[]> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        // TODO: Update API doesn't work correctly
        String[] body = execute.body();
        assertEquals(2, body.length);
    }

    @Test
    public void updatePassword() throws Exception {
        Map<String, String> request = new HashMap<>(4);
        request.put("contact", ID3);
        request.put("token", TOKEN3);
        request.put("password", PASSWORD3);
        request.put("key", BuildConfig.APP_KEY);

        Call<String> call = service.updatePassword(request);
        Response<String> execute = call.execute();

        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);
        assertEquals(execute.body(), STATUS_SUCCESS2);
    }

    @Test
    public void resetPassword() throws Exception {
        Call<String> stringCall = service.resetPassword(BuildConfig.APP_KEY, EMAIL3);
        Response<String> execute = stringCall.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        String body = execute.body();
        String expected = String.format("Email has been sent to %s with the new password!", EMAIL3);
        assertEquals(expected, body);
    }

    @Test
    public void getProgress() throws Exception {
        Call<RewardsCountResponse> call = service.getProgress(BuildConfig.APP_KEY, ID3);
        Response<RewardsCountResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        RewardsCountResponse body = execute.body();
        assertNotNull(body.getCount());
    }

    @Test
    public void registerCard() throws Exception {
        Call<RegisterGiftCardResponse> call = service.registerGiftCard(
                BuildConfig.APP_KEY, ID3, TOKEN3, GIFT_CARD_NUMBER3, GIFT_CARD_PIN3);
        Response<RegisterGiftCardResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        // TODO: Check this API with a real Gift Card.
        RegisterGiftCardResponse body = execute.body();
        assertTrue(!body.isFailed());
    }

    @Test
    public void inquireAboutCard() throws Exception {
        Call<CardBalanceResponse> call = service.inquireAboutCard(BuildConfig.APP_KEY, ID3, TOKEN3, GIFT_CARD_NUMBER3);
        Response<CardBalanceResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        // TODO: Check this API with real a Gift Card. At the moment mock web server returns {}
        CardBalanceResponse body = execute.body();
        assertNotNull(body);
    }

    @Test
    public void inquireAboutCardRaw() throws Exception {
        Call<ResponseBody> call = service.inquireAboutCardRaw(BuildConfig.APP_KEY, ID3, TOKEN3, GIFT_CARD_NUMBER3);
        Response<ResponseBody> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        // TODO: Check this API with real a Gift Card. At the moment mock web server returns {}
        String body = execute.body().string();
        assertNotNull(body);
    }

    @Ignore
    @Test
    public void registerPaperCard() throws Exception {
        // TODO: No information about this API in the docs. Response statuses are weird mix of strings and numbers (see below)
        String contactId = "16249430";
        String cardNumber = "123456789012";
        String cardPin = "1234";
        Call<String> call = service.paperCardRegistration(BuildConfig.APP_KEY, "addNewCard", cardNumber, cardPin, contactId);
        Response<String> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        // TODO: Got more exotic statuses - fail2 and "Invalid Card or Security Code"444
        String body = execute.body();
        assertEquals("card added", body); //TODO Check this 'cause response is empty
    }

    @Test
    public void getGiftCardList() throws Exception {
        // TODO: Need real Gift Card.
        Call<GiftCardListResponse> call = service.getGiftCardList(BuildConfig.APP_KEY, ID3, TOKEN3);
        Response<GiftCardListResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        GiftCardListResponse body = execute.body();
        assertNotNull(body.isFailed());
    }

    @Test
    public void getGiftCardListRaw() throws Exception {
        // TODO: Need real Gift Card.
        Call<ResponseBody> call = service.getGiftCardListRaw(BuildConfig.APP_KEY, ID3, TOKEN3);
        Response<ResponseBody> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        String body = execute.body().string();
        assertNotNull(body);
    }

    @Ignore
    @Test
    public void getStores() throws Exception {
        // TODO: No docs about this API
        Call<StoresResponse> call = service.checkLocation(BuildConfig.APP_KEY, LAT3, LNG3);
        Response<StoresResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        StoresResponse body = execute.body();
        assertNotNull(body);
//        assertFalse(body.isFailed());
    }

    @Ignore
    @Test
    public void checkNovadineUser() throws Exception {
        // TODO: Clarify what Novadine and their users are. Find Novadine user for more tests?
        Call<Contact[]> call = service.getContactByEmail(BuildConfig.APP_KEY, "novadine_test@2.com");
        Response<Contact[]> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        Contact[] body = execute.body();
        assertNotNull(body);
        assertTrue(body.length > 0);
        assertNotNull(body[0]);
        assertTrue(body[0].isNovadineUser());
    }

    @Test
    public void startPayment() throws Exception {
        // TODO: Need more test data and more clarified business logic for this API.
        Call<PaymentTokenResponse> call = service.startPayment(BuildConfig.APP_KEY, ID3, LOYALTY_ID3, TOKEN3, PAYMENT_ID3, COUPONS3);
        Response<PaymentTokenResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        String paymentToken = execute.body().getPaymentToken();
        assertNotNull(paymentToken);
    }

    @Test
    public void setPreferredCard() throws Exception {
        Call<String> call = service.setPreferredCard(BuildConfig.APP_KEY, ID3, TOKEN3, GIFT_CARD_NUMBER3);
        Response<String> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        String body = execute.body();
        assertNotNull(body);
    }

    @Test
    public void getLoyaltyInformation() throws Exception {
        Call<String> call = service.getLoyaltyInformation(BuildConfig.APP_KEY, FKEY3);
        Response<String> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        String body = execute.body();
        assertNotNull(body);
    }

    @Test
    public void getUserOffers() throws Exception {
        // TODO: weird status example ( " Failed to get coupons try again later." );
        /* And even more weird..
        ("If you just registered as a new member, your rewards will be available in a few minutes.
        <br /> No rewards available.
        <br />If you have not used your rewards in a while, they may have expired. ");
         */
        Call<CouponResponse> call = service.getUserOffers(BuildConfig.APP_KEY, ID3);
        Response<CouponResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        // TODO: Need more test data. This API didn't really work.
        Coupon[] coupons = execute.body().getCoupons();
        assertTrue(coupons.length > 0);
    }

    @Test
    public void setTransactionEvent() throws Exception {
        // TODO: Need more details. It isn't clear what is username parameter and if it is incorrect the API returns an error.
        Call<TrackTransactionResponse> call = service.trackTransaction(BuildConfig.APP_KEY, ID3, USERNAME3, DETAILS3);
        Response<TrackTransactionResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        TrackTransactionResponse tx = execute.body();
        assertTrue(tx.isStatus());
    }

    @Test
    public void enrollPushNotification() throws Exception {
        Call<PushSuccessResponse> call = service.enrollPushNotification(BuildConfig.APP_KEY, ID3, DEVICE_TOKEN3, PlatformType.ANDROID);
        Response<PushSuccessResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        PushSuccessResponse status = execute.body();
        assertTrue(status.isSuccess());
    }

    @Test
    public void modifyPushNotification() throws Exception {
        Call<PushSuccessResponse> call = service.modifyPushNotification(BuildConfig.APP_KEY, ID3, DEVICE_TOKEN3, PlatformType.ANDROID);
        Response<PushSuccessResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        PushSuccessResponse status = execute.body();
        assertTrue(status.isSuccess());
    }

    @Test
    public void deletePushNotification() throws Exception {
        Call<PushSuccessResponse> call = service.deletePushNotification(BuildConfig.APP_KEY, ID3);
        Response<PushSuccessResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        PushSuccessResponse status = execute.body();
        assertTrue(status.isSuccess());
    }

    @Test
    public void getPushMessages() throws Exception {
        Call<PushMessagesResponse> call = service.getContactMessages(BuildConfig.APP_KEY, ID3, MAX_MESSAGES3);
        Response<PushMessagesResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        PushMessagesResponse messages = execute.body();
        assertNotNull(messages.getPushMessages());
    }

    @Test
    public void getPushMessagesByOsAndType() throws Exception {
        Call<PushMessagesResponse> call = service.getMessagesByOsAndType(
                BuildConfig.APP_KEY, MessageContentType.HTML, PlatformType.ANDROID);
        Response<PushMessagesResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        PushMessagesResponse messages = execute.body();
        assertNotNull(messages.getPushMessages());
    }

    @Test
    public void getPushMessagesByImageType() throws Exception {
        Call<PushMessagesResponse> call = service.getMessagesByImageType(
                BuildConfig.APP_KEY, MessageContentType.HTML, ImageType.LARGE);
        Response<PushMessagesResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        PushMessagesResponse messages = execute.body();
        assertNotNull(messages.getPushMessages());
    }

    @Test
    public void updateMessageStatus() throws Exception {
        Call<PushSuccessResponse> call = service.updateMessageStatus(BuildConfig.APP_KEY, MESSAGE_ID3, MessageType.READ);
        Response<PushSuccessResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        PushSuccessResponse status = execute.body();
        assertTrue(status.isSuccess());
    }

    @Test
    public void getMessageById() throws Exception {
        Call<PushMessageByIdResponse> call = service.getMessageById(BuildConfig.APP_KEY, MESSAGE_ID3);
        Response<PushMessageByIdResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        PushMessageByIdResponse message = execute.body();
        assertNotNull(message);
    }

    @Ignore
    @Test
    public void getCoordinatesByZip() throws Exception {
        String zip = "32818";
        Call<LocationResponse> call = service.getLocationByZipCode(BuildConfig.GOOGLE_MAPS_KEY, zip);
        Response<LocationResponse> execute = call.execute();
        assertEquals(execute.code(), HttpURLConnection.HTTP_OK);

        LocationResponse body = execute.body();
        assertNotNull(body);
        assertFalse(body.isFailed());
        assertEquals(28.6403769, body.getLocation().getLatitude(), 0.1);
        assertEquals(-81.467637, body.getLocation().getLongitude(), 0.1);
    }
}
