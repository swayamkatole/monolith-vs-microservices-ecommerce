import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

const errorRate = new Rate('errors');

export const options = {
  vus: 50,
  duration: '30s',
};

var BASE_URL = 'http://localhost:8080';

export default function () {
  var userId = ((__VU - 1) % 50) + 1;

  var res = http.get(BASE_URL + '/products');
  var ok = check(res, {
    'catalog products status 200': function (r) { return r.status === 200; },
  });
  errorRate.add(!ok);

  res = http.get(BASE_URL + '/products/1');
  ok = check(res, {
    'catalog product detail status 200': function (r) { return r.status === 200; },
  });
  errorRate.add(!ok);

  var orderPayload = JSON.stringify({
    userId: userId,
    items: [
      { productId: 1, quantity: 1 }
    ]
  });

  res = http.post(BASE_URL + '/orders', orderPayload, {
    headers: { 'Content-Type': 'application/json' },
  });
  ok = check(res, {
    'create order status 200': function (r) { return r.status === 200; },
  });
  errorRate.add(!ok);

  var cartPayload = JSON.stringify({
    productId: 1,
    quantity: 1
  });

  res = http.post(BASE_URL + '/cart/' + userId + '/items', cartPayload, {
    headers: { 'Content-Type': 'application/json' },
  });
  ok = check(res, {
    'add to cart status 200': function (r) { return r.status === 200; },
  });
  errorRate.add(!ok);

  res = http.get(BASE_URL + '/cart/' + userId);
  ok = check(res, {
    'get cart status 200': function (r) { return r.status === 200; },
  });
  errorRate.add(!ok);

  sleep(0.5);
}
