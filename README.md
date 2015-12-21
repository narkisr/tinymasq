# Intro

fdyn,  Clojure based DDNS server. [![Build Status](https://travis-ci.org/kgbvax/fdyn.svg?branch=master)](https://travis-ci.org/kgbvax/fdyn) - version 0.1.2


fdyn is a simple trust-on-first-use DynDNS server which is primarly intended for announcing local services in a wireless community network (Freifunk).

## Features
 * 'dyndns2' compatible update
 * Trust-on-first-use: Pick random username & password on first entry update
 * Entries expire when not updated
 * Poor man's service dirctory: Page listing known services & description
 * Optional: restriction to certain address ranges


# Installation
Prerequisites
 * Linux or OSX
 * JDK8 (7 won't work)
 * http://leiningen.org
 * http://redis.io Server


# Usage


DNS lookup:

```bash
$ dig @localhost bar.com -p 53
```




# Copyright and license

Copyright [2015] [Ingomar Otter]
Based on tinymasq, Copyright [2014] [Ronen Narkis]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
  [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
