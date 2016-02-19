Java AP JustAPI Gateway
===========

This is an SDK that you will want to use to interface with the AnyPresence's JustAPI technology. 

Dependencies
===========

These dependencies are needed:

Gradle

```
compile 'com.google.code.gson:gson:2.5'
compile 'org.apache.commons:commons-lang3:3.4'
compile 'com.google.guava:guava:18.0'
```

Maven

```
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.5</version>
</dependency>

<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.4</version>
</dependency>

<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>18.0</version>
</dependency>

```

Setup
===========

There are a few ways to add these dependencies. The below is for Eclipse.

0) Add APGW-[version]-SNAPSHOT.jar into the libs folder for your java project.

1) You also need to also make sure the library is listed as a dependency in File->Properties->Java Build Path->Jars.

2) If not using maven or gradle, you may need to download the jars for the above dependencies.

If using maven or gradle build system, you need to add the above dependencies into proper build scripts.

Quick Examples
===========

Sends a POST synchronously
```{java}
        APGateway.Builder builder = new APGateway.Builder();
        builder.url("http://foo.lvh.me:3000/api/v1/foo");

        APGateway gw = builder.build();
        Map<String,String> param = new HashMap<String,String>();
        param.put("foo", "bar");
        gw.setPostParam(param);

        gw.post("/bar");

        ResponseFromRequest response = gw.readResponse();
        
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

Certificate pinning allows you to tie certificates against specified domains. It defends against attacks on certificate authorities.
It has it's limitations as well.

```{java}
        APGateway.getCertPinningManager().setupCa("myalias", certificateInBytes);
        
        APGateway.Builder builder = new APGateway.Builder();
        builder.url("https://localhost:3000");
        builder.useCertPinning(true);
        
        APGateway gw = builder.build();
        gw.post("/bar");
```

## Caching

The default cache store is an in-memory cache.

```{java}
      APGateway.Builder builder = new APGateway.Builder();
      builder.url("http://localhost:1080/api/v1/foo");

      APGateway gw = builder.build();
      gw.useCaching(true).get();
      
      ResponseFromRequest response = gw.readResponse();       
      System.out.println("response: " + response.data);
```

You can set your own cache store by subclassing java.net.ResponseCache and calling 
```
    Config.setCacheManager(new MyMemoryCache());
```



