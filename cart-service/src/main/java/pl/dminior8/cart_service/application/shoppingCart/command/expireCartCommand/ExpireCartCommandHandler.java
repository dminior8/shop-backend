//package pl.dminior8.cart_service.application.shoppingCart.command.expireCartCommand;
//
//
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import pl.dminior8.cart_service.domain.entity.Cart;
//import pl.dminior8.cart_service.domain.entity.CartStatus;
//import pl.dminior8.cart_service.domain.entity.Cart;
//import pl.dminior8.cart_service.infrastructure.repository.CartRepository;
//import pl.dminior8.cart_service.infrastructure.messaging.DomainEventPublisher;
//
//@Component
//public class ExpireCartCommandHandler {
//
//    private final CartRepository cartRepo;
//    private final Cart cart;
//    private final DomainEventPublisher eventPublisher;
//
//    public ExpireCartCommandHandler(CartRepository cartRepo,
//                                    Cart cart,
//                                    DomainEventPublisher eventPublisher) {
//        this.cartRepo = cartRepo;
//        this.cart = cart;
//        this.eventPublisher = eventPublisher;
//    }
//
//    @Transactional
//    public void handle(ExpireCartCommand command) {
//        // 1. Pobierz koszyk ACTIVE dla userId
//        Cart cart = cartRepo.findByUserId(command.userId())
//                .filter(c -> c.getStatus() == CartStatus.ACTIVE)
//                .orElse(null);
//        if (cart == null) {
//            // nic do zrobienia
//            return;
//        }
//
//        // 2. Wygaszenie w domenie
//        cart.expire(cart);
//
//        // 3. Zapis
//        cartRepo.save(cart);
//
//        // 4. Publikacja zdarzenia domenowego
//        eventPublisher.publishCartExpired(cart.getId(), cart.getUserId());
//    }
//}
//
