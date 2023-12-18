package universe.service;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class KubernetesUpdaterService {
    private final KubernetesClient kubernetesClient;

    @Autowired
    public KubernetesUpdaterService(KubernetesClient client) {
        this.kubernetesClient = client;
    }

    public void updateNamespace(String namespaceName, String newDescription) {
        Resource<Namespace> namespaceResource = kubernetesClient.namespaces().withName(namespaceName);

        // Retrieve the existing Namespace
        Namespace existingNamespace = namespaceResource.get();

        if (existingNamespace != null) {
            // Update the Namespace
            existingNamespace.getMetadata().setAnnotations(
                    existingNamespace.getMetadata().getAnnotations() == null
                            ? Map.of("description", newDescription)
                            : existingNamespace.getMetadata().getAnnotations());

            namespaceResource.createOrReplace(existingNamespace);
        } else {
           log.error("Given namespace doesnt exist");
        }
    }

    public void updatePod(String podName, String namespace, String newImage) {
        Resource<Pod> podResource = kubernetesClient.pods().inNamespace(namespace).withName(podName);

        // Retrieve the existing Pod
        Pod existingPod = podResource.get();

        if (existingPod != null) {
            // Update the Pod
            existingPod.getSpec().getContainers().get(0).setImage(newImage);

            podResource.createOrReplace(existingPod);
        } else {
            log.error("Given pod doesnt exist");
        }
    }

    public void updateJob(String jobName, String namespace, String newImage) {
        Resource<Job> jobResource = kubernetesClient.batch().v1().jobs().inNamespace(namespace).withName(jobName);

        // Retrieve the existing Job
        Job existingJob = jobResource.get();

        if (existingJob != null) {
            // Update the Job
            existingJob.getSpec().getTemplate().getSpec().getContainers().get(0).setImage(newImage);

            jobResource.createOrReplace(existingJob);
        } else {
            log.error("Given Job doesnt exist");
        }
    }

    public void updateDeployment(String deploymentName, String namespace, String newImage) {
        Resource<Deployment> deploymentResource = kubernetesClient.apps().deployments().inNamespace(namespace).withName(deploymentName);

        // Retrieve the existing Deployment
        Deployment existingDeployment = deploymentResource.get();

        if (existingDeployment != null) {
            // Update the Deployment
            existingDeployment.getSpec().getTemplate().getSpec().getContainers().get(0).setImage(newImage);

            deploymentResource.createOrReplace(existingDeployment);
        } else {
            log.error("Given Deployment doesnt exist");
        }
    }
}
