<?php
function req($url, $json, $token=null, $hmac=null) {
  $ch = curl_init($url);
  curl_setopt($ch, CURLOPT_POST, 1);
  curl_setopt($ch, CURLOPT_HTTPHEADER, array_filter([
    'Content-Type: application/json',
    $token?('Authorization: Bearer '.$token):null,
    $hmac?('X-Signature: '.hash_hmac('sha256',$json,$hmac)):null
  ]));
  curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
  curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
  $t0 = microtime(true);
  $resp = curl_exec($ch);
  $code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
  $dt = (microtime(true)-$t0)*1000;
  curl_close($ch);
  return [$code, $resp, $dt];
}
$base = getenv('TEST_BASE') ?: 'http://localhost';
[$c1,$r1,$d1] = req($base.'/api/heartbeat', json_encode(['device_id'=>'device123','phone_number'=>'+8613912345678','timestamp'=>time()]));
[$c2,$r2,$d2] = req($base.'/api/sms', json_encode(['device_id'=>'device123','sender'=>'10086','content'=>'测试短信','receive_time'=>date('Y-m-d H:i:s')]), getenv('API_TOKEN'), getenv('HMAC_SECRET'));
echo json_encode(['heartbeat'=>['code'=>$c1,'ms'=>$d1],'sms'=>['code'=>$c2,'ms'=>$d2]], JSON_UNESCAPED_UNICODE);
