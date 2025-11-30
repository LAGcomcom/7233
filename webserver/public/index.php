<?php
$cfg = require __DIR__ . '/../config/config.php';
spl_autoload_register(function($c){$p=__DIR__.'/../src/'.str_replace('\\','/',$c).'.php';if(is_file($p))require $p;});
$db = new Db($cfg['db']);
$auth = new Auth($cfg['api_token'], $cfg['hmac_secret']);
header('Content-Type: application/json');
$m = $_SERVER['REQUEST_METHOD'];
$u = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
function json() { return file_get_contents('php://input'); }
function out($code, $data) { http_response_code($code); echo json_encode($data, JSON_UNESCAPED_UNICODE); }
if ($u === '/api/heartbeat' && $m === 'POST') {
    $body = json();
    $sig = $_SERVER['HTTP_X_SIGNATURE'] ?? null;
    if (!$auth->checkHmac($body, $sig)) { out(401, ['error'=>'invalid_signature']); exit; }
    $j = json_decode($body, true);
    if (!$j || !isset($j['device_id'])) { out(400, ['error'=>'invalid_body']); exit; }
    $st = $db->pdo()->prepare('INSERT INTO devices(device_id, phone_number, last_heartbeat, status) VALUES(?,?,NOW(),?) ON DUPLICATE KEY UPDATE phone_number=VALUES(phone_number), last_heartbeat=NOW(), status=VALUES(status)');
    $st->execute([$j['device_id'], $j['phone_number'] ?? null, 'online']);
    out(200, ['status'=>'ok','server_time'=>time()]);
    exit;
}
if ($u === '/api/sms' && $m === 'POST') {
    $authHdr = $_SERVER['HTTP_AUTHORIZATION'] ?? null;
    if (!$auth->checkToken($authHdr)) { out(401, ['error'=>'unauthorized']); exit; }
    $body = json();
    $sig = $_SERVER['HTTP_X_SIGNATURE'] ?? null;
    if (!$auth->checkHmac($body, $sig)) { out(401, ['error'=>'invalid_signature']); exit; }
    $j = json_decode($body, true);
    if (!$j || !isset($j['device_id']) || !isset($j['sender']) || !isset($j['content'])) { out(400, ['error'=>'invalid_body']); exit; }
    $st = $db->pdo()->prepare('INSERT INTO messages(device_id, sender, content, receive_time) VALUES(?,?,?,?)');
    $st->execute([$j['device_id'], $j['sender'], $j['content'], $j['receive_time'] ?? date('Y-m-d H:i:s')]);
    out(200, ['status'=>'ok']);
    exit;
}
out(404, ['error'=>'not_found']);
