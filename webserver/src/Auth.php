<?php
class Auth {
    private $token;
    private $hmac;
    public function __construct($token, $hmac) { $this->token = $token; $this->hmac = $hmac; }
    public function checkToken($hdr) {
        if (!$this->token) return true;
        if (!$hdr) return false;
        if (stripos($hdr, 'Bearer ') !== 0) return false;
        $t = substr($hdr, 7);
        return hash_equals($this->token, $t);
    }
    public function checkHmac($body, $sig) {
        if (!$this->hmac) return true;
        if (!$sig) return false;
        $calc = hash_hmac('sha256', $body, $this->hmac);
        return hash_equals($calc, $sig);
    }
}
