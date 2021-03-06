package com.beanstalkdata.android.core;

import com.beanstalkdata.android.BuildConfig;
import com.beanstalkdata.android.model.Contact;
import com.beanstalkdata.android.model.ContactAsset;
import com.beanstalkdata.android.model.Coupon;
import com.beanstalkdata.android.model.PushMessage;
import com.beanstalkdata.android.model.deserializer.PushMessagesDeserializer;
import com.beanstalkdata.android.model.type.MessageType;
import com.beanstalkdata.android.model.type.PlatformType;
import com.beanstalkdata.android.request.AuthenticateUserRequest;
import com.beanstalkdata.android.request.ContactRequest;
import com.beanstalkdata.android.response.CardBalanceResponse;
import com.beanstalkdata.android.response.ContactUsResponse;
import com.beanstalkdata.android.response.CouponResponse;
import com.beanstalkdata.android.response.GiftCardListResponse;
import com.beanstalkdata.android.response.LocationResponse;
import com.beanstalkdata.android.response.LocationsResponse;
import com.beanstalkdata.android.response.PaymentTokenResponse;
import com.beanstalkdata.android.response.PushMessageByIdResponse;
import com.beanstalkdata.android.response.PushMessagesResponse;
import com.beanstalkdata.android.response.PushSuccessResponse;
import com.beanstalkdata.android.response.RegisterGiftCardResponse;
import com.beanstalkdata.android.response.RelocateResponse;
import com.beanstalkdata.android.response.RewardsCountResponse;
import com.beanstalkdata.android.response.StoreInfoResponse;
import com.beanstalkdata.android.response.StoresResponse;
import com.beanstalkdata.android.response.TrackTransactionResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiTests {

    private static final int TIMEOUT = 60;
    private static final String APP_KEY = BuildConfig.APP_KEY;

    private static final String STATUS_ERROR = "error";
    private static final String STATUS_BLANK = "blank";
    private static final String STATUS_LOGGED_OUT = "logged out";
    private static final String STATUS_INVALID_TOKEN = "invalid token";
    private static final String STATUS_SUCCESS = "Success";
    private static final String STATUS_SUCCESS2 = "success";
    private static final String STATUS_PROGRESS = "{\"Category\":[{\"Name\":\"Orders\",\"Count\":\"0\"}]}";
    private static final String STATUS_SESSION_OK = "valid";
    private static final String STATUS_SESSION_ERROR = "invalid token";

    private static final String EMAIL1 = "not_found@example.com";
    private static final String EMAIL1_ENC = "not_found%40example.com";
    private static final String PASSWORD1 = "WRONG_PASSWORD";
    private static final String AUTH_REQ1 = String.format("email=%s&password=%s&key=%s&time=-1", EMAIL1_ENC, PASSWORD1, APP_KEY);
    private static final String TOKEN1 = "INVALID TOKEN";
    private static final String LOGOUT_REQ1 = "contact=1234567890&token=INVALID%20TOKEN";
    private static final String SESSION_CHECK_REQ1 = "contact=1234567890&token=";

    private static final String EMAIL2 = "test@example.com";
    private static final String EMAIL2_ENC = "test%40example.com";
    private static final String PHONE2 = "1855567443";
    private static final String FKEY2 = "12345";
    private static final String PASSWORD2 = "12345678";
    private static final String AUTH_REQ2 = String.format("email=%s&password=%s&key=%s&time=-1", EMAIL2_ENC, PASSWORD2, APP_KEY);
    private static final String ID2 = "1234567890";
    private static final String TOKEN2 = "de763efff3f11b80034de9b0b5a6575131f6fa19";
    private static final String LOGOUT_REQ2 = "contact=1234567890&token=de763efff3f11b80034de9b0b5a6575131f6fa19";
    private static final String SESSION_CHECK_REQ2 = "contact=1234567890&token=de763efff3f11b80034de9b0b5a6575131f6fa19";
    private static final String GOOGLE_ID2 = "987654321987654321";
    private static final String GOOGLE_TOKEN2 = "GOOGLE_TOKEN2";
    private static final String FACEBOOK_ID2 = "98765432198765";
    private static final String FACEBOOK_TOKEN2 = "FACEBOOK_TOKEN2";

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
    private static final String DEVICE_TOKEN3_ENC = "Some%20android%20device%20token";
    private static final String MESSAGE_ID3 = "12345678";
    private static final int MAX_MESSAGES3 = 2;
    private static final String STORE_ID3 = "1";
    private static final String CURRENT_ASSET_IMAGE3 = "https://example.com/assets/current.html";
    private static final String DEFAULT_ASSET_IMAGE3 = "https://example.com/assets/default.html";
    private static final String TO_EMAIL3 = "to_new@example.com";
    private static final String COMMENTS3 = "test comments";
    private static final String GOOGLE_ID3 = "123456789012345678901";
    private static final String GOOGLE_TOKEN3 = "GOOGLE_TOKEN3";
    private static final String GOOGLE_AUTH_REQ3 = String.format("key=%s&GoogleId=%s&GoogleToken=%s", APP_KEY, GOOGLE_ID3, GOOGLE_TOKEN3);
    private static final String FACEBOOK_ID3 = "123456789012345";
    private static final String FACEBOOK_TOKEN3 = "FACEBOOK_TOKEN";
    private static final String FACEBOOK_AUTH_REQ3 = String.format("key=%s&fb=%s&fbtoken=%s", APP_KEY, FACEBOOK_ID3, FACEBOOK_TOKEN3);

    private static final String ID4_1 = "16666666";
    private static final String ID4_2 = "16666667";
    private static final int MAX_MESSAGES4 = 100;
    private static final String MSG_REQ4_1 = String.format("key=%s&contactId=%s&maxResults=%s", APP_KEY, ID4_1, MAX_MESSAGES4);
    private static final String MSG_REQ4_2 = String.format("key=%s&contactId=%s&maxResults=%s", APP_KEY, ID4_2, MAX_MESSAGES4);

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
            if (path.equals(String.format("/contacts?type=email&key=%s&q=%s", APP_KEY, EMAIL1))) {
                return new MockResponse()
                        .setResponseCode(200)
                        // NOTE: it should be "[]" but current API returns string with "null" text
                        .setBody("null");
            }
            if (path.equals(String.format("/contacts?type=email&key=%s&q=%s", APP_KEY, EMAIL2))) {
                Contact contact1 = new Contact();
                contact1.setEmail(EMAIL2);
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody(new Gson().toJson(new Contact[]{contact1}));
            }
            if (path.equals(String.format("/contacts?type=cell_number&key=%s&q=%s", APP_KEY, PHONE2))) {
                Contact contact2 = new Contact();
                contact2.setPhone(PHONE2);
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody(new Gson().toJson(new Contact[]{contact2}));
            }
            if (path.equals(String.format("/contacts?type=fkey&key=%s&q=%s", APP_KEY, FKEY2))) {
                Contact contact2 = new Contact();
                contact2.setFKey(FKEY2);
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody(new Gson().toJson(new Contact[]{contact2}));
            }
            if (path.equals(String.format("/contacts?key=%s&q=%s", APP_KEY, ID3))) {
                Contact contact3 = new Contact();
                contact3.setFirstName(FIRST_NAME3);
                contact3.setLastName(LAST_NAME3);
                contact3.setEmail(EMAIL3);
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody(new Gson().toJson(new Contact[]{contact3}));
            }
            if (path.equals(String.format("/bsdPayment/list?key=%s&contactId=%s&token=%s", APP_KEY, ID3, TOKEN3))) {
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody("{}");
            }
            if (path.equals(String.format("/bsdStores/locate/?key=%s&lat=28.6403769&long=-81.467637", APP_KEY))) {
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody(getStoreMock());
            }
            if (path.equals(String.format("/bsdLoyalty/indentifyCustomer.php?field=CustomerID&key=%s&value=123073", APP_KEY))) {
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody("{}");
            }
            if (path.equals(String.format("/bsdLoyalty/getOffersM.php?key=%s&Card=%s", APP_KEY, ID3))) {
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody("{\"Coupon\":[{\"CouponNo\":\"Free Side Item\"}]}");
            }
            if (path.equals(String.format("/bsdLoyalty/GetLocation.php?key=%s&locationId=%s", APP_KEY, STORE_ID3))) {
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody(getStoreInfoMock());
            }
            if (path.equals(String.format("/bsdLoyalty/getLocations.php?key=%s", APP_KEY))) {
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody(getLocationsMock());
            }
            if (path.equals(String.format("/pushNotificationEnroll?key=%s&contact_id=16666008&deviceToken=%s&platform=Android", APP_KEY, DEVICE_TOKEN3_ENC))) {
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody("{\"success\":true}");
            }
            if (path.equals(String.format("/pushNotificationModify?key=%s&contact_id=16666008&deviceToken=%s&platform=Android", APP_KEY, DEVICE_TOKEN3_ENC))) {
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody("{\"success\":true}");
            }
            if (path.equals(String.format("/pushNotificationDelete?key=%s&contact_id=16666008", APP_KEY))) {
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody("{\"success\":true}");
            }
            if (path.equals(String.format("/bsdContact/geoAssets.php?key=%s&contactId=%s", APP_KEY, ID3))) {
                // language=JSON
                String contactAsset = "{\n" +
                        "    \"CurrentImage\": \"https://example.com/assets/current.html\",\n" +
                        "    \"DefaultImage\": \"https://example.com/assets/default.html\"\n" +
                        "}\n";
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody(contactAsset);
            }
            if (path.equals(String.format("/bsdContact/geoAssets.php?key=&contactId=%s", ID3))) {
                // language=JSON
                String contactAssetError = "{\n" +
                        "  \"ERROR\": \"key parameter is required\"\n" +
                        "}";
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody(contactAssetError);
            }
            if (path.equals(String.format("/bsdLoyalty/maintainLoyaltyCards.php?function=addNewCard&key=%s&contact=%s&cardNumber=%s", APP_KEY, ID3, ID3))) {
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody("card added");
            }
            return new MockResponse().setResponseCode(404);
        }

        private MockResponse processPost(String path, String body) {
            // language=JSON
            String pushMessageJson = String.format("[{\n" +
                    "  \"AppHook\": \"app://example\",\n" +
                    "  \"CampaignId\": \"12345\",\n" +
                    "  \"Category\": \"OFFER\",\n" +
                    "  \"ContactId\": \"%s\",\n" +
                    "  \"CustomerId\": \"123\",\n" +
                    "  \"MessageBody\": \"\",\n" +
                    "  \"MsgImage\": \"\",\n" +
                    "  \"MsgType\": \"\",\n" +
                    "  \"OS\": \"Android\",\n" +
                    "  \"StepId\": \"12345\",\n" +
                    "  \"TitleImage\": \"\",\n" +
                    "  \"inboxTitle\": \"\",\n" +
                    "  \"messageUrl\": \"\",\n" +
                    "  \"status\": \"UNREAD\",\n" +
                    "  \"subtitle\": \"\",\n" +
                    "  \"thumbnailUrl\": \"\",\n" +
                    "  \"title\": \"\"\n" +
                    "}]", ID3);

            if (path.equals(String.format("/addContact/?key=%s", APP_KEY))) {
                // NOTE: According to the docs in update case the API should return "Update" string instead of "Add"
                // but at the moment nothing updates and the API returns string "Add".
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody(String.format("[\"%s\",\"Add\"]", ID3));
            }
            if (path.equals(String.format("/bsdLoyalty/ResetPassword.php?key=%s", APP_KEY))) {
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody(String.format("Email has been sent to %s with the new password!", EMAIL3));
            }
            if (path.equals(String.format("/bsdLoyalty/getProgress.php?key=%s", APP_KEY))) {
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody(STATUS_PROGRESS);
            }
            if (path.equals(String.format("/bsdTransactions/add/?key=%s&contact=16666008&username=New%%20User", APP_KEY))) {
                return new MockResponse()
                        .setResponseCode(200)
                        .setBody("{\"status\":true,\"success\":{}}");
            }
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
                    break;
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
                    break;
                case "/addUser/":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(STATUS_SUCCESS);
                case "/bsdLoyalty/?function=updatePassword":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(STATUS_SUCCESS2);
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
                case "/pushNotification/getMessages":
                    // Test case for empty response
                    if (body.equals(MSG_REQ4_1)) {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody("");
                    } else if (body.equals(MSG_REQ4_2)) {
                        // Test case for no message response
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody("[]");
                    } else {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(pushMessageJson);
                    }
                case "/pushNotification/getMessagesByOsAndType":
                    // TODO: Update JSON when more info will be available
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(pushMessageJson);
                case "/pushNotification/getMessagesByImageType":
                    // TODO: Update JSON when more info will be available
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(pushMessageJson);
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
                case "/bsdContact/relocate.php":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{\"Success\":true}");
                case "/feedback/contactUs/index.php":
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{\"success\":\"true\"}");
                case "/bsdLoyalty/?function=checkGoogle":
                    if (body.equals(GOOGLE_AUTH_REQ3)) {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(String.format("[%s,\"%s\"]", ID3, TOKEN3));
                    }
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(STATUS_BLANK);
                case "/bsdLoyalty/?function=checkFacebook":
                    if (body.equals(FACEBOOK_AUTH_REQ3)) {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(String.format("[%s,\"%s\"]", ID3, TOKEN3));
                    }
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(STATUS_BLANK);
            }
            return new MockResponse().setResponseCode(404);
        }

        private String getStoreMock() {
            return "{\"status\":true,\n" +
                    "\"stores\":[{\"_id\":{\"$id\":\"1298129837189273\"},\n" +
                    "\"CustomerId\":\"123\",\n" +
                    "\"Date\":{\"sec\":1421863891,\"usec\":937000},\n" +
                    "\"StoreId\":\"1234\",\n" +
                    "\"StoreName\":\"MT VERNON & HWY 178\",\n" +
                    "\"Country\":\"USA\",\n" +
                    "\"Address1\":\"2659 Mt. Vernon Ave.\",\n" +
                    "\"Address2\":\"\",\n" +
                    "\"City\":\"Bakersfield\",\n" +
                    "\"State\":\"CA\",\n" +
                    "\"Zip\":\"93306\",\n" +
                    "\"Phone\":\"661-555-1234\",\n" +
                    "\"Fax\":\"661-555-1324\",\n" +
                    "\"Concept\":\"Panda Express\",\n" +
                    "\"Venue\":\"Street DT\",\n" +
                    "\"SubVenue\":\"Free Standing\",\n" +
                    "\"Region\":\"CCA\",\n" +
                    "\"RegionName\":\"CENTRAL CALIFORNIA\",\n" +
                    "\"Longitude\":\"-118.93041\",\n" +
                    "\"Latitude\":\"35.390652\",\n" +
                    "\"loc\":{\"type\":\"Point\",\"coordinates\":[-118.93041,35.390652],\"category\":\"Store\"},\n" +
                    "\"geoEnabled\":\"1\"}\n" +
                    "]}\n";
        }

        private String getStoreInfoMock() {
            return "{\"storeInfo\":\n" +
                    "{\"Id\":1,\n" +
                    "\"Ownership\":\"Corporate\",\n" +
                    "\"OwnershipID\":\"1\",\n" +
                    "\"Primary_Id\":\"1234\",\n" +
                    "\"StoreName\":\"A Store Name\",\n" +
                    "\"FranchiseName\":null,\n" +
                    "\"ClosingDate\":\"\",\n" +
                    "\"Region\":\"44\",\n" +
                    "\"RegionID\":\"44\",\n" +
                    "\"OpeningDate\":\"7\\/20\\/2005 3:00:00 AM\",\n" +
                    "\"Email\":\"blah@somecompany.com\",\n" +
                    "\"FaxNumber\":\"508-555-8505\",\n" +
                    "\"CustomerId\":1,\n" +
                    "\"PhoneNumber\":\"508-555-0500\",\n" +
                    "\"DisplayName\":\"Some display name\",\n" +
                    "\"PostalCode\":\"01234\",\n" +
                    "\"City\":\"Millbury\",\n" +
                    "\"State\":\"MA\",\n" +
                    "\"Address1\":\"70 Anywhere\",\n" +
                    "\"DMAID\":\"11\",\n" +
                    "\"Address2\":\"\",\n" +
                    "\"DMA\":\"Boston, MA-NH\",\n" +
                    "\"AreaID\":\"0\",\n" +
                    "\"Area\":\"Group (Wickham)\",\n" +
                    "\"DistrictID\":\"10\",\n" +
                    "\"District\":\"District (Arroyo)\",\n" +
                    "\"MarketID\":\"100\",\n" +
                    "\"Market\":\"Boston-Prov\"}\n" +
                    "}";
        }

        private String getLocationsMock() {
            return "{\"Location\":\n" +
                    "[{\"@attributes\":{\"ID\":\"1\"},\n" +
                    "\"LocationNumber\":\"1\",\n" +
                    "\"LocationName\":\"\",\n" +
                    "\"StreetAddress\":\"\",\n" +
                    "\"City\":\"\",\n" +
                    "\"State\":\"\",\n" +
                    "\"Zipcode\":\"\",\n" +
                    "\"Country\":\"\",\n" +
                    "\"X\":\"\",\n" +
                    "\"Y\":\"\",\n" +
                    "\"PhoneNumber\":\"\",\n" +
                    "\"Participation\":\"\"}]\n" +
                    "}";
        }
    };

    private static MockWebServer server;
    private static BeanstalkDataApi service;

    private static Gson getGson() {
        return new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(PushMessagesResponse.class, new PushMessagesDeserializer())
                .create();
    }

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
                .addConverterFactory(GsonConverterFactory.create(getGson()))
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
        Response<Contact[]> response = stringCall.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        Contact[] body = response.body();
        assertTrue(body == null);
        // NOTE: the assert below is for the case if [] is returned by the API.
        //assertTrue(body.length == 0);
    }

    @Test
    public void findUserByEmail() throws Exception {
        Call<Contact[]> stringCall = service.getContactByEmail(BuildConfig.APP_KEY, EMAIL2);
        Response<Contact[]> response = stringCall.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        Contact[] body = response.body();
        assertTrue(body.length > 0);

        Contact contact = body[0];
        assertTrue(EMAIL2.equals(contact.getEmail()));
    }

    @Test
    public void findUserByPhone() throws Exception {
        Call<Contact[]> stringCall = service.getContactByPhone(BuildConfig.APP_KEY, PHONE2);
        Response<Contact[]> response = stringCall.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        Contact[] body = response.body();
        assertTrue(body.length > 0);

        Contact contact = body[0];
        assertTrue(PHONE2.equals(contact.getPhone()));
    }

    @Test
    public void findUserByFkey() throws Exception {
        Call<Contact[]> stringCall = service.getContactByFkey(BuildConfig.APP_KEY, FKEY2);
        Response<Contact[]> response = stringCall.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        Contact[] body = response.body();
        assertTrue(body.length > 0);

        Contact contact = body[0];
        assertTrue(FKEY2.equals(contact.getFKey()));
    }

    @Test
    public void authorizeUserError() throws Exception {
        AuthenticateUserRequest request = new AuthenticateUserRequest(EMAIL1, PASSWORD1);
        Call<ResponseBody> call = service.authenticateUser(request.getEmail(), request.getPassword(), BuildConfig.APP_KEY, request.getTime());
        Response<ResponseBody> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String body = response.body().string();
        assertEquals(body, STATUS_ERROR);
    }

    @Test
    public void authorizeUser() throws Exception {
        AuthenticateUserRequest request = new AuthenticateUserRequest(EMAIL2, PASSWORD2);
        Call<ResponseBody> call = service.authenticateUser(request.getEmail(), request.getPassword(), BuildConfig.APP_KEY, request.getTime());
        Response<ResponseBody> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String body = response.body().string();
        assertNotEquals(body, STATUS_ERROR);

        JSONArray array = linkMockJsonArrayWithBody(body);
        assertEquals(array.length(), 2);
        assertEquals(array.optString(0), ID2);
        assertEquals(array.optString(1), TOKEN2);
    }

    @Test
    public void logoutUserError() throws Exception {
        Call<String> call = service.logoutUser(ID2, TOKEN1);
        Response<String> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String body = response.body();
        assertEquals(body, STATUS_INVALID_TOKEN);
    }

    @Test
    public void logoutUser() throws Exception {
        Call<String> call = service.logoutUser(ID2, TOKEN2);
        Response<String> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String body = response.body();
        assertEquals(body, STATUS_LOGGED_OUT);
    }

    @Test
    public void checkSessionError() throws Exception {
        Call<String> call = service.checkSession(ID2, "");
        Response<String> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String body = response.body();
        assertEquals(body, STATUS_SESSION_ERROR);
    }

    @Test
    public void checkSession() throws Exception {
        Call<String> call = service.checkSession(ID2, TOKEN2);
        Response<String> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String body = response.body();
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
        Response<ResponseBody> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String body = response.body().string();
        assertNotNull(body);

        JSONArray jsonArray = linkMockJsonArrayWithBody(body);
        assertEquals(2, jsonArray.length());
        assertEquals("Add", jsonArray.optString(1));

        String contactId = jsonArray.optString(0);
        Call<String> user = service.createUser(contactRequest.getEmail(), contactRequest.getPassword(), BuildConfig.APP_KEY, contactId);
        Response<String> userResponse = user.execute();
        assertEquals(userResponse.code(), HttpURLConnection.HTTP_OK);

        String userCreateResponse = userResponse.body();
        assertNotNull(userCreateResponse);
        assertEquals("Success", userCreateResponse);
    }

    @Test
    public void getContact() throws Exception {
        Call<Contact[]> call = service.getContact(BuildConfig.APP_KEY, ID3);
        Response<Contact[]> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        Contact[] body = response.body();
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
        Response<ResponseBody> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String body = response.body().string();
        assertNotNull(body);
    }

    @Test
    public void updateContact() throws Exception {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("contactId", ID3);
        data.put("contactZipCode", ZIP3);
        Call<String[]> call = service.updateContact(BuildConfig.APP_KEY, data);
        Response<String[]> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String[] body = response.body();
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
        Response<String> response = call.execute();

        assertEquals(response.code(), HttpURLConnection.HTTP_OK);
        assertEquals(response.body(), STATUS_SUCCESS2);
    }

    @Test
    public void resetPassword() throws Exception {
        Call<String> stringCall = service.resetPassword(BuildConfig.APP_KEY, EMAIL3);
        Response<String> response = stringCall.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String body = response.body();
        String expected = String.format("Email has been sent to %s with the new password!", EMAIL3);
        assertEquals(expected, body);
    }

    @Test
    public void getProgress() throws Exception {
        Call<RewardsCountResponse> call = service.getProgress(BuildConfig.APP_KEY, ID3);
        Response<RewardsCountResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        RewardsCountResponse body = response.body();
        assertNotNull(body.getCount());
    }

    @Test
    public void registerCard() throws Exception {
        Call<RegisterGiftCardResponse> call = service.registerGiftCard(BuildConfig.APP_KEY, ID3, TOKEN3, GIFT_CARD_NUMBER3, GIFT_CARD_PIN3);
        Response<RegisterGiftCardResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        // TODO: Check this API with a real Gift Card.
        RegisterGiftCardResponse body = response.body();
        assertTrue(!body.isFailed());
    }

    @Test
    public void inquireAboutCard() throws Exception {
        Call<CardBalanceResponse> call = service.inquireAboutCard(BuildConfig.APP_KEY, ID3, TOKEN3, GIFT_CARD_NUMBER3);
        Response<CardBalanceResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        // TODO: Check this API with real a Gift Card. At the moment mock web server returns {}
        CardBalanceResponse body = response.body();
        assertNotNull(body);
    }

    @Test
    public void inquireAboutCardRaw() throws Exception {
        Call<ResponseBody> call = service.inquireAboutCardRaw(BuildConfig.APP_KEY, ID3, TOKEN3, GIFT_CARD_NUMBER3);
        Response<ResponseBody> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        // TODO: Check this API with real a Gift Card. At the moment mock web server returns {}
        String body = response.body().string();
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
        Response<String> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        // TODO: Got more exotic statuses - fail2 and "Invalid Card or Security Code"444
        String body = response.body();
        assertEquals("card added", body); //TODO Check this 'cause response is empty
    }

    @Test
    public void getGiftCardList() throws Exception {
        // TODO: Need real Gift Card.
        Call<GiftCardListResponse> call = service.getGiftCardList(BuildConfig.APP_KEY, ID3, TOKEN3);
        Response<GiftCardListResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        GiftCardListResponse body = response.body();
        assertNotNull(body.isFailed());
    }

    @Test
    public void getGiftCardListRaw() throws Exception {
        // TODO: Need real Gift Card.
        Call<ResponseBody> call = service.getGiftCardListRaw(BuildConfig.APP_KEY, ID3, TOKEN3);
        Response<ResponseBody> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String body = response.body().string();
        assertNotNull(body);
    }

    @Test
    public void getStores() throws Exception {
        Call<StoresResponse> call = service.checkLocation(BuildConfig.APP_KEY, LAT3, LNG3);
        Response<StoresResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        StoresResponse body = response.body();
        assertNotNull(body);
        assertFalse(body.isFailed());
    }

    @Test
    public void getStoreInfo() throws Exception {
        Call<StoreInfoResponse> call = service.getStoreInfo(BuildConfig.APP_KEY, STORE_ID3);
        Response<StoreInfoResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        StoreInfoResponse body = response.body();
        assertNotNull(body);
        assertFalse(body.isFailed());
    }

    @Test
    public void getLocations() throws Exception {
        Call<LocationsResponse> call = service.getLocations(BuildConfig.APP_KEY);
        Response<LocationsResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        LocationsResponse body = response.body();
        assertNotNull(body);
        assertFalse(body.isFailed());
    }

    @Test
    public void startPayment() throws Exception {
        // TODO: Need more test data and more clarified business logic for this API.
        Call<PaymentTokenResponse> call = service.startPayment(BuildConfig.APP_KEY, ID3, LOYALTY_ID3, TOKEN3, PAYMENT_ID3, COUPONS3);
        Response<PaymentTokenResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String paymentToken = response.body().getPaymentToken();
        assertNotNull(paymentToken);
    }

    @Test
    public void setPreferredCard() throws Exception {
        Call<String> call = service.setPreferredCard(BuildConfig.APP_KEY, ID3, TOKEN3, GIFT_CARD_NUMBER3);
        Response<String> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String body = response.body();
        assertNotNull(body);
    }

    @Test
    public void getUserOffers() throws Exception {
        // TODO: Re-check this coupons
        Call<CouponResponse> call = service.getUserOffers(BuildConfig.APP_KEY, ID3);
        Response<CouponResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        Coupon[] coupons = response.body().getCoupons();
        assertTrue(coupons.length > 0);
    }

    @Test
    public void setTransactionEvent() throws Exception {
        // TODO: Need more details. It isn't clear what is username parameter and if it is incorrect the API returns an error.
        Call<TrackTransactionResponse> call = service.trackTransaction(BuildConfig.APP_KEY, ID3, USERNAME3, DETAILS3);
        Response<TrackTransactionResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        TrackTransactionResponse tx = response.body();
        assertTrue(tx.isStatus());
    }

    @Test
    public void enrollPushNotification() throws Exception {
        Call<PushSuccessResponse> call = service.enrollPushNotification(BuildConfig.APP_KEY, ID3, DEVICE_TOKEN3, PlatformType.ANDROID);
        Response<PushSuccessResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        PushSuccessResponse status = response.body();
        assertTrue(status.isSuccess());
    }

    @Test
    public void modifyPushNotification() throws Exception {
        Call<PushSuccessResponse> call = service.modifyPushNotification(BuildConfig.APP_KEY, ID3, DEVICE_TOKEN3, PlatformType.ANDROID);
        Response<PushSuccessResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        PushSuccessResponse status = response.body();
        assertTrue(status.isSuccess());
    }

    @Test
    public void deletePushNotification() throws Exception {
        Call<PushSuccessResponse> call = service.deletePushNotification(BuildConfig.APP_KEY, ID3);
        Response<PushSuccessResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        PushSuccessResponse status = response.body();
        assertTrue(status.isSuccess());
    }

    @Test
    public void getPushMessages() throws Exception {
        Call<PushMessagesResponse> call = service.getContactMessages(BuildConfig.APP_KEY, ID3, MAX_MESSAGES3);
        Response<PushMessagesResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        List<PushMessage> messages = response.body().getPushMessages();
        assertNotNull(messages);
        assertEquals(1, messages.size());

        PushMessage message = messages.get(0);
        assertEquals(ID3, message.getContactId());
    }

    @Test
    public void getPushMessagesNull() throws Exception {
        Call<PushMessagesResponse> call = service.getContactMessages(BuildConfig.APP_KEY, ID4_1, MAX_MESSAGES4);
        Response<PushMessagesResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        assertNull(response.body());
    }

    @Test
    public void getPushMessagesEmpty() throws Exception {
        Call<PushMessagesResponse> call = service.getContactMessages(BuildConfig.APP_KEY, ID4_2, MAX_MESSAGES4);
        Response<PushMessagesResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        List<PushMessage> messages = response.body().getPushMessages();
        assertNotNull(messages);
        assertEquals(0, messages.size());
    }

    @Test
    public void updateMessageStatus() throws Exception {
        Call<PushSuccessResponse> call = service.updateMessageStatus(BuildConfig.APP_KEY, MESSAGE_ID3, MessageType.READ);
        Response<PushSuccessResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        PushSuccessResponse status = response.body();
        assertTrue(status.isSuccess());
    }

    @Test
    public void getMessageById() throws Exception {
        Call<PushMessageByIdResponse> call = service.getMessageById(BuildConfig.APP_KEY, MESSAGE_ID3);
        Response<PushMessageByIdResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        PushMessageByIdResponse message = response.body();
        assertNotNull(message);
    }

    @Ignore
    @Test
    public void getCoordinatesByZip() throws Exception {
        String zip = "32818";
        Call<LocationResponse> call = service.getLocationByZipCode(BuildConfig.GOOGLE_MAPS_KEY, zip);
        Response<LocationResponse> response = call.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        LocationResponse body = response.body();
        assertNotNull(body);
        assertFalse(body.isFailed());
        assertEquals(body.getAddress(), "Orlando, FL 32818, USA");
        assertEquals(28.6403769, body.getLocation().getLatitude(), 0.1);
        assertEquals(-81.467637, body.getLocation().getLongitude(), 0.1);
    }

    @Test
    public void getContactAsset() throws Exception {
        Call<ContactAsset> request = service.getContactAsset(APP_KEY, ID3);
        Response<ContactAsset> response = request.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        ContactAsset contactAsset = response.body();
        assertNotNull(contactAsset);
        assertTrue(contactAsset.isSuccess());
        assertEquals(contactAsset.getCurrentImage(), CURRENT_ASSET_IMAGE3);
        assertEquals(contactAsset.getDefaultImage(), DEFAULT_ASSET_IMAGE3);
    }

    @Test
    public void getContactAssetError() throws Exception {
        Call<ContactAsset> request = service.getContactAsset("", ID3);
        Response<ContactAsset> response = request.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        ContactAsset contactAsset = response.body();
        assertNotNull(contactAsset);
        assertFalse(contactAsset.isSuccess());
    }

    @Test
    public void relocateContact() throws Exception {
        Call<RelocateResponse> request = service.relocateContact(APP_KEY, ID3, 1, -1);
        Response<RelocateResponse> response = request.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        RelocateResponse relocateResponse = response.body();
        assertNotNull(relocateResponse);
        assertTrue(relocateResponse.isSuccess());
    }

    @Test
    public void maintainLoyaltyCards() throws Exception {
        Call<String> request = service.maintainLoyaltyCards(APP_KEY, ID3, ID3);
        Response<String> response = request.execute();

        String body = response.body();
        assertNotNull(body);
        assertTrue(body.equals("card added"));
    }

    @Test
    public void contactUs() throws Exception {
        Call<ContactUsResponse> request = service.contactUs(APP_KEY, FIRST_NAME3, LAST_NAME3, EMAIL3, TO_EMAIL3, PHONE3, COMMENTS3);
        Response<ContactUsResponse> response = request.execute();

        ContactUsResponse body = response.body();
        assertNotNull(body);
        assertTrue("true".equals(body.getSuccess()));
    }

    @Test
    public void authenticateUserGoogle() throws Exception {
        Call<ResponseBody> request = service.authenticateUserGoogle(APP_KEY, GOOGLE_ID3, GOOGLE_TOKEN3);
        Response<ResponseBody> response = request.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String body = response.body().string();

        JSONArray array = linkMockJsonArrayWithBody(body);
        assertEquals(array.length(), 2);
        assertEquals(array.optString(0), ID3);
        assertEquals(array.optString(1), TOKEN3);
    }

    @Test
    public void authenticateUserGoogleError() throws Exception {
        Call<ResponseBody> request = service.authenticateUserGoogle(APP_KEY, GOOGLE_ID2, GOOGLE_TOKEN2);
        Response<ResponseBody> response = request.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String body = response.body().string();
        assertEquals(body, STATUS_BLANK);
    }

    @Test
    public void authenticateUserFacebook() throws Exception {
        Call<ResponseBody> request = service.authenticateUserFacebook(APP_KEY, FACEBOOK_ID3, FACEBOOK_TOKEN3);
        Response<ResponseBody> response = request.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String body = response.body().string();

        JSONArray array = linkMockJsonArrayWithBody(body);
        assertEquals(array.length(), 2);
        assertEquals(array.optString(0), ID3);
        assertEquals(array.optString(1), TOKEN3);
    }

    @Test
    public void authenticateUserFacebookError() throws Exception {
        Call<ResponseBody> request = service.authenticateUserFacebook(APP_KEY, FACEBOOK_ID2, FACEBOOK_TOKEN2);
        Response<ResponseBody> response = request.execute();
        assertEquals(response.code(), HttpURLConnection.HTTP_OK);

        String body = response.body().string();
        assertEquals(body, STATUS_BLANK);
    }

}
