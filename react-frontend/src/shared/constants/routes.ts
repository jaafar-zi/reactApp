export const APP_ROUTES = {
  home: '/',
  collections: {
    all: '/collections/all',
    detail: (id: string = ':id') => `/products/${id}`,
  },
  about: '/about',
  checkout: '/checkout',
  cart: '/cart',
  orderConfirmation: '/order-confirmation',
  auth: {
    login: '/login',
    register: '/register',
  },
  profile: '/profile',
} as const;