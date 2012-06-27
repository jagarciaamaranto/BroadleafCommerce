/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.broadleafcommerce.core.order.service.legacy;

import org.broadleafcommerce.core.order.domain.BundleOrderItem;
import org.broadleafcommerce.core.order.domain.DiscreteOrderItem;
import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.core.order.service.call.FulfillmentGroupRequest;
import org.broadleafcommerce.core.order.service.call.OrderItemRequest;
import org.broadleafcommerce.core.order.service.call.legacy.LegacyBundleOrderItemRequest;
import org.broadleafcommerce.core.order.service.call.legacy.LegacyDiscreteOrderItemRequest;
import org.broadleafcommerce.core.order.service.call.legacy.LegacyGiftWrapOrderItemRequest;
import org.broadleafcommerce.core.order.service.exception.ItemNotFoundException;
import org.broadleafcommerce.core.payment.domain.PaymentInfo;
import org.broadleafcommerce.core.payment.domain.Referenced;
import org.broadleafcommerce.core.payment.service.type.PaymentInfoType;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.broadleafcommerce.profile.core.domain.Address;
import org.broadleafcommerce.profile.core.domain.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In Broadleaf Commerce, a Cart and an Order are the same thing,
 * A "cart" becomes an order after it has been submitted.
 *
 * Most of the methods in this order are used to modify the cart
 * during the shopping process.    Although it is common to also
 * use this service for "named" orders (e.g. wishlists).
 *
 */
public interface LegacyOrderService extends OrderService {

    public Order createNamedOrderForCustomer(String name, Customer customer);

    public Order findNamedOrderForCustomer(String name, Customer customer);

    public FulfillmentGroup findDefaultFulfillmentGroupForOrder(Order order);

    public OrderItem addGiftWrapItemToOrder(Order order, LegacyGiftWrapOrderItemRequest itemRequest) throws PricingException;
    
    public OrderItem addGiftWrapItemToOrder(Order order, LegacyGiftWrapOrderItemRequest itemRequest, boolean priceOrder) throws PricingException;

    /**
     * Used to create dynamic bundles groupings of order items.
     * Typically not used with ProductBundles which should instead
     * call addProductToOrder.
     *
     * Prices the order after adding the bundle.
     *
     * @param order
     * @param itemRequest
     * @return
     * @throws PricingException
     */
    public OrderItem addBundleItemToOrder(Order order, LegacyBundleOrderItemRequest itemRequest) throws PricingException;

    /**
     * Used to create dynamic bundles groupings of order items.
     * Typically not used with ProductBundles which should instead
     * call addProductToOrder.
     *
     * Prices the order after adding the bundle if priceOrder = true.  Clients
     * may wish to perform many cart operations without pricing and
     * then use priceOrder = true on the last operation to avoid
     * exercising the pricing engine in a batch order update mode.
     *
     * @param order
     * @param itemRequest
     * @param priceOrder
     * @return
     * @throws PricingException
     */
    public OrderItem addBundleItemToOrder(Order order, LegacyBundleOrderItemRequest itemRequest, boolean priceOrder) throws PricingException;

    public PaymentInfo addPaymentToOrder(Order order, PaymentInfo payment);

    public PaymentInfo addPaymentToOrder(Order order, PaymentInfo payment, Referenced securePaymentInfo);

    public FulfillmentGroup addFulfillmentGroupToOrder(FulfillmentGroupRequest fulfillmentGroupRequest) throws PricingException;
    
    public FulfillmentGroup addFulfillmentGroupToOrder(FulfillmentGroupRequest fulfillmentGroupRequest, boolean priceOrder) throws PricingException;

    public FulfillmentGroup addFulfillmentGroupToOrder(Order order, FulfillmentGroup fulfillmentGroup) throws PricingException;
    
    public FulfillmentGroup addFulfillmentGroupToOrder(Order order, FulfillmentGroup fulfillmentGroup, boolean priceOrder) throws PricingException;

    public FulfillmentGroup addItemToFulfillmentGroup(OrderItem item, FulfillmentGroup fulfillmentGroup, int quantity) throws PricingException;
    
    public FulfillmentGroup addItemToFulfillmentGroup(OrderItem item, FulfillmentGroup fulfillmentGroup, int quantity, boolean priceOrder) throws PricingException;

    public FulfillmentGroup addItemToFulfillmentGroup(OrderItem item, FulfillmentGroup fulfillmentGroup) throws PricingException;
    
    public FulfillmentGroup addItemToFulfillmentGroup(OrderItem item, FulfillmentGroup fulfillmentGroup, boolean priceOrder) throws PricingException;

    public FulfillmentGroup addItemToFulfillmentGroup(Order order, OrderItem item, FulfillmentGroup fulfillmentGroup, int quantity, boolean priceOrder) throws PricingException;
    /**
     * Delegates to the fully parametrized method with priceOrder = true.
     *
     * @param order
     * @param item
     * @throws ItemNotFoundException
     * @throws PricingException
     */
    public void updateItemQuantity(Order order, OrderItem item) throws ItemNotFoundException, PricingException;

    /**
     * Updates the quantity and reprices the order.
     * Removes the orderItem if the quantity is updated to 0 (or less).
     *
     *
     * @param order
     * @param item
     * @param priceOrder
     * @throws ItemNotFoundException
     * @throws PricingException
     */
    public void updateItemQuantity(Order order, OrderItem item, boolean priceOrder) throws ItemNotFoundException, PricingException;
    
    /**
     * From the given OrderItemRequestDTO object, this will look through the order's DiscreteOrderItems
     * to find the item with the matching orderItemId and update this item's quantity with the value of 
     * the quantity field in the OrderItemRequestDTO.
     * 
     * @param order
     * @param item
     * @throws ItemNotFoundException
     * @throws PricingException
     */
	public void updateItemQuantity(Order order, OrderItemRequest item) throws ItemNotFoundException, PricingException;

    public void removeFulfillmentGroupFromOrder(Order order, FulfillmentGroup fulfillmentGroup) throws PricingException;
    
    public void removeFulfillmentGroupFromOrder(Order order, FulfillmentGroup fulfillmentGroup, boolean priceOrder) throws PricingException;

    public Order removeItemFromOrder(Order order, OrderItem item) throws PricingException;
    
    public Order removeItemFromOrder(Order order, OrderItem item, boolean priceOrder) throws PricingException;

    public void removeNamedOrderForCustomer(String name, Customer customer);

    public Order confirmOrder(Order order);

    public void removeAllFulfillmentGroupsFromOrder(Order order) throws PricingException;

    public void removeAllFulfillmentGroupsFromOrder(Order order, boolean priceOrder) throws PricingException;

    public List<PaymentInfo> readPaymentInfosForOrder(Order order);

    public Order removeItemFromOrder(Long orderId, Long itemId) throws PricingException;
    
    public Order removeItemFromOrder(Long orderId, Long itemId, boolean priceOrder) throws PricingException;

    public void removeAllPaymentsFromOrder(Order order);

    public FulfillmentGroup createDefaultFulfillmentGroup(Order order, Address address);

    /**
     * Adds an item to the passed in order.
     *
     * The orderItemRequest can be sparsely populated.
     *
     * When priceOrder is false, the system will not reprice the order.   This is more performant in
     * cases such as bulk adds where the repricing could be done for the last item only.
     *
     * @see OrderItemRequest
     * @param orderItemRequestDTO
     * @param priceOrder
     * @return
     */
    public Order addItemToOrder(Long orderId, OrderItemRequest orderItemRequestDTO, boolean priceOrder) throws PricingException;


    public void removePaymentsFromOrder(Order order, PaymentInfoType paymentInfoType);
    
	/**
	 * Not typically used in versions since 1.7.
	 * See: {@link #addItemToOrder(Long, OrderItemRequest, boolean)}
	 * 
	 * @param skuId
	 * @param productId
	 * @param categoryId
	 * @param quantity
	 * @return
	 */
    public LegacyDiscreteOrderItemRequest createDiscreteOrderItemRequest(Long skuId, Long productId, Long categoryId, Integer quantity);    


    /**
     * Adds an item to the specified bundle.   This is typically used to manage
     * bundles that were created programmatically.
     *
     * It would not be typical to modify an item in a bundle that was
     * created from a ProductBundle.
     *
     * priceOrder must be set to true for the cart to reflect the
     * state of the bundle accurately; however, it does not need
     * to be set here.   The requirement is that a pricing operation
     * happen between the time the bundle is modified and the time
     * it is redisplayed.
     *
     * @param order
     * @param bundle
     * @param newOrderItem
     * @param priceOrder
     * @return
     * @throws PricingException
     */
    public OrderItem addOrderItemToBundle(Order order, BundleOrderItem bundle, DiscreteOrderItem newOrderItem, boolean priceOrder) throws PricingException;

    /**
     * Removes an item from the given bundle.
     *
     * You may wish to set priceOrder to false if performing set of
     * cart operations to avoid the expense of exercising the pricing engine
     * until you are ready to finalize pricing after adding the last item.
     *
     * @param order
     * @param bundle
     * @param item
     * @param priceOrder
     * @return
     * @throws PricingException
     */
    public Order removeItemFromBundle(Order order, BundleOrderItem bundle, OrderItem item, boolean priceOrder) throws PricingException;

    /**
     * Adds the passed in name/value pair to the order-item.    If the
     * attribute already exists, then it is updated with the new value.   
     * 
     * If the value passed in is null or empty string and the attribute exists, it is removed
     * from the order item.
     *
     * You may wish to set priceOrder to false if performing set of
     * cart operations to avoid the expense of exercising the pricing engine
     * until you are ready to finalize pricing after adding the last item.
     *
     * @param order
     * @param item
     * @param attributeValues
     * @param priceOrder
     * @return
     */
    public Order addOrUpdateOrderItemAttributes(Order order, OrderItem item, Map<String,String> attributeValues, boolean priceOrder) throws ItemNotFoundException, PricingException;
    
    /**
     * Adds the passed in name/value pair to the order-item.    If the
     * attribute already exists, then it is updated with the new value.   
     * 
     * If the value passed in is null and the attribute exists, it is removed
     * from the order item.
     *
     * You may wish to set priceOrder to false if performing set of
     * cart operations to avoid the expense of exercising the pricing engine
     * until you are ready to finalize pricing after adding the last item.
     *
     * @param order
     * @param item
     * @param attributeName
     * @param priceOrder
     * @return
     */
    public Order removeOrderItemAttribute(Order order, OrderItem item, String attributeName, boolean priceOrder) throws ItemNotFoundException, PricingException;

    /* *********************************************************************************
     * DEPRECATED METHODS                                                              *
     * *********************************************************************************/

    /**
     * @deprecated Call addItemToOrder(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder)
     * @param order
     * @param itemRequest
     * @return
     * @throws PricingException
     */
    @Deprecated
    public OrderItem addDiscreteItemToOrder(Order order, LegacyDiscreteOrderItemRequest itemRequest) throws PricingException;

    /**
     * @deprecated Call addItemToOrder(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder)
     *
     * Due to cart merging and gathering requirements, the item returned is not an
     * actual cart item.
     *
     * @param order
     * @param itemRequest
     * @param priceOrder
     * @return
     * @throws PricingException
     */
    @Deprecated
    public OrderItem addDiscreteItemToOrder(Order order, LegacyDiscreteOrderItemRequest itemRequest, boolean priceOrder) throws PricingException;

    /**
     * @deprecated Call addItemToOrder(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder)
     * @param orderId
     * @param skuId
     * @param productId
     * @param categoryId
     * @param quantity
     * @return
     * @throws PricingException
     */
    @Deprecated
    public OrderItem addSkuToOrder(Long orderId, Long skuId, Long productId, Long categoryId, Integer quantity) throws PricingException;

    /**
     * @deprecated Call addItemToOrder(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder)
     * @param orderId
     * @param skuId
     * @param productId
     * @param categoryId
     * @param quantity
     * @param orderItemAttributes
     * @return
     * @throws PricingException
     */
    @Deprecated
    public OrderItem addSkuToOrder(Long orderId, Long skuId, Long productId, Long categoryId, Integer quantity, Map<String,String> orderItemAttributes) throws PricingException;

    /**
     * @deprecated Call addItemToOrder(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder)
     * @param orderId
     * @param skuId
     * @param productId
     * @param categoryId
     * @param quantity
     * @param priceOrder
     * @return
     * @throws PricingException
     */
    @Deprecated
    public OrderItem addSkuToOrder(Long orderId, Long skuId, Long productId, Long categoryId, Integer quantity, boolean priceOrder) throws PricingException;

    /**
     * @deprecated Call addItemToOrder(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder)
     * @param orderId
     * @param skuId
     * @param productId
     * @param categoryId
     * @param quantity
     * @param priceOrder
     * @param orderItemAttributes
     * @return
     * @throws PricingException
     */
    @Deprecated
    public OrderItem addSkuToOrder(Long orderId, Long skuId, Long productId, Long categoryId, Integer quantity, boolean priceOrder, Map<String,String> orderItemAttributes) throws PricingException;
    
    /**
     * @deprecated Call addItemToOrder(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder)
     * @param order
     * @param newOrderItem
     * @return
     * @throws PricingException
     */
    @Deprecated
    public OrderItem addOrderItemToOrder(Order order, OrderItem newOrderItem) throws PricingException;

    /**
     * @deprecated Call addItemToOrder(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder)
     * @param order
     * @param newOrderItem
     * @param priceOrder
     * @return
     * @throws PricingException
     */
    @Deprecated
    public OrderItem addOrderItemToOrder(Order order, OrderItem newOrderItem, boolean priceOrder) throws PricingException;
    
    /**
     * @deprecated Call addItemToOrder(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder)
     * @param order
     * @param itemRequest
     * @param skuPricingConsiderations
     * @return
     * @throws PricingException
     */
    @Deprecated
    public OrderItem addDynamicPriceDiscreteItemToOrder(Order order, LegacyDiscreteOrderItemRequest itemRequest, @SuppressWarnings("rawtypes") HashMap skuPricingConsiderations) throws PricingException;

    /**
     * @deprecated Call addItemToOrder(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder)
     * @param order
     * @param itemRequest
     * @param skuPricingConsiderations
     * @param priceOrder
     * @return
     * @throws PricingException
     */
    @Deprecated
    public OrderItem addDynamicPriceDiscreteItemToOrder(Order order, LegacyDiscreteOrderItemRequest itemRequest, @SuppressWarnings("rawtypes") HashMap skuPricingConsiderations, boolean priceOrder) throws PricingException;

}