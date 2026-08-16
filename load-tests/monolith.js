import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = 'http://localhost:8080';

export let options = {
  stages: [
    { duration: '2m', target: 50 },
    { duration: '5m', target: 50 },
    { duration: '2m', target: 100 },
    { duration: '5m', target: 100 },
    { duration: '2m', target: 0 },
  ],
};

export default function () {
  // Browse products
  let res = http.get(BASE_URL + '/products');
  check(res, { 'products status 200': (r) => r.status === 200 });

  sleep(0.5);

  // Place order
  let payload = JSON.stringify({
    userId: 1,
    items: [
      { productId: 1, quantity: 1 },
      { productId: 2, quantity: 2 },
    ],
  });

  res = http.post(BASE_URL + '/orders', payload, {
    headers: { 'Content-Type': 'application/json' },
  });
  check(res, { 'order status 200/201': (r) => r.status === 200 || r.status === 201 });

  sleep(0.5);
}
