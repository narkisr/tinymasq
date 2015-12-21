# fdyn protocol

Based on dyndns2 https://help.dyn.com/remote-access-api/perform-update/

Service is available via HTTP (port 80) and HTTPS (port 443)

## Creating or updating an entry

Creation or changes are done through a simple HTTP GET
```
GET /nic/update?host=<yourhostname>&myip=<ipaddress>[&description=<ServiceDescription>]
```
* host := the name of the host to register/update
* myip := the IP address to register
* description := an optional text describing the service

## Authentication and Authorisation
Authentication is done through HTTP BASIC auth or by providing username/password with the URL.

```
http://lostin:coding@dyn.ffms/nic/update?host=aaseecats&myip=203.0.113.1&description=Die+besten+Katzen+rund+um+den+Aasee%21
```

There is no registration, you can pick any username/password when first registering a service. 
For updates, the same username/password must be used as when first registering

## Update frequency
You should update the entry at least once every hour or when your ip has changed.


## Security
Almost none. Do not use passwords that you use for anything else.

## Deletion
Entries will automatically expire. 
