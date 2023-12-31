package universe.service;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static universe.config.KubernetesConfig.kubernetesClient;

@Service
@Slf4j
public class KubernetesDeleteService {

    public void deletePod(String podName, String namespace) {
        try (var kubclient = kubernetesClient()) {
            Resource<Pod> podResource = kubclient.pods().inNamespace(namespace).withName(podName);

            Pod existingPod = podResource.get();

            if (existingPod != null) {
                podResource.delete();
                log.info("Pod has been successfully deleted");
            } else {
                log.error("There was something wrong when deleting the pod.");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    public void deleteNamespace(String namespaceName) {
        try (var kubclient = kubernetesClient()) {
            Resource<Namespace> namespaceResource = kubclient.namespaces().withName(namespaceName);

            // Retrieve the existing Namespace
            Namespace existingNamespace = namespaceResource.get();

            if (existingNamespace != null) {
                // Delete the Namespace
                namespaceResource.delete();
                log.info("Namespace has been successfully deleted");
            } else {
                log.error("There was something wrong when deleting the namespace.");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public void deleteJob(String jobName, String namespace) {
        try (var kubclient = kubernetesClient()) {
            Resource<Job> jobResource = kubclient.batch().v1().jobs().inNamespace(namespace).withName(jobName);

            // Retrieve the existing Job
            Job existingJob = jobResource.get();

            if (existingJob != null) {
                // Delete the Job
                jobResource.delete();
                log.info("Job has been successfully deleted");
            } else {
                log.error("There was something wrong when deleting the job");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public void deleteDeployment(String deploymentName, String namespace) {
        try (var kubclient = kubernetesClient()) {
            Resource<Deployment> deploymentResource = kubclient.apps().deployments().inNamespace(namespace).withName(deploymentName);

            // Retrieve the existing Deployment
            Deployment existingDeployment = deploymentResource.get();

            if (existingDeployment != null) {
                // Delete the Deployment
                deploymentResource.delete();
                log.info("Deployment has been successfully deleted");
            } else {
                log.error("There was something wrong with deleting the deployment");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }


    }

}
