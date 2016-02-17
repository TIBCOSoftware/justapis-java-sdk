Java AP JustAPI Gateway
===========

This is an SDK that you will want to use to interface with the AnyPresence's JustAPI technology. 

Dependencies
===========

These dependencies are needed:

```
compile 'com.google.code.gson:gson:2.5'
compile 'org.apache.commons:commons-lang3:3.4'
compile 'com.google.guava:guava:18.0'
```

===========
Quick Examples

Sends a POST synchronously
```{java}
        APGateway.Builder builder = new APGateway.Builder();
        builder.url("http://foo.lvh.me:3000/api/v1/foo");

        APGateway gw = builder.build();
        Map<String,String> param = new HashMap<String,String>();
        param.put("foo", "bar");
        gw.setPostParam(param);

        gw.post("/bar");

        ResponseFromRequest response = gw.setPostParam(obj);
        
        System.out.println("response: " + response.data);

```

Sends a request asynchronously
```{java}
        APGateway.Builder builder = new APGateway.Builder();
        builder.url("http://localhost:3000");
        
        APGateway gw = builder.build();
        
        gw.get("/api/v1/foo", new APStringCallback() {
            @Override
            public void finished(String object, Throwable ex) {
                if (ex == null) {
                    System.out.println("success");
                } else {                    
                    System.out.println("failure");
                    ex.printStackTrace();
                }
            }
        });
```

## Certificate pinning

Certificate pinning ties a host to their expected X509 certificate.

```{java}
        APGateway.getCertPinningManager().setupCa("myalias", certificateInBytes);
        
        APGateway.Builder builder = new APGateway.Builder();
        builder.url("https://localhost:3000");
        builder.useCertPinning(true);
        
        APGateway gw = builder.build();
        gw.post("/bar");
```

