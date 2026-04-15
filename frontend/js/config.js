const CONFIG = {
    API_BASE_URL: (() => {
        const host = window.location.hostname;
        const isLoopback = host === 'localhost' || host === '127.0.0.1' || host === '::1';
        const isPrivateLan = /^(10\.|192\.168\.|172\.(1[6-9]|2\d|3[0-1])\.)/.test(host);
        const isLocalFile = window.location.protocol === 'file:';

        // Production (Netlify)
        if (host.includes('netlify.app')) {
            return 'https://ftms-backend-n4oy.onrender.com';
        }

        // Local development (localhost, loopback, LAN, or file preview)
        if (isLoopback || isPrivateLan || isLocalFile) {
            return 'http://localhost:8080';
        }

        // Default fallback
        return 'https://ftms-backend-n4oy.onrender.com';
    })(),

    EXCHANGE_API_KEY: '6bf11c4b17daa3cc2ccfc96c',
    EXCHANGE_API_URL: 'https://v6.exchangerate-api.com/v6',
    CURRENCIES: ['USD', 'EUR', 'GBP', 'JPY', 'AUD', 'CAD', 'CHF', 'INR', 'SGD', 'HKD', 'CNY', 'AED'],

    BRIDGE_CURRENCY: 'USD',
    USE_BRIDGE_CONVERSION: true
};

console.log('CONFIG.API_BASE_URL =', CONFIG.API_BASE_URL);