package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.NewRatingRequest;
import hu.progmasters.gmistore.dto.RatingDetails;
import hu.progmasters.gmistore.dto.RatingInitData;
import hu.progmasters.gmistore.enums.DomainType;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.*;
import hu.progmasters.gmistore.repository.ProductRepository;
import hu.progmasters.gmistore.repository.RatingRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {
    private RatingService ratingService;

    @Mock
    private RatingRepository ratingRepositoryMock;

    @Mock
    private ProductRepository productRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Spy
    List<Rating> ratingList = new ArrayList<>();

    @Mock
    private Principal principalMock;

    @Spy
    List<RatingDetails> ratingDetailsList = new ArrayList<>();

    @BeforeEach
    public void setup() {
        ratingService = new RatingService(ratingRepositoryMock, productRepositoryMock, userRepositoryMock);
    }


    private final Supplier<Product> productSupplier = () -> {
        LookupEntity mainCategory = new LookupEntity();
        mainCategory.setId(1L);
        mainCategory.setDisplayFlag(true);
        mainCategory.setLookupKey("main_category");
        mainCategory.setDisplayName("Main Category");
        mainCategory.setDomainType(DomainType.PRODUCT_CATEGORY);

        LookupEntity subCategory = new LookupEntity();
        subCategory.setId(2L);
        subCategory.setDisplayFlag(true);
        subCategory.setLookupKey("sub_category");
        subCategory.setDisplayName("Sub Category");
        subCategory.setDomainType(DomainType.PRODUCT_CATEGORY);
        subCategory.setParent(mainCategory);

        Product product = new Product();

        Inventory inventory = new Inventory();
        inventory.setQuantityAvailable(1);
        inventory.setId(1L);
        inventory.setProduct(product);

        product.setId(1L);
        product.setName("test product");
        product.setProductCode("GMI123");
        product.setSlug("test-product-gmi123");
        product.setDescription("test 1");
        product.setMainCategory(mainCategory);
        product.setSubCategory(subCategory);
        product.setActive(true);
        product.setPrice(100.0);
        product.setDiscount(0);
        product.setWarrantyMonths(12);
        product.setAddedBy("unknown");
        product.setInventory(inventory);
        return product;
    };

    private final Supplier<Rating> ratingSupplier = () -> {
        Rating rating = new Rating();
        rating.setId(1L);
        rating.setActive(true);
        rating.setUsername("tesztElek01");
        rating.setProduct(new Product());
        rating.setActualRating(5);
        rating.setTitle("Jó termék");
        rating.setPositiveComment("Jó termék");
        rating.setNegativeComment("");
        rating.setPictures(new HashSet<>());
        rating.setUpVotes(5);
        rating.setVoters(new HashSet<>());
        rating.setReported(true);
        rating.setTimeStamp(LocalDateTime.now());
        return rating;
    };

    private final Supplier<RatingDetails> ratingDetailsSupplier = () -> {
        RatingDetails ratingDetails = new RatingDetails();
        ratingDetails.setId(1L);
        ratingDetails.setActive(true);
        ratingDetails.setUsername("tesztElek01");
        ratingDetails.setActualRating(5);
        ratingDetails.setTitle("Termék értékelés");
        ratingDetails.setPositiveComment("jó termék");
        ratingDetails.setNegativeComment("rossz termék");
        ratingDetails.setPictures(new HashSet<>());
        ratingDetails.setUpVotes(5);
        ratingDetails.setVoters(new HashSet<>());
        ratingDetails.setReported(true);
        ratingDetails.setTimeStamp(LocalDateTime.now());
        return ratingDetails;
    };

    private final Supplier<NewRatingRequest> newRatingRequestDTOSupplier = () -> {
        NewRatingRequest newRatingRequest = new NewRatingRequest();
        newRatingRequest.setProduct("test-product-gmi123");
        newRatingRequest.setActualRating(5);
        newRatingRequest.setTitle("Termék értékelés");
        newRatingRequest.setPositiveComment("jó termék");
        newRatingRequest.setNegativeComment("rossz termék");
        newRatingRequest.setPictures(new HashSet<>());
        return newRatingRequest;
    };


    private final Supplier<User> userSupplier = () -> {
        User user = new User();
        user.setId(1L);
        user.setUsername("tesztElek01");
        user.setLastName("Teszt");
        user.setFirstName("Elek");
        user.setShippingAddress(new Address());
        user.setBillingAddress(new Address());
        user.setPassword("Teszt01@");
        user.setEmail("teszt-elek@gmail.com");
        user.setPhoneNumber("06324356644");
        user.setRoles(Arrays.asList(Role.ROLE_USER, Role.ROLE_ADMIN));
        user.setRegistered(LocalDateTime.now());
        user.setActive(true);
        user.setCart(new Cart());
        user.setFavoriteProducts(new HashSet<>());
        user.setOrderList(new ArrayList<>());
        return user;
    };

    @Test
    public void getRatingsByProductSlugTest_addOneProductWithOneRating_resultSizeOne() {
        Product product = productSupplier.get();
        ratingList.add(ratingSupplier.get());

        RatingDetails ratingDetails = ratingDetailsSupplier.get();
        ratingDetailsList.add(ratingDetails);

        when(ratingRepositoryMock.findAllByProductSlug(product.getSlug())).thenReturn(ratingList);

        List<RatingDetails> televizio = ratingService.getRatingsByProductSlug("test-product-gmi123");

        assertEquals(1, televizio.size());

    }

    @Test
    public void getRatingsByProductSlugTest_addOneProductWithTwoRating_resultSizeTwo() {
        Product product = productSupplier.get();
        Rating rating1 = ratingSupplier.get();
        Rating rating2 = ratingSupplier.get();
        rating2.setId(2L);

        ratingList.add(rating1);
        ratingList.add(rating2);

        RatingDetails ratingDetails = ratingDetailsSupplier.get();
        ratingDetailsList.add(ratingDetails);

        when(ratingRepositoryMock.findAllByProductSlug(product.getSlug())).thenReturn(ratingList);

        List<RatingDetails> list = ratingService.getRatingsByProductSlug("test-product-gmi123");

        assertEquals(2, list.size());

        verify(ratingRepositoryMock, times(1)).findAllByProductSlug(product.getSlug());

    }

    @Test
    public void createTest_resultFalse() {
        Product product = productSupplier.get();
        NewRatingRequest newRatingRequest = newRatingRequestDTOSupplier.get();
        Rating rating = ratingSupplier.get();

        when(productRepositoryMock.findProductBySlug("test-product-gmi123")).thenReturn(Optional.of(product));
        when(principalMock.getName()).thenReturn("unknown");
        when(ratingRepositoryMock.findRatingByUsername("unknown", product)).thenReturn(Optional.of(rating));

        boolean result = ratingService.create(newRatingRequest, principalMock);

        assertFalse(result);
        verify(productRepositoryMock, times(1)).findProductBySlug("test-product-gmi123");
        verify(ratingRepositoryMock, times(1)).findRatingByUsername("unknown", product);
        verify(principalMock,times(2)).getName();


    }

    @Test
    public void createTest_resultTrue() {
        Product product = productSupplier.get();
        NewRatingRequest newRatingRequest = newRatingRequestDTOSupplier.get();

        when(productRepositoryMock.findProductBySlug("test-product-gmi123")).thenReturn(Optional.of(product));
        when(principalMock.getName()).thenReturn("unknown");
        when(ratingRepositoryMock.findRatingByUsername("unknown", product)).thenReturn(Optional.empty());

        boolean result = ratingService.create(newRatingRequest, principalMock);

        assertTrue(result);
        verify(productRepositoryMock, times(1)).findProductBySlug("test-product-gmi123");
        verify(ratingRepositoryMock, times(1)).findRatingByUsername("unknown", product);
        verify(principalMock,times(3)).getName();

    }

    @Test
    public void removeRatingTest_resultTrueAndSetTheProductRatingInactive() {
        Rating rating = ratingSupplier.get();
        User user = userSupplier.get();

        when(ratingRepositoryMock.findById(1L)).thenReturn(Optional.of(rating));
        when(principalMock.getName()).thenReturn("tesztElek01");
        when(userRepositoryMock.findUserByUsername("tesztElek01")).thenReturn(Optional.of(user));

        boolean result = ratingService.removeRating(1L, principalMock);
        assertTrue(result);
        verify(ratingRepositoryMock, times(1)).findById(1L);
        verify(userRepositoryMock, times(1)).findUserByUsername("tesztElek01");
        verify(principalMock,times(1)).getName();

    }

    @Test
    public void removeRatingTest_userNotFound_resultFalse() {
        Rating rating = ratingSupplier.get();

        when(ratingRepositoryMock.findById(1L)).thenReturn(Optional.of(rating));
        when(principalMock.getName()).thenReturn("tesztElek01");
        when(userRepositoryMock.findUserByUsername("tesztElek01")).thenReturn(Optional.empty());

        boolean result = ratingService.removeRating(1L, principalMock);
        assertFalse(result);

        verify(ratingRepositoryMock, times(1)).findById(1L);
        verify(userRepositoryMock, times(1)).findUserByUsername("tesztElek01");
        verify(principalMock,times(1)).getName();

    }

    @Test
    public void removeRatingTest_ratingNotFound_resultFalse() {
        User user = userSupplier.get();

        when(ratingRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        when(principalMock.getName()).thenReturn("tesztElek01");
        when(userRepositoryMock.findUserByUsername("tesztElek01")).thenReturn(Optional.of(user));

        boolean result = ratingService.removeRating(1L, principalMock);
        assertFalse(result);

        verify(ratingRepositoryMock, times(1)).findById(1L);
        verify(userRepositoryMock, times(1)).findUserByUsername("tesztElek01");
        verify(principalMock,times(1)).getName();

    }

    @Test
    public void removeRatingTest_ratingNotFound_andUserNotFound_resultFalse() {
        when(ratingRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        when(principalMock.getName()).thenReturn("tesztElek01");
        when(userRepositoryMock.findUserByUsername("tesztElek01")).thenReturn(Optional.empty());

        boolean result = ratingService.removeRating(1L, principalMock);
        assertFalse(result);

        verify(ratingRepositoryMock, times(1)).findById(1L);
        verify(userRepositoryMock, times(1)).findUserByUsername("tesztElek01");
        verify(principalMock,times(1)).getName();

    }

    @Test
    public void upVoteRatingTest_result_secondVotersName() {
        Rating rating = ratingSupplier.get();
        rating.getVoters().add("tesztElek01");

        when(ratingRepositoryMock.findById(1L)).thenReturn(Optional.of(rating));

        ratingService.upVoteRating(1L, "tesztElek");

        Set<String> voters = rating.getVoters();
        Object[] result = voters.toArray();

        String tesztelek = "";

        for (int i = 0; i < result.length; i++) {
            if (i == 1) {
                tesztelek = result[1].toString();
            }
        }
        assertEquals("tesztElek", tesztelek);
        assertEquals(2, rating.getVoters().size());

        verify(ratingRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void upVoteRatingTest_AddMoreVoters_AndResultVoterSetSize_5() {
        Rating rating = ratingSupplier.get();
        rating.getVoters().add("tesztElek01");

        when(ratingRepositoryMock.findById(1L)).thenReturn(Optional.of(rating));

        ratingService.upVoteRating(1L, "tesztElek");
        ratingService.upVoteRating(1L, "tesztGéza");
        ratingService.upVoteRating(1L, "tesztJudit");
        ratingService.upVoteRating(1L, "tesztÁbel");

        assertEquals(5, rating.getVoters().size());

        verify(ratingRepositoryMock, times(4)).findById(1L);

    }

    @Test
    public void upVoteRatingTest_AddVoters_AndRemoveVoter_AndResultSizeIsGood() {
        Rating rating = ratingSupplier.get();
        rating.getVoters().add("tesztElek01");

        when(ratingRepositoryMock.findById(1L)).thenReturn(Optional.of(rating));

        ratingService.upVoteRating(1L, "tesztElek02");
        ratingService.removeUpVoteRating(1L, "tesztElek02");

        int result = rating.getUpVotes();
        assertEquals(5, result);

        verify(ratingRepositoryMock, times(2)).findById(1L);

    }

    @Test
    public void upVoteRatingTest_AddMoreVoters_AndRemoveOneVoter_AndResultSizeIsGood() {
        Rating rating = ratingSupplier.get();
        rating.getVoters().add("tesztElek01");

        when(ratingRepositoryMock.findById(1L)).thenReturn(Optional.of(rating));

        ratingService.upVoteRating(1L, "tesztElek02");
        ratingService.upVoteRating(1L, "tesztElek04");
        ratingService.upVoteRating(1L, "tesztElek05");
        ratingService.removeUpVoteRating(1L, "tesztElek02");

        int result = rating.getUpVotes();
        assertEquals(7, result);

        verify(ratingRepositoryMock, times(4)).findById(1L);

    }

    @Test
    public void repostRatingTest_resultTrue() {
        Rating rating = ratingSupplier.get();

        when(ratingRepositoryMock.findById(1L)).thenReturn(Optional.of(rating));

        boolean result = ratingService.reportRating(1L);
        assertTrue(result);

        verify(ratingRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void repostRatingTest_resultFalse() {

        when(ratingRepositoryMock.findById(1L)).thenReturn(Optional.empty());


        boolean result = ratingService.reportRating(1L);
        assertFalse(result);

        verify(ratingRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void getInitDataTest() {
        Product product = productSupplier.get();

        when(productRepositoryMock.findProductBySlug("test-product-gmi123")).thenReturn(Optional.of(product));

        RatingInitData initData = ratingService.getInitData("test-product-gmi123");

        assertEquals("test product", initData.getName());

        verify(productRepositoryMock, times(1)).findProductBySlug("test-product-gmi123");
    }

}
