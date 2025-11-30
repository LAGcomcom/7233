<?php
class Db {
    private $pdo;
    public function __construct($cfg) {
        $dsn = 'mysql:host='.$cfg['host'].';port='.$cfg['port'].';dbname='.$cfg['name'].';charset=utf8mb4';
        $this->pdo = new PDO($dsn, $cfg['user'], $cfg['pass'], [PDO::ATTR_ERRMODE=>PDO::ERRMODE_EXCEPTION]);
    }
    public function pdo() { return $this->pdo; }
}
