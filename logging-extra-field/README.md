# adding extra field to logs

Example output
```
{"timestamp":"2022-09-18T14:37:37.687+01:00","sequence":1548,"loggerClassName":"org.jboss.logging.Logger","loggerName":"org.acme.GreetingResource","level":"INFO","message":"Hello","threadName":"executor-thread-0","threadId":101,"mdc":{},"ndc":"","hostName":"mintozzy-mach-wx9","processName":"code-with-quarkus-dev.jar","processId":133458,"EXTRA":"test-value"}
```

check [properties](src/main/resources/application.properties)