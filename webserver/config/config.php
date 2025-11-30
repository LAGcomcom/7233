<?php
$envPath = __DIR__ . '/.env';
if (is_file($envPath)) {
    $lines = file($envPath, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
    foreach ($lines as $line) {
        if (strpos($line, '=') !== false) {
            list($k, $v) = array_map('trim', explode('=', $line, 2));
            $_ENV[$k] = $v;
        }
    }
}
function env($k, $d = null) { return $_ENV[$k] ?? getenv($k) ?? $d; }
return [
    'db' => [
        'host' => env('DB_HOST'),
        'port' => env('DB_PORT', '3306'),
        'user' => env('DB_USER'),
        'pass' => env('DB_PASSWORD'),
        'name' => env('DB_NAME'),
    ],
    'api_token' => env('API_TOKEN'),
    'hmac_secret' => env('HMAC_SECRET'),
];
