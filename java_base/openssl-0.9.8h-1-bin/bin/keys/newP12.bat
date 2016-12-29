mkdir out
openssl x509 -req -days 365 -CA ca-cert.pem -CAkey ca-key.pem -CAcreateserial -in client-csr.pem -out ./out/client-cert.pem -extensions v3_req -extfile openssl.cnf
openssl pkcs12 -export -in ./out/client-cert.pem -inkey client-key.pem -certfile ca-cert.pem -out ./out/client.p12
pause