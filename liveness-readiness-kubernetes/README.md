In application.properties there are variables used by helm to bind kube secrets.
The final kube file is:

# Source: app-version-checker/templates/service.yaml
apiVersion: v1
kind: Service
metadata:
  name: app-version-checker
  labels:
    helm.sh/chart: app-version-checker-0.1.0
    app.kubernetes.io/name: app-version-checker
    app.kubernetes.io/instance: app-version-checker
    app.kubernetes.io/version: "DEV-55539419"
    app.kubernetes.io/component: backend
    app.kubernetes.io/managed-by: Helm 
  annotations:
    # Annotations used by prometheus service discovery for scraping metrics from port 6666
    prometheus.io/scrape: "true"
    prometheus.io/port: "6666"
spec:
  type: "ClusterIP"
  ports:
    - port: 8080
      targetPort: http
      protocol: TCP
      name: http
  selector:
    app.kubernetes.io/name: app-version-checker
    app.kubernetes.io/instance: app-version-checker
---
# Source: app-version-checker/templates/deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: app-version-checker
  labels:
    helm.sh/chart: app-version-checker-0.1.0
    app.kubernetes.io/name: app-version-checker
    app.kubernetes.io/instance: app-version-checker
    app.kubernetes.io/version: "DEV-55539419"
    app.kubernetes.io/component: backend
    app.kubernetes.io/managed-by: Helm 
  annotations:    
spec:
  replicas: 1
  selector:
    matchLabels:
      app.kubernetes.io/name: app-version-checker
      app.kubernetes.io/instance: app-version-checker
      app.kubernetes.io/component: backend
  template:
    metadata:
      labels:
        app.kubernetes.io/name: app-version-checker
        app.kubernetes.io/instance: app-version-checker
        app.kubernetes.io/component: backend
      annotations:
        logFormat: "json"  # Used by the logging infrastructure to parse the logs in JSON format
        version: 0.1.0   # Include microservice version as annotation
        
    spec:
      containers:
        - name: app-version-checker
          image: "dedadevregistry.azurecr.io/app-version-checker:DEV-55539419"
          imagePullPolicy: Always
          # Environment variables
          env:
          # Cryptographic keys used for signatures of internal calls
          - name: INTERNAL_KEY_PATH
            value: /secrets/internal-key/internal_key
          # Used by Swagger library to set the base URL inside the swagger.yaml file
          # This is used by Swagger UI to reach the API endpoints when using the "Try it out" button on an API
          - name: EXTERNAL_BASE_PATH
            value:  "/api/app-version-checker/"
          # How to reach Redis service
          - name: REDIS_URL
            value: redis
          - name: REDIS_PORT
            value: "6379"
          - name: JSON_LOGGING
            value: "true"
          - name: DB_URL
            value: jdbc:mariadb://telemedicinamariadb.privatelink.mariadb.database.azure.com/deda_dev_utils
          - name: DB_USERNAME
            value: deda_dev_utils@telemedicinamariadb
          - name: DB_PASSWORD
            valueFrom:
              secretKeyRef:
                key: password
                name: app-version-checker-maria-credentials
          ports:
            - name: http		# The port where the server is serving API requests
              containerPort: 8080
              protocol: TCP
            - name: metrics # The port where the server is serving metrics
              containerPort: 8080
              protocol: TCP
          terminationMessagePolicy: FallbackToLogsOnError  # Use container logs for the exit message
          volumeMounts:			# Mount the cryptographic keys inside the container
            - name: internal-key
              readOnly: true
              mountPath: /secrets/internal-key/
          resources:
      volumes:					# Define the volumes with the keys from the Kubernetes secret
      - name: internal-key
        secret:
          secretName: internal-key
---
# Source: app-version-checker/templates/ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: app-version-checker
  labels:
    helm.sh/chart: app-version-checker-0.1.0
    app.kubernetes.io/name: app-version-checker
    app.kubernetes.io/instance: app-version-checker
    app.kubernetes.io/version: "DEV-55539419"
    app.kubernetes.io/component: backend
    app.kubernetes.io/managed-by: Helm              
  annotations:
    nginx.ingress.kubernetes.io/ssl-redirect: "false"
    nginx.ingress.kubernetes.io/use-regex: "true"
    nginx.ingress.kubernetes.io/rewrite-target: /$1	# Remove /api/session-manager/ from the request URL
spec:
  ingressClassName: nginx
  tls:       
    - hosts:
      - piattaformasalute.dedagroup.it
      secretName: piattaformasalute.dedagroup.it-tls 
  rules:       
    - host: piattaformasalute.dedagroup.it
      http:
        paths:
          - path: /api/app-version-checker/?(.*)
            pathType: Prefix
            backend:
              service: 
                name: app-version-checker
                port: 
                  number: 8080
                  
                  
                  
When in execution the pod answers on /metrics with this:
# HELP base_classloader_loadedClasses_count Displays the number of classes that are currently loaded in the Java virtual machine.
# TYPE base_classloader_loadedClasses_count gauge
base_classloader_loadedClasses_count 7919.0
# HELP base_classloader_loadedClasses_total Displays the total number of classes that have been loaded since the Java virtual machine has started execution.
# TYPE base_classloader_loadedClasses_total counter
base_classloader_loadedClasses_total 7923.0
# HELP base_classloader_unloadedClasses_total Displays the total number of classes unloaded since the Java virtual machine has started execution.
# TYPE base_classloader_unloadedClasses_total counter
base_classloader_unloadedClasses_total 4.0
# HELP base_cpu_availableProcessors Displays the number of processors available to the Java virtual machine. This value may change during a particular invocation of the virtual machine.
# TYPE base_cpu_availableProcessors gauge
base_cpu_availableProcessors 1.0
# HELP base_cpu_processCpuLoad_percent Displays  the "recent cpu usage" for the Java Virtual Machine process. This value is a double in the [0.0,1.0] interval. A value of 0.0 means that none of the CPUs were running threads from the JVM process during the recent period of time observed, while a value of 1.0 means that all CPUs were actively running threads from the JVM 100% of the time during the recent period being observed. Threads from the JVM include the application threads as well as the JVM internal threads. All values between 0.0 and 1.0 are possible depending of the activities going on in the JVM process and the whole system. If the Java Virtual Machine recent CPU usage is not available, the method returns a negative value.
# TYPE base_cpu_processCpuLoad_percent gauge
base_cpu_processCpuLoad_percent 1.616438428918801E-5
# HELP base_cpu_systemLoadAverage Displays the system load average for the last minute. The system load average is the sum of the number of runnable entities queued to the available processors and the number of runnable entities running on the available processors averaged over a period of time. The way in which the load average is calculated is operating system specific but is typically a damped time-dependent average. If the load average is not available, a negative value is displayed. This attribute is designed to provide a hint about the system load and may be queried frequently. The load average may be unavailable on some platforms where it is expensive to implement this method.
# TYPE base_cpu_systemLoadAverage gauge
base_cpu_systemLoadAverage 0.06
# HELP base_gc_time_total Displays the approximate accumulated collection elapsed time in milliseconds. This attribute displays -1 if the collection elapsed time is undefined for this collector. The Java virtual machine implementation may use a high resolution timer to measure the elapsed time. This attribute may display the same value even if the collection count has been incremented if the collection elapsed time is very short.
# TYPE base_gc_time_total counter
base_gc_time_total_seconds{name="Copy"} 0.028
base_gc_time_total_seconds{name="MarkSweepCompact"} 0.054
# HELP base_gc_total Displays the total number of collections that have occurred. This attribute lists -1 if the collection count is undefined for this collector.
# TYPE base_gc_total counter
base_gc_total{name="Copy"} 4.0
base_gc_total{name="MarkSweepCompact"} 2.0
# HELP base_jvm_uptime_seconds Displays the time from the start of the Java virtual machine in milliseconds.
# TYPE base_jvm_uptime_seconds gauge
base_jvm_uptime_seconds 328036.987
# HELP base_memory_committedHeap_bytes Displays the amount of memory in bytes that is committed for the Java virtual machine to use. This amount of memory is guaranteed for the Java virtual machine to use.
# TYPE base_memory_committedHeap_bytes gauge
base_memory_committedHeap_bytes 8.5262336E7
# HELP base_memory_maxHeap_bytes Displays the maximum amount of heap memory in bytes that can be used for memory management. This attribute displays -1 if the maximum heap memory size is undefined. This amount of memory is not guaranteed to be available for memory management if it is greater than the amount of committed memory. The Java virtual machine may fail to allocate memory even if the amount of used memory does not exceed this maximum size.
# TYPE base_memory_maxHeap_bytes gauge
base_memory_maxHeap_bytes 1.348141056E9
# HELP base_memory_usedHeap_bytes Displays the amount of used heap memory in bytes.
# TYPE base_memory_usedHeap_bytes gauge
base_memory_usedHeap_bytes 2.8037112E7
# HELP base_thread_count Displays the current number of live threads including both daemon and non-daemon threads
# TYPE base_thread_count gauge
base_thread_count 11.0
# HELP base_thread_daemon_count Displays the current number of live daemon threads.
# TYPE base_thread_daemon_count gauge
base_thread_daemon_count 7.0
# HELP base_thread_max_count Displays the peak live thread count since the Java virtual machine started or peak was reset. This includes daemon and non-daemon threads.
# TYPE base_thread_max_count gauge
base_thread_max_count 11.0
# HELP vendor_cpu_processCpuTime_seconds Displays the CPU time used by the process on which the Java virtual machine is running in nanoseconds. The returned value is of nanoseconds precision but not necessarily nanoseconds accuracy. This method returns -1 if the the platform does not support this operation.
# TYPE vendor_cpu_processCpuTime_seconds gauge
vendor_cpu_processCpuTime_seconds 308.37
# HELP vendor_cpu_systemCpuLoad_percent Displays the "recent cpu usage" for the whole system. This value is a double in the [0.0,1.0] interval. A value of 0.0 means that all CPUs were idle during the recent period of time observed, while a value of 1.0 means that all CPUs were actively running 100% of the time during the recent period being observed. All values betweens 0.0 and 1.0 are possible depending of the activities going on in the system. If the system recent cpu usage is not available, the method returns a negative value.
# TYPE vendor_cpu_systemCpuLoad_percent gauge
vendor_cpu_systemCpuLoad_percent 1.616638050563325E-5
# HELP vendor_memory_committedNonHeap_bytes Displays the amount of non heap memory in bytes that is committed for the Java virtual machine to use.
# TYPE vendor_memory_committedNonHeap_bytes gauge
vendor_memory_committedNonHeap_bytes 5.7327616E7
# HELP vendor_memory_freePhysicalSize_bytes Displays the amount of free physical memory in bytes.
# TYPE vendor_memory_freePhysicalSize_bytes gauge
vendor_memory_freePhysicalSize_bytes 5.417517056E9
# HELP vendor_memory_freeSwapSize_bytes Displays the amount of free swap space in bytes.
# TYPE vendor_memory_freeSwapSize_bytes gauge
vendor_memory_freeSwapSize_bytes 0.0
# HELP vendor_memory_maxNonHeap_bytes Displays the maximum amount of used non-heap memory in bytes.
# TYPE vendor_memory_maxNonHeap_bytes gauge
vendor_memory_maxNonHeap_bytes -1.0
# HELP vendor_memory_usedNonHeap_bytes Displays the amount of used non-heap memory in bytes.
# TYPE vendor_memory_usedNonHeap_bytes gauge
vendor_memory_usedNonHeap_bytes 5.3062856E7
# HELP vendor_memoryPool_usage_bytes Current usage of the memory pool denoted by the 'name' tag
# TYPE vendor_memoryPool_usage_bytes gauge
vendor_memoryPool_usage_bytes{name="CodeHeap 'non-nmethods'"} 1373696.0
vendor_memoryPool_usage_bytes{name="CodeHeap 'non-profiled nmethods'"} 1171200.0
vendor_memoryPool_usage_bytes{name="CodeHeap 'profiled nmethods'"} 5601024.0
vendor_memoryPool_usage_bytes{name="Compressed Class Space"} 4994392.0
vendor_memoryPool_usage_bytes{name="Eden Space"} 0.0
vendor_memoryPool_usage_bytes{name="Metaspace"} 3.9930848E7
vendor_memoryPool_usage_bytes{name="Survivor Space"} 0.0
vendor_memoryPool_usage_bytes{name="Tenured Gen"} 9946600.0
# HELP vendor_memoryPool_usage_max_bytes Peak usage of the memory pool denoted by the 'name' tag
# TYPE vendor_memoryPool_usage_max_bytes gauge
vendor_memoryPool_usage_max_bytes{name="CodeHeap 'non-nmethods'"} 1420800.0
vendor_memoryPool_usage_max_bytes{name="CodeHeap 'non-profiled nmethods'"} 1171200.0
vendor_memoryPool_usage_max_bytes{name="CodeHeap 'profiled nmethods'"} 5601024.0
vendor_memoryPool_usage_max_bytes{name="Compressed Class Space"} 4994392.0
vendor_memoryPool_usage_max_bytes{name="Eden Space"} 2.3658496E7
vendor_memoryPool_usage_max_bytes{name="Metaspace"} 3.9931168E7
vendor_memoryPool_usage_max_bytes{name="Survivor Space"} 2883576.0
vendor_memoryPool_usage_max_bytes{name="Tenured Gen"} 9946600.0
