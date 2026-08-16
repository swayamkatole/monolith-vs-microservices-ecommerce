import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

const errorRate = new Rate('errors');

export const options = {
  vus: Number(__ENV.VUS || 50),
  duration: __ENV.DURATION || '60s',
};

var BASE_CATALOG = 'http://localhost:8081';
var BASE_ORDER   = 'http://localhost:8082';
var BASE_CART    = 'http://localhost:8083';

export default function () {
  var sessionId = 'user-' + __VU + '-' + Date.now();

  // 1. List products
  var res = http.get(BASE_CATALOG + '/products');
  var ok = check(res, {
    'catalog products status 200': function(r) { return r.status === 200; },
  });
  errorRate.add(!ok);

  // 2. Get a specific product (id=1)
  res = http.get(BASE_CATALOG + '/products/1');
  ok = check(res, {
    'catalog product detail status 200': function(r) { return r.status === 200; },
  });
  errorRate.add(!ok);

  // 3. Create an order
  var orderPayload = JSON.stringify({
    productId: 1,
    quantity: 1,
    totalPrice: 999.99,
  });
  res = http.post(BASE_ORDER + '/orders', orderPayload, {
    headers: { 'Content-Type': 'application/json' },
  });
  ok = check(res, {
    'create order status 200': function(r) { return r.status === 200; },
  });
  errorRate.add(!ok);

  // 4. Add to cart
  var cartPayload = JSON.stringify({
    productId: 1,
    quantity: 1,
    price: 999.99,
  });
  res = http.post(BASE_CART + '/cart/' + sessionId + '/items', cartPayload, {
    headers: { 'Content-Type': 'application/json' },
  });
  ok = check(res, {
    'add to cart status 200': function(r) { return r.status === 200; },
  });
  errorRate.add(!ok);

  // 5. Get cart
  res = http.get(BASE_CART + '/cart/' + sessionId);
  ok = check(res, {
    'get cart status 200': function(r) { return r.status === 200; },
  });
  errorRate.add(!ok);

  sleep(0.5);
}
