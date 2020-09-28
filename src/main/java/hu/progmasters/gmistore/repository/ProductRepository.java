package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.dto.product.ProductTableDto;
import hu.progmasters.gmistore.model.LookupEntity;
import hu.progmasters.gmistore.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findProductByName(String name);

    Optional<Product> findProductById(Long id);

    Optional<Product> findProductByProductCode(String productCode);

    Optional<Product> findProductBySlug(String slug);

    List<Product> findProductsByAddedBy(String name);

    @Query("SELECT p FROM Product p WHERE p.subCategory = :category AND p.active = true")
    Page<Product> findProductsBySubCategory(LookupEntity category, Pageable pageable);

    @Query(value = "select p from Product p where p.discount>0 and p.active=true order by p.discount desc")
    List<Product> findProductByAndDiscountOrderByDiscountAsc();

    @Query("SELECT p FROM Product p WHERE p.discount > 0 AND p.active = true")
    Page<Product> findDiscountedProducts(Pageable pageable);

    @Query(value = "SELECT max(price) FROM product", nativeQuery = true)
    Double getHighestPrice();

    @Query("select new hu.progmasters.gmistore.dto.product.ProductTableDto(" +
            " p.id," +
            " p.name," +
            " p.subCategory," +
            " p.price," +
            " p.active," +
            "p.pictureUrl," +
            "p.slug) from Product p")
    List<ProductTableDto> findAllToTable();

    @Query("SELECT p FROM Product p WHERE " +
            "(p.name LIKE %:query% AND p.active = true) OR " +
            "(p.description LIKE %:query% AND p.active = true)")
    Page<Product> findProductsBySearchInput(@Param("query") String query, Pageable pageable);
}
