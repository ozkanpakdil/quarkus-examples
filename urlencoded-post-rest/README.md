Quarkus guide: https://quarkus.io/guides/openapi-swaggerui

```
curl -v -X 'POST' \
   'http://localhost:8080/fruits' \
   -H 'accept: */*' \
   -H 'Content-Type: application/x-www-form-urlencoded' \
   -d 'items=1&items=2&items=3'
```

