const CONFIG = {
    // Backend URL is built dynamically so the frontend can work from localhost or LAN IP.
    API_BASE_URL: (() => {
        const backendPort = 8080;
        const host = window.location.hostname === 'localhost' ? 'localhost' : window.location.hostname;
        return `${window.location.protocol}//${host}:${backendPort}`;
    })(),
    EXCHANGE_API_KEY: '6bf11c4b17daa3cc2ccfc96c',  // from exchangerate-api.com
    EXCHANGE_API_URL: 'https://v6.exchangerate-api.com/v6',
    CURRENCIES: ['USD', 'EUR', 'GBP', 'JPY', 'AUD', 'CAD', 'CHF', 'INR', 'SGD', 'HKD', 'CNY', 'AED']
};

console.log('CONFIG.API_BASE_URL =', CONFIG.API_BASE_URL);
