# MediTrack Patient Service Grafana Dashboard

This directory contains a Grafana dashboard for monitoring the MediTrack Patient Service application.

## Dashboard Overview

The dashboard provides visualization for the following metrics:

1. **Patient Operations**
   - Operation rates (created, updated, deleted, retrieved)
   - Operation distribution

2. **Operation Timers**
   - Response times (p95) for create, update, and get operations
   - Operation throughput (requests/sec)

3. **JVM Metrics**
   - Heap memory usage
   - Thread counts

## Prerequisites

- Grafana (version 8.0 or higher)
- Prometheus configured to scrape metrics from the Patient Service
- The Patient Service application running with metrics enabled

## Importing the Dashboard

### Option 1: Import via Grafana UI

1. Open your Grafana instance in a web browser
2. Navigate to Dashboards > Import
3. Click "Upload JSON file" and select the `patient-service-dashboard.json` file
4. Select your Prometheus data source in the "prometheus" dropdown
5. Click "Import"

### Option 2: Import via Grafana API

You can also import the dashboard using the Grafana HTTP API:

```bash
curl -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_API_KEY" \
  --data-binary @patient-service-dashboard.json \
  http://your-grafana-instance/api/dashboards/db
```

Replace `YOUR_API_KEY` with your Grafana API key and `your-grafana-instance` with your Grafana URL.

## Configuring Prometheus

Ensure that Prometheus is configured to scrape metrics from the Patient Service. Add the following to your `prometheus.yml` file:

```yaml
scrape_configs:
  - job_name: 'patient-service'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
    static_configs:
      - targets: ['patient-service:80']
```

Replace `patient-service:80` with the actual hostname and port of your Patient Service.

## Customizing the Dashboard

After importing, you can customize the dashboard to suit your needs:

1. Adjust time ranges
2. Add or remove panels
3. Change visualization types
4. Add alerts for important metrics

## Metrics Reference

The dashboard uses the following metrics:

- `patient_created_total` - Total number of patients created
- `patient_updated_total` - Total number of patients updated
- `patient_deleted_total` - Total number of patients deleted
- `patient_retrieved_total` - Total number of patient retrieval operations
- `patient_create_time_seconds` - Time taken to create a patient
- `patient_update_time_seconds` - Time taken to update a patient
- `patient_get_time_seconds` - Time taken to retrieve a patient
- `patient_create_seconds_count` - Count of create operations
- `patient_get_by_id_seconds_count` - Count of get by ID operations
- `patient_update_seconds_count` - Count of update operations
- `patient_delete_seconds_count` - Count of delete operations
- `jvm_memory_used_bytes` - JVM memory usage
- `jvm_memory_committed_bytes` - JVM committed memory
- `jvm_memory_max_bytes` - JVM maximum memory
- `jvm_threads_live_threads` - Number of live threads
- `jvm_threads_daemon_threads` - Number of daemon threads
- `jvm_threads_peak_threads` - Peak number of threads