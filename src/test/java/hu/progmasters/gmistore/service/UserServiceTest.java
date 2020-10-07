package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.RolesInitDto;
import hu.progmasters.gmistore.dto.product.ProductDto;
import hu.progmasters.gmistore.dto.user.UserDto;
import hu.progmasters.gmistore.dto.user.UserEditableDetailsDto;
import hu.progmasters.gmistore.dto.user.UserListDetailDto;
import hu.progmasters.gmistore.enums.DomainType;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.*;
import hu.progmasters.gmistore.repository.PasswordTokenRepository;
import hu.progmasters.gmistore.repository.ProductRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private UserService userService;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private SessionRegistry sessionRegistryMock;

    @Mock
    private PasswordTokenRepository passwordTokenRepositoryMock;

    @Mock
    private ProductRepository productRepositoryMock;

    @Mock
    private Principal principalMock;

    @Spy
    private List<UserListDetailDto> userListDetailDtos = new ArrayList<>();

    @BeforeEach
    public void setup() {
        userService = new UserService(
                passwordEncoderMock,
                userRepositoryMock,
                sessionRegistryMock,
                passwordTokenRepositoryMock,
                productRepositoryMock);
    }

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

    private final Supplier<UserEditableDetailsDto> userEditableDetailsDtoSupplier = () -> {
        UserEditableDetailsDto editableDetailsDto = new UserEditableDetailsDto();
        editableDetailsDto.setId(1L);
        editableDetailsDto.setUsername("tesztElek01");
        editableDetailsDto.setLastName("Teszt");
        editableDetailsDto.setFirstName("Elek");
        editableDetailsDto.setShippingAddress(new Address());
        editableDetailsDto.setBillingAddress(new Address());
        editableDetailsDto.setPhoneNumber("06324356644");
        editableDetailsDto.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
        return editableDetailsDto;
    };

    @Test
    public void getUserByUsernameTest_resultOneUser() {
        User user = userSupplier.get();
        when(userRepositoryMock.findUserByUsername("tesztElek01")).thenReturn(Optional.of(user));

        User tesztElek01 = userService.getUserByUsername("tesztElek01");
        assertEquals("tesztElek01", tesztElek01.getUsername());
    }

    @Test
    public void getUserByUsernameTest_resultNull() {
        when(userRepositoryMock.findUserByUsername("tesztElek01")).thenReturn(Optional.empty());
        User tesztElek01 = userService.getUserByUsername("tesztElek01");
        assertNull(tesztElek01);
    }

    @Test
    public void getUserByIdTest_resultOne() {
        User user = userSupplier.get();
        when(userRepositoryMock.findUserById(1L)).thenReturn(Optional.of(user));
        User userById = userService.getUserById(1L);
        assertEquals("tesztElek01", userById.getUsername());
    }

    @Test
    public void getUserByIdTest_result_UsernameNotFoundException() {
        when(userRepositoryMock.findUserById(1L)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void getUserByNameTest_resultOneUser() {
        User user = userSupplier.get();
        when(userRepositoryMock.findUserByUsername("tesztElek01")).thenReturn(Optional.of(user));

        User tesztElek01 = userService.getUserByName("tesztElek01");
        assertEquals("tesztElek01", tesztElek01.getUsername());
    }

    @Test
    public void getUserByEmailTest_resultOneUser() {
        User user = userSupplier.get();
        when(userRepositoryMock.findUserByEmail("teszt-elek@gmail.com")).thenReturn(Optional.of(user));

        User tesztElek01 = userService.findUserByEmail("teszt-elek@gmail.com");
        assertEquals("tesztElek01", tesztElek01.getUsername());
    }

    @Test
    public void getUserDataTest_resultOneUser() {
        User user = userSupplier.get();
        when(userRepositoryMock.findUserByUsername("tesztElek01")).thenReturn(Optional.of(user));

        UserDto tesztElek01 = userService.getUserData("tesztElek01");

        assertEquals("tesztElek01", tesztElek01.getUsername());
    }

    @Test
    public void updateByUserNameTest_resultNullpointer() {
        UserEditableDetailsDto userEditableDetailsDto = userEditableDetailsDtoSupplier.get();

        when(userRepositoryMock.findUserByUsername("tesztElek01")).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class,()->userService.updateUserByName(userEditableDetailsDto));

    }

    @Test
    public void updateUserByName_resultSuccessUpdate(){
        UserEditableDetailsDto userEditableDetailsDto = userEditableDetailsDtoSupplier.get();
        userEditableDetailsDto.setFirstName("John");
        User user = userSupplier.get();

        when(userRepositoryMock.findUserByUsername("tesztElek01")).thenReturn(Optional.of(user));
        when(userRepositoryMock.save(any(User.class))).thenAnswer(returnsFirstArg());

        userService.updateUserByName(userEditableDetailsDto);

        assertEquals("John",user.getFirstName());

    }

    @Test
    public void updateUserById_resultSuccessUpdate(){
        UserEditableDetailsDto userEditableDetailsDto = userEditableDetailsDtoSupplier.get();
        userEditableDetailsDto.setFirstName("John");
        User user = userSupplier.get();

        when(userRepositoryMock.findUserById(1L)).thenReturn(Optional.of(user));
        when(userRepositoryMock.save(any(User.class))).thenAnswer(returnsFirstArg());

        userService.updateUserById(userEditableDetailsDto);

        assertEquals("John",user.getFirstName());

    }

    @Test
    public void addProductToFavorites_resultTrue(){
        User user = userSupplier.get();
        Product product = productSupplier.get();

        when(userRepositoryMock.findUserByUsername(principalMock.getName())).thenReturn(Optional.of(user));
        when(productRepositoryMock.findById(1L)).thenReturn(Optional.of(product));


        boolean result = userService.addProductToFavorites(1L, principalMock);
        assertTrue(result);

    }
    @Test
    public void addProductToFavorites_resultFalse(){
        Product product = productSupplier.get();

        when(userRepositoryMock.findUserByUsername(principalMock.getName())).thenReturn(Optional.empty());
        when(productRepositoryMock.findById(1L)).thenReturn(Optional.of(product));


        boolean result = userService.addProductToFavorites(1L, principalMock);
        assertFalse(result);

    }

    @Test
    public void removeProductFromFavoritesTest_result_True(){
        User user = userSupplier.get();
        Product product = productSupplier.get();
        user.getFavoriteProducts().add(product);

        when(userRepositoryMock.findUserByUsername(principalMock.getName())).thenReturn(Optional.of(user));
        when(productRepositoryMock.findById(1L)).thenReturn(Optional.of(product));

        boolean result = userService.removeProductFromFavorites(1L, principalMock);

        assertTrue(result);

    }

    @Test
    public void removeProductFromFavoritesTest_result_False(){
        Product product = productSupplier.get();

        when(userRepositoryMock.findUserByUsername(principalMock.getName())).thenReturn(Optional.empty());
        when(productRepositoryMock.findById(1L)).thenReturn(Optional.of(product));

        boolean result = userService.removeProductFromFavorites(1L, principalMock);

        assertFalse(result);

        verify(userRepositoryMock,times(1)).findUserByUsername(principalMock.getName());
        verify(productRepositoryMock,times(1)).findById(1L);
    }

    @Test
    public void getUserList_return_ListSize_Zero(){

        when(userRepositoryMock.findAllUsersWithListDetails()).thenReturn(userListDetailDtos);

        List<UserListDetailDto> userList = userService.getUserList();

        assertEquals(0,userList.size());
    }

    @Test
    public void getCountOfFavoriteProductsTest_resultOne(){
        User user = userSupplier.get();
        Product product = productSupplier.get();
        user.getFavoriteProducts().add(product);

        when(userRepositoryMock.findUserByUsername(principalMock.getName())).thenReturn(Optional.of(user));

        int result = userService.getCountOfFavoriteProducts(principalMock);
        assertEquals(1,result);

    }

    @Test
    public void getCountOfFavoriteProductsTest_addTwoProductToList_resultTwo(){
        User user = userSupplier.get();
        Product product = productSupplier.get();
        Product product2 = productSupplier.get();
        product2.setId(2L);
        user.getFavoriteProducts().add(product);
        user.getFavoriteProducts().add(product2);

        when(userRepositoryMock.findUserByUsername(principalMock.getName())).thenReturn(Optional.of(user));

        int result = userService.getCountOfFavoriteProducts(principalMock);
        assertEquals(2,result);

    }

    @Test
    public void getCountOfFavoriteProductsTest_resultSizeZero(){

        when(userRepositoryMock.findUserByUsername(principalMock.getName())).thenReturn(Optional.empty());

        int result = userService.getCountOfFavoriteProducts(principalMock);
        assertEquals(0,result);

    }

    @Test
    public void getFavoriteProductsTest_resultSetSizeTwo(){
        User user = userSupplier.get();
        Product product = productSupplier.get();
        Product product2 = productSupplier.get();
        product2.setId(2L);
        user.getFavoriteProducts().add(product);
        user.getFavoriteProducts().add(product2);

        when(userRepositoryMock.findUserByUsername(principalMock.getName())).thenReturn(Optional.of(user));

        Set<ProductDto> favoriteProducts = userService.getFavoriteProducts(principalMock);

        assertEquals(2,favoriteProducts.size());
    }

    @Test
    public void getFavoriteProductsTest_resultSetSizeZero(){
        User user = userSupplier.get();

        when(userRepositoryMock.findUserByUsername(principalMock.getName())).thenReturn(Optional.of(user));

        Set<ProductDto> favoriteProducts = userService.getFavoriteProducts(principalMock);

        assertEquals(0,favoriteProducts.size());
    }
    @Test
    public void getFavoriteProductsTest_userNotFoundException(){

        when(userRepositoryMock.findUserByUsername(principalMock.getName())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, ()->userService.getFavoriteProducts(principalMock));
    }

}
