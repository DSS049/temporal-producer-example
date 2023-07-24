# temporal-producer-example

### service-composition library need below properties in environment
~~~
ADMIRAL_PRODUCT_CODE=ohp-telikos
CLUSTER_CONFIGURATION=westeurope-01
TEMPORAL_CLIENT_ID=XXXXXXXXXXXXXX
TEMPORAL_CLIENT_SECRET=XXXXXXXXXXXXXX
TEMPORAL_ENVIRONMENT=cdt
TEMPORAL_NAMESPACE=telikos-temporal-cdt
~~~

### Temporal UI
~~~
https://cdt-westeurope-01-primary-web.maersk-digital.net/namespaces/telikos-temporal-cdt/workflows/
~~~


### CURL Request
~~~
curl --location 'http://localhost:8086/temporal/send/FDYV6W5RH8F' \
--header 'status: Ready For Planning'
~~~

### LOCAL SETUP
~~~
PROMETHEUS_PORT=9095;TEMPORAL_ENVIRONMENT=local;TEMPORAL_NAMESPACE=default;TEMPORAL_HOST_PORT=localhost:7233
~~~
