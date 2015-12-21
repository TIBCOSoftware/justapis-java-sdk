Android AP Gateway
===========
This is an SDK that you will want to use to interface with the AnyPresence's JustAPI technology. 

The design is meant to be modular where you can plug in your own JSON parser or rest client.
===========
Quick Examples

Sends a POST synchronously
```java
        APGateway.Builder builder = new APGateway.Builder();
        builder.url("http://foo.lvh.me:3000/api/v1/foo");

        APGateway gw = builder.build();

        APObject obj = new APObject();
        gw.setBody("{'foo':'bar'}");

        gw.post("/bar");

        APObject responseObject = gw.readResponseObject(obj);
        
        String id = responseObject.get("id");

```

Sends a request asynchronously
```{java}
        APGateway.Builder builder = new APGateway.Builder();
        builder.url("http://localhost:3000");
        
        APGateway gw = builder.build();
        
        gw.get("/api/v1/foo", new APCallback<APObject>() {
            @Override
            public void finished(APObject object, Throwable ex) {
                if (ex == null) {
                    System.out.println("success");
                } else {                    
                    System.out.println("failure");
                    ex.printStackTrace();
                }
            }
        });
```

Plug in your own JSON Parser
```{java}

    class MyParser implements IParser {

        @Override
        public Map<String, String> parseData(String data) {
            // Parse the data
            return null;
        }
        
    }
    
    ...
    
        APGateway.Builder builder = new APGateway.Builder();
        builder.url("http://localhost:3000");
        
        APGateway gw = builder.build();
        gw.setJsonParser(new MyParser());
```