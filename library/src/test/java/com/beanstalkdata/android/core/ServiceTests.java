package com.beanstalkdata.android.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.beanstalkdata.android.BuildConfig;
import com.beanstalkdata.android.callback.OnReturnDataListener;
import com.beanstalkdata.android.callback.OnReturnListener;
import com.beanstalkdata.android.model.Contact;
import com.beanstalkdata.android.model.Coupon;
import com.beanstalkdata.android.model.GiftCard;
import com.beanstalkdata.android.request.AuthenticateUserRequest;
import com.beanstalkdata.android.request.ContactRequest;
import com.beanstalkdata.android.response.CardBalanceResponse;
import com.beanstalkdata.android.response.LocationResponse;
import com.beanstalkdata.android.response.PaymentTokenResponse;
import com.beanstalkdata.android.response.RegisterGiftCardResponse;
import com.beanstalkdata.android.response.StoresResponse;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class ServiceTests {
    private static BeanstalkService beanstalkService;
    //   private static BeanstalkService coreSpyService;
    private static String MOCK_REQUEST_DATA = "UnitTest generated data";
    private static String MOCK_MSG_STRING = "Some message from app resource string";
    private static LocationResponse.Location location;
    private static long TEST_TIMEOUT = 20;

    @BeforeClass
    public static void initCoreService() {
        location = mock(LocationResponse.Location.class);
        Context context = mock(Context.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        Resources resources = mock(Resources.class);

        when(location.getLatitude()).thenReturn(49.9808);
        when(location.getLongitude()).thenReturn(36.2527);

        when(editor.putString(anyString(), anyString())).thenReturn(editor);
        when(editor.commit()).thenReturn(true);
        when(editor.clear()).thenReturn(editor);

        when(sharedPreferences.edit()).thenReturn(editor);
        when(sharedPreferences.getString(anyString(), anyString())).thenReturn(MOCK_REQUEST_DATA);

        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
        when(context.getResources()).thenReturn(resources);
        when(resources.getString(anyInt())).thenReturn(MOCK_MSG_STRING);

        beanstalkService = new BeanstalkService(context, BuildConfig.APP_KEY, BuildConfig.GOOGLE_MAPS_KEY);
    }

    @Test  //used Log
    public void logoutUser() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        beanstalkService.logoutUser(new OnReturnListener() {
            @Override
            public void onFinished(String error) {
                System.out.println("Successfully call of OnReturnListener from BeanstalkService by logoutUser");
                signal.countDown();
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test //used Log
    public void getUserOffers() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        beanstalkService.getUserOffers(new OnReturnDataListener<Coupon[]>() {
            @Override
            public void onFinished(@Nullable Coupon[] data, String error) {
                System.out.println("Successfully call of OnReturnDataListener from BeanstalkService by getUserOffers");
                signal.countDown();
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test
    public void getProgress() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        beanstalkService.getProgress(new OnReturnDataListener<Double>() {
            @Override
            public void onFinished(@Nullable Double data, String error) {
                System.out.println("Successfully call of OnReturnDataListener from BeanstalkService by getProgress");
                signal.countDown();
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test //used log
    public void getContact() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        beanstalkService.getContact(new OnReturnDataListener<Contact>() {
            @Override
            public void onFinished(@Nullable Contact data, String error) {
                System.out.println("Successfully call of OnReturnDataListener from BeanstalkService by getContact");
                signal.countDown();
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test
    public void getLoyaltyInformation() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        beanstalkService.getLoyaltyInformation(new OnReturnListener() {
            @Override
            public void onFinished(String error) {
                System.out.println("Successfully call of OnReturnListener from BeanstalkService by getLoyaltyInformation");
                signal.countDown();
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test
    public void inquireAboutCard() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        final BeanstalkService coreSpyService = spy(beanstalkService);
        doReturn("").when(coreSpyService).getErrorFromCBResponse(any(CardBalanceResponse.class));
        coreSpyService.inquireAboutCard("6276000000221935380", new OnReturnDataListener<String>() {
            @Override
            public void onFinished(@NonNull String data, String error) {
                System.out.println("Successfully call of OnReturnDataListener from BeanstalkService by inquireAboutCard");
                signal.countDown();
                assertNotEquals("The returned data is null", data, null);
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test
    public void setPreferredCard() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        beanstalkService.setPreferredCard("6276000000221935380", new OnReturnListener() {
            @Override
            public void onFinished(String error) {
                System.out.println("Successfully call of OnReturnListener from BeanstalkService by setPreferredCard");
                signal.countDown();
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test
    public void startPaymentWithPaymentId() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        List<String> coupons = new ArrayList<>();
        coupons.add("firstCoupon");
        coupons.add("secondCoupon");
        BeanstalkService coreSpyService = spy(beanstalkService);
        doReturn("firstCoupon, secondCoupon").when(coreSpyService).getCouponsString(coupons);
        doReturn(true).when(coreSpyService).isPaymentTokenEmpty(any(PaymentTokenResponse.class));
        coreSpyService.startPaymentWithPaymentId("00001", coupons, new OnReturnDataListener<String>() {
            @Override
            public void onFinished(@Nullable String data, String error) {
                System.out.println("Successfully call of OnReturnDataListener from BeanstalkService by startPaymentWithPaymentId");
                signal.countDown();
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test
    public void registerNewGiftCard() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        final BeanstalkService coreSpyService = spy(beanstalkService);
        doReturn("").when(coreSpyService).getErrorFromRGCResponse(any(RegisterGiftCardResponse.class));
        coreSpyService.registerNewGiftCard("12345678", "1234", new OnReturnListener() {
            @Override
            public void onFinished(String error) {
                System.out.println("Successfully call of OnReturnListener from BeanstalkService by registerNewGiftCard");
                signal.countDown();
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test
    public void getGiftCardList() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        beanstalkService.getGiftCardList(new OnReturnDataListener<GiftCard[]>() {
            @Override
            public void onFinished(@Nullable GiftCard[] data, @Nullable String error) {
                System.out.println("Successfully call of OnReturnDataListener from BeanstalkService by getGiftCardList");
                signal.countDown();
                assertNotEquals("Both are null", data, error);
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test
    public void resetPassword() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        beanstalkService.resetPassword("test@gmail.com", new OnReturnDataListener<String>() {
            @Override
            public void onFinished(@Nullable String data, @Nullable String error) {
                System.out.println("Successfully call of OnReturnDataListener from BeanstalkService by resetPassword");
                signal.countDown();
                assertNotEquals("Both are null", data, error);
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test
    public void updatePassword() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        beanstalkService.updatePassword("test@gmail.com", new OnReturnDataListener<String>() {
            @Override
            public void onFinished(@Nullable String data, @Nullable String error) {
                System.out.println("Successfully call of OnReturnDataListener from BeanstalkService by updatePassword");
                signal.countDown();
                assertNotEquals("Both are null", data, error);
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test
    public void authenticateUser() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        AuthenticateUserRequest userRequest = new AuthenticateUserRequest("test@example.com", "password");
        final BeanstalkService coreSpyService = spy(beanstalkService);
        doReturn(null).when(coreSpyService).parseAuthUserResponse(anyString());
        coreSpyService.authenticateUser(userRequest, new OnReturnDataListener<Boolean>() {
            @Override
            public void onFinished(@Nullable Boolean data, @Nullable String error) {
                System.out.println("Successfully call of OnReturnDataListener from BeanstalkService by authenticateUser");
                signal.countDown();
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test
    public void updateContact() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        ContactRequest contactRequest = new ContactRequest("01.01.1980", "test@example.com", "Tester", "Test", "0987654321", "61000", "Best reward");
        beanstalkService.updateContact(contactRequest, new OnReturnListener() {
            @Override
            public void onFinished(String error) {
                System.out.println("Successfully call of OnReturnListener from BeanstalkService by updateContact");
                signal.countDown();
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test
    public void addContactWithEmail() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        ContactRequest contactRequest = new ContactRequest("01.01.1980", "test@example.com", "Tester", "Test", "0987654321", "61000", "Best reward");
        beanstalkService.addContactWithEmail(contactRequest, new OnReturnListener() {
            @Override
            public void onFinished(String error) {
                System.out.println("Successfully call of OnReturnListener from BeanstalkService by addContactWithEmail");
                signal.countDown();
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test
    public void paperCardRegistration() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        beanstalkService.paperCardRegistration("12345678", "1234", new OnReturnListener() {
            @Override
            public void onFinished(String error) {
                System.out.println("Successfully call of OnReturnListener from BeanstalkService by paperCardRegistration");
                signal.countDown();
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test
    public void checkStores() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        beanstalkService.checkStores("61000", new OnReturnDataListener<LocationResponse>() {
            @Override
            public void onFinished(LocationResponse data, String error) {
                System.out.println("Successfully call of OnReturnListener from BeanstalkService by checkStores");
                signal.countDown();
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test
    public void checkLocation() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        beanstalkService.checkLocation(location, new OnReturnDataListener<StoresResponse>() {
            @Override
            public void onFinished(StoresResponse data, String error) {
                System.out.println("Successfully call of OnReturnListener from BeanstalkService by checkLocation");
                signal.countDown();
            }
        });
        signal.await(TEST_TIMEOUT, TimeUnit.SECONDS);
    }

}
