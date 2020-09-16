package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.LookupEntity;
import hu.progmasters.gmistore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByName(String name);

    Optional<Product> findProductById(Long id);

    Optional<Product> findProductByProductCode(String productCode);

    Optional<Product> findProductBySlug(String slug);

    List<Product> findProductsByAddedBy(String name);

    List<Product> findProductsBySubCategory(LookupEntity category);

    @Query(value = "select p from Product p where p.discount>0 order by p.discount asc")
    List<Product>findProductByAndDiscountOrderByDiscountAsc();
}
