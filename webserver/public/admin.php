<?php
$cfg = require __DIR__ . '/../config/config.php';
spl_autoload_register(function($c){$p=__DIR__.'/../src/'.str_replace('\\','/',$c).'.php';if(is_file($p))require $p;});
$db = new Db($cfg['db']);
$pdo = $db->pdo();
$ds = $pdo->query("SELECT device_id, phone_number, last_heartbeat, CASE WHEN TIMESTAMPDIFF(SECOND,last_heartbeat,NOW())<=30 THEN 'online' ELSE 'offline' END AS st FROM devices ORDER BY last_heartbeat DESC")->fetchAll(PDO::FETCH_ASSOC);
echo '<!doctype html><meta charset="utf-8"><title>设备监控</title><style>body{font-family:sans-serif} table{border-collapse:collapse} td,th{border:1px solid #ccc;padding:6px} .ok{color:#0a0} .bad{color:#a00}</style>';
echo '<h2>设备状态</h2><table><tr><th>设备ID</th><th>号码</th><th>最后心跳</th><th>状态</th></tr>';
foreach($ds as $d){
  $cls = $d['st']==='online'?'ok':'bad';
  echo '<tr><td>'.htmlspecialchars($d['device_id']).'</td><td>'.htmlspecialchars($d['phone_number']).'</td><td>'.htmlspecialchars($d['last_heartbeat']).'</td><td class="'.$cls.'">'.htmlspecialchars($d['st']).'</td></tr>';
}
echo '</table>';
echo '<h2>短信记录</h2>';
$ms = $pdo->query("SELECT message_id, device_id, sender, LEFT(content,120) AS content, receive_time FROM messages ORDER BY receive_time DESC LIMIT 50")->fetchAll(PDO::FETCH_ASSOC);
echo '<table><tr><th>ID</th><th>设备</th><th>发件人</th><th>内容</th><th>时间</th></tr>';
foreach($ms as $m){
  echo '<tr><td>'.htmlspecialchars($m['message_id']).'</td><td>'.htmlspecialchars($m['device_id']).'</td><td>'.htmlspecialchars($m['sender']).'</td><td>'.htmlspecialchars($m['content']).'</td><td>'.htmlspecialchars($m['receive_time']).'</td></tr>';
}
echo '</table>';
