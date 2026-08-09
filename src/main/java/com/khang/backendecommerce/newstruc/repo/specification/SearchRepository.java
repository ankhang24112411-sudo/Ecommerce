package com.khang.backendecommerce.newstruc.repo.specification;

import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.PageResponse;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import com.khang.backendecommerce.newstruc.entity.StoreEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.khang.backendecommerce.infrastructure.util.AppConst.SEARCH_SPEC_OPERATOR;

@Component
@Slf4j
public class SearchRepository {
    private EntityManager entityManager;
//    public BaseResponse<?> searchProductByCriteriaWithJoin(Pageable pageable, String[] product, String[] store) {
//        log.info("-------------- searchUserByCriteriaWithJoin --------------");
//
//        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<ProductEntity> query = builder.createQuery(ProductEntity.class);
//        Root<ProductEntity> productRoot = query.from(ProductEntity.class);
//        Join<ProductEntity, StoreEntity> storeRoot = productRoot.join("store");
//
//        List<Predicate> productPreList = new ArrayList<>();
//        Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
//        for (String u : product) {
//            Matcher matcher = pattern.matcher(u);
//            if (matcher.find()) {
//                SpecSearchCriteria searchCriteria = new SpecSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
//                productPreList.add(toProductPredicate(productRoot, builder, searchCriteria));
//            }
//        }
//
//        List<Predicate> storePreList = new ArrayList<>();
//        for (String a : store) {
//            Matcher matcher = pattern.matcher(a);
//            if (matcher.find()) {
//                SpecSearchCriteria searchCriteria = new SpecSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
//                storePreList.add(toStorePredicate(storeRoot, builder, searchCriteria));
//            }
//        }
//
//        Predicate userPre = builder.or(productPreList.toArray(new Predicate[0]));
//        Predicate addPre = builder.or(storePreList.toArray(new Predicate[0]));
//        Predicate finalPre = builder.and(userPre, addPre);
//
//        query.where(finalPre);
//
//        List<ProductEntity> products = entityManager.createQuery(query)
//                .setFirstResult(pageable.getPageNumber())
//                .setMaxResults(pageable.getPageSize())
//                .getResultList();
//
//        long count = countUserJoinAddress(product, store);
//
//     return BaseResponse.ofSuccess(PageResponse.of(products, pageable, count));
//    }
//
//    private Predicate toProductPredicate(Root<ProductEntity> root, CriteriaBuilder builder, SpecSearchCriteria criteria) {
//        log.info("-------------- toUserPredicate --------------");
//        return switch (criteria.getOperation()) {
//            case EQUALITY -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
//            case NEGATION -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
//            case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
//            case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
//            case LIKE -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
//            case STARTS_WITH -> builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
//            case ENDS_WITH -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
//            case CONTAINS -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
//        };
//    }
//
//    private Predicate toStorePredicate(Join<ProductEntity, StoreEntity> root, CriteriaBuilder builder, SpecSearchCriteria criteria) {
//        log.info("-------------- toAddressPredicate --------------");
//        return switch (criteria.getOperation()) {
//            case EQUALITY -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
//            case NEGATION -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
//            case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
//            case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
//            case LIKE -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
//            case STARTS_WITH -> builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
//            case ENDS_WITH -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
//            case CONTAINS -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
//        };
//    }
//    private long countUserJoinAddress(String[] product, String[] store) {
//        log.info("-------------- countUserJoinAddress --------------");
//
//        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Long> query = builder.createQuery(Long.class);
//        Root<ProductEntity> productRoot = query.from(ProductEntity.class);
//        Join<ProductEntity, StoreEntity> storeRoot = productRoot.join("addresses");
//
//        List<Predicate> productPreList = new ArrayList<>();
//
//        Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
//        for (String u : product) {
//            Matcher matcher = pattern.matcher(u);
//            if (matcher.find()) {
//                SpecSearchCriteria searchCriteria = new SpecSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
//                productPreList.add(toProductPredicate(productRoot, builder, searchCriteria));
//            }
//        }
//
//        List<Predicate> storePreList = new ArrayList<>();
//        for (String a : store) {
//            Matcher matcher = pattern.matcher(a);
//            if (matcher.find()) {
//                SpecSearchCriteria searchCriteria = new SpecSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
//                storePreList.add(toStorePredicate(storeRoot, builder, searchCriteria));
//            }
//        }
//
//        Predicate productPre = builder.or(productPreList.toArray(new Predicate[0]));
//        Predicate storePre = builder.or(storePreList.toArray(new Predicate[0]));
//        Predicate finalPre = builder.and(productPre, storePre);
//
//        query.select(builder.count(productRoot));
//        query.where(finalPre);
//
//        return entityManager.createQuery(query).getSingleResult();
//    }
//

        public BaseResponse<?> searchProductByCriteriaWithJoin(Pageable pageable, String[] product, String[] store, String[] inventory) {

            log.info("-------------- searchProductByCriteriaWithJoin --------------");

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<ProductEntity> query = builder.createQuery(ProductEntity.class);

            Root<ProductEntity> productRoot = query.from(ProductEntity.class);

            Join<ProductEntity, StoreEntity> storeRoot = null;
            Join<ProductEntity, InventoryEntity> inventoryRoot = null;

            if (store != null) {
                storeRoot = productRoot.join("store");
            }

            if (inventory != null) {
                inventoryRoot = productRoot.join("inventoryList");
            }


            Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
            List<Predicate> finalPreList = new ArrayList<>();

            if (product != null) {
                List<Predicate> productPreList = new ArrayList<>();
                for (String u : product) {
                    Matcher matcher = pattern.matcher(u);
                    if (matcher.find()) {
                        SpecSearchCriteria searchCriteria = new SpecSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
                        productPreList.add(toProductPredicate(productRoot, builder, searchCriteria));
                    }
                }

                if (!productPreList.isEmpty()) {
                    Predicate productPre = builder.or(productPreList.toArray(new Predicate[0]));
                    finalPreList.add(productPre);
                }
            }

            if (store != null) {
                List<Predicate> storePreList = new ArrayList<>();
                for (String a : store) {
                    Matcher matcher = pattern.matcher(a);
                    if (matcher.find()) {
                        SpecSearchCriteria searchCriteria = new SpecSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
                        storePreList.add(toStorePredicate(storeRoot, builder, searchCriteria));
                    }
                }

                if (!storePreList.isEmpty()) {

                    Predicate storePre =
                            builder.or(
                                    storePreList.toArray(new Predicate[0])
                            );

                    finalPreList.add(storePre);
                }
            }


            if (inventory != null) {
                List<Predicate> inventoryPreList = new ArrayList<>();
                for (String i : inventory) {
                    Matcher matcher = pattern.matcher(i);
                    if (matcher.find()) {
                        SpecSearchCriteria searchCriteria = new SpecSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4),matcher.group(5));
                        inventoryPreList.add(toInventoryPredicate(inventoryRoot, builder, searchCriteria));
                    }
                }

                if (!inventoryPreList.isEmpty()) {
                    Predicate inventoryPre = builder.or(inventoryPreList.toArray(new Predicate[0]));
                    finalPreList.add(inventoryPre);
                }
            }


            Predicate finalPre = builder.and(finalPreList.toArray(new Predicate[0]));
            query.where(finalPre);

            if (inventory != null) {
                query.distinct(true);
            }

            List<ProductEntity> products = entityManager.createQuery(query).setFirstResult(pageable.getPageNumber()).setMaxResults(pageable.getPageSize()).getResultList();

            long count = countProductJoinStoreInventory(product, store, inventory);


            return BaseResponse.ofSuccess(PageResponse.of(products, pageable, count));
        }


        private Predicate toProductPredicate(
                Root<ProductEntity> root,
                CriteriaBuilder builder,
                SpecSearchCriteria criteria) {

            log.info("-------------- toProductPredicate --------------");

            return switch (criteria.getOperation()) {

                case EQUALITY ->
                        builder.equal(
                                root.get(criteria.getKey()),
                                criteria.getValue()
                        );

                case NEGATION ->
                        builder.notEqual(
                                root.get(criteria.getKey()),
                                criteria.getValue()
                        );

                case GREATER_THAN ->
                        builder.greaterThan(
                                root.get(criteria.getKey()),
                                criteria.getValue().toString()
                        );

                case LESS_THAN ->
                        builder.lessThan(
                                root.get(criteria.getKey()),
                                criteria.getValue().toString()
                        );

                case LIKE ->
                        builder.like(
                                root.get(criteria.getKey()),
                                "%" + criteria.getValue().toString() + "%"
                        );

                case STARTS_WITH ->
                        builder.like(
                                root.get(criteria.getKey()),
                                criteria.getValue() + "%"
                        );

                case ENDS_WITH ->
                        builder.like(
                                root.get(criteria.getKey()),
                                "%" + criteria.getValue()
                        );

                case CONTAINS ->
                        builder.like(
                                root.get(criteria.getKey()),
                                "%" + criteria.getValue() + "%"
                        );
            };
        }


        private Predicate toStorePredicate(
                Join<ProductEntity, StoreEntity> root,
                CriteriaBuilder builder,
                SpecSearchCriteria criteria) {

            log.info("-------------- toStorePredicate --------------");

            return switch (criteria.getOperation()) {

                case EQUALITY ->
                        builder.equal(
                                root.get(criteria.getKey()),
                                criteria.getValue()
                        );

                case NEGATION ->
                        builder.notEqual(
                                root.get(criteria.getKey()),
                                criteria.getValue()
                        );

                case GREATER_THAN ->
                        builder.greaterThan(
                                root.get(criteria.getKey()),
                                criteria.getValue().toString()
                        );

                case LESS_THAN ->
                        builder.lessThan(
                                root.get(criteria.getKey()),
                                criteria.getValue().toString()
                        );

                case LIKE ->
                        builder.like(
                                root.get(criteria.getKey()),
                                "%" + criteria.getValue().toString() + "%"
                        );

                case STARTS_WITH ->
                        builder.like(
                                root.get(criteria.getKey()),
                                criteria.getValue() + "%"
                        );

                case ENDS_WITH ->
                        builder.like(
                                root.get(criteria.getKey()),
                                "%" + criteria.getValue()
                        );

                case CONTAINS ->
                        builder.like(
                                root.get(criteria.getKey()),
                                "%" + criteria.getValue() + "%"
                        );
            };
        }


        private Predicate toInventoryPredicate(
                Join<ProductEntity, InventoryEntity> root,
                CriteriaBuilder builder,
                SpecSearchCriteria criteria) {

            log.info("-------------- toInventoryPredicate --------------");

            return switch (criteria.getOperation()) {

                case EQUALITY ->
                        builder.equal(
                                root.get(criteria.getKey()),
                                criteria.getValue()
                        );

                case NEGATION ->
                        builder.notEqual(
                                root.get(criteria.getKey()),
                                criteria.getValue()
                        );

                case GREATER_THAN ->
                        builder.greaterThan(
                                root.get(criteria.getKey()),
                                criteria.getValue().toString()
                        );

                case LESS_THAN ->
                        builder.lessThan(
                                root.get(criteria.getKey()),
                                criteria.getValue().toString()
                        );

                case LIKE ->
                        builder.like(
                                root.get(criteria.getKey()),
                                "%" + criteria.getValue().toString() + "%"
                        );

                case STARTS_WITH ->
                        builder.like(
                                root.get(criteria.getKey()),
                                criteria.getValue() + "%"
                        );

                case ENDS_WITH ->
                        builder.like(
                                root.get(criteria.getKey()),
                                "%" + criteria.getValue()
                        );

                case CONTAINS ->
                        builder.like(
                                root.get(criteria.getKey()),
                                "%" + criteria.getValue() + "%"
                        );
            };
        }


        private long countProductJoinStoreInventory(
                String[] product,
                String[] store,
                String[] inventory) {

            log.info("-------------- countProductJoinStoreInventory --------------");

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();

            CriteriaQuery<Long> query =
                    builder.createQuery(Long.class);

            Root<ProductEntity> productRoot =
                    query.from(ProductEntity.class);


            Join<ProductEntity, StoreEntity> storeRoot = null;
            Join<ProductEntity, InventoryEntity> inventoryRoot = null;


            if (store != null) {
                storeRoot = productRoot.join("store");
            }

            if (inventory != null) {
                inventoryRoot = productRoot.join("inventoryList");
            }


            Pattern pattern =
                    Pattern.compile(SEARCH_SPEC_OPERATOR);

            List<Predicate> finalPreList =
                    new ArrayList<>();


            // PRODUCT
            if (product != null) {

                List<Predicate> productPreList =
                        new ArrayList<>();

                for (String u : product) {

                    Matcher matcher =
                            pattern.matcher(u);

                    if (matcher.find()) {

                        SpecSearchCriteria searchCriteria =
                                new SpecSearchCriteria(
                                        matcher.group(1),
                                        matcher.group(2),
                                        matcher.group(3),
                                        matcher.group(4),
                                        matcher.group(5)
                                );

                        productPreList.add(
                                toProductPredicate(
                                        productRoot,
                                        builder,
                                        searchCriteria
                                )
                        );
                    }
                }

                if (!productPreList.isEmpty()) {

                    Predicate productPre =
                            builder.or(
                                    productPreList.toArray(new Predicate[0])
                            );

                    finalPreList.add(productPre);
                }
            }


            // STORE
            if (store != null) {

                List<Predicate> storePreList =
                        new ArrayList<>();

                for (String a : store) {

                    Matcher matcher =
                            pattern.matcher(a);

                    if (matcher.find()) {

                        SpecSearchCriteria searchCriteria =
                                new SpecSearchCriteria(
                                        matcher.group(1),
                                        matcher.group(2),
                                        matcher.group(3),
                                        matcher.group(4),
                                        matcher.group(5)
                                );

                        storePreList.add(
                                toStorePredicate(
                                        storeRoot,
                                        builder,
                                        searchCriteria
                                )
                        );
                    }
                }

                if (!storePreList.isEmpty()) {

                    Predicate storePre =
                            builder.or(
                                    storePreList.toArray(new Predicate[0])
                            );

                    finalPreList.add(storePre);
                }
            }


            // INVENTORY
            if (inventory != null) {

                List<Predicate> inventoryPreList =
                        new ArrayList<>();

                for (String i : inventory) {

                    Matcher matcher =
                            pattern.matcher(i);

                    if (matcher.find()) {

                        SpecSearchCriteria searchCriteria =
                                new SpecSearchCriteria(
                                        matcher.group(1),
                                        matcher.group(2),
                                        matcher.group(3),
                                        matcher.group(4),
                                        matcher.group(5)
                                );

                        inventoryPreList.add(
                                toInventoryPredicate(
                                        inventoryRoot,
                                        builder,
                                        searchCriteria
                                )
                        );
                    }
                }

                if (!inventoryPreList.isEmpty()) {

                    Predicate inventoryPre =
                            builder.or(
                                    inventoryPreList.toArray(new Predicate[0])
                            );

                    finalPreList.add(inventoryPre);
                }
            }


            Predicate finalPre =
                    builder.and(
                            finalPreList.toArray(new Predicate[0])
                    );


            query.select(
                    builder.countDistinct(productRoot)
            );

            query.where(finalPre);


            return entityManager
                    .createQuery(query)
                    .getSingleResult();
        }
    }
}

