# Intro

Tinymasq,  Clojure based DNS server.

[![Build Status](https://travis-ci.org/narkisr/tinymasq.png)](https://travis-ci.org/narkisr/tinymasq)

# Usage

API:

```bash
# Adding 
$ curl https://localhost:8444/hosts -H Content-Type: application/json -d {"hostname":"foo","ip":"1.2.3.4"} -k
# Updating 
$ curl -X PUT https://localhost:8444/hosts -H Content-Type: application/json -d {"hostname":"foo","ip":"1.2.3.4"} -k
# Deleting 
$ curl -X DELETE https://localhost:8444/hosts -H Content-Type: application/json -d {"hostname":"foo"} -k
# Listing
$ curl -X GET https://localhost:8444/hosts -H Content-Type: application/json -d {"hostname":"foo"} -k
```

DNS lookup:

```bash
$ dig @localhost bar.com -p 53
```

Importing an existing hosts file:

```bash
# TBD
$ tinyimport /etc/hosts
```
# Copyright and license

Copyright [2014] [Ronen Narkis]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
