package ru.yandex.practicum.shoppingcart.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.iteractionapi.dto.BookedProductsDto;
import ru.yandex.practicum.iteractionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.iteractionapi.feign.WarehouseClient;
import ru.yandex.practicum.iteractionapi.request.ChangeProductQuantityRequest;
import ru.yandex.practicum.shoppingcart.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.shoppingcart.exception.NotAuthorizedUserException;
import ru.yandex.practicum.shoppingcart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.shoppingcart.model.ShoppingCart;
import ru.yandex.practicum.shoppingcart.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    public ShoppingCartDto findCart(String username) {
        log.info("Получение корзины покупок для пользователя: {}", username);
        ShoppingCartDto shoppingCartDto = ShoppingCartMapper.INSTANCE.toShoppingCartDto(findShoppingCartByUser(username));
        log.debug("Корзина успешно найдена для пользователя: {}", username);
        return shoppingCartDto;
    }

    @Override
    @Transactional(readOnly = true)
    public ShoppingCart findShoppingCartByUser(String username) {
        log.info("Поиск активной корзины для пользователя: {}", username);
        checkUsername(username);

        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUsernameAndActive(username, true);

        if (shoppingCart.isEmpty()) {
            log.info("Активная корзина не найдена. Создание новой корзины для пользователя: {}", username);
            ShoppingCart newCart = new ShoppingCart();
            newCart.setUsername(username);
            newCart.setActive(true);

            shoppingCart = Optional.of(shoppingCartRepository.save(newCart));
            log.info("Новая корзина создана (id корзины =: {}) для пользователя: {}",
                    shoppingCart.get().getShoppingCartId(), username);
        } else {
            log.debug("Найдена существующая корзина (id корзины =: {}) для пользователя: {}",
                    shoppingCart.get().getShoppingCartId(), username);
        }
        return shoppingCart.get();
    }

    @Override
    public ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> request) {
        log.info("Добавление товаров в корзину. Пользователь: {}, Кол-во товаров: {}", username, request.size());

        checkUsername(username);

        checkProductAvailability(request);

        ShoppingCart shoppingCart = ShoppingCart.builder()
                .username(username)
                .products(request)
                .active(true)
                .build();
        ShoppingCart savedCart = shoppingCartRepository.save(shoppingCart);
        log.info("Товары успешно добавлены в корзину. id корзины: {}, Кол-во товаров: {}",
                savedCart.getShoppingCartId(),
                savedCart.getProducts().size());

        return shoppingCartMapper.toShoppingCartDto(savedCart);
    }

    @Override
    public void deactivateShoppingCartByUser(String username) {
        log.info("Деактивация корзины пользователя: {}", username);
        ShoppingCart cart = findShoppingCartByUser(username);
        cart.setActive(false);
        shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCartDto deleteProductsFromShoppingCart(String username, List<UUID> products) {
        log.info("Удаление товаров из корзины. Пользователь: {}, Кол-во удаляемых товаров: {}", username, products.size());

        ShoppingCart shoppingCart = findShoppingCartByUser(username);
        if (!shoppingCart.getProducts().keySet().containsAll(products)) {
            throw new NoProductsInShoppingCartException("Товар не найден в корзине");
        }
        products.forEach(shoppingCart.getProducts()::remove);

        shoppingCart = shoppingCartRepository.save(shoppingCart);

        return ShoppingCartMapper.INSTANCE.toShoppingCartDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto updateProductQuantity(String username, ChangeProductQuantityRequest requestDto) {
        log.info("Изменение количества товара. Пользователь: {}, Товар: {}, Новое количество: {}",
                username,
                requestDto.getProductId(),
                requestDto.getNewQuantity());

        ShoppingCart cart = findShoppingCartByUser(username);

        if (!cart.getProducts().containsKey(requestDto.getProductId())) {
            throw new NoProductsInShoppingCartException("Товар не найден в корзине");
        }

        cart.getProducts().put(requestDto.getProductId(), requestDto.getNewQuantity());

        cart = shoppingCartRepository.save(cart);

        return ShoppingCartMapper.INSTANCE.toShoppingCartDto(cart);
    }

    @Override
    public BookedProductsDto bookingProductsForUser(String username) {
        checkUsername(username);
        ShoppingCart shoppingCart = findShoppingCartByUser(username);
        return warehouseClient.bookingCartProducts(shoppingCartMapper.toShoppingCartDto(shoppingCart));
    }

    private void checkUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("Имя пользователя должно быть заполнено.");
        }
    }

    private void checkProductAvailability(Map<UUID, Long> products) {
        ShoppingCartDto cartDto = ShoppingCartDto.builder()
                .shoppingCartId(UUID.randomUUID())
                .products(products)
                .build();

        try {
            BookedProductsDto response = warehouseClient.checkProductQuantityForCart(cartDto);
            log.debug("Проверка наличия товаров завершена. Ответ склада: {}", response);
        } catch (FeignException e) {
            log.error("Ошибка при проверке наличия товаров: {}", e.getMessage());
            throw new IllegalStateException("Сервис склада недоступен. Попробуйте позже.");
        }
    }
}
